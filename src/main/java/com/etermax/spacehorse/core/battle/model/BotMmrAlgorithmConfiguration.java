package com.etermax.spacehorse.core.battle.model;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;

public class BotMmrAlgorithmConfiguration {

	private final int mmrDeltaOnVictory;
	private final int mmrDeltaOnVictoryPerScoreDiff;

	private final int mmrDeltaOnDefeat;
	private final int mmrDeltaOnDefeatPerScoreDiff;

	private final int mmrDeltaOnTie;

	public int getMmrDeltaOnVictory() {
		return mmrDeltaOnVictory;
	}

	public int getMmrDeltaOnVictoryPerScoreDiff() {
		return mmrDeltaOnVictoryPerScoreDiff;
	}

	public int getMmrDeltaOnDefeat() {
		return mmrDeltaOnDefeat;
	}

	public int getMmrDeltaOnDefeatPerScoreDiff() {
		return mmrDeltaOnDefeatPerScoreDiff;
	}

	public int getMmrDeltaOnTie() {
		return mmrDeltaOnTie;
	}

	public BotMmrAlgorithmConfiguration(int mmrDeltaOnVictory, int mmrDeltaOnVictoryPerScoreDiff, int mmrDeltaOnDefeat,
			int mmrDeltaOnDefeatPerScoreDiff, int mmrDeltaOnTie) {
		this.mmrDeltaOnVictory = mmrDeltaOnVictory;
		this.mmrDeltaOnVictoryPerScoreDiff = mmrDeltaOnVictoryPerScoreDiff;
		this.mmrDeltaOnDefeat = mmrDeltaOnDefeat;
		this.mmrDeltaOnDefeatPerScoreDiff = mmrDeltaOnDefeatPerScoreDiff;
		this.mmrDeltaOnTie = mmrDeltaOnTie;
	}

	public void validate() {
		validateParameter(mmrDeltaOnVictory >= 0, "mmrDeltaOnVictory < 0");
		validateParameter(mmrDeltaOnVictoryPerScoreDiff >= 0, "mmrDeltaOnVictoryPerScoreDiff < 0");
		validateParameter(mmrDeltaOnDefeat <= 0, "mmrDeltaOnDefeat > 0");
		validateParameter(mmrDeltaOnDefeatPerScoreDiff <= 0, "mmrDeltaOnDefeatPerScoreDiff > 0");
	}

	protected void validateParameter(boolean condition, String errorMessage) {
		if (!condition) {
			throw new CatalogException("Error validating BotMmrAlgorithmConfiguration: " + errorMessage);
		}
	}
}
