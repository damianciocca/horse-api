package com.etermax.spacehorse.core.specialoffer;

import static org.assertj.core.api.Assertions.assertThat;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.model.NextRefreshTimeCalculator;
import com.google.common.collect.Lists;

public class NextRefreshTimeCalculatorTest {

	private FixedServerTimeProvider timeProvider;
	private NextRefreshTimeCalculator nextRefreshTimeCalculator;

	@Before
	public void setUp() throws Exception {
		timeProvider = new FixedServerTimeProvider();
		nextRefreshTimeCalculator = new NextRefreshTimeCalculator(timeProvider);
	}

	@Test
	public void nextRefreshTimeCaseWhenActivationIsNotReachedCase1() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime(); //13-12
		DateTime activationIsOneDayBefore = timeProvider.getDateTime().minusDays(1); //12-12
		int frequencyInDays = 2; ////15-12
		// when
		DateTime nextRefreshTime = nextRefreshTimeCalculator.nextRefreshTime(now, activationIsOneDayBefore, frequencyInDays);
		// then
		DateTime expectedTime = timeProvider.getDateTime().plusDays(1).withTimeAtStartOfDay();
		assertThat(nextRefreshTime).isEqualTo(expectedTime);
	}

	@Test
	public void nextRefreshTimeCaseWhenActivationIsReachedCase1() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime(); //13-12
		DateTime activationIsThreeDayBefore = timeProvider.getDateTime().minusDays(3); //10-12
		int frequencyInDays = 15; // 25-12
		// when
		DateTime nextRefreshTime = nextRefreshTimeCalculator.nextRefreshTime(now, activationIsThreeDayBefore, frequencyInDays);
		// then
		DateTime expectedTime = activationIsThreeDayBefore.plusDays(15).withTimeAtStartOfDay();
		assertThat(nextRefreshTime).isEqualTo(expectedTime);
	}

	@Test
	public void nextRefreshTimeWhenActivationTimeIsReachedCase2() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime().withHourOfDay(11);
		DateTime activationTimeIsNow = timeProvider.getDateTime().withHourOfDay(10);
		int frequencyInDays = 2; //28-11
		// when
		DateTime nextRefreshTime = nextRefreshTimeCalculator.nextRefreshTime(now, activationTimeIsNow, frequencyInDays);
		// then
		DateTime expectedTime = timeProvider.getDateTime().plusDays(frequencyInDays).withTimeAtStartOfDay();
		assertThat(nextRefreshTime).isEqualTo(expectedTime);
	}

	@Test
	public void minNextRefreshTimeCase1() throws Exception {
		// given
		DateTime nextRefreshTime1 = timeProvider.getDateTime().withHourOfDay(11);
		DateTime nextRefreshTime2 = timeProvider.getDateTime().withHourOfDay(12);
		DateTime nextRefreshTime3 = timeProvider.getDateTime().withHourOfDay(13);
		// when
		DateTime minNextRefreshTime = nextRefreshTimeCalculator
				.minNextRefreshTimeOrDefault(Lists.newArrayList(nextRefreshTime1, nextRefreshTime2, nextRefreshTime3));
		// then
		assertThat(minNextRefreshTime).isEqualTo(nextRefreshTime1);
	}

	@Test
	public void minNextRefreshTimeCase2() throws Exception {
		// given
		DateTime nextRefreshTime1 = timeProvider.getDateTime().withHourOfDay(13);
		DateTime nextRefreshTime2 = timeProvider.getDateTime().withHourOfDay(12);
		DateTime nextRefreshTime3 = timeProvider.getDateTime().withHourOfDay(11);
		//when
		DateTime minNextRefreshTime = nextRefreshTimeCalculator
				.minNextRefreshTimeOrDefault(Lists.newArrayList(nextRefreshTime1, nextRefreshTime2, nextRefreshTime3));
		// then
		assertThat(minNextRefreshTime).isEqualTo(nextRefreshTime3);
	}

	@Test
	public void minNextRefreshTimeCase3() throws Exception {
		// given
		DateTime nextRefreshTime1 = timeProvider.getDateTime().plusDays(1);
		DateTime nextRefreshTime2 = timeProvider.getDateTime().minusDays(1);
		DateTime nextRefreshTime3 = timeProvider.getDateTime().plusDays(2);
		//when
		DateTime minNextRefreshTime = nextRefreshTimeCalculator
				.minNextRefreshTimeOrDefault(Lists.newArrayList(nextRefreshTime1, nextRefreshTime2, nextRefreshTime3));
		// then
		assertThat(minNextRefreshTime).isEqualTo(nextRefreshTime2);
	}
}
