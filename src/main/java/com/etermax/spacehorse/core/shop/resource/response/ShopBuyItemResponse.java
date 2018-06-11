package com.etermax.spacehorse.core.shop.resource.response;

import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ShopBuyItemResponse {

	@JsonProperty("rewards")
	private List<RewardResponse> rewards;

	public List<RewardResponse> getRewards() {
		return rewards;
	}

	public ShopBuyItemResponse() {
		this.rewards = new ArrayList<>();
	}

	public ShopBuyItemResponse(List<RewardResponse> rewards) {
		this.rewards = rewards;
	}

}
