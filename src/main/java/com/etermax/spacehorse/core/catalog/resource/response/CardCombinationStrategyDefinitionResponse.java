package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.CardCombinationStrategyDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardCombinationStrategyDefinitionResponse {
	@JsonProperty("Id")
	private String id;

	@JsonProperty("Card1Id")
	private String card1Id;

	@JsonProperty("Delay")
	private FintResponse delay;

	@JsonProperty("Card2Id")
	private String card2Id;

	public String getId() {
		return id;
	}

	public String getCard1Id() {
		return card1Id;
	}

	public FintResponse getDelay() {
		return delay;
	}

	public String getCard2Id() {
		return card2Id;
	}

	public CardCombinationStrategyDefinitionResponse() {
	}

	public CardCombinationStrategyDefinitionResponse(CardCombinationStrategyDefinition definition) {
		this.id = definition.getId();
		this.card1Id = definition.getCard1Id();
		this.delay = new FintResponse(definition.getDelay());
		this.card2Id = definition.getCard2Id();
	}
}
