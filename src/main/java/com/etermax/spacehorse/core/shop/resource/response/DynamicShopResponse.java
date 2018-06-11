package com.etermax.spacehorse.core.shop.resource.response;

import com.etermax.spacehorse.core.shop.model.DynamicShop;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DynamicShopResponse {

	@JsonProperty("shopCards")
	private ShopCardsResponse shopCards;

	public DynamicShopResponse(DynamicShop dynamicShop) {
		shopCards = new ShopCardsResponse(dynamicShop.getShopCards());
	}

	public DynamicShopResponse() {
	}

	public ShopCardsResponse getShopCards() {
		return shopCards;
	}

}
