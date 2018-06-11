package com.etermax.spacehorse.core.quest.model;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.BattlePlayerBuilder;
import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.handler.DefaultQuestProgressHandlerFactory;
import com.etermax.spacehorse.core.quest.model.handler.QuestProgressHandlerFactory;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class FullScoreVictoriesQuestTest {

	private final int GOAL_AMOUNT = 5;

	private final String QUEST_ID = "questId";
	private final String CHEST_ID = "chestId";
	private final String BATTLE_ID = "battleId";
	private final String PLAYER_ID = "playerId";
	private final String OPPONENT_PLAYER_ID = "opponentPlayerId";
	private final String MAP_ID = "mapId";

	private Quest quest;
	private Player player;
	private QuestProgressHandlerFactory progressHandlerFactory;
	private FixedServerTimeProvider timeProvider;

	@Before
	public void setUp() {
		timeProvider = new FixedServerTimeProvider();
		player = new PlayerScenarioBuilder(PLAYER_ID).build();
		progressHandlerFactory = new DefaultQuestProgressHandlerFactory();
	}

	@After
	public void tearDown() {
		player = null;
		quest = null;
	}

	@Test
	public void givenAFullScoreVictoriesQuestThenWinningFullScoreBattlesCompletesTheQuest() {

		givenAFullScoreVictoriesQuest();

		whenGoalBattlesAreWonWithFullScore();

		thenTheQuestIsComplete();
	}

	@Test
	public void givenAFullScoreVictoriesQuestThenWinningBattleWithoutFullScoreDoesntTheQuest() {

		givenAFullScoreVictoriesQuest();

		whenGoalBattlesAreWonWithoutFullScore();

		thenTheQuestIsNotComplete();
	}

	@Test
	public void givenAFullScoreVictoriesQuestThenLosingBattlesDoesntTheQuest() {

		givenAFullScoreVictoriesQuest();

		whenGoalBattlesAreLost();

		thenTheQuestIsNotComplete();
	}

	private void thenTheQuestIsNotComplete() {
		assertFalse(quest.isCompleted());
	}

	private void thenTheQuestIsComplete() {
		assertTrue(quest.isCompleted());
	}

	private void whenGoalBattlesAreWonWithFullScore() {
		for (int i = 0; i < GOAL_AMOUNT; i++) {
			assertFalse(quest.isCompleted());
			quest.handleBattleEnded(buildDefaultBattleWithWinner(player.getUserId(), true), player, progressHandlerFactory,
					timeProvider.getTimeNowAsSeconds());
		}
	}

	private void whenGoalBattlesAreWonWithoutFullScore() {
		for (int i = 0; i < GOAL_AMOUNT; i++) {
			assertFalse(quest.isCompleted());
			quest.handleBattleEnded(buildDefaultBattleWithWinner(player.getUserId(), false), player, progressHandlerFactory,
					timeProvider.getTimeNowAsSeconds());
		}
	}

	private void whenGoalBattlesAreLost() {
		for (int i = 0; i < GOAL_AMOUNT; i++) {
			assertFalse(quest.isCompleted());
			quest.handleBattleEnded(buildDefaultBattleWithWinner(OPPONENT_PLAYER_ID, true), player, progressHandlerFactory,
					timeProvider.getTimeNowAsSeconds());
		}
	}

	private Battle buildDefaultBattleWithWinner(String winnerId, boolean fullScoreVictory) {
		return new Battle(BATTLE_ID, buildBattlePlayers(), "catalogId", 0, MAP_ID, new Date(), true, new Date(), true, new Date(), winnerId, "",
				MatchType.CHALLENGE, fullScoreVictory, new TeamStats(), new TeamStats());
	}

	private List<BattlePlayer> buildBattlePlayers() {
		return Arrays.asList(new BattlePlayerBuilder().setUserId(PLAYER_ID).setCatalogId("catalogId").createBattlePlayer(),
				new BattlePlayerBuilder().setUserId(OPPONENT_PLAYER_ID).setCatalogId("catalogId").createBattlePlayer());
	}

	private void givenAFullScoreVictoriesQuest() {
		QuestDefinition questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_FULL_SCORE_VICTORIES, CHEST_ID, GOAL_AMOUNT,
				QuestDifficultyType.EASY.toString());
		quest = new Quest(questDefinition);
	}

}
