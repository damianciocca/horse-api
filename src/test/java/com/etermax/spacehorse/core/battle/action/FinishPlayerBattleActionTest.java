package com.etermax.spacehorse.core.battle.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.battle.exception.BattleNotFinishedException;
import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.BattlePlayerBuilder;
import com.etermax.spacehorse.core.battle.model.BattleRewardsStrategy;
import com.etermax.spacehorse.core.battle.model.BotMmrAlgorithmConfiguration;
import com.etermax.spacehorse.core.battle.model.FinishPlayerBattleDomainService;
import com.etermax.spacehorse.core.battle.model.MmrCalculatorDomainService;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.league.LeagueRewardsDefinition;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.PlayerBuilder;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestProgressUpdater;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.google.common.collect.Lists;

public class FinishPlayerBattleActionTest {

	private BattleRepository battleRepository;
	private PlayerRepository playerRepository;
	private QuestBoardRepository questBoardRepository;
	private CatalogRepository catalogRepository;
	private PlayerWinRateRepository playerWinRateRepository;

	private FinishPlayerBattleAction finishPlayerBattleAction;

	private final String BATTLE_ID = "battleId";
	private final String PLAYER_ID = "playerId";
	private final String OPPONENT_PLAYER_ID = "opponentPlayerId";
	private final String MAP_ID = "mapId";

	private static final int AFTER_BATTLE_MMR = 75;
	private static final int GOLD_REWARD = 100;
	private static final int GEMS_REWARD = 50;
	private static final int GEMS_INITIAL = 0;
	private static final int GOLD_INITIAL = 0;
	private static final int BOT_MMR_INITIAL = 100;

	private Player player;
	private List<Reward> rewards;
	private BattleRewardsStrategy battleRewardsStrategy;
	private CompleteAchievementAction completeAchievementAction;
	private AchievementsFactory achievementsFactory;
	private PlayerLeagueService playerLeagueService;

	@Before
	public void setUp() {

		battleRepository = mock(BattleRepository.class);
		playerRepository = mock(PlayerRepository.class);
		catalogRepository = mock(CatalogRepository.class);
		playerWinRateRepository = mock(PlayerWinRateRepository.class);
		questBoardRepository = mock(QuestBoardRepository.class);

		battleRewardsStrategy = mock(BattleRewardsStrategy.class);

		Catalog catalog = mock(Catalog.class);
		when(catalogRepository.getActiveCatalogWithTag(any())).thenReturn(catalog);

		completeAchievementAction = mock(CompleteAchievementAction.class);
		achievementsFactory = mock(AchievementsFactory.class);
		AchievementsObserver achievementsObserver = mock(AchievementsObserver.class);
		when(achievementsFactory.createBattleFriendlyPlaysReached(any())).thenReturn(achievementsObserver);

		GameConstants gameConstants = mock(GameConstants.class);
		BotMmrAlgorithmConfiguration botMmrAlgorithmConfiguration = new BotMmrAlgorithmConfiguration(10, 0, -10, 0, 0);
		when(gameConstants.getBotMmrAlgorithmConfiguration()).thenReturn(botMmrAlgorithmConfiguration);
		when(gameConstants.getLeagueMapId()).thenReturn("");

		when(catalog.getCardDefinitionsCollection()).thenReturn(new CatalogEntriesCollection<>());
		when(catalog.getQuestCollection()).thenReturn(new CatalogEntriesCollection<>());
		when(catalog.getGameConstants()).thenReturn(gameConstants);
		when(catalog.getFeatureByPlayerLvlDefinitionCollection()).thenReturn(new CatalogEntriesCollection<>());
		when(catalog.getAchievementsDefinitionsCollection()).thenReturn(new CatalogEntriesCollection<>());
		CatalogEntriesCollection<MapDefinition> mapsCollection = mock(CatalogEntriesCollection.class);
		when(catalog.getMapsCollection()).thenReturn(mapsCollection);
		when(mapsCollection.findByIdOrFail(eq(MAP_ID))).thenReturn(new MapDefinition(MAP_ID, 0, 0, 0, 0));

		CatalogEntriesCollection<LeagueRewardsDefinition> leagueRewardsDefinitionsCollection =  mock(CatalogEntriesCollection.class);
		LeagueRewardsDefinition leagueRewardsDefinition = mock(LeagueRewardsDefinition.class);
		when(leagueRewardsDefinitionsCollection.getEntries()).thenReturn(Lists.newArrayList(leagueRewardsDefinition));
		when(mapsCollection.findByIdOrFail(eq(""))).thenReturn(new MapDefinition(MAP_ID, 0, 3000, 0, 0));
		when(catalog.getLeagueRewardsDefinitionCollection()).thenReturn(leagueRewardsDefinitionsCollection);

		playerLeagueService = mock(PlayerLeagueService.class);
		aFinishPlayerBattleAction();

		player = buildDefaultPlayer();

		Quest dailyQuest = mock(Quest.class);
		when(questBoardRepository.findOrDefaultBy(any()))
				.thenReturn(new QuestBoard(new FixedServerTimeProvider(), getQuestBoardConfiguration(), dailyQuest));
		when(playerRepository.find(PLAYER_ID)).thenReturn(player);
		when(playerWinRateRepository.findOrCrateDefault(eq(PLAYER_ID))).thenReturn(new PlayerWinRate(PLAYER_ID, 0, 0, 0, AFTER_BATTLE_MMR));

	}

	@After
	public void tearDown() {
		reset(battleRepository);
		reset(playerRepository);
		reset(catalogRepository);
		reset(playerWinRateRepository);
		reset(battleRewardsStrategy);

		player = null;
		finishPlayerBattleAction = null;
		rewards = null;
	}

	@Test
	public void finishingGivesRewardsToThePlayer() {
		givenABattleWonWithRewards();
		whenFinishingTheBattle();
		thenThePlayerHasTheRewards();
		thenThePlayerWasUpdated();
	}

	@Test
	public void finishingAWonMatchUpdatesMatchStats() {
		givenABattleWonWithRewards();
		whenFinishingTheBattle();
		thenMatchWonStatsWereUpdated();
	}

	@Test
	public void finishingALostMatchUpdatesMatchStats() {
		givenABattleLostWithoutRewards();
		whenFinishingTheBattle();
		thenMatchLostStatsWereUpdated();
	}

	@Test
	public void finishingWithoutRewardsDoesntFail() {
		givenABattleLostWithoutRewards();
		whenFinishingTheBattle();
		thenThePlayerHasNoRewards();
	}

	@Test
	public void finishingANonBotBattleDoesntModifyBotMmr() {
		givenABattleWonWithRewards();
		whenFinishingTheBattle();
		thenThePlayerHasTheSameBotMmr();
	}

	@Test
	public void winningABotBattleIncreasesBotMmr() {
		givenABattleWonAgainstABot();
		whenFinishingTheBattle();
		thenThePlayerHasMoreBotMmr();
	}

	@Test
	public void losingABotBattleDecresesBotMmr() {
		givenABattleLostAgainstABot();
		whenFinishingTheBattle();
		thenThePlayerHasLessBotMmr();
	}

	private QuestBoardConfiguration getQuestBoardConfiguration() {
		return new QuestBoardConfiguration(100, 10, 720, 1);
	}

	private void thenThePlayerHasMoreBotMmr() {
		assertThat(player.getBotMmr(), is(greaterThan(BOT_MMR_INITIAL)));
	}

	private void thenThePlayerHasLessBotMmr() {
		assertThat(player.getBotMmr(), is(lessThan(BOT_MMR_INITIAL)));
	}

	private void givenABattleWonAgainstABot() {
		Battle battle = buildDefaultBotBattleWithWinner(PLAYER_ID);

		when(battleRepository.find(BATTLE_ID)).thenReturn(Optional.of(battle));

		when(battleRewardsStrategy.getBattleRewards(eq(player), eq(battle), any())).thenReturn(new ArrayList<>());
	}

	private void givenABattleLostAgainstABot() {
		Battle battle = buildDefaultBotBattleWithWinner(OPPONENT_PLAYER_ID);

		when(battleRepository.find(BATTLE_ID)).thenReturn(Optional.of(battle));

		when(battleRewardsStrategy.getBattleRewards(eq(player), eq(battle), any())).thenReturn(new ArrayList<>());
	}

	private void thenThePlayerHasTheSameBotMmr() {
		assertThat(player.getBotMmr(), is(equalTo(BOT_MMR_INITIAL)));
	}

	@Test(expected = BattleNotFinishedException.class)
	public void finishingANotFinishedBattleFails() {
		givenABattleNotFinished();
		whenFinishingTheBattle();
	}

	private void thenMatchWonStatsWereUpdated() {
		assertThat(player.getPlayerStats().getMatchStats().getMultiplayerPlayed(), is(equalTo(1)));
		assertThat(player.getPlayerStats().getMatchStats().getMultiplayerWon(), is(equalTo(1)));
		assertThat(player.getPlayerStats().getMatchStats().getMultiplayerLost(), is(equalTo(0)));
		assertThat(player.getPlayerStats().getMaxMMR(), is(equalTo(AFTER_BATTLE_MMR)));
	}

	private void thenMatchLostStatsWereUpdated() {
		assertThat(player.getPlayerStats().getMatchStats().getMultiplayerPlayed(), is(equalTo(1)));
		assertThat(player.getPlayerStats().getMatchStats().getMultiplayerWon(), is(equalTo(0)));
		assertThat(player.getPlayerStats().getMatchStats().getMultiplayerLost(), is(equalTo(1)));
	}

	private void thenThePlayerWasUpdated() {
		verify(playerRepository, atLeastOnce()).update(eq(player));
	}

	private void givenABattleNotFinished() {
		Battle battle = buildANonFinishedBattle();
		when(battleRepository.find(BATTLE_ID)).thenReturn(Optional.of(battle));
	}

	private void thenThePlayerHasNoRewards() {
		assertThat(player.getInventory().getGems().getAmount(), is(equalTo(GEMS_INITIAL)));
		assertThat(player.getInventory().getGold().getAmount(), is(equalTo(GOLD_INITIAL)));
	}

	private void givenABattleLostWithoutRewards() {
		Battle battle = buildDefaultBattleWithWinner(OPPONENT_PLAYER_ID);

		when(battleRepository.find(BATTLE_ID)).thenReturn(Optional.of(battle));

		when(battleRewardsStrategy.getBattleRewards(eq(player), eq(battle), any())).thenReturn(Arrays.asList());
	}

	private void thenThePlayerHasTheRewards() {
		assertThat(player.getInventory().getGems().getAmount(), is(equalTo(GEMS_REWARD)));
		assertThat(player.getInventory().getGold().getAmount(), is(equalTo(GOLD_REWARD)));
	}

	private void givenABattleWonWithRewards() {

		Battle battle = buildDefaultBattleWithWinner(PLAYER_ID);

		when(battleRepository.find(BATTLE_ID)).thenReturn(Optional.of(battle));

		rewards = Arrays.asList(new Reward(RewardType.GOLD, GOLD_REWARD), new Reward(RewardType.GEMS, GEMS_REWARD));

		when(battleRewardsStrategy.getBattleRewards(eq(player), eq(battle), any())).thenReturn(rewards);
	}

	private void aFinishPlayerBattleAction() {
		MmrCalculatorDomainService mmrCalculatorDomainService = new MmrCalculatorDomainService(catalogRepository);
		FinishPlayerBattleDomainService finishPlayerBattleDomainService = new FinishPlayerBattleDomainService(playerRepository,
				playerWinRateRepository, battleRewardsStrategy, new ApplyRewardDomainService(), mmrCalculatorDomainService, Lists.newArrayList(),
				new QuestProgressUpdater(questBoardRepository, completeAchievementAction), achievementsFactory, playerLeagueService);
		finishPlayerBattleAction = new FinishPlayerBattleAction(battleRepository, playerRepository, catalogRepository,
				finishPlayerBattleDomainService);
	}

	private Player buildDefaultPlayer() {
		return new PlayerBuilder().setUserId(PLAYER_ID).setLastBattleId(BATTLE_ID).setBotMmr(BOT_MMR_INITIAL).createPlayer();
	}

	private Battle buildDefaultBattleWithWinner(String winnerId) {
		return new Battle(BATTLE_ID, buildBattlePlayers(), "catalogId", 0, MAP_ID, new Date(), true, new Date(), true, new Date(), winnerId, "",
				MatchType.CHALLENGE, false, new TeamStats(), new TeamStats());
	}

	private Battle buildDefaultBotBattleWithWinner(String winnerId) {
		return new Battle(BATTLE_ID, buildBattlePlayersWithBot(), "catalogId", 0, MAP_ID, new Date(), true, new Date(), true, new Date(), winnerId,
				"", MatchType.CHALLENGE, false, new TeamStats(), new TeamStats());
	}

	private Battle buildANonFinishedBattle() {
		return new Battle(BATTLE_ID, buildBattlePlayers(), "catalogId", 0, MAP_ID, new Date(), true, new Date(), false, null, "", "",
				MatchType.CHALLENGE, false, new TeamStats(), new TeamStats());
	}

	private List<BattlePlayer> buildBattlePlayers() {
		return Arrays.asList(new BattlePlayerBuilder().setUserId(PLAYER_ID).setBotMmr(BOT_MMR_INITIAL).setCatalogId("catalogId").createBattlePlayer(),
				new BattlePlayerBuilder().setUserId(OPPONENT_PLAYER_ID).setCatalogId("catalogId").createBattlePlayer());
	}

	private List<BattlePlayer> buildBattlePlayersWithBot() {
		return Arrays.asList(new BattlePlayerBuilder().setUserId(PLAYER_ID).setBotMmr(BOT_MMR_INITIAL).setCatalogId("catalogId").createBattlePlayer(),
				new BattlePlayerBuilder().setUserId(OPPONENT_PLAYER_ID).setCatalogId("catalogId").setIsBot(true).createBattlePlayer());
	}

	private void whenFinishingTheBattle() {
		finishPlayerBattleAction.finishPlayerBattle(PLAYER_ID, BATTLE_ID);
	}

}
