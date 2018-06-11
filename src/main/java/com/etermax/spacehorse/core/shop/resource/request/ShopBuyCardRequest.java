package com.etermax.spacehorse.core.shop.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopBuyCardRequest {

	@JsonProperty("shopCardId")
	private String shopCardId;

	public ShopBuyCardRequest(@JsonProperty("shopCardId") String shopCardId) {
		this.shopCardId = shopCardId;
	}

	public String getShopCardId() {
		return shopCardId;
	}

}
