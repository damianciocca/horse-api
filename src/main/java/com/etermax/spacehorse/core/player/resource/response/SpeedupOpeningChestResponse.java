package com.etermax.spacehorse.core.player.resource.response;

import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SpeedupOpeningChestResponse {
	@JsonProperty("rewards")
	private List<RewardResponse> rewards;

	@JsonProperty("costInGems")
	private Integer costInGems;

	public List<RewardResponse> getRewards() {
		return rewards;
	}

	public Integer getCostInGems() {
		return costInGems;
	}

	public SpeedupOpeningChestResponse() {
		this.rewards = new ArrayList<>();
		this.costInGems = 0;
	}

	public SpeedupOpeningChestResponse(List<RewardResponse> rewards, Integer costInGems) {
		this.rewards = rewards;
		this.costInGems = costInGems;
	}
}
