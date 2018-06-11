package com.etermax.spacehorse.core.player.model.progress;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.etermax.spacehorse.core.servertime.model.ServerTime;

@DynamoDBDocument
public class RewardsReceivedToday {

	@DynamoDBAttribute(attributeName = "expirationServerTime")
	private long expirationServerTime = 0L;

	@DynamoDBAttribute(attributeName = "goldRewardsReceivedToday")
	private int goldRewardsReceivedToday = 0;

	public RewardsReceivedToday() {
		// Just for dynamo mapper
	}

	public RewardsReceivedToday(long timeNowInSeconds) {
		this.expirationServerTime = ServerTime.roundToEndOfDay(timeNowInSeconds);
	}

	public long getExpirationServerTime() {
		return expirationServerTime;
	}

	public void setExpirationServerTime(long expirationServerTime) {
		this.expirationServerTime = expirationServerTime;
	}

	public int getGoldRewardsReceivedToday() {
		return this.goldRewardsReceivedToday;
	}

	public void setGoldRewardsReceivedToday(int goldRewardsReceivedToday) {
		this.goldRewardsReceivedToday = goldRewardsReceivedToday;
	}

    public Boolean canReceiveGoldRewardTodayWithLimit(Integer goldRewardsLimitPerDay, long timeNowInSeconds) {
        return this.getGoldRewardsReceivedToday(timeNowInSeconds) < goldRewardsLimitPerDay;
    }

    @DynamoDBIgnore
    public int getGoldRewardsReceivedToday(long serverTime) {
        if (serverTime >= expirationServerTime) {
            return 0;
        }
        return goldRewardsReceivedToday;
    }

    public void incrementGoldRewardReceivedToday(long timeNowInSeconds) {
		this.resetRewardsReceivedWhenNotSameDay(timeNowInSeconds);
        this.goldRewardsReceivedToday += 1;
    }

    private void resetRewardsReceivedWhenNotSameDay(long serverTime) {
        if (serverTime >= expirationServerTime) {
            this.goldRewardsReceivedToday = 0;
            this.expirationServerTime = ServerTime.roundToEndOfDay(serverTime);
        }
    }

}
