package com.etermax.spacehorse.core.player.model.inventory;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.inventory.cardparts.CardParts;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chests;
import com.etermax.spacehorse.core.player.model.inventory.currency.Currency;

@DynamoDBDocument
public class Inventory {

	@DynamoDBAttribute(attributeName = "gold")
	private Currency gold;

	@DynamoDBAttribute(attributeName = "gems")
	private Currency gems;

	@DynamoDBAttribute(attributeName = "cardParts")
	private CardParts cardParts;

	@DynamoDBAttribute(attributeName = "chests")
	private Chests chests;

	public Inventory() {
		gold = new Currency();
		gems = new Currency();
		cardParts = new CardParts();
		chests = new Chests();
	}

	public Currency getGold() {
		return gold;
	}

	public Currency getGems() {
		return gems;
	}

	public CardParts getCardParts() {
		return cardParts;
	}

	public Chests getChests() {
		return chests;
	}

	public void setGold(Currency gold) {
		this.gold = gold;
	}

	public void setGems(Currency gems) {
		this.gems = gems;
	}

	public void setCardParts(CardParts cardParts) {
		this.cardParts = cardParts;
	}

	public void setChests(Chests chests) {
		this.chests = chests;
	}

	public void checkIntegrity(Catalog catalog) {
		cardParts.checkIntegrity(catalog);
		chests.checkIntegrity(catalog);
	}

	public static Inventory createInventory(InventoryConfiguration configuration) {
		Inventory inventory = new Inventory();
		inventory.getGems().setAmount(configuration.getStartingGems());
		inventory.getGold().setAmount(configuration.getStartingGold());
		return inventory;
	}

}
