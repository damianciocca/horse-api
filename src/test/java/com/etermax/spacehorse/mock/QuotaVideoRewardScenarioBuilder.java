package com.etermax.spacehorse.mock;

import com.etermax.spacehorse.core.ads.videorewards.model.VideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardFactory;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class QuotaVideoRewardScenarioBuilder {

	private QuotaVideoReward quotaVideoReward;

	public QuotaVideoRewardScenarioBuilder(ServerTimeProvider timeProvider, String placeName, String userId) {
		this.quotaVideoReward = new QuotaVideoRewardFactory(timeProvider).create(userId, placeName);
	}

	public QuotaVideoRewardScenarioBuilder withConsume(VideoReward videoReward) {
		quotaVideoReward.consume(videoReward);
		return this;
	}

	public QuotaVideoReward build() {
		return quotaVideoReward;
	}
}
