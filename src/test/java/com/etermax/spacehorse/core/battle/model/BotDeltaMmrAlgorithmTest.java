package com.etermax.spacehorse.core.battle.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BotDeltaMmrAlgorithmTest {

	private BotMmrAlgorithmConfiguration botMmrAlgorithmConfiguration;
	private BotMmrAlgorithm botMmrAlgorithm;

	private final int MMR_DELTA_ON_VICTORY = 10;
	private final int MMR_DELTA_ON_VICTORY_PER_SCORE_DIFF = 5;

	private final int MMR_DELTA_ON_DEFEAT = -9;
	private final int MMR_DELTA_ON_DEFEAT_PER_SCORE_DIFF = -4;

	private final int MMR_DELTA_ON_TIE = 1;

	private int startingMmr;
	private int updatedMmr;
	private int playerScore;
	private int opponentScore;

	@Before
	public void setUp() {
		botMmrAlgorithmConfiguration = new BotMmrAlgorithmConfiguration(MMR_DELTA_ON_VICTORY, MMR_DELTA_ON_VICTORY_PER_SCORE_DIFF,
				MMR_DELTA_ON_DEFEAT, MMR_DELTA_ON_DEFEAT_PER_SCORE_DIFF, MMR_DELTA_ON_TIE);
		botMmrAlgorithm = new BotMmrAlgorithm(botMmrAlgorithmConfiguration);
	}

	@After
	public void tearDown() {
		startingMmr = 0;
		updatedMmr = 0;
		playerScore = 0;
		opponentScore = 0;
	}

	@Test
	public void winningABattleIncreasesMmr() {
		givenAStartingMmrAndBattleResult(100, 3, 0);

		whenUpdatingTheBotMmr();

		thenTheNewMmrIs(100 + MMR_DELTA_ON_VICTORY + MMR_DELTA_ON_VICTORY_PER_SCORE_DIFF * 3);
	}

	@Test
	public void losingABattleDecreasesMmr() {
		givenAStartingMmrAndBattleResult(100, 0, 3);

		whenUpdatingTheBotMmr();

		thenTheNewMmrIs(100 + MMR_DELTA_ON_DEFEAT + MMR_DELTA_ON_DEFEAT_PER_SCORE_DIFF * 3);
	}

	@Test
	public void tieABattleIncreasesMmr() {
		givenAStartingMmrAndBattleResult(100, 0, 0);

		whenUpdatingTheBotMmr();

		thenTheNewMmrIs(100 + MMR_DELTA_ON_TIE);
	}

	@Test
	public void mmrNeverGoesBelowZeroAfterLosing() {
		givenAStartingMmrAndBattleResult(0, 0, 3);

		whenUpdatingTheBotMmr();

		thenTheNewMmrIs(0);
	}

	@Test
	public void winningByOnlyOnePointUpdatedMmrCorrectly() {
		givenAStartingMmrAndBattleResult(100, 3, 2);

		whenUpdatingTheBotMmr();

		thenTheNewMmrIs(100 + MMR_DELTA_ON_VICTORY + MMR_DELTA_ON_VICTORY_PER_SCORE_DIFF * 1);
	}

	@Test
	public void losingByOnlyOnePointUpdatedMmrCorrectly() {
		givenAStartingMmrAndBattleResult(100, 2, 3);

		whenUpdatingTheBotMmr();

		thenTheNewMmrIs(100 + MMR_DELTA_ON_DEFEAT + MMR_DELTA_ON_DEFEAT_PER_SCORE_DIFF * 1);
	}

	private void thenTheNewMmrIs(int mmr) {
		assertThat(updatedMmr, is(equalTo(mmr)));
	}

	private void whenUpdatingTheBotMmr() {
		updatedMmr = botMmrAlgorithm.updateMmr(startingMmr, getBattleResultFromScore(), playerScore, opponentScore);
	}

	private void givenAStartingMmrAndBattleResult(int mmr, int playerScore, int opponentScore) {
		givenAStartingMmr(mmr);
		givenABattleResult(playerScore, opponentScore);
	}

	private void givenABattleResult(int playerScore, int opponentScore) {
		this.playerScore = playerScore;
		this.opponentScore = opponentScore;
	}

	private void givenAStartingMmr(int mmr) {
		startingMmr = mmr;
	}

	public BattleResult getBattleResultFromScore() {
		if (playerScore > opponentScore) {
			return BattleResult.WIN;
		}
		if (playerScore < opponentScore) {
			return BattleResult.LOSE;
		}

		return BattleResult.TIE;
	}
}