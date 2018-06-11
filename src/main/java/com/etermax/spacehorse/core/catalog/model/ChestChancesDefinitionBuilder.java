package com.etermax.spacehorse.core.catalog.model;

public class ChestChancesDefinitionBuilder {
	private String id = "";
	private String chestType = "";
	private int mapNumber;
	private int gold;
	private int goldTolerance;
	private int gems;
	private int gemsTolerance;
	private int common;
	private int commonMinPartitions;
	private int commonMaxPartitions;
	private int rare;
	private int rareMinPartitions;
	private int rareMaxPartitions;
	private int rareAdditionalAmount;
	private int rareAdditionalChances;
	private int epic;
	private int epicMinPartitions;
	private int epicMaxPartitions;
	private int epicAdditionalAmount;
	private int epicAdditionalChances;
	private int legendary;
	private int legendaryMinPartitions;
	private int legendaryMaxPartitions;
	private int legendaryAdditionalAmount;
	private int legendaryAdditionalChances;

	public ChestChancesDefinitionBuilder setId(String id) {
		this.id = id;
		return this;
	}

	public ChestChancesDefinitionBuilder setChestType(String chestType) {
		this.chestType = chestType;
		return this;
	}

	public ChestChancesDefinitionBuilder setMapNumber(int mapNumber) {
		this.mapNumber = mapNumber;
		return this;
	}

	public ChestChancesDefinitionBuilder setGold(int gold) {
		this.gold = gold;
		return this;
	}

	public ChestChancesDefinitionBuilder setGoldTolerance(int goldTolerance) {
		this.goldTolerance = goldTolerance;
		return this;
	}

	public ChestChancesDefinitionBuilder setGems(int gems) {
		this.gems = gems;
		return this;
	}

	public ChestChancesDefinitionBuilder setGemsTolerance(int gemsTolerance) {
		this.gemsTolerance = gemsTolerance;
		return this;
	}

	public ChestChancesDefinitionBuilder setCommon(int common) {
		this.common = common;
		return this;
	}

	public ChestChancesDefinitionBuilder setCommonMaxPartitions(int commonMaxPartitions) {
		this.commonMaxPartitions = commonMaxPartitions;
		return this;
	}

	public ChestChancesDefinitionBuilder setRare(int rare) {
		this.rare = rare;
		return this;
	}

	public ChestChancesDefinitionBuilder setRareMaxPartitions(int rareMaxPartitions) {
		this.rareMaxPartitions = rareMaxPartitions;
		return this;
	}

	public ChestChancesDefinitionBuilder setRareAdditionalAmount(int rareAdditionalAmount) {
		this.rareAdditionalAmount = rareAdditionalAmount;
		return this;
	}

	public ChestChancesDefinitionBuilder setRareAdditionalChances(int rareAdditionalChances) {
		this.rareAdditionalChances = rareAdditionalChances;
		return this;
	}

	public ChestChancesDefinitionBuilder setEpic(int epic) {
		this.epic = epic;
		return this;
	}

	public ChestChancesDefinitionBuilder setEpicMaxPartitions(int epicMaxPartitions) {
		this.epicMaxPartitions = epicMaxPartitions;
		return this;
	}

	public ChestChancesDefinitionBuilder setEpicAdditionalAmount(int epicAdditionalAmount) {
		this.epicAdditionalAmount = epicAdditionalAmount;
		return this;
	}

	public ChestChancesDefinitionBuilder setEpicAdditionalChances(int epicAdditionalChances) {
		this.epicAdditionalChances = epicAdditionalChances;
		return this;
	}

	public ChestChancesDefinitionBuilder setLegendary(int legendary) {
		this.legendary = legendary;
		return this;
	}

	public ChestChancesDefinitionBuilder setLegendaryMaxPartitions(int legendaryMaxPartitions) {
		this.legendaryMaxPartitions = legendaryMaxPartitions;
		return this;
	}

	public ChestChancesDefinitionBuilder setLegendaryAdditionalAmount(int legendaryAdditionalAmount) {
		this.legendaryAdditionalAmount = legendaryAdditionalAmount;
		return this;
	}

	public ChestChancesDefinitionBuilder setLegendaryAdditionalChances(int legendaryAdditionalChances) {
		this.legendaryAdditionalChances = legendaryAdditionalChances;
		return this;
	}

	public ChestChancesDefinitionBuilder setCommonMinPartitions(int commonMinPartitions) {
		this.commonMinPartitions = commonMinPartitions;
		return this;
	}

	public ChestChancesDefinitionBuilder setRareMinPartitions(int rareMinPartitions) {
		this.rareMinPartitions = rareMinPartitions;
		return this;
	}

	public ChestChancesDefinitionBuilder setEpicMinPartitions(int epicMinPartitions) {
		this.epicMinPartitions = epicMinPartitions;
		return this;
	}

	public ChestChancesDefinitionBuilder setLegendaryMinPartitions(int legendaryMinPartitions) {
		this.legendaryMinPartitions = legendaryMinPartitions;
		return this;
	}

	public ChestChancesDefinition create() {
		return new ChestChancesDefinition(id, chestType, mapNumber, gold, goldTolerance, gems, gemsTolerance, common, commonMinPartitions,
				commonMaxPartitions, rare, rareMinPartitions, rareMaxPartitions, rareAdditionalAmount, rareAdditionalChances, epic, epicMinPartitions,
				epicMaxPartitions, epicAdditionalAmount, epicAdditionalChances, legendary, legendaryMinPartitions, legendaryMaxPartitions,
				legendaryAdditionalAmount, legendaryAdditionalChances);
	}
}