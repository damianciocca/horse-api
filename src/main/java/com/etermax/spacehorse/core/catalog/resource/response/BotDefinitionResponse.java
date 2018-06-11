package com.etermax.spacehorse.core.catalog.resource.response;

import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.BotDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BotDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("Level")
	private int level;

	@JsonProperty("MinMMR")
	private int minMMR;

	@JsonProperty("MaxMMR")
	private int maxMMR;

	@JsonProperty("Cards")
	private String cards;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getMinMMR() {
		return minMMR;
	}

	public int getMaxMMR() {
		return maxMMR;
	}

	public String getCards() {
		return cards;
	}

	public BotDefinitionResponse() {
	}

	public BotDefinitionResponse(BotDefinition botDefinition) {
		this.id = botDefinition.getId();
		this.name = botDefinition.getName();
		this.level = botDefinition.getLevel();
		this.minMMR = botDefinition.getMinMMR();
		this.maxMMR = botDefinition.getMaxMMR();
		this.cards = botDefinition.getCards().stream().map(card -> card.getCardType() + ":" + card.getLevel()).collect(Collectors.joining(","));
	}

}
