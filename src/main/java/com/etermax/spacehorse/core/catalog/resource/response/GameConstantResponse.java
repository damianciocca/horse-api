package com.etermax.spacehorse.core.catalog.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameConstantResponse {

	@JsonProperty("Id")
	private String id = null;

	@JsonProperty("Value")
	private String value = null;

	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}
}
