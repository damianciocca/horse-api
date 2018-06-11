package com.etermax.spacehorse.core.catalog.resource.response;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoRewardDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("PlaceName")
	private String placeName;

	@JsonProperty("TimeFrameInSeconds")
	private int timeFrameInSeconds;

	@JsonProperty("MaxViewsPerTimeFrame")
	private int maxViewsPerTimeFrame;

	@JsonProperty("SpeedupTimeInSeconds")
	private int speedupTimeInSeconds;

	@JsonProperty("Coins")
	private int coins;

	@JsonProperty("MapNumber")
	private int mapNumber;

	@JsonProperty("Type")
	private String type;

	@JsonProperty("MapFilterEnabled")
	private boolean mapFilterEnabled;

	public VideoRewardDefinitionResponse(){
	}

	public VideoRewardDefinitionResponse(String id, String placeName, int timeFrameInSeconds, int maxViewsPerTimeFrame, int speedupTimeInSeconds,
			int coins, int mapNumber, String type, boolean mapFilterEnabled) {
		this.id = id;
		this.placeName = placeName;
		this.timeFrameInSeconds = timeFrameInSeconds;
		this.maxViewsPerTimeFrame = maxViewsPerTimeFrame;
		this.speedupTimeInSeconds = speedupTimeInSeconds;
		this.coins = coins;
		this.mapNumber = mapNumber;
		this.type = type;
		this.mapFilterEnabled = mapFilterEnabled;
	}

	public boolean isMapFilterEnabled() {
		return mapFilterEnabled;
	}

	public String getId() {
		return id;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public String getPlaceName() {
		return placeName;
	}

	public int getTimeFrameInSeconds() {
		return timeFrameInSeconds;
	}

	public int getMaxViewsPerTimeFrame() {
		return maxViewsPerTimeFrame;
	}

	public int getSpeedupTimeInSeconds() {
		return speedupTimeInSeconds;
	}

	public int getCoins() {
		return coins;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
