package com.etermax.spacehorse.core.quest.model.unlock.strategies;

import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;

public class DefaultQuestSlotsInspector implements QuestSlotsInspector {

	@Override
	public boolean isAvailable(int playerLevel, QuestDifficultyType currentQuestDifficultyType) {
		return true;
	}
}
