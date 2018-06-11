package com.etermax.spacehorse.core.specialoffer.resource.response.inapp;

import java.util.List;

import com.etermax.spacehorse.core.inapps.resource.response.InAppsItemResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialOfferBuyInAppPendingRewardsResponse {

	@JsonProperty("inAppItems")
	private List<InAppsItemResponse> inAppItems;

	public SpecialOfferBuyInAppPendingRewardsResponse(@JsonProperty("inAppItems")
			List<InAppsItemResponse> inAppItems) {
		this.inAppItems = inAppItems;
	}

	public List<InAppsItemResponse> getInAppItems() {
		return inAppItems;
	}

}
