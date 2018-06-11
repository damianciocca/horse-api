package com.etermax.spacehorse.core.liga.response;

import com.etermax.spacehorse.core.liga.domain.PlayerSeason;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerSeasonResponse {

	@JsonProperty("initialDateTime")
	private long initialDateTimeInSeconds;
	@JsonProperty("mmr")
	private Integer mmr;
	@JsonProperty("rewardPending")
	private boolean rewardPending;

	public PlayerSeasonResponse(PlayerSeason playerSeason) {
		this.initialDateTimeInSeconds = ServerTime.fromDate(playerSeason.getInitialDateTime());
		this.mmr = playerSeason.getMmr();
		this.rewardPending = playerSeason.isRewardPending();
	}

	public long getInitialDateTimeInSeconds() {
		return initialDateTimeInSeconds;
	}

	public Integer getMmr() {
		return mmr;
	}

	public boolean isRewardPending() {
		return rewardPending;
	}
}
