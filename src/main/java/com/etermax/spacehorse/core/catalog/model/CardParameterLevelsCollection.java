package com.etermax.spacehorse.core.catalog.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CardParameterLevelsCollection extends CatalogEntriesCollection<CardParameterLevel> {

	private final Map<CardRarity, Map<Integer, CardParameterLevel>> entriesByRarityAndLevel;
	private final Map<CardRarity, Integer> maxLevelByRarity;

	public CardParameterLevelsCollection(List<CardParameterLevel> entries) {
		super(entries);

		entriesByRarityAndLevel = new HashMap<>();
		maxLevelByRarity = new HashMap<>();

		for (CardRarity rarity : CardRarity.values()) {
			entriesByRarityAndLevel.put(rarity, new HashMap<>());
			maxLevelByRarity.put(rarity, 0);
		}

		this.getEntries().forEach(t -> {
			entriesByRarityAndLevel.get(t.getCardRarity()).put(t.getLevel(), t);
			maxLevelByRarity.put(t.getCardRarity(), Math.max(t.getLevel() + 1, maxLevelByRarity.get(t.getCardRarity())));
		});
	}

	public Optional<CardParameterLevel> findByRarityAndLevel(CardRarity rarity, int level) {
		return Optional.ofNullable(entriesByRarityAndLevel.get(rarity).get(level));
	}

	public int getMaxCardLevelByRarity(CardRarity rarity) {
		return maxLevelByRarity.get(rarity);
	}

}
