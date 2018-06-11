package com.etermax.spacehorse.core.club.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckSessionRequest {
	@JsonProperty("user_id")
	private String userId;

	@JsonProperty("cookie")
	private String cookie;

	public CheckSessionRequest() {

	}

	public CheckSessionRequest(String userId, String cookie) {
		this.userId = userId;
		this.cookie = cookie;
	}

	public String getUserId() {
		return userId;
	}

	public String getCookie() {
		return cookie;
	}

}
