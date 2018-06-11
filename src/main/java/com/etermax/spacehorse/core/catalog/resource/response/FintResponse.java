package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.csv.field.Fint;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FintResponse {

	@JsonProperty("raw")
	private long raw;

	public long getRaw() {
		return raw;
	}

	public FintResponse() {
	}

	public FintResponse(long raw) {
		this.raw = raw;
	}

	public FintResponse(Fint fint) {
		this.raw = fint.getRaw();
	}

}
