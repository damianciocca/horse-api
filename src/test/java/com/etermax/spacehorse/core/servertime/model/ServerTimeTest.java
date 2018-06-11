package com.etermax.spacehorse.core.servertime.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class ServerTimeTest {

	private ServerTimeProvider serverTimeProvider;

	@Before
	public void setUp() throws Exception {
		serverTimeProvider = new ServerTime();
	}

	@Test
	public void testRoundToMiddleOfDayIsBefore() {

		Calendar currentCalendar = ServerTime.getTodayCalendarAtTime(serverTimeProvider.getTimeNowAsSeconds(), 13, 0, 0);
		long serverTime = ServerTime.fromDate(currentCalendar.getTime());

		long middayLong = ServerTime.roundToMiddleOfDay(serverTime);

		assertFalse(DateUtils.isSameDay(ServerTime.toDate(serverTime), ServerTime.toDate(middayLong)));

		Calendar compare = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
		compare.set(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH), 12, 0, 0);

		SimpleDateFormat fmt = new SimpleDateFormat("HHmmss");
		assertEquals(fmt.format(compare.getTime()), fmt.format(ServerTime.toCalendar(middayLong).getTime()));
	}

	@Test
	public void testRoundToMiddleOfDayIsAfter() {

		Calendar currentCalendar = ServerTime.getTodayCalendarAtTime(serverTimeProvider.getTimeNowAsSeconds(), 11, 0, 0);
		long serverTime = ServerTime.fromDate(currentCalendar.getTime());

		long middayLong = ServerTime.roundToMiddleOfDay(serverTime);

		assertTrue(DateUtils.isSameDay(ServerTime.toDate(serverTime), ServerTime.toDate(middayLong)));

		Calendar compare = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
		compare.set(currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH), 12, 0, 0);

		SimpleDateFormat fmt = new SimpleDateFormat("HHmmss");
		assertEquals(fmt.format(compare.getTime()), fmt.format(ServerTime.toCalendar(middayLong).getTime()));
	}

	@Test
	public void testSecondsToDateToDateTimeToSeconds() throws Exception {
		// given
		long serverTimeInSeconds = serverTimeProvider.getTimeNowAsSeconds();
		Date date = ServerTime.toDate(serverTimeInSeconds);
		long seconds = ServerTime.fromDate(date);
		DateTime dateTime = ServerTime.toDateTime(seconds);
		// when
		long dateTimeInSeconds = ServerTime.fromDate(dateTime);
		// then
		assertThat(serverTimeInSeconds).isEqualTo(dateTimeInSeconds);
	}

	@Test
	public void testSecondsToDateToSeconds() throws Exception {
		// given
		long serverTimeInSeconds = serverTimeProvider.getTimeNowAsSeconds();
		Date date = ServerTime.toDate(serverTimeInSeconds);
		// when
		long seconds = ServerTime.fromDate(date);
		// then
		assertThat(serverTimeInSeconds).isEqualTo(seconds);
	}

	@Test
	public void testSecondsToDateTimeToSeconds() throws Exception {
		// given
		long serverTimeInSeconds = serverTimeProvider.getTimeNowAsSeconds();
		DateTime dateTime = ServerTime.toDateTime(serverTimeInSeconds);
		// when
		long seconds = ServerTime.fromDate(dateTime);
		// then
		assertThat(serverTimeInSeconds).isEqualTo(seconds);
	}

	@Test
	public void testDateToSecondsToDate() throws Exception {
		// given
		Date date = serverTimeProvider.getDate();
		long seconds = ServerTime.fromDate(date);
		// when
		Date newDate = ServerTime.toDate(seconds);
		// then
		assertThat(getSeconds(newDate)).isEqualTo(getSeconds(date));
	}

	@Test
	public void testDateTimeToSecondsToDateTime() throws Exception {
		// given
		DateTime dateTime = serverTimeProvider.getDateTime();
		long seconds = ServerTime.fromDate(dateTime);
		// when
		DateTime newDateTime = ServerTime.toDateTime(seconds);
		// then
		assertThat(newDateTime.getSecondOfDay()).isEqualTo(dateTime.getSecondOfDay());
	}

	@Test
	public void testRoundToStartDayAsText() throws Exception {
		// given
		String serverTimeAsText = "2017-02-20";
		// when
		DateTime dateTime = ServerTime.roundToStartOfDay(serverTimeAsText);
		// then
		assertThat(dateTime.getYear()).isEqualTo(2017);
		assertThat(dateTime.getMonthOfYear()).isEqualTo(2);
		assertThat(dateTime.getDayOfMonth()).isEqualTo(20);
		assertThat(dateTime.getHourOfDay()).isEqualTo(0);
		assertThat(dateTime.getMinuteOfDay()).isEqualTo(0);
		assertThat(dateTime.getSecondOfDay()).isEqualTo(0);
	}

	private int getSeconds(Date date) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		return instance.get(Calendar.SECOND);
	}
}
