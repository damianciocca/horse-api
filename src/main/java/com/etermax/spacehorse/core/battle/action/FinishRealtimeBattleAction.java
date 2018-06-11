package com.etermax.spacehorse.core.battle.action;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Date;
import java.util.stream.Stream;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.BattleResult;
import com.etermax.spacehorse.core.battle.model.MmrCalculatorDomainService;
import com.etermax.spacehorse.core.battle.model.PlayerWinRateConfiguration;
import com.etermax.spacehorse.core.battle.model.PlayersDeltaMmr;
import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.tutorialprogress.TutorialProgressEntry;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.club.infrastructure.ClubReporter;
import com.etermax.spacehorse.core.error.TutorialIdDoesNotExistException;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class FinishRealtimeBattleAction {

	private static final int PLAYER_1_INDEX = 0;
	private static final int PLAYER_2_INDEX = 1;

	private final BattleRepository battleRepository;
	private final PlayerRepository playerRepository;
	private final CatalogRepository catalogRepository;
	private final PlayerWinRateRepository playerWinRateRepository;
	private final MmrCalculatorDomainService mmrCalculatorDomainService;
	private final ClubReporter clubReporter;

	public FinishRealtimeBattleAction(BattleRepository battleRepository, PlayerRepository playerRepository, CatalogRepository catalogRepository,
			PlayerWinRateRepository playerWinRateRepository, MmrCalculatorDomainService mmrCalculatorDomainService, ClubReporter clubReporter) {
		this.battleRepository = battleRepository;
		this.playerRepository = playerRepository;
		this.catalogRepository = catalogRepository;
		this.playerWinRateRepository = playerWinRateRepository;
		this.mmrCalculatorDomainService = mmrCalculatorDomainService;
		this.clubReporter = clubReporter;
	}

	public boolean finishBattle(String battleId, String winnerLoginId, Boolean isWinFullScore, TeamStats team1Stats, TeamStats team2Stats) {
		if (isBlank(battleId)) {
			return false;
		}
		return battleRepository.find(battleId).map(battle -> finishBattle(battle, winnerLoginId, isWinFullScore, team1Stats, team2Stats))
				.orElse(false);
	}

	private boolean finishBattle(Battle battle, String winnerLoginId, Boolean isWinFullScore, TeamStats team1Stats, TeamStats team2Stats) {
		if (finishBattleFails(battle, winnerLoginId, isWinFullScore, team1Stats, team2Stats)) {
			return false;
		}

		switch (battle.getMatchType()) {
			case CHALLENGE:
				updateMMRandScore(battle);
				break;

			case TUTORIAL:
				updateMMRForTutorial(battle);
				break;

			case FRIENDLY:
				//Nothing to do
				break;
		}

		battleRepository.update(battle);
		return true;
	}

	private boolean finishBattleFails(Battle battle, String winnerLoginId, Boolean isWinFullScore, TeamStats team1Stats, TeamStats team2Stats) {
		return !battle.finish(new Date(), winnerLoginId, isWinFullScore, team1Stats, team2Stats);
	}

	private void updateMMRandScore(Battle battle) {
		BattlePlayer player1 = battle.getPlayers().get(PLAYER_1_INDEX);
		BattlePlayer player2 = battle.getPlayers().get(PLAYER_2_INDEX);
		if (playerWon(player1, battle)) {
			updatePlayersMmrAndScore(player1, player2, battle);
		} else if (playerWon(player2, battle)) {
			updatePlayersMmrAndScore(player2, player1, battle);
		} else {
			//No need to update MMR
			updateScore(battle, battle.getWinnerLoginId());
		}
	}

	private boolean playerWon(BattlePlayer player, Battle battle) {
		return player.getUserId().equals(battle.getWinnerLoginId());
	}

	private boolean hasPlayerWonTheTutorialBattle(Battle battle, BattlePlayer player1) {
		return !player1.getBot() && playerWon(player1, battle);
	}

	private void updateMMRForTutorial(Battle battle) {
		BattlePlayer battlePlayer = battle.getPlayers().get(PLAYER_1_INDEX);
		if (hasPlayerWonTheTutorialBattle(battle, battlePlayer)) {
			Integer fixedMMR = getTutorialConfiguredMMR(battlePlayer);
			int deltaNewMMR = fixedMMR - battlePlayer.getMmr();
			String userId = battlePlayer.getUserId();
			playerWinRateRepository.updateMmr(userId, deltaNewMMR, createPlayerWinRateConfiguration(battlePlayer));
			clubReporter.updateUserScore(userId, fixedMMR);
		}
	}

	private Integer getTutorialConfiguredMMR(BattlePlayer battlePlayer) {
		String activeTutorialId = getActiveTutorialIdFrom(battlePlayer);
		String catalogId = battlePlayer.getCatalogId();
		TutorialProgressEntry tutorialProgressEntry = catalogRepository.find(catalogId).getTutorialProgressCollection().findById(activeTutorialId)
				.orElseThrow(TutorialIdDoesNotExistException::new);
		return tutorialProgressEntry.getFixedMMR();
	}

	private String getActiveTutorialIdFrom(BattlePlayer battlePlayer) {
		return playerRepository.find(battlePlayer.getUserId()).getTutorialProgress().getActiveTutorial();
	}

	private void updatePlayersMmrAndScore(BattlePlayer winnerPlayer, BattlePlayer loserPlayer, Battle battle) {

		PlayersDeltaMmr playersDeltaMmr = mmrCalculatorDomainService.calculate(winnerPlayer, loserPlayer);

		if (!winnerPlayer.getBot()) {
			Integer newWinnerDeltaMmr = playersDeltaMmr.getWinnerDeltaMmr();
			Integer winnerOldMmer = winnerPlayer.getMmr();
			Integer winnerMmrToReport = winnerOldMmer + newWinnerDeltaMmr;
			playerWinRateRepository
					.updateMmrAndScoreOnlyIfOldValueIs(winnerPlayer.getUserId(), winnerOldMmer, newWinnerDeltaMmr,
							getScoreResult(winnerPlayer, battle.getWinnerLoginId()), createPlayerWinRateConfiguration(winnerPlayer));
			clubReporter.updateUserScore(winnerPlayer.getUserId(), winnerMmrToReport);
		}
		if (!loserPlayer.getBot()) {
			Integer newLoserDeltaMmr = playersDeltaMmr.getLoserDeltaMmr();
			Integer loserOldMmr = loserPlayer.getMmr();
			Integer loserMmrToReport = loserOldMmr + newLoserDeltaMmr;
			playerWinRateRepository
					.updateMmrAndScoreOnlyIfOldValueIs(loserPlayer.getUserId(), loserOldMmr, newLoserDeltaMmr,
							getScoreResult(loserPlayer, battle.getWinnerLoginId()), createPlayerWinRateConfiguration(loserPlayer));
			clubReporter.updateUserScore(loserPlayer.getUserId(), loserMmrToReport);
		}
	}

	private void updateScore(Battle battle, String winnerLoginId) {
		getHumanBattlePlayersStream(battle).forEach(battlePlayer -> {
			BattleResult result = getScoreResult(battlePlayer, winnerLoginId);
			playerWinRateRepository.updateScore(battlePlayer.getUserId(), result);
		});
	}

	private BattleResult getScoreResult(BattlePlayer battlePlayer, String winnerLoginId) {
		BattleResult result;
		if (winnerLoginId.isEmpty()) {
			result = BattleResult.TIE;
		} else if (winnerLoginId.equals(battlePlayer.getUserId())) {
			result = BattleResult.WIN;
		} else {
			result = BattleResult.LOSE;
		}
		return result;
	}

	private Stream<BattlePlayer> getHumanBattlePlayersStream(Battle battle) {
		return battle.getPlayers().stream().filter(x -> !x.getBot());
	}

	private PlayerWinRateConfiguration createPlayerWinRateConfiguration(BattlePlayer battlePlayer) {
		return PlayerWinRateConfiguration.create(battlePlayer, catalogRepository);
	}
}
