package com.etermax.spacehorse.mock;

import com.etermax.spacehorse.core.ads.videorewards.model.SpeedupVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.VideoRewardFactory;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardType;

public class VideoRewardScenarioBuilder {

	private static final int TIME_FRAME_IN_SECONDS = 300;
	private static final int MAX_VIEWS_PER_TIME_FRAME = 5;
	private static final int SPEEDUP_TIME_IN_SECONDS = 200;

	private SpeedupVideoReward speedupVideoReward;

	public VideoRewardScenarioBuilder(String placeName) {
		VideoRewardDefinition videoRewardDefinition = new VideoRewardDefinition( //
				"1", //
				placeName, //
				TIME_FRAME_IN_SECONDS,//
				MAX_VIEWS_PER_TIME_FRAME, //
				SPEEDUP_TIME_IN_SECONDS, //
				0, 0, VideoRewardType.SPEEDUP_TIME, false);
		this.speedupVideoReward = (SpeedupVideoReward) new VideoRewardFactory().create(videoRewardDefinition);
	}

	public SpeedupVideoReward build() {
		return speedupVideoReward;
	}

}
