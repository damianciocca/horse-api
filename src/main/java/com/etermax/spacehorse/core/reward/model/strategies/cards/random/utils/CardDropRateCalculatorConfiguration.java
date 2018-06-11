package com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;

public class CardDropRateCalculatorConfiguration {

	private final int dropDiffBase;

	private final int dropDiffExp;

	private final int dropDiffReducer;

	private final int winLoseReference;

	private final int winLoseReducer;

	public CardDropRateCalculatorConfiguration(int dropDiffBase, int dropDiffExp, int dropDiffReducer, int winLoseReference, int winLoseReducer) {
		this.dropDiffBase = dropDiffBase;
		this.dropDiffExp = dropDiffExp;
		this.dropDiffReducer = dropDiffReducer;
		this.winLoseReference = winLoseReference;
		this.winLoseReducer = winLoseReducer;
	}

	public CardDropRateCalculatorConfiguration() {
		this.dropDiffBase = 0;
		this.dropDiffExp = 0;
		this.dropDiffReducer = 0;
		this.winLoseReference = 0;
		this.winLoseReducer = 0;
	}

	public int getDropDiffBase() {
		return dropDiffBase;
	}

	public int getDropDiffExp() {
		return dropDiffExp;
	}

	public int getWinLoseReference() {
		return winLoseReference;
	}

	public int getWinLoseReducer() {
		return winLoseReducer;
	}

	public int getDropDiffReducer() {
		return dropDiffReducer;
	}

	public void validate() {
		validateParameter(dropDiffBase >= 0, "dropDiffBase < 0");
		validateParameter(dropDiffExp >= 0, "dropDiffExp < 0");
		validateParameter(dropDiffReducer > 0, "dropDiffReducer <= 0");
		validateParameter(winLoseReference >= 0, "winLoseReference < 0");
		validateParameter(winLoseReducer > 0, "winLoseReducer <= 0");
	}

	protected void validateParameter(boolean condition, String errorMessage) {
		if (!condition) {
			throw new CatalogException("Error validating CardDropRateCalculatorConfiguration: " + errorMessage);
		}
	}
}
