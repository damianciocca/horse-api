package com.etermax.spacehorse.core.quest.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.handler.DefaultQuestProgressHandlerFactory;
import com.etermax.spacehorse.core.quest.model.unlock.strategies.AvailableByPlayerLevelQuestSlotsInspector;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.BattleScenarioBuilder;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.etermax.spacehorse.mock.QuestScenarioBuilder;
import com.etermax.spacehorse.mock.QuestSlotsInspectorScenarioBuilder;

public class QuestBoardTest {

	public static final String PLAYER_ID = "100";
	private FixedServerTimeProvider serverTimeProvider;
	private QuestBoard questBoard;
	private DefaultQuestProgressHandlerFactory defaultQuestProgressHandlerFactory;
	private int winnerScore;

	@Before
	public void setUp() throws Exception {
		serverTimeProvider = new FixedServerTimeProvider();
		Quest dailyQuest = mock(Quest.class);
		questBoard = new QuestBoard(serverTimeProvider, getQuestBoardConfiguration(), dailyQuest);
		Quest quest1 = new QuestScenarioBuilder().buildDefaultQuest();
		Quest quest2 = new QuestScenarioBuilder().buildDefaultQuest();
		Quest quest3 = new QuestScenarioBuilder().buildDefaultQuest();
		questBoard.putFreeQuest(QuestDifficultyType.EASY.toString(), quest1);
		questBoard.putFreeQuest(QuestDifficultyType.MEDIUM.toString(), quest2);
		questBoard.putFreeQuest(QuestDifficultyType.HARD.toString(), quest3);
		defaultQuestProgressHandlerFactory = new DefaultQuestProgressHandlerFactory();
		winnerScore = 3;
	}

	@Test
	public void whenPlayerIsInLevel1ThenTheProgressOfActiveQuestInSlotEasyShouldBeUpdated() {
		// given
		int playerLevel = 1;
		int startLevelForEasyDifficulty = 1;
		Battle battle = new BattleScenarioBuilder(serverTimeProvider, PLAYER_ID, winnerScore).build();
		Player player = new PlayerScenarioBuilder(PLAYER_ID).withLevel(playerLevel).build();
		AvailableByPlayerLevelQuestSlotsInspector availableByPlayerLevelQuestSlotsInspector = new QuestSlotsInspectorScenarioBuilder(
				startLevelForEasyDifficulty).build();
		// when
		boolean updated = questBoard
				.updateQuestsProgress(defaultQuestProgressHandlerFactory, battle, player, availableByPlayerLevelQuestSlotsInspector);
		// then
		assertThat(updated).isTrue();
		assertThat(questBoard.getSlot(QuestDifficultyType.EASY.toString()).getActiveQuest().getCurrentProgress()).isEqualTo(winnerScore);
		assertThat(questBoard.getSlot(QuestDifficultyType.MEDIUM.toString()).getActiveQuest().getCurrentProgress()).isEqualTo(0);
		assertThat(questBoard.getSlot(QuestDifficultyType.HARD.toString()).getActiveQuest().getCurrentProgress()).isEqualTo(0);
	}

	@Test
	public void whenPlayerIsInLevel1ThenTheProgressOfActiveQuestInEachSlotShouldNotBeUpdated() {
		// given
		int playerLevel = 1;
		int startLevelForEasyDifficulty = 2;
		Battle battle = new BattleScenarioBuilder(serverTimeProvider, PLAYER_ID, winnerScore).build();
		Player player = new PlayerScenarioBuilder(PLAYER_ID).withLevel(playerLevel).build();
		AvailableByPlayerLevelQuestSlotsInspector availableByPlayerLevelQuestSlotsInspector = new QuestSlotsInspectorScenarioBuilder(
				startLevelForEasyDifficulty).build();
		// when
		boolean updated = questBoard
				.updateQuestsProgress(defaultQuestProgressHandlerFactory, battle, player, availableByPlayerLevelQuestSlotsInspector);
		// then
		assertThat(updated).isFalse();
		assertThat(questBoard.getSlot(QuestDifficultyType.EASY.toString()).getActiveQuest().getCurrentProgress()).isEqualTo(0);
		assertThat(questBoard.getSlot(QuestDifficultyType.MEDIUM.toString()).getActiveQuest().getCurrentProgress()).isEqualTo(0);
		assertThat(questBoard.getSlot(QuestDifficultyType.HARD.toString()).getActiveQuest().getCurrentProgress()).isEqualTo(0);
	}

	@Test
	public void whenPlayerIsInLevel10ThenTheProgressOfAllActiveQuestShouldBeUpdated() {
		// given
		int playerLevel = 10;
		int startLevelForEasyDifficulty = 2;
		Battle battle = new BattleScenarioBuilder(serverTimeProvider, PLAYER_ID, winnerScore).build();
		Player player = new PlayerScenarioBuilder(PLAYER_ID).withLevel(playerLevel).build();
		AvailableByPlayerLevelQuestSlotsInspector availableByPlayerLevelQuestSlotsInspector = new QuestSlotsInspectorScenarioBuilder(
				startLevelForEasyDifficulty).build();
		// when
		boolean updated = questBoard
				.updateQuestsProgress(defaultQuestProgressHandlerFactory, battle, player, availableByPlayerLevelQuestSlotsInspector);
		// then
		assertThat(updated).isTrue();
		assertThat(questBoard.getSlot(QuestDifficultyType.EASY.toString()).getActiveQuest().getCurrentProgress()).isEqualTo(winnerScore);
		assertThat(questBoard.getSlot(QuestDifficultyType.MEDIUM.toString()).getActiveQuest().getCurrentProgress()).isEqualTo(winnerScore);
		assertThat(questBoard.getSlot(QuestDifficultyType.HARD.toString()).getActiveQuest().getCurrentProgress()).isEqualTo(winnerScore);
	}

	private QuestBoardConfiguration getQuestBoardConfiguration() {
		return new QuestBoardConfiguration(100, 10, 720, 1);
	}
}
