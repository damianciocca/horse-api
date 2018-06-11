package com.etermax.spacehorse.core.catalog.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayerLevelsCollection extends CatalogEntriesCollection<PlayerLevel> {

	private final Map<Integer, PlayerLevel> entriesByLevel;

	private final int maxLevel;

	public int getMaxLevel() {
		return maxLevel;
	}

	public PlayerLevelsCollection(List<PlayerLevel> entries) {
		super(entries);
		this.entriesByLevel = new HashMap<>();
		entries.forEach(playerLevel -> entriesByLevel.put(playerLevel.getLevel(), playerLevel));
		this.maxLevel = findMaxLevel(entries);
	}

	private int findMaxLevel(List<PlayerLevel> entries) {
		return entries.stream().mapToInt(x -> x.getLevel() + 1).max().orElse(0);
	}

	public Optional<PlayerLevel> findByLevel(int level) {
		return Optional.ofNullable(entriesByLevel.get(level));
	}

}
