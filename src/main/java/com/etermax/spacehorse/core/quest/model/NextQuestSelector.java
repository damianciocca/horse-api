package com.etermax.spacehorse.core.quest.model;

import com.etermax.spacehorse.core.catalog.model.QuestDefinition;

public interface NextQuestSelector {
	QuestDefinition getNextQuest(NextQuestSelectorConfiguration configuration, String slotId, int slotSequence);

	QuestDefinition getDailyQuest(DailyQuestSelectorConfiguration configuration);
}
