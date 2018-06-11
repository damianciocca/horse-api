package com.etermax.spacehorse.core.catalog.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ShopInAppCollection extends CatalogEntriesCollection<ShopInAppDefinition> {

	private final Map<String, ShopInAppDefinition> entriesByProductIdAndMarketType;

	private final Map<String, List<ShopInAppDefinition>> entriesByShopItemId;

	public ShopInAppCollection(List<ShopInAppDefinition> entries) {
		super(entries);
		entriesByProductIdAndMarketType = new HashMap<>();
		entries.forEach(shopInApp -> {
			entriesByProductIdAndMarketType.put(combineProductIdAndMarketType(shopInApp.getProductId(), shopInApp.getMarketType()), shopInApp);
		});
		entriesByShopItemId = new HashMap<>();
		entries.forEach(shopInApp -> {
			List<ShopInAppDefinition> shopInAppDefinitionConsumer = entriesByShopItemId.get(shopInApp.getItemId());
			if (shopInAppDefinitionConsumer == null) {
				shopInAppDefinitionConsumer = new ArrayList<>();
			}
			shopInAppDefinitionConsumer.add(shopInApp);
			entriesByShopItemId.put(shopInApp.getItemId(), shopInAppDefinitionConsumer);
		});
	}

	public Optional<ShopInAppDefinition> findByProductIdAndMarketType(String productId, MarketType marketType) {
		String combinedId = combineProductIdAndMarketType(productId, marketType);
		return Optional.ofNullable(entriesByProductIdAndMarketType.get(combinedId));
	}

	private String combineProductIdAndMarketType(String productId, MarketType marketType) {
		return productId + "-" + marketType.toString();
	}

}
