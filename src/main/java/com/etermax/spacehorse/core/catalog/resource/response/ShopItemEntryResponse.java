package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.ShopItemDefinition;
import com.etermax.spacehorse.core.catalog.model.ShopItemType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopItemEntryResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("ItemType")
	private ShopItemType itemType;

	@JsonProperty("ItemId")
	private String itemId;

	@JsonProperty("ItemName")
	private String itemName;

	@JsonProperty("ItemQuantity")
	private int itemQuantity;

	@JsonProperty("ItemGemPrice")
	private int itemGemPrice;

	@JsonProperty("InAppPurchase")
	private boolean inAppPurchase;

	public ShopItemEntryResponse() {
	}

	public ShopItemEntryResponse(ShopItemDefinition shopItemEntry) {
		this.id = shopItemEntry.getId();
		this.itemType = shopItemEntry.getItemType();
		this.itemId = shopItemEntry.getItemId();
		this.itemName = shopItemEntry.getItemName();
		this.itemQuantity = shopItemEntry.getItemQuantity();
		this.itemGemPrice = shopItemEntry.getItemGemPrice();
		this.inAppPurchase = shopItemEntry.getInAppPurchase();
	}

	public String getId() {
		return id;
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
		return itemQuantity;
	}

	public int getItemGemPrice() {
		return itemGemPrice;
	}

	public boolean getInAppPurchase() {
		return inAppPurchase;
	}
}
