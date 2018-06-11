package com.etermax.spacehorse.core.shop.resource.response;

import com.etermax.spacehorse.core.shop.model.PriceType;
import com.etermax.spacehorse.core.shop.model.ShopCard;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ShopCardResponse {

	@JsonProperty("id")
	private String id;

	@JsonProperty("cardType")
	private String cardType;

	@JsonProperty("total")
	private int total;

	@JsonProperty("remaining")
	private int remaining;

	@JsonProperty("basePrice")
	private int basePrice;

	@JsonProperty("priceIncreasePerPurchase")
	private int priceIncreasePerPurchase;

	@JsonProperty("priceType")
	private PriceType priceType;

	public String getId() {
		return id;
	}

	public String getCardType() {
		return cardType;
	}

	public int getTotal() {
		return total;
	}

	public int getRemaining() {
		return remaining;
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

	public ShopCardResponse(ShopCard shopCard) {
		this.id = shopCard.getId();
		this.cardType = shopCard.getCardType();
		this.total = shopCard.getTotal();
		this.remaining = shopCard.getRemaining();
		this.basePrice = shopCard.getBasePrice();
		this.priceIncreasePerPurchase = shopCard.getPriceIncreasePerPurchase();
		this.priceType = shopCard.getPriceType();
	}
}
