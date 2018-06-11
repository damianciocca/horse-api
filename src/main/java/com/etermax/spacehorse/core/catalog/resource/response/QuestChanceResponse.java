package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.QuestChancesList;
import com.etermax.spacehorse.core.catalog.model.QuestDefinitionWithChance;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestChanceResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("Difficulty")
	private String difficulty;

	@JsonProperty("MapNumber")
	private int mapNumber;

	@JsonProperty("QuestDefinitionId")
	private String questDefinitionId;

	@JsonProperty("Chance")
	private int chance;

	public QuestChanceResponse() {
	}

	public QuestChanceResponse(QuestChancesList chancesList, QuestDefinitionWithChance definitionWithChance) {
		this.id = definitionWithChance.getId();
		this.difficulty = chancesList.getDifficulty();
		this.mapNumber = chancesList.getMapNumber();
		this.questDefinitionId = definitionWithChance.getQuestDefinition().getId();
		this.chance = definitionWithChance.getChance();
	}

	public String getId() {
		return id;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public String getQuestDefinitionId() {
		return questDefinitionId;
	}

	public int getChance() {
		return chance;
	}
}
