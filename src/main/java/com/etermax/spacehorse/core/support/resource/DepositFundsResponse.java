package com.etermax.spacehorse.core.support.resource;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepositFundsResponse {

	@JsonProperty("playerid")
	private String playerId;

	@JsonProperty("playerName")
	private String playerName;

	@JsonProperty("newGolds")
	private int newGolds;

	@JsonProperty("newGems")
	private int newGems;

	public DepositFundsResponse(String playerId, String playerName, int newGolds, int newGems) {
		this.playerId = playerId;
		this.playerName = playerName;
		this.newGolds = newGolds;
		this.newGems = newGems;
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getNewGolds() {
		return newGolds;
	}

	public int getNewGems() {
		return newGems;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
