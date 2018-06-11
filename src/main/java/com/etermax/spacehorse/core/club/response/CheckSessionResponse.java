package com.etermax.spacehorse.core.club.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheckSessionResponse {
	@JsonProperty("isValidSession")
	private boolean isValidSession;

	public CheckSessionResponse(boolean content) {
		this.isValidSession = content;
	}

	public boolean getIsValidSession() {
		return isValidSession;
	}
}
