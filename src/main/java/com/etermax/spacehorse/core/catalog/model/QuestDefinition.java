package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.resource.response.QuestDefinitionResponse;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;

public class QuestDefinition extends CatalogEntry implements CatalogEntryWithChestInformation {

	private final String questType;

	private final String chestId;

	private final int goalAmount;

	private final int parameter;

	private final String difficulty;

	public QuestDefinition(String id) {
		super(id);
		this.questType = "";
		this.chestId = "";
		this.goalAmount = 0;
		this.parameter = 0;
		this.difficulty = QuestDifficultyType.EASY.toString();
	}

	public QuestDefinition(String id, String questType, String chestId, int goalAmount, String difficulty) {
		this(id, questType, chestId, goalAmount, 0, difficulty);
	}

	public QuestDefinition(String id, String questType, String chestId, int goalAmount, int parameter, String difficulty) {
		super(id);
		this.questType = questType;
		this.chestId = chestId;
		this.goalAmount = goalAmount;
		this.parameter = parameter;
		this.difficulty = difficulty;
	}

	public QuestDefinition(QuestDefinitionResponse questDefinitionResponse) {
		this(questDefinitionResponse.getId(), questDefinitionResponse.getQuestType(), questDefinitionResponse.getChestId(),
				questDefinitionResponse.getGoalAmount(), questDefinitionResponse.getParameter(), questDefinitionResponse.getDifficulty());
	}

	public String getQuestType() {
		return questType;
	}

	public String getChestId() {
		return chestId;
	}

	public int getGoalAmount() {
		return goalAmount;
	}

	public int getParameter() {
		return parameter;
	}

	public String getDifficulty() {
		return difficulty;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(goalAmount > 0, "goalAmount <= 0");
		catalog.getChestDefinitionsCollection().findById(this.chestId)
				.orElseThrow(() -> new CatalogException("Chest Definition Id not found: " + this.chestId));
	}
}
