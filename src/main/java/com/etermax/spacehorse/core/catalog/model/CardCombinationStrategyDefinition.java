package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.CardCombinationStrategyDefinitionResponse;

public class CardCombinationStrategyDefinition extends CatalogEntry {

	private final String card1Id;

	private final long delay;

	private final String card2Id;

	public String getCard1Id() {
		return card1Id;
	}

	public long getDelay() {
		return delay;
	}

	public String getCard2Id() {
		return card2Id;
	}

	public CardCombinationStrategyDefinition(CardCombinationStrategyDefinitionResponse response) {
		super(response.getId());
		this.card1Id = response.getCard1Id();
		this.card2Id = response.getCard2Id();
		this.delay = response.getDelay().getRaw();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(
				getCard1Id() != null && !getCard1Id().isEmpty() && catalog.getCardDefinitionsCollection().findById(getCard1Id()).isPresent(),
				"Invalid Card1Id %s", getCard1Id());
		validateParameter(
				getCard2Id() != null && !getCard2Id().isEmpty() && catalog.getCardDefinitionsCollection().findById(getCard2Id()).isPresent(),
				"Invalid Card2Id %s", getCard2Id());
		validateParameter(delay >= 0, "Delay < 0");
	}
}
