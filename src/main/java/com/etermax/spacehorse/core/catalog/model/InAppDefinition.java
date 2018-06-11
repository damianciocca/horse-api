package com.etermax.spacehorse.core.catalog.model;

public interface InAppDefinition {

	String getItemId();

	MarketType getMarketType();

	String getProductId();

	int getInAppPriceInUsdCents();
}
