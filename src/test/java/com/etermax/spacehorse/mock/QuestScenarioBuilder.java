package com.etermax.spacehorse.mock;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;

import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.quest.model.Quest;

public class QuestScenarioBuilder {

	private static final String QUEST_ID = "quest_ShipsDestroyedQuest";
	private static final String QUEST_TYPE = "ShipsDestroyedQuest";
	private final QuestDefinition defaultQuestDefinition;

	public QuestScenarioBuilder() {
		defaultQuestDefinition = new QuestDefinition(QUEST_ID, QUEST_TYPE, "", 3, EASY.toString());
	}

	public Quest buildDefaultQuest() {
		return new Quest(defaultQuestDefinition);
	}

}
