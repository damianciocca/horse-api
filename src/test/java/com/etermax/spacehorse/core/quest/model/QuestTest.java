package com.etermax.spacehorse.core.quest.model;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class QuestTest {

	private final String CHEST_ID = "chestId";
	private final String QUEST_ID = "questId";
	private final String BATTLE_ID = "battleId";
	private final String PLAYER_ID = "playerId";
	private final String OPPONENT_PLAYER_ID = "opponentPlayerId";
	private final String MAP_ID = "mapId";
	private final int GOAL_AMOUNT = 10;

	private FixedServerTimeProvider serverTimeProvider;
	private DefaultQuestProgressHandlerFactory progressHandlerFactory;

	@Before
	public void setUp() throws Exception {
		serverTimeProvider = new FixedServerTimeProvider();
		progressHandlerFactory = new DefaultQuestProgressHandlerFactory();
	}

	@Test
	public void testQuestIsCompleteWhenBattlesAreWon() {
		Player player = buildDefaultPlayer();
		QuestDefinition questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_SIMPLE_VICTORIES, CHEST_ID, GOAL_AMOUNT, EASY.toString());
		Quest quest = new Quest(questDefinition);

		for (long i = 0; i < GOAL_AMOUNT; i++) {
			quest.handleBattleEnded(buildDefaultBattleWithWinner(player.getUserId()), player, progressHandlerFactory,
					serverTimeProvider.getTimeNowAsSeconds());
		}

		assertTrue(quest.isCompleted());
	}

	private Battle buildDefaultBattleWithWinner(String winnerId) {
		return new Battle(BATTLE_ID, buildBattlePlayers(), "catalogId", 0, MAP_ID, new Date(), true, new Date(), true, new Date(), winnerId, "",
				MatchType.CHALLENGE, false, new TeamStats(), new TeamStats());
	}

	private List<BattlePlayer> buildBattlePlayers() {
		return Arrays.asList(new BattlePlayerBuilder().setUserId(PLAYER_ID).setCatalogId("catalogId").createBattlePlayer(),
				new BattlePlayerBuilder().setUserId(OPPONENT_PLAYER_ID).setCatalogId("catalogId").createBattlePlayer());
	}

	private Player buildDefaultPlayer() {
		return new PlayerScenarioBuilder(PLAYER_ID).build();
	}

	@Test
	public void testQuestIsCompleteWithCheat() {
		QuestDefinition questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_SIMPLE_VICTORIES, CHEST_ID, GOAL_AMOUNT, EASY.toString());
		Quest quest = new Quest(questDefinition);
		quest.cheatSetRemainingAmount(0);

		assertTrue(quest.isCompleted());
	}

	@Test
	public void testQuestIsIncomplete() {
		Player player = buildDefaultPlayer();
		QuestDefinition questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_SIMPLE_VICTORIES, CHEST_ID, GOAL_AMOUNT, EASY.toString());
		Quest quest = new Quest(questDefinition);

		for (long i = 0; i < GOAL_AMOUNT - 1; i++) {
			quest.handleBattleEnded(buildDefaultBattleWithWinner(player.getUserId()), player, progressHandlerFactory,
					serverTimeProvider.getTimeNowAsSeconds());
		}

		assertFalse(quest.isCompleted());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCannotCheatNegativeRemainingAmount() {
		QuestDefinition questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_SIMPLE_VICTORIES, CHEST_ID, GOAL_AMOUNT, EASY.toString());
		Quest quest = new Quest(questDefinition);

		quest.cheatSetRemainingAmount(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCannotCheatExcessiveRemainingAmount() {
		QuestDefinition questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_SIMPLE_VICTORIES, CHEST_ID, GOAL_AMOUNT, EASY.toString());
		Quest quest = new Quest(questDefinition);

		quest.cheatSetRemainingAmount(GOAL_AMOUNT + 1);
	}

	@Test
	public void testCheatRemainingAmount() {
		QuestDefinition questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_SIMPLE_VICTORIES, CHEST_ID, GOAL_AMOUNT, EASY.toString());
		Quest quest = new Quest(questDefinition);

		for (long cheatAmount = GOAL_AMOUNT; cheatAmount >= 0; cheatAmount--) {
			quest.cheatSetRemainingAmount(cheatAmount);
			assertEquals(cheatAmount, quest.getRemainingAmount());
		}
	}

	@Test
	public void testQuestRefreshTimeIsNotExpired() {
		long now = serverTimeProvider.getTimeNowAsSeconds();
		long refreshTimeInSeconds = serverTimeProvider.getTimeNowAsSeconds() + 5;
		QuestDefinition questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_SIMPLE_VICTORIES, CHEST_ID, GOAL_AMOUNT, EASY.toString());
		Quest quest = new Quest(questDefinition);
		quest.claim();
		quest.setRefreshTimeInSeconds(refreshTimeInSeconds);

		boolean refreshTimeIsNotExpired = quest.refreshTimeIsNotExpired(now + 1);

		assertTrue(refreshTimeIsNotExpired);
	}

	@Test
	public void testQuestRefreshTimeIsExpired() {
		long now = serverTimeProvider.getTimeNowAsSeconds();
		long refreshTimeInSeconds = serverTimeProvider.getTimeNowAsSeconds() + 5;
		QuestDefinition questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_SIMPLE_VICTORIES, CHEST_ID, GOAL_AMOUNT, EASY.toString());
		Quest quest = new Quest(questDefinition);
		quest.claim();
		quest.refreshTimeIsNotExpired(refreshTimeInSeconds);

		boolean refreshTimeIsNotExpired = quest.refreshTimeIsNotExpired(now + 10);

		assertFalse(refreshTimeIsNotExpired);
	}

}
