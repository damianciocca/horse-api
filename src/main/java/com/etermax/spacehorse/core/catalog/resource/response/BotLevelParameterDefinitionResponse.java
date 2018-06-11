package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.BotLevelParameterDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BotLevelParameterDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("Level")
	private int level;

	@JsonProperty("UseBestStrategyChance")
	private int useBestStrategyChance;

	public String getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getUseBestStrategyChance() {
		return useBestStrategyChance;
	}

	public BotLevelParameterDefinitionResponse() {
	}

	public BotLevelParameterDefinitionResponse(BotLevelParameterDefinition definition) {
		this.id = definition.getId();
		this.level = definition.getLevel();
		this.useBestStrategyChance = definition.getUseBestStrategyChance();
	}
}
