package com.etermax.spacehorse.core.liga.repository;

import org.joda.time.DateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.liga.domain.PlayerSeason;
import com.etermax.spacehorse.core.servertime.model.ServerTime;

@DynamoDBDocument
public class DynamoPlayerSeason {

	@DynamoDBAttribute(attributeName = "initialDateInSeconds")
	private long initialDateInSeconds;
	@DynamoDBAttribute(attributeName = "mmr")
	private int mmr;
	@DynamoDBAttribute(attributeName = "rewardPending")
	private boolean rewardPending;

	public DynamoPlayerSeason(){
		// just for dynamo mapper
	}

	public DynamoPlayerSeason(PlayerSeason playerSeason){
		initialDateInSeconds = ServerTime.fromDate(playerSeason.getInitialDateTime());
		mmr = playerSeason.getMmr();
		rewardPending = playerSeason.isRewardPending();
	}

	public PlayerSeason toPlayerSeason(){
		DateTime initialDateTime = ServerTime.toDateTime(initialDateInSeconds);
		return new PlayerSeason(initialDateTime, mmr, rewardPending);
	}

	public long getInitialDateInSeconds() {
		return initialDateInSeconds;
	}

	public void setInitialDateInSeconds(long initialDateInSeconds) {
		this.initialDateInSeconds = initialDateInSeconds;
	}

	public Integer getMmr() {
		return mmr;
	}

	public void setMmr(Integer mmr) {
		this.mmr = mmr;
	}

	public boolean isRewardPending() {
		return rewardPending;
	}

	public void setRewardPending(boolean rewardPending) {
		this.rewardPending = rewardPending;
	}
}
