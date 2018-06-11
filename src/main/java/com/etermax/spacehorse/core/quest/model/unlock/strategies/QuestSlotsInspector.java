package com.etermax.spacehorse.core.quest.model.unlock.strategies;

import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;

public interface QuestSlotsInspector {

	boolean isAvailable(int playerLevel, QuestDifficultyType currentQuestDifficultyType);
}
