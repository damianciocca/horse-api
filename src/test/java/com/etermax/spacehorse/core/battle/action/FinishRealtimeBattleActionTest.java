package com.etermax.spacehorse.core.battle.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.MmrCalculatorDomainService;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.battle.repository.dynamo.DynamoPlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.dynamo.PlayerWinRateDynamoRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.club.infrastructure.ClubReporter;
import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.etermax.spacehorse.mock.BattlePlayerScenarioBuilder;
import com.etermax.spacehorse.mock.MockCatalog;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.google.common.collect.Lists;

public class FinishRealtimeBattleActionTest {

	private static final String BATTLE_ID = "1";
	private static final String PLAYER_1_ID = "10";
	private static final String PLAYER_2_ID = "20";
	private static final boolean IS_NOT_TUTORIAL_BATTLE = false;
	private static final boolean IS_TUTORIAL_BATTLE = true;
	private static final boolean WIN_FULL_SCORE = true;
	private static final String NO_WINNER_ID = "";
	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();
	private FinishRealtimeBattleAction finishRealtimeBattleAction;
	private PlayerWinRateRepository playerWinRateRepository;
	private BattlePlayer battlePlayer1;
	private BattlePlayer battlePlayer2;
	private CatalogRepository catalogRepository;
	private MmrCalculatorDomainService mmrCalculatorDomainService;
	private PlayerRepository playerRepository;

	@Before
	public void setUp() {
		RULE.createSimpleTable(DynamoPlayerWinRate.class);

		Catalog catalog = MockCatalog.buildCatalog();
		battlePlayer1 = new BattlePlayerScenarioBuilder(PLAYER_1_ID).build();
		battlePlayer2 = new BattlePlayerScenarioBuilder(PLAYER_2_ID).build();

		catalogRepository = mock(CatalogRepository.class);
		when(catalogRepository.getActiveCatalogWithTag(any())).thenReturn(catalog);
		when(catalogRepository.find(catalog.getCatalogId())).thenReturn(catalog);

		mmrCalculatorDomainService = new MmrCalculatorDomainService(catalogRepository);

		Player player1 = new PlayerScenarioBuilder(PLAYER_1_ID).withActiveTutorial().build();
		playerRepository = mock(PlayerRepository.class);
		when(playerRepository.find(PLAYER_1_ID)).thenReturn(player1);

		playerWinRateRepository = new PlayerWinRateDynamoRepository(new DynamoDao(RULE.getAmazonDynamoDB()));
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	@Test
	public void whenPlayer1WonThenTheWinRatesShouldBeThePlayer1WonAndPlayer1Lost() throws Exception {
		boolean finishedBattle = true;
		givenABattleFor(PLAYER_1_ID, finishedBattle, IS_NOT_TUTORIAL_BATTLE);

		boolean isFinish = whenFinishBattle(WIN_FULL_SCORE, PLAYER_1_ID);

		thenThePlayer1Won(isFinish);
	}

	@Test
	public void whenPlayer1WonTheFirstTutorialBattleThenTheWinRatesShouldBeZero() throws Exception {
		boolean finishedBattle = true;
		givenABattleFor(PLAYER_1_ID, finishedBattle, IS_TUTORIAL_BATTLE);

		boolean isFinish = whenFinishBattle(WIN_FULL_SCORE, PLAYER_1_ID);

		thenThePlayer1WonATutorialBattle(isFinish);
	}

	@Test
	public void whenPlayer1LostThenTheWinRatesShouldBeThePlayer1LostAndPlayer1Won() throws Exception {
		boolean finishedBattle = true;
		givenABattleFor(PLAYER_2_ID, finishedBattle, IS_NOT_TUTORIAL_BATTLE);

		boolean isFinish = whenFinishBattle(WIN_FULL_SCORE, PLAYER_2_ID);

		thenThePlayer2Won(isFinish);
	}

	@Test
	public void whenPlayer1DidNotFinishTheBattleThenTheWinRatesShouldNotBeUpdated() throws Exception {
		boolean finishedBattle = false;
		givenABattleFor(PLAYER_1_ID, finishedBattle, IS_NOT_TUTORIAL_BATTLE);

		boolean isFinish = whenFinishBattle(WIN_FULL_SCORE, PLAYER_1_ID);

		thenThePlayer1HasNotFinishedTheBattle(isFinish);
	}

	@Test
	public void whenBothPlayersTiedThenTheWinRatesShouldBeThePlayer1TiedAndPlayer1Tied() throws Exception {
		boolean finishedBattle = true;
		givenABattleFor(NO_WINNER_ID, finishedBattle, IS_NOT_TUTORIAL_BATTLE);

		boolean isFinish = whenFinishBattle(WIN_FULL_SCORE, NO_WINNER_ID);

		thenTheBothPlayersTied(isFinish);
	}

	private void givenABattleFor(String winnerId, boolean finished, boolean isTutorialBattle) {
		Battle battle = aBattle(battlePlayer1, battlePlayer2, finished, winnerId, isTutorialBattle);
		BattleRepository battleRepository = aBattleRepository(battle);
		ClubReporter clubReporter = mock(ClubReporter.class);
		finishRealtimeBattleAction = new FinishRealtimeBattleAction(battleRepository, playerRepository, catalogRepository, playerWinRateRepository,
				mmrCalculatorDomainService, clubReporter);
	}

	private boolean whenFinishBattle(boolean isWinFullScore, String winnerId) {
		return finishRealtimeBattleAction.finishBattle(BATTLE_ID, winnerId, isWinFullScore, new TeamStats(1, false, new ArrayList<>()),
				new TeamStats(2, false, new ArrayList<>()));
	}

	private void thenThePlayer1WonATutorialBattle(boolean finished) {
		assertThat(finished).isTrue();
		thenAssertPlayerWinRateFor(PLAYER_1_ID, 0, 0, 0, 0);
		thenAssertPlayerWinRateFor(PLAYER_2_ID, 0, 0, 0, 0);
	}

	private void thenTheBothPlayersTied(boolean finished) {
		assertThat(finished).isTrue();
		thenAssertPlayerWinRateFor(PLAYER_1_ID, 0, 0, 1, 0);
		thenAssertPlayerWinRateFor(PLAYER_2_ID, 0, 0, 1, 0);
	}

	private void thenThePlayer2Won(boolean finished) {
		assertThat(finished).isTrue();
		thenAssertPlayerWinRateFor(PLAYER_1_ID, 0, 1, 0, 0);
		thenAssertPlayerWinRateFor(PLAYER_2_ID, 1, 0, 0, 30);
	}

	private void thenThePlayer1Won(boolean finished) {
		assertThat(finished).isTrue();
		thenAssertPlayerWinRateFor(PLAYER_1_ID, 1, 0, 0, 30);
		thenAssertPlayerWinRateFor(PLAYER_2_ID, 0, 1, 0, 0);
	}

	private void thenThePlayer1HasNotFinishedTheBattle(boolean finished) {
		assertThat(finished).isFalse();
		thenAssertPlayerWinRateFor(PLAYER_1_ID, 0, 0, 0, 0);
		thenAssertPlayerWinRateFor(PLAYER_2_ID, 0, 0, 0, 0);
	}

	private void thenAssertPlayerWinRateFor(String playerId, int won, int lose, int tie, int mmr) {
		PlayerWinRate winner = this.playerWinRateRepository.findOrCrateDefault(playerId);
		assertThat(winner.getWin()).isEqualTo(won);
		assertThat(winner.getLose()).isEqualTo(lose);
		assertThat(winner.getTie()).isEqualTo(tie);
		assertThat(winner.getMmr()).isEqualTo(mmr);
	}

	private BattleRepository aBattleRepository(Battle battle) {
		BattleRepository battleRepository = mock(BattleRepository.class);
		when(battleRepository.find(BATTLE_ID)).thenReturn(Optional.of(battle));
		return battleRepository;
	}

	private Battle aBattle(BattlePlayer battlePlayer1, BattlePlayer battlePlayer2, boolean finished, String winnerId, boolean isTutorialBattle) {
		Battle battle = mock(Battle.class);
		when(battle.finish(any(), anyString(), anyBoolean(), any(), any())).thenReturn(finished);
		when(battle.getPlayers()).thenReturn(Lists.newArrayList(battlePlayer1, battlePlayer2));
		when(battle.getWinnerLoginId()).thenReturn(winnerId);
		when(battle.getMatchType()).thenReturn(isTutorialBattle ? MatchType.TUTORIAL : MatchType.CHALLENGE);
		return battle;
	}

}
