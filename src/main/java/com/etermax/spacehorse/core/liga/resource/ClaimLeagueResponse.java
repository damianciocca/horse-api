package com.etermax.spacehorse.core.liga.resource;

import java.util.List;

import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClaimLeagueResponse {

	@JsonProperty("rewards")
	private List<RewardResponse> rewards;

	public ClaimLeagueResponse(List<RewardResponse> rewards) {
		this.rewards = rewards;
	}
}
