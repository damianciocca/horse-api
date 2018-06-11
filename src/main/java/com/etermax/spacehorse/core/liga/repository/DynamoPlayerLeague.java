package com.etermax.spacehorse.core.liga.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;
import com.etermax.spacehorse.core.liga.domain.PlayerSeason;
import com.etermax.spacehorse.core.liga.domain.PlayerLeague;

@DynamoDBTable(tableName = "playerLeague")
public class DynamoPlayerLeague implements AbstractDynamoDAO {

	@DynamoDBHashKey(attributeName = "userId")
	private String userId;

	@DynamoDBAttribute(attributeName = "current")
	private DynamoPlayerSeason current;

	@DynamoDBAttribute(attributeName = "previous")
	private DynamoPlayerSeason previous;

	public DynamoPlayerLeague() {
		// just for dynamo mapper
	}

	public DynamoPlayerLeague(String userId, PlayerLeague playerLeague) {
		this.userId = userId;
		this.current = playerLeague.getCurrent().map(DynamoPlayerSeason::new).orElse(null);
		this.previous = playerLeague.getPrevious().map(DynamoPlayerSeason::new).orElse(null);

	}

	public PlayerLeague toPlayerSeasons() {
		PlayerSeason playerSeasonCurrent = current != null ? current.toPlayerSeason() : null;
		PlayerSeason playerSeasonPrevious = previous != null ? previous.toPlayerSeason() : null;
		return new PlayerLeague(playerSeasonCurrent, playerSeasonPrevious);
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setCurrent(DynamoPlayerSeason current) {
		this.current = current;
	}

	public void setPrevious(DynamoPlayerSeason previous) {
		this.previous = previous;
	}

	public String getUserId() {
		return userId;
	}

	public DynamoPlayerSeason getCurrent() {
		return current;
	}

	public DynamoPlayerSeason getPrevious() {
		return previous;
	}

	@Override
	public void setId(String id) {
		userId = id;
	}

	@Override
	public String getId() {
		return userId;
	}
}
