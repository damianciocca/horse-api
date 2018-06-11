package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;

public class ChestChancesCardsDefinition {

	private final CardRarity rarity;

	private final int amount;

	private final int minPartitions;

	private final int maxPartitions;

	private final int additionalAmount;

	private final int additionalAmountChances;

	private final int additionalAmountChancesBase;

	public CardRarity getRarity() {
		return rarity;
	}

	public int getAmount() {
		return amount;
	}

	public int getMinPartitions() {
		return minPartitions;
	}

	public int getMaxPartitions() {
		return maxPartitions;
	}

	public int getAdditionalAmount() {
		return additionalAmount;
	}

	public int getAdditionalAmountChances() {
		return additionalAmountChances;
	}

	public int getAdditionalAmountChancesBase() {
		return additionalAmountChancesBase;
	}

	public ChestChancesCardsDefinition(CardRarity rarity, int amount, int minParititons, int maxPartitions, int additionalAmount, int
			additionalAmountChances,
			int additionalAmountChancesBase) {
		this.rarity = rarity;
		this.amount = amount;
		this.minPartitions = minParititons;
		this.maxPartitions = maxPartitions;
		this.additionalAmount = additionalAmount;
		this.additionalAmountChances = additionalAmountChances;
		this.additionalAmountChancesBase = additionalAmountChancesBase;
	}

	public void validate() {
		validateParameter(amount >= 0, "amount < 0");
		validateParameter(minPartitions >= 0, "minPartitions < 0");
		validateParameter(maxPartitions >= 0, "maxPartitions < 0");
		validateParameter(minPartitions <= maxPartitions, "minPartitions > maxPartitions");
		validateParameter(additionalAmount >= 0, "additionalAmount < 0");
		validateParameter(additionalAmountChancesBase > 0, "additionalAmountChancesBase <= 0");
		validateParameter(additionalAmountChances >= 0, "additionalAmountChances < 0");
		validateParameter(additionalAmountChances <= additionalAmountChancesBase, "additionalAmountChances > additionalAmountChancesBase");

		validateParameter(additionalAmountChances == 0 || additionalAmountChances > 0 && additionalAmount > 0,
				"additionalAmountChances > 0 but addtionalAmount is zero");

		validateParameter(additionalAmountChances > 0 || additionalAmountChances == 0 && additionalAmount == 0,
				"additionalAmountChances is 0 but addtionalAmount > 0");
	}

	private void validateParameter(boolean condition, String errorMessage) {
		if (!condition) {
			throw new CatalogException("Error validating card chest chances of rarity " + getRarity() + ": " + errorMessage);
		}
	}

}
