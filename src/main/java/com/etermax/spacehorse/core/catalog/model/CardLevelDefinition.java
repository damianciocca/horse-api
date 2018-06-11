package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.CardLevelDefinitionResponse;

public class CardLevelDefinition extends CatalogEntry {

	private final String cardId;

	private final int level;

	private final int unitsAmount;

	private final int unitsLevel;

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

	public CardLevelDefinition(CardLevelDefinitionResponse cardLevelDefinitionResponse) {
		super(cardLevelDefinitionResponse.getId());
		this.cardId = cardLevelDefinitionResponse.getCardId();
		this.level = cardLevelDefinitionResponse.getLevel();
		this.unitsAmount = cardLevelDefinitionResponse.getUnitsAmount();
		this.unitsLevel = cardLevelDefinitionResponse.getUnitsLevel();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(cardId != null, "cardId == null");
		validateParameter(level >= 0, "level < 0");
		validateParameter(unitsAmount >= 0, "unitsAmount < 0");
		validateParameter(unitsLevel >= 0, "unitsLevel < 0");
	}

}
