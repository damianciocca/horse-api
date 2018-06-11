package com.etermax.spacehorse.core.shop.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopRefreshCardsResponse {
	@JsonProperty("shopCards")
	private ShopCardsResponse shopCards;

	public ShopRefreshCardsResponse(ShopCardsResponse shopCards) {
		this.shopCards = shopCards;
	}

	public ShopCardsResponse getShopCards() {
		return shopCards;
	}
}
