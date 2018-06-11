package com.etermax.spacehorse.core.catalog.model.specialoffer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;
import com.etermax.spacehorse.core.catalog.model.InAppDefinition;
import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.catalog.resource.response.SpecialOfferInAppDefinitionResponse;

public class SpecialOfferInAppDefinition extends CatalogEntry implements InAppDefinition {

	private final String itemId;
	private final MarketType marketType;
	private final String productId;
	private final int inAppPriceInUsdCents;

	public SpecialOfferInAppDefinition(SpecialOfferInAppDefinitionResponse response) {
		super(response.getId());
		this.itemId = response.getShopItemId();
		this.marketType = response.getMarketType();
		this.productId = response.getProductId();
		this.inAppPriceInUsdCents = response.getInAppPriceInUsdCents();
	}

	@Override
	public String getItemId() {
		return itemId;
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
		validateParameter(isNotBlank(itemId), "shopItemId == null");
		validateParameter(marketType != null, "marketType == null");
		validateParameter(isNotBlank(productId), "productId == null");
		validateParameter(inAppPriceInUsdCents >= 0, "inAppPriceInUsdCents < 0");
	}

}
