package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.ShopCardDefinition;
import com.etermax.spacehorse.core.shop.model.PriceType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopCardDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("CardRarity")
	private CardRarity cardRarity;

	@JsonProperty("Amount")
	private int amount;

	@JsonProperty("BasePrice")
	private int basePrice;

	@JsonProperty("PriceIncreasePerPurchase")
	private int priceIncreasePerPurchase;

	@JsonProperty("PriceType")
	private PriceType priceType;

	public ShopCardDefinitionResponse() {

	}

	public ShopCardDefinitionResponse(ShopCardDefinition shopCardDefinition) {
		this.id = shopCardDefinition.getId();
		this.cardRarity = shopCardDefinition.getCardRarity();
		this.amount = shopCardDefinition.getAmount();
		this.basePrice = shopCardDefinition.getBasePrice();
		this.priceIncreasePerPurchase = shopCardDefinition.getPriceIncreasePerPurchase();
		this.priceType = shopCardDefinition.getPriceType();
	}

	public String getId() {
		return id;
	}

	public CardRarity getCardRarity() {
		return cardRarity;
	}

	public int getAmount() {
		return amount;
	}

	public int getBasePrice() {
		return basePrice;
	}

	public int getPriceIncreasePerPurchase() {
		return priceIncreasePerPurchase;
	}

	public PriceType getPriceType() {
		return priceType;
	}
}
