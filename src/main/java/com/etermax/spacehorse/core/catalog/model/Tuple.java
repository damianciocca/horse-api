package com.etermax.spacehorse.core.catalog.model;

public class Tuple {

	private final String id;

	private final String value;

	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public Tuple(String id, String value) {
		this.id = id;
		this.value = value;
	}

}
