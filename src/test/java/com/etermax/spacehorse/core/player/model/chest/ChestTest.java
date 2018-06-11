package com.etermax.spacehorse.core.player.model.chest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.mock.ChestScenarionBuilder;

public class ChestTest {

	private static final int SPEEDUP_TIME_IN_SECONDS = 10;
	private ServerTimeProvider timeProvider;

	@Before
	public void setUp() throws Exception {
		timeProvider = new FixedServerTimeProvider();
	}

	@Test
	public void whenSpeedupOpeningOnceThenTheOpeningEndTimeWasDecreased10Seconds() throws Exception {
		// given
		Chest aChest = new ChestScenarionBuilder(timeProvider, 300).startOpening().build();
		Date openingEndDateBeforeSpeedup = aChest.getChestOpeningEndDate();
		// When
		aChest.speedupOpening(timeProvider.getDate(), SPEEDUP_TIME_IN_SECONDS);
		// Given
		assertThatOpeningEndTimeWasUpdated(aChest, openingEndDateBeforeSpeedup, SPEEDUP_TIME_IN_SECONDS);
		assertThatOpeningStartTimeIsNotEqualsToOpeningEndTime(aChest);
	}

	@Test
	public void whenSpeedupOpeningTwiceThenTheOpeningEndTimeWasDecreased10Seconds() throws Exception {
		// given
		Chest aChest = new ChestScenarionBuilder(timeProvider, 300).startOpening().build();
		Date openingEndDateBeforeSpeedup = aChest.getChestOpeningEndDate();
		aChest.speedupOpening(timeProvider.getDate(), SPEEDUP_TIME_IN_SECONDS);
		// When
		aChest.speedupOpening(timeProvider.getDate(), SPEEDUP_TIME_IN_SECONDS);
		// Given
		assertThatOpeningEndTimeWasUpdated(aChest, openingEndDateBeforeSpeedup, SPEEDUP_TIME_IN_SECONDS * 2);
		assertThatOpeningStartTimeIsNotEqualsToOpeningEndTime(aChest);
	}

	@Test
	public void whenSpeedupOpeningManyTimesThenTheOpeningEndIsEqualsToOpeningStartTime() throws Exception {
		// given
		Chest aChest = new ChestScenarionBuilder(timeProvider, 30).startOpening().build();
		Date openingEndDateBeforeSpeedup = aChest.getChestOpeningEndDate();
		aChest.speedupOpening(timeProvider.getDate(), SPEEDUP_TIME_IN_SECONDS);
		aChest.speedupOpening(timeProvider.getDate(), SPEEDUP_TIME_IN_SECONDS);
		// When
		aChest.speedupOpening(timeProvider.getDate(), SPEEDUP_TIME_IN_SECONDS);
		// Given
		assertThatOpeningEndTimeWasUpdated(aChest, openingEndDateBeforeSpeedup, SPEEDUP_TIME_IN_SECONDS * 3);
		assertThatOpeningStartTimeIsEqualsToOpeningEndTime(aChest);
	}

	@Test
	public void whenSpeedupOpeningManyTimesThenTheSpeedupShouldBeThrownAnException() throws Exception {
		// given
		Chest aChest = new ChestScenarionBuilder(timeProvider, 30).startOpening().build();
		aChest.speedupOpening(timeProvider.getDate(), SPEEDUP_TIME_IN_SECONDS);
		aChest.speedupOpening(timeProvider.getDate(), SPEEDUP_TIME_IN_SECONDS);
		aChest.speedupOpening(timeProvider.getDate(), SPEEDUP_TIME_IN_SECONDS);
		// When
		Throwable thrown = catchThrowable(() -> aChest.speedupOpening(timeProvider.getDate(), SPEEDUP_TIME_IN_SECONDS));
		//Then
		assertThat(thrown).isInstanceOf(ApiException.class).hasMessage("Chest opening can't be speedup");
	}

	private void assertThatOpeningStartTimeIsNotEqualsToOpeningEndTime(Chest aChest) {
		long currentOpeningStartTime = ServerTime.fromDate(aChest.getChestOpeningStartDate());
		long currentOpeningEndTime = ServerTime.fromDate(aChest.getChestOpeningEndDate());
		assertThat(currentOpeningStartTime).isNotEqualTo(currentOpeningEndTime);
	}

	private void assertThatOpeningStartTimeIsEqualsToOpeningEndTime(Chest aChest) {
		long currentOpeningStartTime = ServerTime.fromDate(aChest.getChestOpeningStartDate());
		long currentOpeningEndTime = ServerTime.fromDate(aChest.getChestOpeningEndDate());
		assertThat(currentOpeningStartTime).isEqualTo(currentOpeningEndTime);
	}

	private void assertThatOpeningEndTimeWasUpdated(Chest aChest, Date openingEndDateBeforeSpeedup, int speedupTimeInSeconds) {
		long actualOpeningEndTime = ServerTime.fromDate(aChest.getChestOpeningEndDate());
		long expectedOpeningEndTime = ServerTime.fromDate(openingEndDateBeforeSpeedup) - speedupTimeInSeconds;
		assertThat(actualOpeningEndTime).isEqualTo(expectedOpeningEndTime);
	}

}
