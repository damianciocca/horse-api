package com.etermax.spacehorse.core.specialoffer.model;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

public class ScheduledSpecialOfferValidator {

	public boolean isAvailable(DateTime now, DateTime activationTime, int frequencyInDays, List<SpecialOfferHistory> specialOfferHistories,
			String specialOfferId, String groupId) {
		boolean available = isAvailable(now, activationTime, frequencyInDays);
		return available && !wasAlreadyShownInTheCurrentDay(now, specialOfferHistories, specialOfferId, groupId);
	}

	private boolean isAvailable(DateTime now, DateTime activationTime, int frequencyInDays) {
		int daysFromActivationTime = getDaysBetween(now, activationTime);
		if (activationTimeIsNotReached(daysFromActivationTime)) {
			return false;
		}
		int difference = Math.abs(daysFromActivationTime % frequencyInDays);
		return difference == 0;
	}

	private boolean wasAlreadyShownInTheCurrentDay(DateTime now, List<SpecialOfferHistory> specialOfferHistories, String specialOfferId,
			String groupId) {
		Optional<SpecialOfferHistory> history = specialOfferHistories.stream().filter(bySpecialOfferHistory(specialOfferId, groupId)).findFirst();
		if (history.isPresent()) {
			int daysBetween = getDaysBetween(now, history.get().getCreationTime());
			return isTheSameDay(daysBetween);
		}
		return false;
	}

	private boolean isTheSameDay(int daysBetween) {
		return daysBetween == 0;
	}

	private Predicate<SpecialOfferHistory> bySpecialOfferHistory(String specialOfferId, String groupId) {
		return specialOfferHistory -> specialOfferHistory.getSpecialOfferId().equals(specialOfferId) || (isNotBlank(groupId) && groupId
				.equals(specialOfferHistory.getGroupId()));
	}

	private boolean activationTimeIsNotReached(int daysFromActivationTime) {
		return daysFromActivationTime < 0;
	}

	private int getDaysBetween(DateTime now, DateTime activationTime) {
		return Days
				.daysBetween(activationTime.withZone(DateTimeZone.UTC).withTimeAtStartOfDay(), now.withZone(DateTimeZone.UTC).withTimeAtStartOfDay())
				.getDays();
	}
}
