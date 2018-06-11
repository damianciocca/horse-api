package com.etermax.spacehorse.core.freechest.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class FreeChestConfiguration implements FreeChestConstants {

	private final String freeChestId;
	private final int timeBetweenFreeChestsInSeconds;
	private final int maxFreeChests;

	public FreeChestConfiguration(String freeChestId, int timeBetweenFreeChestsInSeconds, int maxFreeChests) {
		this.freeChestId = freeChestId;
		this.timeBetweenFreeChestsInSeconds = timeBetweenFreeChestsInSeconds;
		this.maxFreeChests = maxFreeChests;
	}

	@Override
	public String getFreeChestId() {
		return freeChestId;
	}

	@Override
	public int getTimeBetweenFreeChestsInSeconds() {
		return timeBetweenFreeChestsInSeconds;
	}

	@Override
	public int getMaxFreeChests() {
		return maxFreeChests;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
