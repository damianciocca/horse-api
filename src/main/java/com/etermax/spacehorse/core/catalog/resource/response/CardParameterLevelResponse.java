package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.CardParameterLevel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardParameterLevelResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("CardRarity")
	private int cardRarity;

	@JsonProperty("Level")
	private int level;

	@JsonProperty("UpgradeCostCardParts")
	private int upgradeCostCardParts;

	@JsonProperty("UpgradeCostGold")
	private int upgradeCostGold;

	@JsonProperty("UpgradeRewardXP")
	private int upgradeRewardXP;

	public String getId() {
		return id;
	}

	public int getCardRarity() {
		return cardRarity;
	}

	public int getLevel() {
		return level;
	}

	public int getUpgradeCostCardParts() {
		return upgradeCostCardParts;
	}

	public int getUpgradeCostGold() {
		return upgradeCostGold;
	}

	public int getUpgradeRewardXP() {
		return upgradeRewardXP;
	}

	public CardParameterLevelResponse() {
	}

	public CardParameterLevelResponse(CardParameterLevel cardParameterLevel) {
		this.id = cardParameterLevel.getId();
		this.cardRarity = CardRarityResponse.fromCardRarityEnum(cardParameterLevel.getCardRarity());
		this.level = cardParameterLevel.getLevel();
		this.upgradeCostCardParts = cardParameterLevel.getUpgradeCostCardParts();
		this.upgradeCostGold = cardParameterLevel.getUpgradeCostGold();
		this.upgradeRewardXP = cardParameterLevel.getUpgradeRewardXP();
	}
}
