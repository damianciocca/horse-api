package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.BotNameDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BotNameDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("Name")
	private String name;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public BotNameDefinitionResponse() {
	}

	public BotNameDefinitionResponse(BotNameDefinition botNameDefinition) {
		this.id = botNameDefinition.getId();
		this.name = botNameDefinition.getName();
	}
}
