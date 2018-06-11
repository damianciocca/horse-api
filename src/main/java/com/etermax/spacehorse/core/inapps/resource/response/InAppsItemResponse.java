package com.etermax.spacehorse.core.inapps.resource.response;

import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class InAppsItemResponse {

	@JsonProperty("validation")
	private ProductPurchaseStatus productPurchaseStatus;

	@JsonProperty("rewards")
	private List<RewardResponse> rewards;

	public InAppsItemResponse(@JsonProperty("validation") ProductPurchaseStatus productPurchaseStatus,
			@JsonProperty("rewards") List<RewardResponse> rewards) {
		this.productPurchaseStatus = productPurchaseStatus;
		this.rewards = rewards;
	}

	public ProductPurchaseStatus getProductPurchaseStatus() {
		return productPurchaseStatus;
	}

	public List<RewardResponse> getRewards() {
		return rewards;
	}

}
