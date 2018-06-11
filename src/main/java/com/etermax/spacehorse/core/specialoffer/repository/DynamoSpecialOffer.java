package com.etermax.spacehorse.core.specialoffer.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class DynamoSpecialOffer {

	@DynamoDBAttribute(attributeName = "id")
	private String id;

	@DynamoDBAttribute(attributeName = "expirationTimeInSeconds")
	private long expirationTimeInSeconds;

	@DynamoDBAttribute(attributeName = "availableAmountUntilExpiration")
	private int availableAmountUntilExpiration;

	public DynamoSpecialOffer() {
		// just for dynamo mapper
	}

	public DynamoSpecialOffer(String id, long expirationTimeInSeconds, int availableAmountUntilExpiration) {
		this.id = id;
		this.expirationTimeInSeconds = expirationTimeInSeconds;
		this.availableAmountUntilExpiration = availableAmountUntilExpiration;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getExpirationTimeInSeconds() {
		return expirationTimeInSeconds;
	}

	public void setExpirationTimeInSeconds(long expirationTimeInSeconds) {
		this.expirationTimeInSeconds = expirationTimeInSeconds;
	}

	public int getAvailableAmountUntilExpiration() {
		return availableAmountUntilExpiration;
	}

	public void setAvailableAmountUntilExpiration(int availableAmountUntilExpiration) {
		this.availableAmountUntilExpiration = availableAmountUntilExpiration;
	}
}
