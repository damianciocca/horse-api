package com.etermax.spacehorse.core.freechest.model;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.common.exception.ApiException;

@DynamoDBDocument
public class FreeChest {

	private static final long SECONDS_TO_MS = 1000L;

	@DynamoDBAttribute(attributeName = "lastChestOpeningDate")
	private Date lastChestOpeningDate;

	public FreeChest() {
	}

	public FreeChest(Date lastChestOpeningDate) {
		this.lastChestOpeningDate = lastChestOpeningDate;
	}

	public int getAvailableChests(Date now, FreeChestConstants constants) {
		if (lastChestOpeningDate == null) {
			return constants.getMaxFreeChests();
		}

		if (constants.getTimeBetweenFreeChestsInSeconds() <= 0 || constants.getMaxFreeChests() <= 0) {
			return 0;
		}

		int availableChests = getAvailableChestsFromElapsedTime(now, constants);

		return availableChests;
	}

	private int getAvailableChestsFromElapsedTime(Date now, FreeChestConstants constants) {
		long deltaTimeSeconds = getSecondsBetween(lastChestOpeningDate, now);

		if (deltaTimeSeconds < 0) {
			deltaTimeSeconds = 0;
		}

		long availableChests = deltaTimeSeconds / constants.getTimeBetweenFreeChestsInSeconds();

		availableChests = clamp(availableChests, 0, constants.getMaxFreeChests());

		return (int) availableChests;
	}

	private long clamp(long value, long min, long max) {
		if (value > max) {
			return max;
		}
		if (value < min) {
			return min;
		}
		return value;
	}

	public int getSecondsToNextFreeChest(Date now, FreeChestConstants constants) {

		int availableChests = getAvailableChests(now, constants);

		if (availableChests >= constants.getMaxFreeChests()) {
			return 0;
		}

		Date nextChestOpeninigDate = addSecondsToDate(lastChestOpeningDate,
				constants.getTimeBetweenFreeChestsInSeconds() * (availableChests + 1));

		return (int) getSecondsBetween(now, nextChestOpeninigDate);
	}

	public boolean canBeOpened(Date now, FreeChestConstants constants) {
		return getAvailableChests(now, constants) > 0;
	}

	public Date getLastChestOpeningDate() {
		return lastChestOpeningDate;
	}

	public void setLastChestOpeningDate(Date lastChestOpeningDate) {
		this.lastChestOpeningDate = lastChestOpeningDate;
	}

	public void open(Date now, FreeChestConstants constants) {
		if (!canBeOpened(now, constants)) {
			throw new ApiException("Chest can't be opened");
		}

		int availableChests = getAvailableChests(now, constants);

		int secondsToNextFreeChest = getSecondsToNextFreeChest(now, constants);

		this.lastChestOpeningDate = removeSecondsFromDate(now, (availableChests - 1) * constants
				.getTimeBetweenFreeChestsInSeconds());

		if (secondsToNextFreeChest > 0) {
			this.lastChestOpeningDate = removeSecondsFromDate(lastChestOpeningDate,
					constants.getTimeBetweenFreeChestsInSeconds() - secondsToNextFreeChest);
		}
	}

	static private Date addSecondsToDate(Date date, long secondsToAdd) {
		return new Date(date.getTime() + secondsToAdd * SECONDS_TO_MS);
	}

	static private Date removeSecondsFromDate(Date date, long secondsToRemove) {
		return new Date(date.getTime() - secondsToRemove * SECONDS_TO_MS);
	}

	static private long getSecondsBetween(Date date1, Date date2) {
		return (date2.getTime() - date1.getTime()) / SECONDS_TO_MS;
	}

	public static FreeChest buildNewFreeChest(Date date, FreeChestConstants freeChestConstants) {
		Date lastChestOpeningDate = new Date(date.getTime() - freeChestConstants.getTimeBetweenFreeChestsInSeconds() * SECONDS_TO_MS);
		return new FreeChest(lastChestOpeningDate);
	}
}
