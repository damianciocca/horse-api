package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.CardDefenseStrategyDefinitionResponse;

public class CardDefenseStrategyDefinition extends CatalogEntry {

	private final String againstUnitId;

	private final String useCardId;

	public String getAgainstUnitId() {
		return againstUnitId;
	}

	public String getUseCardId() {
		return useCardId;
	}

	public CardDefenseStrategyDefinition(CardDefenseStrategyDefinitionResponse response) {
		super(response.getId());
		this.againstUnitId = response.getAgainstUnitId();
		this.useCardId = response.getUseCardId();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(
				getUseCardId() != null && !getUseCardId().isEmpty() && catalog.getCardDefinitionsCollection().findById(getUseCardId()).isPresent(),
				"Invalid UseCardId %s", getUseCardId());

		validateParameter(
				getAgainstUnitId() != null && !getAgainstUnitId().isEmpty() && catalog.getUnitDefinitionsCollection().findById(getAgainstUnitId())
						.isPresent(), "Invalid AgainstUnitId %s", getAgainstUnitId());

	}
}
