package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.ShopInAppDefinitionResponse;

public class ShopInAppDefinition extends CatalogEntry implements InAppDefinition {

	private final String shopItemId;

	private final MarketType marketType;

	private final String productId;

	private final int inAppPriceInUsdCents;

	public ShopInAppDefinition(ShopInAppDefinitionResponse shopInAppDefinitionResponse) {
		super(shopInAppDefinitionResponse.getId());
		this.shopItemId = shopInAppDefinitionResponse.getShopItemId();
		this.marketType = shopInAppDefinitionResponse.getMarketType();
		this.productId = shopInAppDefinitionResponse.getProductId();
		this.inAppPriceInUsdCents = shopInAppDefinitionResponse.getInAppPriceInUsdCents();
	}

	@Override
	public String getItemId() {
		return shopItemId;
	}

	@Override
	public MarketType getMarketType() {
		return marketType;
	}

	@Override
	public String getProductId() {
		return productId;
	}

	@Override
	public int getInAppPriceInUsdCents() {
		return inAppPriceInUsdCents;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(shopItemId != null, "shopItemId == null");
		validateParameter(marketType != null, "marketType == null");
		validateParameter(productId != null, "productId == null");
		validateParameter(inAppPriceInUsdCents >= 0, "inAppPriceInUsdCents < 0");
	}

}
