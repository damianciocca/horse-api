package com.etermax.spacehorse.core.shop.resource.response;

import com.etermax.spacehorse.core.inapps.resource.response.InAppsItemResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ShopBuyInAppItemResponse {

	@JsonProperty("inAppItems")
	List<InAppsItemResponse> inAppItems;

	public ShopBuyInAppItemResponse(@JsonProperty("inAppItems")
			List<InAppsItemResponse> inAppItems) {
		this.inAppItems = inAppItems;
	}

	public List<InAppsItemResponse> getInAppItems() {
		return inAppItems;
	}

}
