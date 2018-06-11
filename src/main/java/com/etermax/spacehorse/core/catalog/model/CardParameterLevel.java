package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.CardParameterLevelResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CardRarityResponse;

public class CardParameterLevel extends CatalogEntry {

	private final CardRarity cardRarity;

	private final int level;

	private final int upgradeCostCardParts;

	private final int upgradeCostGold;

	private final int upgradeRewardXP;

	public CardRarity getCardRarity() {
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

	public CardParameterLevel(CardParameterLevelResponse cardParameterLevelResponse) {
		super(cardParameterLevelResponse.getId());
		this.cardRarity = CardRarityResponse.toCardRarityEnum(cardParameterLevelResponse.getCardRarity());
		this.level = cardParameterLevelResponse.getLevel();
		this.upgradeCostCardParts = cardParameterLevelResponse.getUpgradeCostCardParts();
		this.upgradeCostGold = cardParameterLevelResponse.getUpgradeCostGold();
		this.upgradeRewardXP = cardParameterLevelResponse.getUpgradeRewardXP();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(level >= 0, "level < 0");
		validateParameter(level == 0 || upgradeCostCardParts >= 0, "upgradeCostCardParts < 0");
		validateParameter(level == 0 || upgradeCostGold >= 0, "upgradeCostGold < 0");
		validateParameter(level == 0 || upgradeRewardXP >= 0, "upgradeRewardXP < 0");
	}

}
