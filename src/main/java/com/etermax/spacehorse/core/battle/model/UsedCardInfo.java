package com.etermax.spacehorse.core.battle.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class UsedCardInfo {

	@DynamoDBAttribute(attributeName = "cardType")
	private String cardType;

	@DynamoDBAttribute(attributeName = "useAmount")
	private int useAmount;

	public UsedCardInfo() {
	}

	public UsedCardInfo(String cardType, int useAmount) {
		this.cardType = cardType;
		this.useAmount = useAmount;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getUseAmount() {
		return useAmount;
	}

	public void setUseAmount(int useAmount) {
		this.useAmount = useAmount;
	}
}
