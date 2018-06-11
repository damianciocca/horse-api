package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.Tuple;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TupleResponse {
	@JsonProperty("Id")
	private String id;

	@JsonProperty("Value")
	private String value;

	public TupleResponse() {
	}

	public TupleResponse(String id, String value) {
		this.id = id;
		this.value = value;
	}

	public TupleResponse(Tuple t) {
		this.id = t.getId();
		this.value = t.getValue();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
