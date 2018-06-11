package com.etermax.spacehorse.core.matchmaking.model;

public class MatchmakingAlgorithmConfiguration {
	private final double indexTolerance;
	private final int mmrLimit;

	public double getIndexTolerance() {
		return indexTolerance;
	}

	public int getMmrLimit() {
		return mmrLimit;
	}

	public MatchmakingAlgorithmConfiguration(double indexTolerance, int mmrLimit) {
		this.indexTolerance = indexTolerance;
		this.mmrLimit = mmrLimit;
	}
}
