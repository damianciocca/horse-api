package com.etermax.spacehorse.core.battle.model;

import java.util.List;

public class DeltaMmrPercentageSelector {

	private final List<DeltaMmrPercentage> deltaMmrPercentages;

	public DeltaMmrPercentageSelector(List<DeltaMmrPercentage> deltaMmrPercentages) {
		this.deltaMmrPercentages = deltaMmrPercentages;
	}

	public DeltaMmrPercentage find(int mmr) {
		return deltaMmrPercentages.stream().filter(deltaMmrPercentage -> deltaMmrPercentage.appliesTo(mmr)).findFirst()
				.orElse(createDefaultDeltaMmrPercentage());
	}

	private DeltaMmrPercentage createDefaultDeltaMmrPercentage() {
		return new DeltaMmrPercentage(0, 0, 100, 100);
	}
}
