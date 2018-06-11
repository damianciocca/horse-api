package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("QuestType")
	private String questType;

	@JsonProperty("GoalAmount")
	private int goalAmount;

	@JsonProperty("Parameter")
	private int parameter;

	@JsonProperty("ChestId")
	private String chestId;

	@JsonProperty("Difficulty")
	private String difficulty;

	public QuestDefinitionResponse() {
	}

	public QuestDefinitionResponse(QuestDefinition questDefinition) {
		this.id = questDefinition.getId();
		this.questType = questDefinition.getQuestType();
		this.chestId = questDefinition.getChestId();
		this.goalAmount = questDefinition.getGoalAmount();
		this.parameter = questDefinition.getParameter();
		this.difficulty = questDefinition.getDifficulty();
	}

	public String getId() {
		return id;
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
}
