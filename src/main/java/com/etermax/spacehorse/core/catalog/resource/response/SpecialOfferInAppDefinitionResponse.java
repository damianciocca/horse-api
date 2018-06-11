package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferInAppDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialOfferInAppDefinitionResponse {

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

	public SpecialOfferInAppDefinitionResponse() {
	}

	public SpecialOfferInAppDefinitionResponse(SpecialOfferInAppDefinition definition) {
		this.id = definition.getId();
		this.shopItemId = definition.getItemId();
		this.marketType = definition.getMarketType();
		this.productId = definition.getProductId();
		this.inAppPriceInUsdCents = definition.getInAppPriceInUsdCents();
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
