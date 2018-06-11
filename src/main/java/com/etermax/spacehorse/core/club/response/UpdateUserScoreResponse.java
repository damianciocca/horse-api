package com.etermax.spacehorse.core.club.response;


import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateUserScoreResponse {
	@JsonProperty("success")
	private boolean success;

	@JsonProperty("content")
	private Object content;

	public boolean isSuccess() {
		return success;
	}

	public Object getContent() {
		return content;
	}
}
