package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.BotNameDefinitionResponse;

public class BotNameDefinition extends CatalogEntry {

	private final String name;

	public String getName() {
		return name;
	}

	public BotNameDefinition(BotNameDefinitionResponse botNameResponse) {
		super(botNameResponse.getId());
		this.name = botNameResponse.getName();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(name != null && !name.isEmpty(), "bot name is null or empty");
	}

}
