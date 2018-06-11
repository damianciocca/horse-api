package com.etermax.spacehorse.core.quest.model;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.BattlePlayerBuilder;
import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.etermax.spacehorse.core.battle.model.UsedCardInfo;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.quest.model.handler.DefaultQuestProgressHandlerFactory;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class UseAllCardsAtLeastOnceQuestTest {

	private final int GOAL_AMOUNT = 5;

	private final String QUEST_ID = "questId";
	private final String CHEST_ID = "chestId";
	private final String BATTLE_ID = "battleId";
	private final String PLAYER_ID = "playerId";
	private final String OPPONENT_PLAYER_ID = "opponentPlayerId";
	private final String MAP_ID = "mapId";

	private final String CARD_ID_1 = "cardId1";
	private final String CARD_ID_2 = "cardId2";
	private final String CARD_ID_3 = "cardId3";

	private Quest quest;
	private Player player;
	private DefaultQuestProgressHandlerFactory progressHandlerFactory;
	private QuestDefinition questDefinition;
	private ServerTimeProvider timeProvider;

	@Before
	public void setUp() {
		timeProvider = new FixedServerTimeProvider();
		player = new PlayerScenarioBuilder(PLAYER_ID).build();

		questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_USE_ALL_CARDS_AT_LEAST_ONCE, CHEST_ID, GOAL_AMOUNT,
				QuestDifficultyType.EASY.toString());

		progressHandlerFactory = new DefaultQuestProgressHandlerFactory(
				Arrays.asList(buildCardDefinition(CARD_ID_1), buildCardDefinition(CARD_ID_2), buildCardDefinition(CARD_ID_3)),
				Arrays.asList(questDefinition));
	}

	private CardDefinition buildCardDefinition(String cardId) {
		return new CardDefinition(cardId, CardRarity.COMMON, 0);
	}

	@After
	public void tearDown() {
		player = null;
		quest = null;
	}

	@Test
	public void usingAllCardsCompletesQuest() {

		givenUseAllCardsAtLeastOnceQuest();

		whenGoalCardsAreUsedInBattles(CARD_ID_1, CARD_ID_2, CARD_ID_3);

		thenTheQuestIsComplete();
	}

	@Test
	public void notUsingAllCardsDontCompletesQuest() {

		givenUseAllCardsAtLeastOnceQuest();

		whenGoalCardsAreUsedInBattles(CARD_ID_1, CARD_ID_3);

		thenTheQuestIsNotComplete();
	}

	private void thenTheQuestIsNotComplete() {
		assertFalse(quest.isCompleted());
	}

	private void thenTheQuestIsComplete() {
		assertTrue(quest.isCompleted());
	}

	private void whenGoalCardsAreUsedInBattles(String... cardIds) {
		for (int i = 0; i < GOAL_AMOUNT; i++) {
			assertFalse(quest.isCompleted());
			quest.handleBattleEnded(buildDefaultBattleWithUsedCards(cardIds), player, progressHandlerFactory, timeProvider.getTimeNowAsSeconds());
		}
	}

	private Battle buildDefaultBattleWithUsedCards(String[] cardIds) {
		return new Battle(BATTLE_ID, buildBattlePlayers(), "catalogId", 0, MAP_ID, new Date(), true, new Date(), true, new Date(), "", "",
				MatchType.CHALLENGE, false, buildTeamStats(cardIds), new TeamStats());
	}

	private TeamStats buildTeamStats(String[] usedCardIds) {
		return new TeamStats(0, false, Arrays.asList(usedCardIds).stream().map(x -> new UsedCardInfo(x, 1)).collect(Collectors.toList()));
	}

	private List<BattlePlayer> buildBattlePlayers() {
		return Arrays.asList(new BattlePlayerBuilder().setUserId(PLAYER_ID).setSelectedCards(buildSelectedCards()).setCatalogId("catalogId")
						.createBattlePlayer(),
				new BattlePlayerBuilder().setUserId(OPPONENT_PLAYER_ID).setSelectedCards(buildSelectedCards()).setCatalogId("catalogId")
						.createBattlePlayer());
	}

	private List<Card> buildSelectedCards() {
		return Arrays.asList(new Card(0L, CARD_ID_1, 0), new Card(1L, CARD_ID_2, 0), new Card(2L, CARD_ID_3, 0));
	}

	private void givenUseAllCardsAtLeastOnceQuest() {
		quest = new Quest(questDefinition);
	}
}
