package com.etermax.spacehorse.core.player.resource.response.player.progress;

import com.etermax.spacehorse.core.player.model.progress.RewardsReceivedToday;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RewardsReceivedTodayResponse {

	@JsonProperty("goldRewardsReceivedToday")
	private Integer goldRewardsReceivedToday;

	@JsonProperty("expirationServerTime")
	private long expirationServerTime = 0L;

	public Integer getGoldRewardsReceivedToday() {
		return goldRewardsReceivedToday;
	}

	public long getExpirationServerTime() {
		return expirationServerTime;
	}

	public RewardsReceivedTodayResponse() {
	}

	public RewardsReceivedTodayResponse(RewardsReceivedToday rewardsReceivedToday) {
		this.goldRewardsReceivedToday = rewardsReceivedToday.getGoldRewardsReceivedToday();
		this.expirationServerTime = rewardsReceivedToday.getExpirationServerTime();
	}
}
