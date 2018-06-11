package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;

public class QuestDefinitionWithChance {

	private final String id;
	private final QuestDefinition questDefinition;
	private final int chance;

	public String getId() {
		return id;
	}

	public QuestDefinitionWithChance(String id, QuestDefinition questDefinition, int chance) {
		this.id = id;
		this.questDefinition = questDefinition;
		this.chance = chance;
	}

	public void validate() {
		if (chance < 0)
			throw new CatalogException("Invalid chance in Quest Chance " + getId());
	}

	public QuestDefinition getQuestDefinition() {
		return questDefinition;
	}

	public int getChance() {
		return chance;
	}
}
