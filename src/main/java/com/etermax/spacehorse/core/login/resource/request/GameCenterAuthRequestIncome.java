package com.etermax.spacehorse.core.login.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameCenterAuthRequestIncome {

	@JsonProperty("gameCenterId")
	private String gameCenterId;

	public GameCenterAuthRequestIncome(@JsonProperty("gameCenterId") String gameCenterId) {
		this.gameCenterId = gameCenterId;
	}

	public GameCenterAuthRequestIncome() {
	}

	public void setGameCenterId(String gameCenterId) {
		this.gameCenterId = gameCenterId;
	}

	public String getGameCenterId() {
		return this.gameCenterId;
	}

}
