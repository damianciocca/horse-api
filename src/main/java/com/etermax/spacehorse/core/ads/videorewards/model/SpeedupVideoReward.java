package com.etermax.spacehorse.core.ads.videorewards.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;

public class SpeedupVideoReward implements VideoReward {

	private final String placeName;
	private final int timeFrameInSeconds;
	private final int maximumNumberOfViews;
	private final int speedupTimeInSeconds;

	public SpeedupVideoReward(String placeName, int timeFrameInSeconds, int maximumNumberOfViews, int speedupTimeInSeconds) {
		this.placeName = placeName;
		this.timeFrameInSeconds = timeFrameInSeconds;
		this.maximumNumberOfViews = maximumNumberOfViews;
		this.speedupTimeInSeconds = speedupTimeInSeconds;
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

	public int getSpeedupTimeInSeconds() {
		return speedupTimeInSeconds;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
