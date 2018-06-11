package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.catalog.model.ShopInAppDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopInAppDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("ItemId")
	private String shopItemId;

	@JsonProperty("Market")
	private MarketType marketType;

	@JsonProperty("InAppId")
	private String productId;

	@JsonProperty("InAppPriceInUsdCents")
	private int inAppPriceInUsdCents;

	public ShopInAppDefinitionResponse() {
	}

	public ShopInAppDefinitionResponse(ShopInAppDefinition shopInAppDefinition) {
		this.id = shopInAppDefinition.getId();
		this.shopItemId = shopInAppDefinition.getItemId();
		this.marketType = shopInAppDefinition.getMarketType();
		this.productId = shopInAppDefinition.getProductId();
		this.inAppPriceInUsdCents = shopInAppDefinition.getInAppPriceInUsdCents();
	}

	public String getId() {
		return id;
	}

	public MarketType getMarketType() {
		return marketType;
	}

	public String getProductId() {
		return productId;
	}

	public String getShopItemId() {
		return shopItemId;
	}

	public int getInAppPriceInUsdCents() {
		return inAppPriceInUsdCents;
	}
}
