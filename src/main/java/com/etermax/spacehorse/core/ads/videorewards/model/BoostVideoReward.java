package com.etermax.spacehorse.core.ads.videorewards.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;

public class BoostVideoReward implements VideoReward {

	private final String placeName;
	private final int timeFrameInSeconds;
	private final int maximumNumberOfViews;
	private final int coins;

	public BoostVideoReward(String placeName, int timeFrameInSeconds, int maximumNumberOfViews, int coins) {
		this.placeName = placeName;
		this.timeFrameInSeconds = timeFrameInSeconds;
		this.maximumNumberOfViews = maximumNumberOfViews;
		this.coins = coins;
	}

	@Override
	public String getPlaceName() {
		return placeName;
	}

	@Override
	public int getTimeFrameInSeconds() {
		return timeFrameInSeconds;
	}

	@Override
	public int getMaximumNumberOfViews() {
		return maximumNumberOfViews;
	}

	@Override
	public int getExpectedMaxDurationInSeconds() {
		return VideoRewardDefinition.MAX_VIDEO_DURATION;
	}

	public int getCoins() {
		return coins;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}