package com.etermax.spacehorse.core.catalog.model.ads.videorewards;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;

public class VideoRewardDefinition extends CatalogEntry {

	public static final String SPEEDUP_COURIER_ID = "speedup_courier_place";
	public static final String BATTLE_FINISH_PLACE = "battle_finish_place";
	public static final int MAX_VIDEO_DURATION = 60;
	private final String placeName; // should be unique
	private final int timeFrameInSeconds;
	private final int maxViewsPerTimeFrame;
	private final int speedupTimeInSeconds;
	private final int coins;
	private final int mapNumber;
	private final VideoRewardType type;
	private final boolean filterMapEnabled;

	public VideoRewardDefinition(String id, String placeName, int timeFrameInSeconds, int maxViewsPerTimeFrame, int speedupTimeInSeconds, int coins,
			int mapNumber, VideoRewardType type, boolean filterMapEnabled) {
		super(id);
		this.placeName = placeName;
		this.timeFrameInSeconds = timeFrameInSeconds;
		this.maxViewsPerTimeFrame = maxViewsPerTimeFrame;
		this.speedupTimeInSeconds = speedupTimeInSeconds;
		this.coins = coins;
		this.mapNumber = mapNumber;
		this.type = type;
		this.filterMapEnabled = filterMapEnabled;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(isNotBlank(placeName), "the placeName should not be blank");
		validateParameter(timeFrameInSeconds > 0, "timeFrameInSeconds =< 0");
		validateParameter(maxViewsPerTimeFrame > 0, "maxViewsPerTimeFrame =< 0");
		if(VideoRewardType.SPEEDUP_TIME.equals(type)){
			validateParameter(speedupTimeInSeconds > 0, "speedupTimeInSeconds =< 0");
		}else{
			validateParameter(coins > 0, "reward =< 0");
		}
	}

	public boolean isFilterMapEnabled() {
		return filterMapEnabled;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public int getCoins() {
		return coins;
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

	public VideoRewardType getType() {
		return type;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
