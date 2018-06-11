package com.etermax.spacehorse.core.player.model.inventory.cardparts;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.etermax.spacehorse.core.catalog.model.Catalog;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@DynamoDBDocument
public class CardParts {

	@DynamoDBAttribute(attributeName = "amounts")
	private Map<String, Integer> amounts;

	public CardParts() {
		amounts = new HashMap<>();
	}

	@DynamoDBIgnore
	public Set<String> getCardsTypes() {
		return amounts.keySet();
	}

	public Map<String, Integer> getAmounts() {
		return amounts;
	}

	public void setAmounts(Map<String, Integer> amounts) {
		this.amounts = amounts;
	}

	@DynamoDBIgnore
	public int getMax(String cardType) {
		//TODO
		return Integer.MAX_VALUE;
	}

	public int getAmount(String cardType) {
		if (amounts.containsKey(cardType)) {
			return amounts.get(cardType);
		}

		return 0;
	}

	public void add(String cardType, int toAdd) {
		if (toAdd <= 0) {
			return;
		}

		long amount = getAmount(cardType);
		long newAmount = amount + (long) toAdd;
		long max = getMax(cardType);

		if (newAmount > max) {
			newAmount = (int) max;
		}

		amounts.put(cardType, (int) newAmount);
	}

	public void remove(String cardType, int toRemove) {
		if (toRemove <= 0) {
			return;
		}

		long amount = getAmount(cardType);
		long newAmount = amount - (long) toRemove;

		if (newAmount < 0) {
			newAmount = 0;
		}

		if (newAmount == 0) {
			amounts.remove(cardType);
		} else {
			amounts.put(cardType, (int) newAmount);
		}
	}

	public int cheatSetAmount(String cardPartId, int amount) {
		amount = Math.max(amount, 0);
		if (amount <= 0) {
			amounts.remove(cardPartId);
		} else {
			amounts.put(cardPartId, amount);
		}
		return amount;
	}

	public void checkIntegrity(Catalog catalog) {
		amounts.entrySet().removeIf(keyValue -> !catalog.getCardDefinitionsCollection().findById(keyValue.getKey()).isPresent());
	}

}
