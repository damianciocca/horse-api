package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.ShopItemsMapConfigEntryResponse;

public class ShopItemsMapConfigEntry extends CatalogEntry {
	private final String itemId;
	private final int mapNumber;
	private final int itemQuantity;
	private final int itemGemPrice;

	public ShopItemsMapConfigEntry(ShopItemsMapConfigEntryResponse shopItemsMapConfigEntryResponse) {
		super(shopItemsMapConfigEntryResponse.getItemId() + "-" + shopItemsMapConfigEntryResponse.getMapNumber());
		this.itemId = shopItemsMapConfigEntryResponse.getItemId();
		this.mapNumber = shopItemsMapConfigEntryResponse.getMapNumber();
		this.itemQuantity = shopItemsMapConfigEntryResponse.getItemQuantity();
		this.itemGemPrice = shopItemsMapConfigEntryResponse.getItemGemPrice();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(doesItemIdExist(itemId, catalog.getShopItemsCollection()), "ItemId: " + itemId + " not found among ShopItems.");
		validateParameter(isValidMapNumber(mapNumber, catalog.getMapsCollection()), "Invalid map number: " + mapNumber + ".");
	}

	private boolean isValidMapNumber(int mapNumber, CatalogEntriesCollection<MapDefinition> mapCollection) {
		return mapCollection.getEntries().stream().anyMatch(mapDefinition -> mapDefinition.getMapNumber() == mapNumber);
	}

	private boolean doesItemIdExist(String itemId, CatalogEntriesCollection<ShopItemDefinition> shopItemCollection) {
		return shopItemCollection.getEntries().stream().anyMatch(shopItemDefinition -> shopItemDefinition.getItemId().equals(itemId));
	}

	public String getItemId() {
		return itemId;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public int getItemGemPrice() {
		return itemGemPrice;
	}
}
