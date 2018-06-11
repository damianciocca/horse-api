package com.etermax.spacehorse.core.catalog.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.resource.response.ShopItemEntryResponse;

public class ShopItemDefinition extends CatalogEntry {

	private final ShopItemType itemType;

	private final String itemId;

	private final String itemName;

	private final int itemQuantity;

	private final int itemGemPrice;

	private final boolean inAppPurchase;

	private final Map<Integer, Integer> mapsNumbersAndItemGemPrices;

	private final Map<Integer, Integer> mapsNumbersAndItemQuantity;

	public ShopItemDefinition(ShopItemEntryResponse shopItemEntryResponse) {
		super(shopItemEntryResponse.getId());
		this.itemId = shopItemEntryResponse.getItemId();
		this.itemType = shopItemEntryResponse.getItemType();
		this.itemName = shopItemEntryResponse.getItemName();
		this.itemQuantity = shopItemEntryResponse.getItemQuantity();
		this.itemGemPrice = shopItemEntryResponse.getItemGemPrice();
		this.inAppPurchase = shopItemEntryResponse.getInAppPurchase();
		this.mapsNumbersAndItemGemPrices = new HashMap<>();
		this.mapsNumbersAndItemQuantity = new HashMap<>();
	}

	public ShopItemType getItemType() {
		return itemType;
	}

	public String getItemId() {
		return itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public int getItemQuantity() {
		return this.itemQuantity;
	}

	public int getItemGemPrice() {
		return this.itemGemPrice;
	}

	public int getItemQuantity(int mapNumber) {
		return getItemQuantityForMap(mapNumber).orElse(itemQuantity);
	}

	public int getItemGemPrice(int mapNumber) {
		return getItemGemPriceForMap(mapNumber).orElse(itemGemPrice);
	}

	private Optional<Integer> getItemQuantityForMap(int mapNumber) {
		return Optional.ofNullable(mapsNumbersAndItemQuantity.get(mapNumber));
	}

	private Optional<Integer> getItemGemPriceForMap(int mapNumber) {
		return Optional.ofNullable(mapsNumbersAndItemGemPrices.get(mapNumber));
	}

	public Map<Integer, Integer> getMapsNumbersAndItemGemPrices() {
		return mapsNumbersAndItemGemPrices;
	}

	public Map<Integer, Integer> getMapsNumbersAndItemQuantity() {
		return mapsNumbersAndItemQuantity;
	}

	public boolean getInAppPurchase() {
		return inAppPurchase;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(itemType != null, "itemType != null");
		validateParameter(itemQuantity > 0, "itemQuantity =< 0");
		validateParameter(itemGemPrice > 0 || inAppPurchase, "itemGemPrice =< 0");

		boolean existConfigsForAllMaps = existConfigsForAllMaps(catalog);
		validateParameter(validateIfItemHasConfigurationsForAllMaps(catalog, existConfigsForAllMaps),
				"Item hasn't a configuration for all maps available.");

		hydratePropertiesWhenNecessary(existConfigsForAllMaps, catalog);

		if (getItemType().equals(ShopItemType.CHEST)) {
			validateParameter(catalog.getChestDefinitionsCollection().findById(getItemId()).isPresent(),
					"No chest with id " + getItemId() + " is found");
		}
	}

	private boolean existConfigsForAllMaps(Catalog catalog) {
		List<Integer> mapsNumbers = getExistentMapsNumbers(catalog);
		List<ShopItemsMapConfigEntry> shopItemsConfig = catalog.getShopItemsMapConfigCollection().getEntries();
		List<Integer> itemMapsConfigurations = getConfigurationsMapNumbersForItem(shopItemsConfig);
		return itemMapsConfigurations.containsAll(mapsNumbers);
	}

	private void hydratePropertiesWhenNecessary(boolean existConfigsForAllMaps, Catalog catalog) {
		List<ShopItemsMapConfigEntry> shopItemsConfig = catalog.getShopItemsMapConfigCollection().getEntries();
		if (existConfigsForAllMaps) {
			hydrateConfigurations(shopItemsConfig);
		}
	}

	private boolean validateIfItemHasConfigurationsForAllMaps(Catalog catalog, boolean existConfigsForAllMaps) {
		return !hasItemAnyConfigForMaps(catalog) || existConfigsForAllMaps;
	}

	private void hydrateConfigurations(List<ShopItemsMapConfigEntry> shopItemsConfig) {
		List<ShopItemsMapConfigEntry> itemConfigurations = getConfigurationsForItem(shopItemsConfig);
		itemConfigurations.forEach(mapConfig -> {
			int mapNumber = mapConfig.getMapNumber();
			int itemGemPrice = mapConfig.getItemGemPrice();
			int itemQuantity = mapConfig.getItemQuantity();
			this.mapsNumbersAndItemGemPrices.put(mapNumber, itemGemPrice);
			this.mapsNumbersAndItemQuantity.put(mapNumber, itemQuantity);
		});
	}

	private List<Integer> getConfigurationsMapNumbersForItem(List<ShopItemsMapConfigEntry> shopItemsConfig) {
		return getConfigurationsForItem(shopItemsConfig).stream().map(ShopItemsMapConfigEntry::getMapNumber).collect(Collectors.toList());
	}

	private List<ShopItemsMapConfigEntry> getConfigurationsForItem(List<ShopItemsMapConfigEntry> shopItemsConfig) {
		return shopItemsConfig.stream().filter(entry -> entry.getItemId().equals(this.itemId)).collect(Collectors.toList());
	}

	private List<Integer> getExistentMapsNumbers(Catalog catalog) {
		return catalog.getMapsCollection().getEntries().stream().map(MapDefinition::getMapNumber).collect(Collectors.toList());
	}

	private boolean hasItemAnyConfigForMaps(Catalog catalog) {
		return catalog.getShopItemsMapConfigCollection().getEntries().stream().anyMatch(entry -> entry.getItemId().equals(this.getItemId()));
	}
}

