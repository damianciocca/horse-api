package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.ChestChancesDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChestChancesDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("ChestType")
	private String chestType;

	@JsonProperty("MapNumber")
	private int mapNumber;

	@JsonProperty("Gold")
	private int gold;

	@JsonProperty("GoldTolerance")
	private int goldTolerance;

	@JsonProperty("Gems")
	private int gems;

	@JsonProperty("GemsTolerance")
	private int gemsTolerance;

	@JsonProperty("Common")
	private int common;

	@JsonProperty("CommonMaxPartitions")
	private int commonMaxPartitions;

	@JsonProperty("Rare")
	private int rare;

	@JsonProperty("RareMaxPartitions")
	private int rareMaxPartitions;

	@JsonProperty("RareAdditionalAmount")
	private int rareAdditionalAmount;

	@JsonProperty("RareAdditionalChances")
	private int rareAdditionalChances;

	@JsonProperty("Epic")
	private int epic;

	@JsonProperty("EpicMaxPartitions")
	private int epicMaxPartitions;

	@JsonProperty("EpicAdditionalAmount")
	private int epicAdditionalAmount;

	@JsonProperty("EpicAdditionalChances")
	private int epicAdditionalChances;

	@JsonProperty("Legendary")
	private int legendary;

	@JsonProperty("LegendaryMaxPartitions")
	private int legendaryMaxPartitions;

	@JsonProperty("LegendaryAdditionalAmount")
	private int legendaryAdditionalAmount;

	@JsonProperty("LegendaryAdditionalChances")
	private int legendaryAdditionalChances;

	public ChestChancesDefinitionResponse() {
	}

	public ChestChancesDefinitionResponse(ChestChancesDefinition chestChancesDefinition) {
		this.id = chestChancesDefinition.getId();
		this.chestType = chestChancesDefinition.getChestType();
		this.mapNumber = chestChancesDefinition.getMapNumber();
		this.gold = chestChancesDefinition.getGold();
		this.goldTolerance = chestChancesDefinition.getGoldTolerance();
		this.gems = chestChancesDefinition.getGems();
		this.gemsTolerance = chestChancesDefinition.getGemsTolerance();
		this.common = chestChancesDefinition.getCardsDefinition(CardRarity.COMMON).getAmount();
		this.commonMaxPartitions = chestChancesDefinition.getCardsDefinition(CardRarity.COMMON).getMaxPartitions();
		this.rare = chestChancesDefinition.getCardsDefinition(CardRarity.RARE).getAmount();
		this.rareMaxPartitions = chestChancesDefinition.getCardsDefinition(CardRarity.RARE).getMaxPartitions();
		this.rareAdditionalAmount = chestChancesDefinition.getCardsDefinition(CardRarity.RARE).getAdditionalAmount();
		this.rareAdditionalChances = chestChancesDefinition.getCardsDefinition(CardRarity.RARE).getAdditionalAmountChances();
		this.epic = chestChancesDefinition.getCardsDefinition(CardRarity.EPIC).getAmount();
		this.epicMaxPartitions = chestChancesDefinition.getCardsDefinition(CardRarity.EPIC).getMaxPartitions();
		this.epicAdditionalAmount = chestChancesDefinition.getCardsDefinition(CardRarity.EPIC).getAdditionalAmount();
		this.epicAdditionalChances = chestChancesDefinition.getCardsDefinition(CardRarity.EPIC).getAdditionalAmountChances();
		this.legendary = chestChancesDefinition.getCardsDefinition(CardRarity.LEGENDARY).getAmount();
		this.legendaryMaxPartitions = chestChancesDefinition.getCardsDefinition(CardRarity.LEGENDARY).getMaxPartitions();
		this.legendaryAdditionalAmount = chestChancesDefinition.getCardsDefinition(CardRarity.LEGENDARY).getAdditionalAmount();
		this.legendaryAdditionalChances = chestChancesDefinition.getCardsDefinition(CardRarity.LEGENDARY).getAdditionalAmountChances();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChestType() {
		return chestType;
	}

	public void setChestType(String chestType) {
		this.chestType = chestType;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public void setMapNumber(Integer mapNumber) {
		this.mapNumber = mapNumber;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getGoldTolerance() {
		return goldTolerance;
	}

	public void setGoldTolerance(int goldTolerance) {
		this.goldTolerance = goldTolerance;
	}

	public int getGems() {
		return gems;
	}

	public void setGems(int gems) {
		this.gems = gems;
	}

	public int getGemsTolerance() {
		return gemsTolerance;
	}

	public void setGemsTolerance(int gemsTolerance) {
		this.gemsTolerance = gemsTolerance;
	}

	public int getCommon() {
		return common;
	}

	public void setCommon(int common) {
		this.common = common;
	}

	public int getRare() {
		return rare;
	}

	public void setRare(int rare) {
		this.rare = rare;
	}

	public int getEpic() {
		return epic;
	}

	public void setEpic(int epic) {
		this.epic = epic;
	}

	public int getCommonMaxPartitions() {
		return commonMaxPartitions;
	}

	public void setCommonMaxPartitions(int commonMaxPartitions) {
		this.commonMaxPartitions = commonMaxPartitions;
	}

	public int getRareMaxPartitions() {
		return rareMaxPartitions;
	}

	public void setRareMaxPartitions(int rareMaxPartitions) {
		this.rareMaxPartitions = rareMaxPartitions;
	}

	public int getEpicMaxPartitions() {
		return epicMaxPartitions;
	}

	public void setEpicMaxPartitions(int epicMaxPartitions) {
		this.epicMaxPartitions = epicMaxPartitions;
	}

	public int getRareAdditionalAmount() {
		return rareAdditionalAmount;
	}

	public void setRareAdditionalAmount(int rareAdditionalAmount) {
		this.rareAdditionalAmount = rareAdditionalAmount;
	}

	public int getRareAdditionalChances() {
		return rareAdditionalChances;
	}

	public void setRareAdditionalChances(int rareAdditionalChances) {
		this.rareAdditionalChances = rareAdditionalChances;
	}

	public int getEpicAdditionalAmount() {
		return epicAdditionalAmount;
	}

	public void setEpicAdditionalAmount(int epicAdditionalAmount) {
		this.epicAdditionalAmount = epicAdditionalAmount;
	}

	public int getEpicAdditionalChances() {
		return epicAdditionalChances;
	}

	public void setEpicAdditionalChances(int epicAdditionalChances) {
		this.epicAdditionalChances = epicAdditionalChances;
	}

	public int getLegendary() {
		return legendary;
	}

	public void setLegendary(int legendary) {
		this.legendary = legendary;
	}

	public int getLegendaryMaxPartitions() {
		return legendaryMaxPartitions;
	}

	public void setLegendaryMaxPartitions(int legendaryMaxPartitions) {
		this.legendaryMaxPartitions = legendaryMaxPartitions;
	}

	public int getLegendaryAdditionalAmount() {
		return legendaryAdditionalAmount;
	}

	public void setLegendaryAdditionalAmount(int legendaryAdditionalAmount) {
		this.legendaryAdditionalAmount = legendaryAdditionalAmount;
	}

	public int getLegendaryAdditionalChances() {
		return legendaryAdditionalChances;
	}

	public void setLegendaryAdditionalChances(int legendaryAdditionalChances) {
		this.legendaryAdditionalChances = legendaryAdditionalChances;
	}
}
