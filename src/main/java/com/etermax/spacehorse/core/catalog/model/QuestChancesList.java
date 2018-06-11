package com.etermax.spacehorse.core.catalog.model;

import java.util.List;

import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;

public class QuestChancesList extends CatalogEntry {

	private final String difficulty;
	private final int mapNumber;
	private final List<QuestDefinitionWithChance> questDefinitionWithChances;

	public QuestChancesList(String difficulty, int mapNumber, List<QuestDefinitionWithChance> questDefinitionWithChances) {
		super(difficulty + "_" + mapNumber);
		this.difficulty = difficulty;
		this.mapNumber = mapNumber;
		this.questDefinitionWithChances = questDefinitionWithChances;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(questDefinitionWithChances.size() > 0, "The list is empty");
		validateParameter(validateDifficulty(difficulty), "Invalid difficulty " + difficulty);
		questDefinitionWithChances.forEach(questDefinitionWithChance -> questDefinitionWithChance.validate());
	}

	private boolean validateDifficulty(String difficulty) {
		try {
			QuestDifficultyType.valueOf(difficulty);
			return true;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}

	public String getDifficulty() {
		return difficulty;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public List<QuestDefinitionWithChance> getQuestDefinitionWithChances() {
		return questDefinitionWithChances;
	}
}
