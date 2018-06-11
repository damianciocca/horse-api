package com.etermax.spacehorse.core.player.resource.response;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishOpeningChestResponse {
	@JsonProperty("rewards")
	private List<RewardResponse> rewards;

	public List<RewardResponse> getRewards() {
		return rewards;
	}

	public FinishOpeningChestResponse() {
		this.rewards = new ArrayList<>();
	}

	public FinishOpeningChestResponse(List<RewardResponse> rewards) {
		this.rewards = rewards;
	}
}
