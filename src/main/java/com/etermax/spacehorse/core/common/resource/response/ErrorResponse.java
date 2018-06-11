package com.etermax.spacehorse.core.common.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorResponse {

	@JsonProperty("error")
	private String error;

	public ErrorResponse(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	static public ErrorResponse build(String error) {
		return new ErrorResponse(error);
	}
}
