package com.etermax.spacehorse.core.battle.model;

import org.apache.commons.lang3.Range;

import com.etermax.spacehorse.core.catalog.model.DeltaMmrPercentageDefinition;

public class DeltaMmrPercentage {

	private final Range<Integer> mmrRange;
	private final int percentageByVictory;
	private final int percentageByLose;

	public DeltaMmrPercentage(int minMmr, int maxMmr, int percentageByVictory, int percentageByLose) {
		mmrRange = Range.between(minMmr, maxMmr);
		this.percentageByVictory = percentageByVictory;
		this.percentageByLose = percentageByLose;
	}

	public DeltaMmrPercentage(DeltaMmrPercentageDefinition deltaMmrPercentageDefinition) {
		this(deltaMmrPercentageDefinition.getMinMmr(), deltaMmrPercentageDefinition.getMaxMmr(), deltaMmrPercentageDefinition.getPercentageByVictory(),
				deltaMmrPercentageDefinition.getPercentageByLose());
	}

	public boolean appliesTo(int mmr) {
		return mmrRange.contains(mmr);
	}

	public int getPercentageByVictory() {
		return percentageByVictory;
	}

	public int getPercentageByLose() {
		return percentageByLose;
	}
}
