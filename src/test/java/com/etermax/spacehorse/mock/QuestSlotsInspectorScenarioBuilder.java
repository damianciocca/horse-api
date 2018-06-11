package com.etermax.spacehorse.mock;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.HARD;
import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.MEDIUM;

import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.model.unlock.AvailableQuestSlotDifficultiesByPlayerLevel;
import com.etermax.spacehorse.core.quest.model.unlock.strategies.AvailableByPlayerLevelQuestSlotsInspector;
import com.etermax.spacehorse.core.quest.model.unlock.QuestSlotDifficultiesConfiguration;
import com.google.common.collect.Lists;

public class QuestSlotsInspectorScenarioBuilder {

	private static final int PLAYER_LEVEL_0 = 0;
	private static final int PLAYER_LEVEL_4 = 4;
	private static final int PLAYER_LEVEL_7 = 7;
	private static final int PLAYER_LEVEL_2 = 2;
	private final AvailableByPlayerLevelQuestSlotsInspector availableByPlayerLevelQuestSlotsInspector;

	public QuestSlotsInspectorScenarioBuilder() {
		AvailableQuestSlotDifficultiesByPlayerLevel level0 = new AvailableQuestSlotDifficultiesByPlayerLevel(PLAYER_LEVEL_0,
				QuestDifficultyType.getTypesUpTo(EASY.toString()));
		AvailableQuestSlotDifficultiesByPlayerLevel level4 = new AvailableQuestSlotDifficultiesByPlayerLevel(PLAYER_LEVEL_4,
				QuestDifficultyType.getTypesUpTo(MEDIUM.toString()));
		AvailableQuestSlotDifficultiesByPlayerLevel level7 = new AvailableQuestSlotDifficultiesByPlayerLevel(PLAYER_LEVEL_7,
				QuestDifficultyType.getTypesUpTo(HARD.toString()));
		QuestSlotDifficultiesConfiguration configuration = new QuestSlotDifficultiesConfiguration(Lists.newArrayList(level0, level4, level7));
		availableByPlayerLevelQuestSlotsInspector = new AvailableByPlayerLevelQuestSlotsInspector(configuration);
	}

	public QuestSlotsInspectorScenarioBuilder(int startLevelForEasyDifficulty) {
		AvailableQuestSlotDifficultiesByPlayerLevel level0 = new AvailableQuestSlotDifficultiesByPlayerLevel(startLevelForEasyDifficulty,
				QuestDifficultyType.getTypesUpTo(EASY.toString()));
		AvailableQuestSlotDifficultiesByPlayerLevel level4 = new AvailableQuestSlotDifficultiesByPlayerLevel(startLevelForEasyDifficulty + 1,
				QuestDifficultyType.getTypesUpTo(MEDIUM.toString()));
		AvailableQuestSlotDifficultiesByPlayerLevel level7 = new AvailableQuestSlotDifficultiesByPlayerLevel(startLevelForEasyDifficulty + 2,
				QuestDifficultyType.getTypesUpTo(HARD.toString()));
		QuestSlotDifficultiesConfiguration configuration = new QuestSlotDifficultiesConfiguration(Lists.newArrayList(level0, level4, level7));
		availableByPlayerLevelQuestSlotsInspector = new AvailableByPlayerLevelQuestSlotsInspector(configuration);
	}

	public AvailableByPlayerLevelQuestSlotsInspector build() {
		return availableByPlayerLevelQuestSlotsInspector;
	}

}
