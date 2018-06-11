package com.etermax.spacehorse.core.quest.model;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;

public class DailyQuestSelectorConfiguration {

	private final List<QuestDefinition> questDefinitions;
	private final String currentDailyQuestId;

	public DailyQuestSelectorConfiguration(List<QuestDefinition> questDefinitions, String currentDailyQuestId) {
		this.questDefinitions = questDefinitions;
		this.currentDailyQuestId = currentDailyQuestId;
	}

	public QuestDefinition findActiveDailyQuestDefinition() {
		return questDefinitions.stream().filter(questDefinition -> questDefinition.getId().equals(currentDailyQuestId)).findFirst().get();
	}
}
