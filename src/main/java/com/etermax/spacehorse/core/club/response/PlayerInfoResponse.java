package com.etermax.spacehorse.core.club.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerInfoResponse {
	@JsonProperty("id")
	private final String userId;

	@JsonProperty("userName")
	private final String userName;

	public PlayerInfoResponse(String userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}
}
