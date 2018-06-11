package com.etermax.spacehorse.core.ads.videorewards.model.quota;

import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class QuotaVideoRewardFactory {

	private final ServerTimeProvider timeProvider;

	public QuotaVideoRewardFactory(ServerTimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	public QuotaVideoReward create(String userId, String placeName) {
		return new QuotaVideoReward(userId, placeName, timeProvider.getDateTime(), timeProvider);
	}
}
