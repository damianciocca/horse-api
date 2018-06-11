package com.etermax.spacehorse.core.specialoffer.model;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class NextRefreshTimeCalculator {

	public final ServerTimeProvider timeProvider;

	public NextRefreshTimeCalculator(ServerTimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public DateTime defaultNextRefreshTime() {
		return ServerTime.roundToStartOfNextDay(timeProvider.getDateTime());
	}

	public DateTime nextRefreshTime(DateTime now, DateTime activationTime, int frequencyInDays) {

		if (activationDateIsNotReached(now, activationTime)) {
			return ServerTime.roundToStartOfDay(activationTime);
		}

		int daysBetween = getDaysBetween(now, activationTime);
		int daysDifference = Math.abs(daysBetween % frequencyInDays);

		if (daysDifference == 0) {
			return timeProvider.getDateTime().withZone(DateTimeZone.UTC).plusDays(frequencyInDays).withTimeAtStartOfDay();
		}

		DateTime elapsedTime = getElapsedTime(daysDifference);
		return elapsedTime.plusDays(frequencyInDays).withTimeAtStartOfDay();
	}

	private DateTime getElapsedTime(int daysDifference) {
		DateTime nowTime = timeProvider.getDateTime().withZone(DateTimeZone.UTC);
		return nowTime.minusDays(daysDifference);
	}

	public DateTime minNextRefreshTimeOrDefault(List<DateTime> nextRefreshTimes) {
		if (nextRefreshTimes.isEmpty()) {
			return defaultNextRefreshTime();
		}
		nextRefreshTimes.sort(DateTimeComparator.getInstance());
		return nextRefreshTimes.stream().findFirst().get();
	}

	private boolean activationDateIsNotReached(DateTime now, DateTime activationTime) {
		return activationTime.isAfter(now);
	}

	private int getDaysBetween(DateTime now, DateTime activationTime) {
		return Days.daysBetween(ServerTime.roundToStartOfDay(activationTime), ServerTime.roundToStartOfDay(now)).getDays();
	}
}
