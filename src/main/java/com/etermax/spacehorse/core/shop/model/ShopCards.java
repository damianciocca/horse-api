package com.etermax.spacehorse.core.shop.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.etermax.spacehorse.core.catalog.model.Catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@DynamoDBDocument
public class ShopCards {

	@DynamoDBAttribute(attributeName = "cards")
	private List<ShopCard> cards = new ArrayList<>();

	@DynamoDBAttribute(attributeName = "expirationServerTime")
	private long expirationServerTime = 0L;

	public void setCards(List<ShopCard> cards) {
		this.cards = cards;
	}

	public void setExpirationServerTime(long expirationServerTime) {
		this.expirationServerTime = expirationServerTime;
	}

	public List<ShopCard> getCards() {
		return cards;
	}

	public long getExpirationServerTime() {
		return expirationServerTime;
	}

	public ShopCards() {
	}

	public ShopCards(List<ShopCard> cards, long expirationServerTime) {
		this.cards = cards;
		this.expirationServerTime = expirationServerTime;
	}

	public void checkIntegrity(Catalog catalog) {
		if (cards.stream().anyMatch(card -> catalog.getCardDefinitionsCollection().findById(card.getCardType()) == null))
			cards = cards.stream().filter(card -> catalog.getCardDefinitionsCollection().findById(card.getCardType()) != null).collect(Collectors.toList());
	}

	public Optional<ShopCard> findById(String shopCardId) {
		return cards.stream().filter(x -> x.getId().equals(shopCardId)).findFirst();
	}

	@DynamoDBIgnore
	public boolean isAvailable(long serverTime) {
		return getRemaingTime(serverTime) > 0;
	}

	@DynamoDBIgnore
	private long getRemaingTime(long serverTime) {
		if (serverTime < expirationServerTime)
			return expirationServerTime - serverTime;

		return 0;
	}

	public void refresh(ShopCards shopCards) {
		this.cards = shopCards.cards;
		this.expirationServerTime = shopCards.expirationServerTime;
	}

	public void cheatSetExpirationTime(long serverTime) {
		this.expirationServerTime = serverTime;
	}

}
