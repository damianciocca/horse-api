package com.etermax.spacehorse.core.catalog.model;

import java.util.HashMap;

import com.etermax.spacehorse.core.catalog.resource.response.ChestChancesDefinitionResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChestChancesDefinition extends CatalogEntry implements CatalogEntryWithMapInformation, CatalogEntryWithChestInformation {

	public static final int CHANCES_BASE = 100;
	public static final int LEGENDARY_CHANCES_BASE = 10000;
	static public final int MAX_PARTITIONS = 6;

	private final String chestType;

	private final int mapNumber;

	private final int gold;

	private final int goldTolerance;

	private final int gems;

	private final int gemsTolerance;

	private final HashMap<CardRarity, ChestChancesCardsDefinition> cardsDefinitions;

	public ChestChancesDefinition(String id, String chestType, int mapNumber, int gold, int goldTolerance, int gems, int gemsTolerance, int common,
			int commonMinPartitions, int commonMaxPartitions, int rare, int rareMinParititons, int rareMaxPartitions, int rareAdditionalAmount,
			int rareAdditionalChances, int epic, int epicMinParititons, int epicMaxPartitions, int epicAdditionalAmount, int epicAdditionalChances,
			int legendary, int legendaryMinPartitions, int legendaryMaxPartitions, int legendaryAdditionalAmount, int legendaryAdditionalChances) {
		super(id);
		this.chestType = chestType;
		this.mapNumber = mapNumber;
		this.gold = gold;
		this.goldTolerance = goldTolerance;
		this.gems = gems;
		this.gemsTolerance = gemsTolerance;

		cardsDefinitions = new HashMap<>();
		cardsDefinitions.put(CardRarity.COMMON,
				new ChestChancesCardsDefinition(CardRarity.COMMON, common, commonMinPartitions, commonMaxPartitions, 0, 0, CHANCES_BASE));
		cardsDefinitions.put(CardRarity.RARE,
				new ChestChancesCardsDefinition(CardRarity.RARE, rare, rareMinParititons, rareMaxPartitions, rareAdditionalAmount,
						rareAdditionalChances, CHANCES_BASE));
		cardsDefinitions.put(CardRarity.EPIC,
				new ChestChancesCardsDefinition(CardRarity.EPIC, epic, epicMinParititons, epicMaxPartitions, epicAdditionalAmount,
						epicAdditionalChances, CHANCES_BASE));
		cardsDefinitions.put(CardRarity.LEGENDARY,
				new ChestChancesCardsDefinition(CardRarity.LEGENDARY, legendary, legendaryMinPartitions, legendaryMaxPartitions,
						legendaryAdditionalAmount, legendaryAdditionalChances, LEGENDARY_CHANCES_BASE));
	}

	public ChestChancesDefinition(ChestChancesDefinitionResponse response) {
		this(response.getId(), response.getChestType(), response.getMapNumber(), response.getGold(), response.getGoldTolerance(), response.getGems(),
				response.getGemsTolerance(), response.getCommon(), 0, response.getCommonMaxPartitions(), response.getRare(), 0,
				response.getRareMaxPartitions(), response.getRareAdditionalAmount(), response.getRareAdditionalChances(), response.getEpic(), 0,
				response.getEpicMaxPartitions(), response.getEpicAdditionalAmount(), response.getEpicAdditionalChances(), response.getLegendary(), 0,
				response.getLegendaryMaxPartitions(), response.getLegendaryAdditionalAmount(), response.getLegendaryAdditionalChances());
	}

	public void validate(Catalog catalog) {
		validateParameter(chestType != null, "chestType == null");
		validateParameter(mapNumber >= 0, "mapNumber < 0");
		validateParameter(0 <= gold, "gold < 0");
		validateParameter(0 <= goldTolerance, "goldTolerance < 0");
		validateParameter(0 <= gems, "gems < 0");
		validateParameter(0 <= gemsTolerance, "gemsTolerance < 0");

		cardsDefinitions.values().forEach(cardsDefinitions -> cardsDefinitions.validate());

		int partitionsSum = cardsDefinitions.values().stream().mapToInt(cardsDefinitions -> cardsDefinitions.getMaxPartitions()).sum();

		validateParameter(partitionsSum > 0, "The sum of all maxPartitions must be at least 1");
	}

	public String getChestType() {
		return chestType;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public int getGold() {
		return gold;
	}

	public int getGoldTolerance() {
		return goldTolerance;
	}

	public int getGems() {
		return gems;
	}

	public int getGemsTolerance() {
		return gemsTolerance;
	}

	public ChestChancesCardsDefinition getCardsDefinition(CardRarity rarity) {
		if (cardsDefinitions.containsKey(rarity))
			return cardsDefinitions.get(rarity);
		return new ChestChancesCardsDefinition(rarity, 0, 0, 0, 0, 0, 100);
	}

	@Override
	@JsonIgnore
	public String getChestId() {
		return getChestType();
	}

}
