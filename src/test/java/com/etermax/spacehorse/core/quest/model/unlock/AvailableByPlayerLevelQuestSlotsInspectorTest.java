package com.etermax.spacehorse.core.quest.model.unlock;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.model.unlock.strategies.AvailableByPlayerLevelQuestSlotsInspector;
import com.etermax.spacehorse.mock.QuestSlotsInspectorScenarioBuilder;

public class AvailableByPlayerLevelQuestSlotsInspectorTest {

	private static final int PLAYER_LEVEL_0 = 0;
	private static final int PLAYER_LEVEL_1 = 1;
	private static final int PLAYER_LEVEL_3 = 3;
	private static final int PLAYER_LEVEL_4 = 4;
	private static final int PLAYER_LEVEL_5 = 5;
	private static final int PLAYER_LEVEL_7 = 7;
	private AvailableByPlayerLevelQuestSlotsInspector availableByPlayerLevelQuestSlotsInspector;

	@Before
	public void setUp() throws Exception {
		availableByPlayerLevelQuestSlotsInspector = new QuestSlotsInspectorScenarioBuilder().build();
	}

	@Test
	public void whenPlayerLevel0AndCurrentDifficultyEasyThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyEasy = QuestDifficultyType.EASY;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_0, currentDifficultyEasy);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel0AndCurrentDifficultyMediumThenTheQuestSlotDifficultyShouldNotBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyMedium = QuestDifficultyType.MEDIUM;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_0, currentDifficultyMedium);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void whenPlayerLevel0AndCurrentDifficultyHardThenTheQuestSlotDifficultyShouldNotBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyHard = QuestDifficultyType.HARD;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_0, currentDifficultyHard);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void whenPlayerLevel1AndCurrentDifficultyEasyThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyEasy = QuestDifficultyType.EASY;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_1, currentDifficultyEasy);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel1AndCurrentDifficultyMediumThenTheQuestSlotDifficultyShouldNotBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyMedium = QuestDifficultyType.MEDIUM;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_1, currentDifficultyMedium);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void whenPlayerLevel1AndCurrentDifficultyHardThenTheQuestSlotDifficultyShouldotBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyHard = QuestDifficultyType.HARD;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_1, currentDifficultyHard);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void whenPlayerLevel3AndCurrentDifficultyEasyThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyEasy = QuestDifficultyType.EASY;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_3, currentDifficultyEasy);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel3AndCurrentDifficultyMediumThenTheQuestSlotDifficultyShouldNotBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyMedium = QuestDifficultyType.MEDIUM;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_3, currentDifficultyMedium);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void whenPlayerLevel3AndCurrentDifficultyHardThenTheQuestSlotDifficultyShouldotBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyHard = QuestDifficultyType.HARD;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_3, currentDifficultyHard);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void whenPlayerLevel4AndCurrentDifficultyEasyThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyEasy = QuestDifficultyType.EASY;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_4, currentDifficultyEasy);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel4AndCurrentDifficultyMediumThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyMedium = QuestDifficultyType.MEDIUM;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_4, currentDifficultyMedium);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel4AndCurrentDifficultyHardThenTheQuestSlotDifficultyShouldotBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyHard = QuestDifficultyType.HARD;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_4, currentDifficultyHard);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void whenPlayerLevel5AndCurrentDifficultyEasyThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyEasy = QuestDifficultyType.EASY;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_5, currentDifficultyEasy);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel5AndCurrentDifficultyMediumThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyMedium = QuestDifficultyType.MEDIUM;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_5, currentDifficultyMedium);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel5AndCurrentDifficultyHardThenTheQuestSlotDifficultyShouldNotBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyHard = QuestDifficultyType.HARD;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_5, currentDifficultyHard);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void whenPlayerLevel7AndCurrentDifficultyEasyThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyEasy = QuestDifficultyType.EASY;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_7, currentDifficultyEasy);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel7AndCurrentDifficultyMediumThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyMedium = QuestDifficultyType.MEDIUM;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_7, currentDifficultyMedium);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel7AndCurrentDifficultyHardThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyHard = QuestDifficultyType.HARD;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_7, currentDifficultyHard);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel8AndCurrentDifficultyEasyThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyEasy = QuestDifficultyType.EASY;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_7, currentDifficultyEasy);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel8AndCurrentDifficultyMediumThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyMedium = QuestDifficultyType.MEDIUM;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_7, currentDifficultyMedium);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenPlayerLevel8AndCurrentDifficultyHardThenTheQuestSlotDifficultyShouldBeAvailable() throws Exception {
		// given
		QuestDifficultyType currentDifficultyHard = QuestDifficultyType.HARD;
		// when
		boolean isAvailable = availableByPlayerLevelQuestSlotsInspector.isAvailable(PLAYER_LEVEL_7, currentDifficultyHard);
		// then
		assertThat(isAvailable).isTrue();
	}
}
