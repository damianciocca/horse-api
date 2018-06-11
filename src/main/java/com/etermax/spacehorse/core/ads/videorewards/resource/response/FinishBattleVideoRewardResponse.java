package com.etermax.spacehorse.core.ads.videorewards.resource.response;

import java.util.List;

import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishBattleVideoRewardResponse {

	@JsonProperty("rewards")
	private List<RewardResponse> rewards;

	public FinishBattleVideoRewardResponse(List<RewardResponse> rewards) {
		this.rewards = rewards;
	}

	public List<RewardResponse> getRewards() {
		return rewards;
	}
}
