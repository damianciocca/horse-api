package com.etermax.spacehorse.core.specialoffer.resource.response;

import java.util.List;

import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

public class SpecialOfferBuyResponse {

	@JsonProperty("rewards")
	private List<RewardResponse> rewards;

	public SpecialOfferBuyResponse() {
		this.rewards = Lists.newArrayList();
	}

	public SpecialOfferBuyResponse(List<RewardResponse> rewards) {
		this.rewards = rewards;
	}

	public List<RewardResponse> getRewards() {
		return rewards;
	}

}
