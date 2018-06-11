package com.etermax.spacehorse.core.specialoffer.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

@DynamoDBDocument
public class DynamoSpecialOfferHistory {

	@DynamoDBAttribute(attributeName = "id")
	private String id;

	@DynamoDBAttribute(attributeName = "groupId")
	private String groupId;

	@DynamoDBAttribute(attributeName = "creationTimeInSeconds")
	private long creationTimeInSeconds;

	public DynamoSpecialOfferHistory() {
		// just for dynamo mapper
	}

	public DynamoSpecialOfferHistory(String id, String groupId, long creationTimeInSeconds) {
		this.id = id;
		this.groupId = groupId;
		this.creationTimeInSeconds = creationTimeInSeconds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public long getCreationTimeInSeconds() {
		return creationTimeInSeconds;
	}

	public void setCreationTimeInSeconds(long creationTimeInSeconds) {
		this.creationTimeInSeconds = creationTimeInSeconds;
	}
}
