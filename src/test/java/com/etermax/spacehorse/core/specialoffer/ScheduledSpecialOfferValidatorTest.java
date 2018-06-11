package com.etermax.spacehorse.core.specialoffer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.specialoffer.model.ScheduledSpecialOfferValidator;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferHistory;
import com.google.common.collect.Lists;

public class ScheduledSpecialOfferValidatorTest {

	private static final String SPECIAL_OFFER_ID = "offerId";
	private static final String GROUP_ID = "";
	private FixedServerTimeProvider timeProvider;
	private ScheduledSpecialOfferValidator validator;

	@Before
	public void setUp() throws Exception {
		timeProvider = new FixedServerTimeProvider();
		validator = new ScheduledSpecialOfferValidator();
	}

	@Test
	public void activationTimeIsNotReachedToActivateTheSpecialOfferCase1() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime();
		DateTime activationTime = timeProvider.getDateTime().plusDays(1);
		int frequencyInDays = 2;
		// when
		boolean isAvailable = validator.isAvailable(now, activationTime, frequencyInDays, Lists.newArrayList(), SPECIAL_OFFER_ID, GROUP_ID);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void activationTimeIsNotReachedToActivateTheSpecialOfferCase2() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime();
		DateTime activationTime = timeProvider.getDateTime().plusDays(2);
		int frequencyInDays = 2;
		// when
		boolean isAvailable = validator.isAvailable(now, activationTime, frequencyInDays, Lists.newArrayList(), SPECIAL_OFFER_ID, GROUP_ID);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void activationTimeIsNotReachedToActivateTheSpecialOfferCase3() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime();
		DateTime activationTime = timeProvider.getDateTime().plusDays(3);
		int frequencyInDays = 2;
		// when
		boolean isAvailable = validator.isAvailable(now, activationTime, frequencyInDays, Lists.newArrayList(), SPECIAL_OFFER_ID, GROUP_ID);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void activationTimeIsNotReachedToActivateTheSpecialOfferCase4() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime();
		DateTime activationTime = timeProvider.getDateTime().plusDays(4);
		int frequencyInDays = 2;
		// when
		boolean isAvailable = validator.isAvailable(now, activationTime, frequencyInDays, Lists.newArrayList(), SPECIAL_OFFER_ID, GROUP_ID);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void activationTimeIsNotReachedToActivateTheSpecialOfferCase5() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime().withHourOfDay(23).withMinuteOfHour(59);
		DateTime activationTime = timeProvider.getDateTime().plusDays(1).withHourOfDay(00).withMinuteOfHour(01);
		int frequencyInDays = 2;
		// when
		boolean isAvailable = validator.isAvailable(now, activationTime, frequencyInDays, Lists.newArrayList(), SPECIAL_OFFER_ID, GROUP_ID);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void activationTimeIsNotReachedToActivateTheSpecialOfferCase6() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime().withHourOfDay(23).withMinuteOfHour(59);
		DateTime activationTime = timeProvider.getDateTime().minusDays(2);
		int frequencyInDays = 2;
		// when
		boolean isAvailable = validator.isAvailable(now, activationTime, frequencyInDays, Lists.newArrayList(), SPECIAL_OFFER_ID, GROUP_ID);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void activationTimeIsNotReachedToActivateTheSpecialOfferCase7() throws Exception {
		// given
		DateTime activationTime = ServerTime.roundToStartOfDay(timeProvider.getDateTime());
		int frequencyInDays = 1;
		increaseTimeInDays(1);
		DateTime oneDayAfter = timeProvider.getDateTime();
		// when
		boolean isAvailable = validator.isAvailable(oneDayAfter, activationTime, frequencyInDays, Lists.newArrayList(), SPECIAL_OFFER_ID, GROUP_ID);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void activationTimeIsReachedToActivateTheSpecialOffer() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime().plusDays(3);
		DateTime activationTime = timeProvider.getDateTime().plusDays(3);
		int frequencyInDays = 2;
		// when
		boolean isAvailable = validator.isAvailable(now, activationTime, frequencyInDays, Lists.newArrayList(), SPECIAL_OFFER_ID, GROUP_ID);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void timeIsNotReachedToActivateTheSpecialOffer() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime().plusDays(1);
		DateTime activationTime = timeProvider.getDateTime();
		int frequencyInDays = 2;
		// when
		boolean isAvailable = validator.isAvailable(now, activationTime, frequencyInDays, Lists.newArrayList(), SPECIAL_OFFER_ID, GROUP_ID);
		// then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void timeIsReachedToActivateTheSpecialOffer() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime().plusDays(2);
		DateTime activationTime = timeProvider.getDateTime();
		int frequencyInDays = 2;
		// when
		boolean isAvailable = validator.isAvailable(now, activationTime, frequencyInDays, Lists.newArrayList(), SPECIAL_OFFER_ID, GROUP_ID);
		// then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenSpecialOfferAlreadyWasShownCase1() throws Exception {
		// given
		DateTime now = timeProvider.getDateTime();
		DateTime activationTime = ServerTime.roundToStartOfDay(timeProvider.getDateTime());
		int frequencyInDays = 1;
		List<SpecialOfferHistory> specialOfferHistories = Lists
				.newArrayList(new SpecialOfferHistory(SPECIAL_OFFER_ID, GROUP_ID, timeProvider.getDateTime()));
		// when
		boolean isAvailable = validator.isAvailable(now, activationTime, frequencyInDays, specialOfferHistories, SPECIAL_OFFER_ID, GROUP_ID);
		// Then
		assertThat(isAvailable).isFalse();
	}

	@Test
	public void whenSpecialOfferAlreadyWasShownCase2() throws Exception {
		// given
		DateTime activationTime = ServerTime.roundToStartOfDay(timeProvider.getDateTime());
		int frequencyInDays = 1;
		SpecialOfferHistory specialOfferHistory = new SpecialOfferHistory(SPECIAL_OFFER_ID, GROUP_ID, timeProvider.getDateTime());
		List<SpecialOfferHistory> specialOfferHistories = Lists.newArrayList(specialOfferHistory);
		// when
		increaseTimeInDays(1);
		DateTime oneDayAfter = timeProvider.getDateTime();
		boolean isAvailable = validator.isAvailable(oneDayAfter, activationTime, frequencyInDays, specialOfferHistories, SPECIAL_OFFER_ID, GROUP_ID);
		// Then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenSpecialOfferAlreadyWasShownCase3() throws Exception {
		// given
		DateTime activationTime = ServerTime.roundToStartOfDay(timeProvider.getDateTime());
		int frequencyInDays = 1;
		SpecialOfferHistory specialOfferHistory = new SpecialOfferHistory(SPECIAL_OFFER_ID, GROUP_ID, timeProvider.getDateTime());
		List<SpecialOfferHistory> specialOfferHistories = Lists.newArrayList(specialOfferHistory);
		// when
		increaseTimeInDays(2);
		DateTime twoDaysAfter = timeProvider.getDateTime();
		boolean isAvailable = validator.isAvailable(twoDaysAfter, activationTime, frequencyInDays, specialOfferHistories, SPECIAL_OFFER_ID, GROUP_ID);
		// Then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenSpecialOfferAlreadyWasShownCase4() throws Exception {
		// given
		DateTime activationTime = ServerTime.roundToStartOfDay(timeProvider.getDateTime());
		int frequencyInDays = 1;
		SpecialOfferHistory specialOfferHistory = new SpecialOfferHistory(SPECIAL_OFFER_ID, GROUP_ID, timeProvider.getDateTime());
		List<SpecialOfferHistory> specialOfferHistories = Lists.newArrayList(specialOfferHistory);
		// when
		increaseTimeInDays(3);
		DateTime threeDaysAfter = timeProvider.getDateTime();
		boolean isAvailable = validator
				.isAvailable(threeDaysAfter, activationTime, frequencyInDays, specialOfferHistories, SPECIAL_OFFER_ID, GROUP_ID);
		// Then
		assertThat(isAvailable).isTrue();
	}

	@Test
	public void whenSpecialOfferAlreadyWasShownCase5() throws Exception {
		// given
		DateTime activationTime = ServerTime.roundToStartOfDay(timeProvider.getDateTime());
		int frequencyInDays = 5;
		SpecialOfferHistory specialOfferHistory = new SpecialOfferHistory(SPECIAL_OFFER_ID, GROUP_ID, timeProvider.getDateTime());
		List<SpecialOfferHistory> specialOfferHistories = Lists.newArrayList(specialOfferHistory);
		// when
		increaseTimeInDays(4);
		DateTime fourDaysAfter = timeProvider.getDateTime();
		boolean isAvailable = validator
				.isAvailable(fourDaysAfter, activationTime, frequencyInDays, specialOfferHistories, SPECIAL_OFFER_ID, GROUP_ID);
		// Then
		assertThat(isAvailable).isFalse();
	}

	private void increaseTimeInDays(int days) {
		DateTime timeIncreased = timeProvider.getDateTime().plusDays(days);
		timeProvider.changeTime(timeIncreased);
	}

}
