package com.etermax.spacehorse.core.club.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClubInfoResponse {

	@JsonProperty("success")
	private boolean success;

	@JsonProperty("content")
	private Object content;

	public ClubInfoResponse(boolean success, Object clubContentResponse) {
		this.success = success;
		this.content = clubContentResponse;
	}

	public boolean getSuccess() {
		return success;
	}

	public Object getContent() {
		return content;
	}
}
