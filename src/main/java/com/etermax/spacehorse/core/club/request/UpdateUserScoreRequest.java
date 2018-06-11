package com.etermax.spacehorse.core.club.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateUserScoreRequest {
	@JsonProperty("userId")
	private String userId;

	@JsonProperty("appId")
	private String appId;

	@JsonProperty("score")
	private int score;

	public UpdateUserScoreRequest(String environmentPrefix, String userId, Integer newScore) {
		this.userId = userId;
		this.appId = environmentPrefix;
		this.score = newScore;
	}

	public String getUserId() {
		return userId;
	}

	public String getAppId() {
		return appId;
	}

	public int getScore() {
		return score;
	}
}
