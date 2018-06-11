package com.etermax.spacehorse.core.servertime.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ServerTime implements ServerTimeProvider {

	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	@Override
	public DateTime getDateTime() {
		return new DateTime().withZone(DateTimeZone.UTC);
	}

	@Override
	public Date getDate() {
		return getDateTime().toDate();
	}

	@Override
	public long getTimeNowAsSeconds() {
		return fromTimeInMillis(System.currentTimeMillis());
	}

	static public long fromDate(DateTime date) {
		if (date != null) {
			return fromTimeInMillis(date.getMillis());
		} else {
			return 0;
		}
	}

	static public long fromDate(Date date) {
		if (date != null) {
			return fromTimeInMillis(date.getTime());
		} else {
			return 0;
		}
	}

	static public Date toDate(long serverTimeInSeconds) {
		return new Date(getMillisFromSeconds(serverTimeInSeconds));
	}

	public static DateTime toDateTime(long serverTimeInSeconds) {
		return new DateTime(getMillisFromSeconds(serverTimeInSeconds)).withZone(DateTimeZone.UTC);
	}


	static public Calendar toCalendar(long serverTime) {
		Calendar cal = getCalendarInstance();
		cal.setTime(toDate(serverTime));
		return cal;
	}

	static public Calendar getCalendarInstance() {
		return Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.ENGLISH);
	}

	static public long fromTimeInMillis(long timeMillis) {
		return timeMillis / 1000L;
	}

	static public Calendar getTodayCalendarAtTime(long serverTime, int hourOfDay, int minute, int second) {
        Calendar serverTimeCalendar = ServerTime.toCalendar(serverTime);
        Calendar specificHourCalendar = ServerTime.getCalendarInstance();
        specificHourCalendar.set(serverTimeCalendar.get(Calendar.YEAR), serverTimeCalendar.get(Calendar.MONTH),
                serverTimeCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, second);
        return specificHourCalendar;
    }

    static public long roundToEndOfDay(long serverTime) {
        Calendar endOfDayCalendar = getTodayCalendarAtTime(serverTime, 23, 59, 59);
        return ServerTime.fromDate(endOfDayCalendar.getTime());
    }

    static public long roundToMiddleOfDay(long serverTime) {
        Calendar startOfDayCalendar = getTodayCalendarAtTime(serverTime, 12, 00, 00);

        if (ServerTime.fromDate(startOfDayCalendar.getTime()) > serverTime) {
            return ServerTime.fromDate(startOfDayCalendar.getTime());
        }

        startOfDayCalendar.add(Calendar.HOUR, 24);
        return ServerTime.fromDate(startOfDayCalendar.getTime());
    }

	static public DateTime roundToStartOfNextDay(DateTime serverTime){
		return serverTime.plusDays(1).withTimeAtStartOfDay();
	}

	static public DateTime roundToStartOfDay(DateTime serverTime){
		return serverTime.withZone(DateTimeZone.UTC).withTimeAtStartOfDay();
	}

    static public DateTime roundToStartOfDay(String serverTimeAsText){
		DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(ServerTime.YYYY_MM_DD);
		return dateFormatter.parseDateTime(serverTimeAsText).withZone(DateTimeZone.UTC).withTimeAtStartOfDay();
	}

	static public String toDateTimeAsText(DateTime serverTime){
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(ServerTime.YYYY_MM_DD);
		return serverTime.toString(dateTimeFormatter);
	}

	private static long getMillisFromSeconds(long serverTimeInSeconds) {
		return serverTimeInSeconds * 1000L;
	}
}
