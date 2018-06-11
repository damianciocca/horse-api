package com.etermax.spacehorse.core.catalog.resource.response;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BotsChancesByMmrDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("MinMmr")
	private int minMmr;

	@JsonProperty("MaxMmr")
	private int maxMmr;

	@JsonProperty("Chance")
	private int chance;

	public BotsChancesByMmrDefinitionResponse() {
		// just for jacksonn library
	}

	public BotsChancesByMmrDefinitionResponse(String id, int minMmr, int maxMmr, int chance) {
		this.id = id;
		this.minMmr = minMmr;
		this.maxMmr = maxMmr;
		this.chance = chance;
	}

	public String getId() {
		return id;
	}

	public int getMinMmr() {
		return minMmr;
	}

	public int getMaxMmr() {
		return maxMmr;
	}

	public int getChance() {
		return chance;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
