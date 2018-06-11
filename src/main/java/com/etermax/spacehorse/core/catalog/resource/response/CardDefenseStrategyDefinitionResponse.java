package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.CardDefenseStrategyDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDefenseStrategyDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("AgainstUnitId")
	private String againstUnitId;

	@JsonProperty("UseCardId")
	private String useCardId;

	public String getId() {
		return id;
	}

	public String getAgainstUnitId() {
		return againstUnitId;
	}

	public String getUseCardId() {
		return useCardId;
	}

	public CardDefenseStrategyDefinitionResponse() {
	}

	public CardDefenseStrategyDefinitionResponse(CardDefenseStrategyDefinition definition) {
		this.id = definition.getId();
		this.useCardId = definition.getUseCardId();
		this.againstUnitId = definition.getAgainstUnitId();
	}
}
