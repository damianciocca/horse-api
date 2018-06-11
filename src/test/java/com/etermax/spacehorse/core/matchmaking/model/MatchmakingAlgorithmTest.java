package com.etermax.spacehorse.core.matchmaking.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;

public class MatchmakingAlgorithmTest {

	private MatchmakingAlgorithm matchmakingAlgorithm;
	private MatchmakingAlgorithmConfiguration matchmakingAlgorithmConfiguration;

	private PlayerWinRate playerWinRateA;
	private PlayerWinRate playerWinRateB;
	private boolean matches;

	private final int MMR_LIMIT = 60;
	private final double INDEX_TOLERANCE = 0.25;

	@Before
	public void setUp() {
		matchmakingAlgorithm = new MatchmakingAlgorithm();
		matchmakingAlgorithmConfiguration = new MatchmakingAlgorithmConfiguration(INDEX_TOLERANCE, MMR_LIMIT);
	}

	@After
	public void tearDown() {
		matches = false;
	}

	@Test
	public void testMatch() {

		givenPlayerAWithMmr(0);
		givenPlayerBWithMmr(MMR_LIMIT - 1);

		whenInvokingMatchmkingAlgorithm();

		thenPlayersMatch();
	}

	@Test
	public void testMatch2() {

		givenPlayerAWithMmr(100);
		givenPlayerBWithMmr(100 + MMR_LIMIT - 1);

		whenInvokingMatchmkingAlgorithm();

		thenPlayersMatch();
	}

	@Test
	public void testDontMatch() {

		givenPlayerAWithMmr(0);
		givenPlayerBWithMmr(MMR_LIMIT + 1);

		whenInvokingMatchmkingAlgorithm();

		thenPlayersDontMatch();
	}

	@Test
	public void testDontMatch2() {

		givenPlayerAWithMmr(100);
		givenPlayerBWithMmr(100 + MMR_LIMIT + 1);

		whenInvokingMatchmkingAlgorithm();

		thenPlayersDontMatch();
	}

	private void thenPlayersDontMatch() {
		assertFalse(matches);
	}

	private void thenPlayersMatch() {
		assertTrue(matches);
	}

	private void whenInvokingMatchmkingAlgorithm() {
		matches = this.matchmakingAlgorithm.invokeWith(matchmakingAlgorithmConfiguration, playerWinRateA, playerWinRateB);
	}

	private void givenPlayerAWithMmr(int mmr) {
		playerWinRateA = new PlayerWinRate("", 0, 0, 0, mmr);
	}

	private void givenPlayerBWithMmr(int mmr) {
		playerWinRateB = new PlayerWinRate("", 0, 0, 0, mmr);
	}
}
