package com.etermax.spacehorse.core.ads.videorewards.model;

import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardType;
import com.etermax.spacehorse.core.common.exception.ApiException;

public class VideoRewardFactory {

	public VideoReward create(VideoRewardDefinition definition) {
		switch(definition.getType()){

			case SPEEDUP_TIME:
				return new SpeedupVideoReward( //
						definition.getPlaceName(), //
						definition.getTimeFrameInSeconds(), //
						definition.getMaxViewsPerTimeFrame(),//
						definition.getSpeedupTimeInSeconds());
			case REWARD:
				return new BoostVideoReward( //
						definition.getPlaceName(), //
						definition.getTimeFrameInSeconds(), //
						definition.getMaxViewsPerTimeFrame(),//
						definition.getCoins());
			default:
				throw new ApiException(
						"Unexpected error when trying to create a video reward . Expected Type [ " + VideoRewardType.SPEEDUP_TIME + " ] . " + "Actual [ "
								+ definition.getType() + " " + "]");
		}

	}
}
