package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.BotLevelParameterDefinitionResponse;

public class BotLevelParameterDefinition extends CatalogEntry {

	private final int level;

	private final int useBestStrategyChance;

	public int getLevel() {
		return level;
	}

	public int getUseBestStrategyChance() {
		return useBestStrategyChance;
	}

	public BotLevelParameterDefinition(String id, int level, int useBestStrategyChance) {
		super(id);
		this.level = level;
		this.useBestStrategyChance = useBestStrategyChance;
	}

	public BotLevelParameterDefinition(BotLevelParameterDefinitionResponse response) {
		super(response.getId());
		this.level = response.getLevel();
		this.useBestStrategyChance = response.getUseBestStrategyChance();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(level >= 0, "level < 0");
		validateParameter(useBestStrategyChance >= 0, "useBestStrategyChance < 0");
		validateParameter(useBestStrategyChance <= 100, "useBestStrategyChance > 100");

	}
}
