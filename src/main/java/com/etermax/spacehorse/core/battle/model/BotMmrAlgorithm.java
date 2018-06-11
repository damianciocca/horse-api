package com.etermax.spacehorse.core.battle.model;

public class BotMmrAlgorithm {

	private static final int MAX_SCORE = 3;
	private final BotMmrAlgorithmConfiguration configuration;

	public BotMmrAlgorithm(BotMmrAlgorithmConfiguration configuration) {
		this.configuration = configuration;
	}

	public int updateMmr(int currentBotMmr, BattleResult result, int playerScore, int opponentScore) {

		int newBotMmr = currentBotMmr;

		switch (result) {
			case WIN:
				newBotMmr += configuration.getMmrDeltaOnVictory() + configuration.getMmrDeltaOnVictoryPerScoreDiff() * getScoreDiff(playerScore,
						opponentScore);
				break;

			case TIE:
				newBotMmr += configuration.getMmrDeltaOnTie();
				break;

			case LOSE:
				newBotMmr += configuration.getMmrDeltaOnDefeat() + configuration.getMmrDeltaOnDefeatPerScoreDiff() * getScoreDiff(playerScore,
						opponentScore);
				break;
		}

		if (newBotMmr < 0)
			newBotMmr = 0;

		return newBotMmr;
	}

	private int getScoreDiff(int playerScore, int opponentScore) {
		return Math.min(Math.abs(playerScore - opponentScore), MAX_SCORE);
	}

}
