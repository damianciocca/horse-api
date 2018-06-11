package com.etermax.spacehorse.core.shop.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopBuyInAppItemRequest {

	@JsonProperty("shopItemId")
	private String shopItemId;

	@JsonProperty("receipt")
	private Object receipt;

	public ShopBuyInAppItemRequest(@JsonProperty("shopItemId") String shopItemId, @JsonProperty("receipt") Object receipt) {
		this.shopItemId = shopItemId;
		this.receipt = receipt;
	}

	public String getShopItemId() {
		return shopItemId;
	}

	public Object getReceipt() {
		return receipt;
	}

}
