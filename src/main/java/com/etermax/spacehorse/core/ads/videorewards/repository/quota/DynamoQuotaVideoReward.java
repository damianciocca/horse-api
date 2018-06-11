package com.etermax.spacehorse.core.ads.videorewards.repository.quota;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

@DynamoDBTable(tableName = "quotaVideoRewards")
public class DynamoQuotaVideoReward implements AbstractDynamoDAO {

	@DynamoDBHashKey(attributeName = "userId")
	private String userId;

	@DynamoDBRangeKey(attributeName = "placeName")
	private String placeName;

	@DynamoDBAttribute(attributeName = "creationTimeInSeconds")
	private long creationTimeInSeconds;

	@DynamoDBAttribute(attributeName = "counter")
	private int counter;

	public DynamoQuotaVideoReward() {
		// just for dynamo mapper
	}

	public static DynamoQuotaVideoReward toDynamoQuotaVideReward(String userId, QuotaVideoReward quotaVideoReward) {
		DynamoQuotaVideoReward dynamoQuotaVideoReward = new DynamoQuotaVideoReward();
		dynamoQuotaVideoReward.setUserId(userId);
		dynamoQuotaVideoReward.setPlaceName(quotaVideoReward.getPlaceName());
		dynamoQuotaVideoReward.setCounter(quotaVideoReward.getCounter());
		dynamoQuotaVideoReward.setCreationTimeInSeconds(quotaVideoReward.getCreationTimeInSeconds());
		return dynamoQuotaVideoReward;
	}

	public static QuotaVideoReward toQuotaVideoReward(DynamoQuotaVideoReward dynamoQuotaVideoReward, ServerTimeProvider timeProvider) {
		return QuotaVideoReward.restore( //
				dynamoQuotaVideoReward.getUserId(), //
				dynamoQuotaVideoReward.getPlaceName(), //
				dynamoQuotaVideoReward.getCreationTimeInSeconds(),//
				dynamoQuotaVideoReward.getCounter(),//
				timeProvider);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public long getCreationTimeInSeconds() {
		return creationTimeInSeconds;
	}

	public void setCreationTimeInSeconds(long creationTimeInSeconds) {
		this.creationTimeInSeconds = creationTimeInSeconds;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	@Override
	public String getId() {
		return getUserId();
	}

	@Override
	public void setId(String id) {
		setUserId(id);
	}
}
