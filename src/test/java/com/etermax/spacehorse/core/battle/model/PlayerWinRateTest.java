package com.etermax.spacehorse.core.battle.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class PlayerWinRateTest {

	private static final int MMR = 140;

	@Test
	public void givenAnMmrLessThanCappedMmr_WhenUpdateMmr_ThenTheMmrShouldBeUpdated() {
		// given
		PlayerWinRate playerWinRate = aPlayerWinRate(MMR);
		int mmrToUpdate = 180;
		int cappedMmr = 200;
		// when
		playerWinRate.updateMmr(mmrToUpdate, cappedMmr, true);
		// then
		assertThat(playerWinRate.getMmr()).isEqualTo(mmrToUpdate);
	}

	@Test
	public void givenAnMmrGreaterThanCappedMmr_WhenUpdateMmr_ThenTheMmrShouldBeUpdatedWithCappedMmr() {
		// given
		PlayerWinRate playerWinRate = aPlayerWinRate(MMR);
		int mmrToUpdate = 210;
		int cappedMmr = 200;
		// when
		playerWinRate.updateMmr(mmrToUpdate, cappedMmr, true);
		// then
		assertThat(playerWinRate.getMmr()).isEqualTo(cappedMmr);
	}

	@Test
	public void whenUpdateMmrWithMmrHigherToCappedMmrAndCappedIsNotEnabledThenIsUpdated(){
		// given
		PlayerWinRate playerWinRate = aPlayerWinRate(MMR);
		int mmrToUpdate = 210;
		int cappedMmr = 200;
		// when
		playerWinRate.updateMmr(mmrToUpdate, cappedMmr, false);
		// then
		assertThat(playerWinRate.getMmr()).isEqualTo(mmrToUpdate);
	}

	@Test
	public void givenAnMmrLessThanCappedMmr_WhenTryToFixMmr_ThenTheMmrShouldNotBeUpdated() {
		// given
		PlayerWinRate playerWinRate = aPlayerWinRate(MMR);
		int cappedMmr = 200;
		// when
		boolean updated = playerWinRate.tryToFixMmrWith(cappedMmr, true);
		// then
		assertThat(updated).isFalse();
		assertThat(playerWinRate.getMmr()).isEqualTo(MMR);
	}

	@Test
	public void givenAnMmrGreaterThanCappedMmr_WhenTryToFixMmr_ThenTheMmrShouldBeUpdatedWithCappedMmr() {
		// given
		PlayerWinRate playerWinRate = aPlayerWinRate(MMR);
		int cappedMmr = 130;
		// when
		boolean updated = playerWinRate.tryToFixMmrWith(cappedMmr, true);
		// then
		assertThat(updated).isTrue();
		assertThat(playerWinRate.getMmr()).isEqualTo(cappedMmr);
	}

	private PlayerWinRate aPlayerWinRate(int mmr) {
		return new PlayerWinRate("100", 10, 2, 0, mmr);
	}
}
