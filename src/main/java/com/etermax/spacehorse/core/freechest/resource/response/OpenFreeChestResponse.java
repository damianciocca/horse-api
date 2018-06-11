package com.etermax.spacehorse.core.freechest.resource.response;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenFreeChestResponse {
	@JsonProperty("rewards")
	private List<RewardResponse> rewards;

	@JsonProperty("newLastChestOpeningServerTime")
	private long newLastChestOpeningServerTime;

	public List<RewardResponse> getRewards() {
		return rewards;
	}

	public long getNewLastChestOpeningServerTime() {
		return newLastChestOpeningServerTime;
	}

	public OpenFreeChestResponse() {
		this.rewards = new ArrayList<>();
	}

	public OpenFreeChestResponse(List<RewardResponse> rewards, long newLastChestOpeningServerTimeInSeconds) {
		this.rewards = rewards;
		this.newLastChestOpeningServerTime = newLastChestOpeningServerTimeInSeconds;
	}
}
