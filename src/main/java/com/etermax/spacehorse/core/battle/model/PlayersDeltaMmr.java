package com.etermax.spacehorse.core.battle.model;

public class PlayersDeltaMmr {

	private final int winnerDeltaMmr;
	private final int loserDeltaMmr;

	public PlayersDeltaMmr(int winnerDeltaMmr, int loserDeltaMmr) {
		this.winnerDeltaMmr = winnerDeltaMmr;
		this.loserDeltaMmr = loserDeltaMmr;
	}

	public int getWinnerDeltaMmr() {
		return winnerDeltaMmr;
	}

	public int getLoserDeltaMmr() {
		return loserDeltaMmr;
	}
}
