package com.etermax.spacehorse.core.login.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerSmallResponse {
	@JsonProperty("userId")
	private String userId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("level")
	private int level;

	public PlayerSmallResponse(String userId, String name, int level) {
		this.userId = userId;
		this.name = name;
		this.level = level;
	}

	public String getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}
}
