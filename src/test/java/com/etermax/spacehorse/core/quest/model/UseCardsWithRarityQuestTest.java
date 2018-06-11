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
import com.etermax.spacehorse.core.battle.model.UsedCardInfo;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.catalog.resource.response.CardRarityResponse;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.handler.DefaultQuestProgressHandlerFactory;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class UseCardsWithRarityQuestTest {

	private final int GOAL_AMOUNT = 5;
	private final int REQUIRED_RARITY = 2;

	private final String QUEST_ID = "questId";
	private final String CHEST_ID = "chestId";
	private final String BATTLE_ID = "battleId";
	private final String PLAYER_ID = "playerId";
	private final String OPPONENT_PLAYER_ID = "opponentPlayerId";
	private final String MAP_ID = "mapId";

	private final String CARD_ID_WITH_RARITY = "cardIdWithRarity";
	private final String CARD_ID_WITHOUT_RARITY = "cardIdWithoutRarity";

	private Quest quest;
	private Player player;
	private DefaultQuestProgressHandlerFactory progressHandlerFactory;
	private QuestDefinition questDefinition;
	private ServerTimeProvider timeProvider;

	@Before
	public void setUp() {
		timeProvider = new FixedServerTimeProvider();
		player = new PlayerScenarioBuilder(PLAYER_ID).build();

		questDefinition = new QuestDefinition(QUEST_ID, QuestType.QUEST_USE_CARDS_WITH_RARITY, CHEST_ID, GOAL_AMOUNT, REQUIRED_RARITY,
				QuestDifficultyType.EASY.toString());

		progressHandlerFactory = new DefaultQuestProgressHandlerFactory(Arrays.asList(buildCardDefinition(CARD_ID_WITH_RARITY, REQUIRED_RARITY),
				buildCardDefinition(CARD_ID_WITHOUT_RARITY, REQUIRED_RARITY - 1)), Arrays.asList(questDefinition));
	}

	private CardDefinition buildCardDefinition(String cardId, int rarity) {
		return new CardDefinition(cardId, CardRarityResponse.toCardRarityEnum(rarity), 0);
	}

	@After
	public void tearDown() {
		player = null;
		quest = null;
	}

	@Test
	public void givenACardsWithRarityQuestUsingCardsWithValidRarityCompletesTheQuest() {

		givenACardsWithRarityQuest();

		whenGoalCardsAreUsedInBattles(CARD_ID_WITH_RARITY);

		thenTheQuestIsComplete();
	}

	@Test
	public void givenACardsWithRarityQuestUsingCardsWithoutValidRarityDoesntCompleteTheQuest() {

		givenACardsWithRarityQuest();

		whenGoalCardsAreUsedInBattles(CARD_ID_WITHOUT_RARITY);

		thenTheQuestIsNotComplete();
	}

	private void thenTheQuestIsNotComplete() {
		assertFalse(quest.isCompleted());
	}

	private void thenTheQuestIsComplete() {
		assertTrue(quest.isCompleted());
	}

	private void whenGoalCardsAreUsedInBattles(String cardId) {
		for (int i = 0; i < GOAL_AMOUNT; i++) {
			assertFalse(quest.isCompleted());
			quest.handleBattleEnded(buildDefaultBattleWithUsedCard(cardId, 1), player, progressHandlerFactory, timeProvider.getTimeNowAsSeconds());
		}
	}

	private Battle buildDefaultBattleWithUsedCard(String usedCardId, int usedCardAmount) {
		return new Battle(BATTLE_ID, buildBattlePlayers(), "catalogId", 0, MAP_ID, new Date(), true, new Date(), true, new Date(), "", "",
				MatchType.CHALLENGE, false, buildTeamStats(usedCardId, usedCardAmount), new TeamStats());
	}

	private TeamStats buildTeamStats(String usedCardId, int usedCardAmount) {
		return new TeamStats(0, false, Arrays.asList(new UsedCardInfo(usedCardId, usedCardAmount)));
	}

	private List<BattlePlayer> buildBattlePlayers() {
		return Arrays.asList(new BattlePlayerBuilder().setUserId(PLAYER_ID).setCatalogId("catalogId").createBattlePlayer(),
				new BattlePlayerBuilder().setUserId(OPPONENT_PLAYER_ID).setCatalogId("catalogId").createBattlePlayer());
	}

	private void givenACardsWithRarityQuest() {
		quest = new Quest(questDefinition);
	}
}
