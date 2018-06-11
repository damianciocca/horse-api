package com.etermax.spacehorse.core.battle.model;

import static java.lang.Math.abs;

public class DeltaMmrAlgorithm {

	private final int mmrAlgorithmDefault;
	private final double mmrAlgorithmMultiplier;
	private final DeltaMmrPercentageSelector deltaMmrPercentageSelector;
	private final int minimumMMR;

	public DeltaMmrAlgorithm(int mmrAlgorithmDefault, double mmrAlgorithmMultiplier, DeltaMmrPercentageSelector deltaMmrPercentageSelector,
			int minimumMMR) {
		this.mmrAlgorithmDefault = mmrAlgorithmDefault;
		this.mmrAlgorithmMultiplier = mmrAlgorithmMultiplier;
		this.deltaMmrPercentageSelector = deltaMmrPercentageSelector;
		this.minimumMMR = minimumMMR;
	}

	public PlayersDeltaMmr calculate(int winnerMmr, int loserMmr) {
		int percentageByVictory = deltaMmrPercentageSelector.find(winnerMmr).getPercentageByVictory();
		int percentageByLose = deltaMmrPercentageSelector.find(loserMmr).getPercentageByLose();
		int mmr = getMmr(winnerMmr, loserMmr);

		int newWinnerDeltaMmr = applyPercentage(mmr, percentageByVictory);
		int newLoserDeltaMmr = calculateNewLoserDeltaMmr(loserMmr, percentageByLose, mmr);

		return new PlayersDeltaMmr(newWinnerDeltaMmr, newLoserDeltaMmr);
	}

	private int calculateNewLoserDeltaMmr(int loserMmr, int percentageByLose, int mmr) {
		int newLoserDeltaMmr;
		if (loserMmr <= minimumMMR) {
			newLoserDeltaMmr = 0;
		} else if ((loserMmr - mmr) < minimumMMR) {
			newLoserDeltaMmr = applyPercentage(-(loserMmr - minimumMMR), percentageByLose);
		} else {
			newLoserDeltaMmr = applyPercentage(-mmr, percentageByLose);
		}
		return newLoserDeltaMmr;
	}

	private double getMultiplier() {
		Double multiplier = mmrAlgorithmMultiplier;
		if (multiplier.intValue() == 0) {
			multiplier = 1.0;
		}
		return multiplier;
	}

	private int getMmr(int winnerMmr, int loserMmr) {
		double mmr;
		int diffMmrs = Math.min(abs(winnerMmr - loserMmr), 150);
		double multiplier = getMultiplier();

		if (winnerMmr > loserMmr) {
			mmr = mmrAlgorithmDefault - diffMmrs / multiplier;
		} else {
			mmr = mmrAlgorithmDefault + diffMmrs / multiplier;
		}

		return (int) Math.round(mmr);
	}

	private int applyPercentage(int mmr, int percentageDeltaMmr) {
		return (mmr * percentageDeltaMmr) / 100;
	}

}