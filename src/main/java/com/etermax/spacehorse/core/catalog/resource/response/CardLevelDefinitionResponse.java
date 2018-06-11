package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.CardLevelDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardLevelDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("CardId")
	private String cardId;

	@JsonProperty("Level")
	private int level;

	@JsonProperty("UnitsAmount")
	private int unitsAmount;

	@JsonProperty("UnitsLevel")
	private int unitsLevel;

	public String getId() {
		return id;
	}

	public String getCardId() {
		return cardId;
	}

	public int getLevel() {
		return level;
	}

	public int getUnitsAmount() {
		return unitsAmount;
	}

	public int getUnitsLevel() {
		return unitsLevel;
	}

	public CardLevelDefinitionResponse() {
	}

	public CardLevelDefinitionResponse(CardLevelDefinition cardLevelDefinition) {
		this.id = cardLevelDefinition.getId();
		this.cardId = cardLevelDefinition.getCardId();
		this.level = cardLevelDefinition.getLevel();
		this.unitsAmount = cardLevelDefinition.getUnitsAmount();
		this.unitsLevel = cardLevelDefinition.getUnitsLevel();
	}
}