package com.etermax.spacehorse.core.shop.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopBuyItemRequest {

	@JsonProperty("shopItemId")
	private String shopItemId;

	public ShopBuyItemRequest(@JsonProperty("shopItemId") String id) {
		this.shopItemId = id;
	}

	public String getShopItemId() {
		return this.shopItemId;
	}

}
