package com.etermax.spacehorse.core.player.model.inventory.currency;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.etermax.spacehorse.core.common.exception.ApiException;

@DynamoDBDocument
public class Currency {

	@DynamoDBAttribute(attributeName = "amount")
	private int amount;

	@DynamoDBIgnore
	public int getMin() {
		return 0;
	}

	@DynamoDBIgnore
	public int getMax() {
		return Integer.MAX_VALUE;
	}

	public int getAmount() {
		return amount;
	}

	public Currency() {
		this.amount = 0;
	}

	public void add(int toAdd) {
		if (toAdd < 0) {
			throw new ApiException("toAdd < 0");
		}

		long newAmount = (long) amount + (long) toAdd;

		if (newAmount > (long) getMax()) {
			newAmount = getMax();
		}

		this.amount = (int) newAmount;
	}

	public void remove(int toRemove) {
		if (toRemove < 0) {
			throw new ApiException("toRemove < 0");
		}

		long newAmount = (long) amount - (long) toRemove;

		if (newAmount < (long) getMin()) {
			newAmount = getMin();
		}

		this.amount = (int) newAmount;
	}

	public void setAmount(int amount) {
		if (amount >= 0) {
			this.amount = amount;
		}
	}
}
