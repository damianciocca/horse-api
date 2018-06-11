package com.etermax.spacehorse.core.battle.model;

import java.util.List;
import java.util.Optional;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.battle.resource.response.FinishBattlePlayerResponse;
import com.etermax.spacehorse.core.battle.resource.response.TeamStatsResponse;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.liga.domain.LeagueConfiguration;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.stats.MatchStats;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.QuestProgressUpdater;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;

public class FinishPlayerBattleDomainService {

	private final PlayerRepository playerRepository;
	private final PlayerWinRateRepository playerWinRateRepository;
	private final BattleRewardsStrategy battleRewardsStrategy;
	private final ApplyRewardDomainService applyRewardDomainService;
	private final MmrCalculatorDomainService mmrCalculatorDomainService;
	private final List<AchievementsObserver> achievementsObservers;
	private final QuestProgressUpdater questProgressUpdater;
	private final AchievementsFactory achievementsFactory;
	private final PlayerLeagueService playerLeagueService;

	public FinishPlayerBattleDomainService(PlayerRepository playerRepository, PlayerWinRateRepository playerWinRateRepository,
			BattleRewardsStrategy battleRewardsStrategy, ApplyRewardDomainService applyRewardDomainService,
			MmrCalculatorDomainService mmrCalculatorDomainService, List<AchievementsObserver> achievementsObservers,
			QuestProgressUpdater questProgressUpdater, AchievementsFactory achievementsFactory, PlayerLeagueService playerLeagueService) {
		this.playerRepository = playerRepository;
		this.playerWinRateRepository = playerWinRateRepository;
		this.battleRewardsStrategy = battleRewardsStrategy;
		this.applyRewardDomainService = applyRewardDomainService;
		this.mmrCalculatorDomainService = mmrCalculatorDomainService;
		this.achievementsObservers = achievementsObservers;
		this.questProgressUpdater = questProgressUpdater;
		this.achievementsFactory = achievementsFactory;
		this.playerLeagueService = playerLeagueService;
	}

	public FinishBattlePlayerResponse finishBattlePlayer(Player player, Catalog catalog, Battle battle) {
		if (!battle.getFinished()) {
			updateQuittingPlayerMmr(player, battle, catalog);
		}

		PlayerWinRate playerWinRate = playerWinRateRepository.findOrCrateDefault(player.getUserId());
		int opponentShipsDestroyed = BattleUtils.getOpponentShipsDestroyed(player, battle);
		List<Reward> rewards = battleRewardsStrategy.getBattleRewards(player, battle, catalog);

		if (isTutorialBattleWonByPlayer(player, battle)) {
			finishActiveTutorial(player);
		}

		if (isBotMatch(battle) && isChallenge(battle)) {
			updatePlayerBotMmr(player, battle, catalog);
		}

		updatePlayerStats(player, battle, playerWinRate.getMmr(), opponentShipsDestroyed);
		questProgressUpdater.updateQuestsProgress(player, battle, catalog);
		updateMapNumber(player, playerWinRate, catalog);

		updateAchievements(player, catalog, battle);

		List<RewardResponse> rewardResponses = applyBattleRewards(player, rewards, battle, catalog);
		player.setLastBattleId("");
		updatePlayer(player);

		LeagueConfiguration leagueConfiguration = new LeagueConfiguration(catalog);
		playerLeagueService.addOrUpdatePlayerSeasons(player, leagueConfiguration);

		return new FinishBattlePlayerResponse(rewardResponses, playerWinRate.getMmr(), player.getBotMmr(), battle.getWinnerLoginId(),
				battle.getWinFullScore(), TeamStatsResponse.fromTeamStats(battle.getTeam1Stats()),
				TeamStatsResponse.fromTeamStats(battle.getTeam2Stats()));
	}

	private boolean isChallenge(Battle battle) {
		return battle.getMatchType().equals(MatchType.CHALLENGE);
	}

	private boolean isFriendlyMatch(Battle battle) {
		return battle.getMatchType().equals(MatchType.FRIENDLY);
	}

	private void updatePlayerBotMmr(Player player, Battle battle, Catalog catalog) {
		BotMmrAlgorithm botMmrAlgorithm = new BotMmrAlgorithm(buildMmrAlgorithmConfiguration(catalog.getGameConstants()));
		player.setBotMmr(botMmrAlgorithm.updateMmr(player.getBotMmr(), getBattleResult(player, battle), battle.getTeam1Stats().getScore(),
				battle.getTeam2Stats().getScore()));
	}

	private BotMmrAlgorithmConfiguration buildMmrAlgorithmConfiguration(GameConstants gameConstants) {
		return gameConstants.getBotMmrAlgorithmConfiguration();
	}

	private BattleResult getBattleResult(Player player, Battle battle) {
		if (!battle.getFinished())
			return BattleResult.LOSE;

		if (battle.getWinnerLoginId() == null || battle.getWinnerLoginId().isEmpty())
			return BattleResult.TIE;

		if (battle.getWinnerLoginId().equals(player.getUserId()))
			return BattleResult.WIN;

		return BattleResult.LOSE;
	}

	private boolean isBotMatch(Battle battle) {
		return battle.getPlayers().stream().anyMatch(BattlePlayer::getBot);
	}

	private void updateMapNumber(Player player, PlayerWinRate playerWinRate, Catalog catalog) {
		int mapNumber = new DefaultPlayerMapNumberCalculator(catalog.getMapsCollection().getEntries()).getMapNumber(playerWinRate.getMmr());
		if (mapNumber != player.getMapNumber()) {
			player.setMapNumber(mapNumber);
		}
	}

	private void updateQuittingPlayerMmr(Player player, Battle battle, Catalog catalog) {

		if (battle.getMatchType().equals(MatchType.FRIENDLY))
			return;

		//Trying to finish a battle before it actually finishes? Assume player quit, and lost.
		Optional<BattlePlayer> playerBattlePlayer = BattleUtils.findBattlePlayer(player, battle);
		Optional<BattlePlayer> opponentBattlePlayer = BattleUtils.findNonBattlePlayer(player, battle);

		if (playerBattlePlayer.isPresent() && opponentBattlePlayer.isPresent()) {
			PlayersDeltaMmr playersDeltaMmr = mmrCalculatorDomainService.calculate(opponentBattlePlayer.get(), playerBattlePlayer.get());
			playerWinRateRepository
					.updateMmrOnlyIfOldValueIs(player.getUserId(), playerBattlePlayer.get().getMmr(), playersDeltaMmr.getLoserDeltaMmr(),
							PlayerWinRateConfiguration.create(catalog));
		}
	}

	private void updatePlayer(Player player) {
		playerRepository.update(player);
	}

	private boolean isTutorialBattleWonByPlayer(Player player, Battle battle) {
		return BattleUtils.playerIsWinner(player, battle) && player.hasActiveTutorial();
	}

	private void finishActiveTutorial(Player player) {
		player.finishActiveTutorial();
	}

	private void updatePlayerStats(Player player, Battle battle, int mmr, int opponentShipsDestroyed) {

		if (isFriendlyMatch(battle))
			return;

		player.getPlayerStats().updateMaxMMR(mmr);

		MatchStats matchStats = player.getPlayerStats().getMatchStats();
		matchStats.incrementMultiplayerPlayed();
		if (BattleUtils.playerIsWinner(player, battle)) {
			matchStats.incrementMultiplayerWon();
			if (battle.getWinFullScore()) {
				matchStats.incrementMultiplayerWonFullScore();
			}
		} else if (BattleUtils.playerIsLoser(player, battle)) {
			matchStats.incrementMultiplayerLost();
		}

		matchStats.setOpponentShipsDestroyed(matchStats.getOpponentShipsDestroyed() + opponentShipsDestroyed);
	}

	private List<RewardResponse> applyBattleRewards(Player player, List<Reward> rewards, Battle battle, Catalog catalog) {
		MapDefinition map = catalog.getMapsCollection().findByIdOrFail(battle.getMapId());
		ApplyRewardConfiguration configuration = ApplyRewardConfiguration.createBy(catalog);

		List<AchievementsObserver> achievementsObservers = achievementsFactory.createForApplyRewards();
		return applyRewardDomainService
				.applyRewards(player, rewards, RewardContext.fromMapNumber(map.getMapNumber()), configuration, achievementsObservers);
	}

	private void updateAchievements(Player player, Catalog catalog, Battle battle) {
		List<AchievementDefinition> achievementDefinitions = catalog.getAchievementsDefinitionsCollection().getEntries();
		achievementsObservers.forEach(achievementsObserver -> achievementsObserver.update(player, achievementDefinitions));
		achievementsFactory.createBattleFriendlyPlaysReached(battle).update(player, achievementDefinitions);
	}
}

