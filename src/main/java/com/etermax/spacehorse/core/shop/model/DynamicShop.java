package com.etermax.spacehorse.core.shop.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.catalog.model.Catalog;

@DynamoDBDocument
public class DynamicShop {

	@DynamoDBAttribute(attributeName = "shopCards")
	private ShopCards shopCards = new ShopCards();

	public void setShopCards(ShopCards shopCards) {
		this.shopCards = shopCards;
	}

	public ShopCards getShopCards() {
		return shopCards;
	}

	public DynamicShop() {
	}

	public DynamicShop(ShopCards shopCards) {
		this.shopCards = shopCards;
	}

	public void checkIntegrity(Catalog catalog) {
		shopCards.checkIntegrity(catalog);
	}
}
