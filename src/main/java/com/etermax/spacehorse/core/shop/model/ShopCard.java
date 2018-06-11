package com.etermax.spacehorse.core.shop.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;

@DynamoDBDocument
public class ShopCard {

	@DynamoDBAttribute(attributeName = "id")
	private String id;

	@DynamoDBAttribute(attributeName = "cardType")
	private String cardType;

	@DynamoDBAttribute(attributeName = "total")
	private int total;

	@DynamoDBAttribute(attributeName = "remaining")
	private int remaining;

	@DynamoDBAttribute(attributeName = "basePrice")
	private int basePrice;

	@DynamoDBAttribute(attributeName = "priceIncreasePerPurchase")
	private int priceIncreasePerPurchase;

	@DynamoDBTypeConvertedEnum
	@DynamoDBAttribute(attributeName = "priceType")
	private PriceType priceType;

	public void setId(String id) {
		this.id = id;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setRemaining(int remaining) {
		this.remaining = remaining;
	}

	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}

	public void setPriceIncreasePerPurchase(int priceIncreasePerPurchase) {
		this.priceIncreasePerPurchase = priceIncreasePerPurchase;
	}

	public void setPriceType(PriceType priceType) {
		this.priceType = priceType;
	}

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

	@DynamoDBIgnore
	public boolean isAvailable() {
		return remaining > 0 && total > 0;
	}

	public ShopCard() {
	}

	public ShopCard(String id, String cardType, int total, int remaining, int basePrice, int priceIncreasePerPurchase, PriceType priceType) {
		this.id = id;
		this.cardType = cardType;
		this.total = total;
		this.remaining = remaining;
		this.basePrice = basePrice;
		this.priceIncreasePerPurchase = priceIncreasePerPurchase;
		this.priceType = priceType;
	}

	@DynamoDBIgnore
	public int getCurrentPrice() {
		if (isAvailable()) {
			return basePrice + (total - remaining) * priceIncreasePerPurchase;
		}

		return Integer.MAX_VALUE;
	}

	public void consumeRemaining() {
		remaining--;
		if (remaining < 0)
			remaining = 0;
	}

}
