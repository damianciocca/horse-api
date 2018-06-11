package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.DeltaMmrPercentageDefinitionRepresentation;

public class DeltaMmrPercentageDefinition extends CatalogEntry  {

	private final int minMmr;
	private final int maxMmr;
	private final int percentageByVictory;
	private final int percentageByLose;

	public DeltaMmrPercentageDefinition(DeltaMmrPercentageDefinitionRepresentation representation) {
		super(representation.getId());
		minMmr = representation.getMinMmr();
		maxMmr = representation.getMaxMmr();
		percentageByVictory = representation.getPercentageByVictory();
		percentageByLose = representation.getPercentageByLose();
	}

	public int getMinMmr() {
		return minMmr;
	}

	public int getMaxMmr() {
		return maxMmr;
	}

	public int getPercentageByVictory() {
		return percentageByVictory;
	}

	public int getPercentageByLose() {
		return percentageByLose;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(minMmr >= 0, "minMmr < 0");
		validateParameter(maxMmr > minMmr, "maxMmr > minMmr");
		validateParameter(percentageByVictory <= 100, "percentageByVictory <= 100");
		validateParameter(percentageByVictory > 0, "percentageByVictory > 0");
		validateParameter(percentageByLose <= 100, "percentageByLose <= 100");
		validateParameter(percentageByLose > 0, "percentageByLose > 0");
	}
}
