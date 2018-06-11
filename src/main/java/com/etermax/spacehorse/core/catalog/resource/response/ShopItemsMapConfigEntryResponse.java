package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.ShopItemsMapConfigEntry;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopItemsMapConfigEntryResponse {
	@JsonProperty("Id")
	private String id;

	@JsonProperty("ItemId")
	private String itemId;

	@JsonProperty("MapNumber")
	private int mapNumber;

	@JsonProperty("ItemQuantity")
	private int itemQuantity;

	@JsonProperty("ItemGemPrice")
	private int itemGemPrice;

	public ShopItemsMapConfigEntryResponse() {
	}

	public ShopItemsMapConfigEntryResponse(ShopItemsMapConfigEntry shopItemsMapConfigEntry) {
		this.id = shopItemsMapConfigEntry.getId();
		this.itemId = shopItemsMapConfigEntry.getItemId();
		this.mapNumber = shopItemsMapConfigEntry.getMapNumber();
		this.itemGemPrice = shopItemsMapConfigEntry.getItemGemPrice();
		this.itemQuantity = shopItemsMapConfigEntry.getItemQuantity();
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

	public String getId() {
		return itemId+"-"+mapNumber;
	}
}
