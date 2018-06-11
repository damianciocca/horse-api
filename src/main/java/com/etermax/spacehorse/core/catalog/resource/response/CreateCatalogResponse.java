package com.etermax.spacehorse.core.catalog.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCatalogResponse {
	@JsonProperty("newCatalogId")
	private String newCatalogId;

	public String getNewCatalogId() {
		return newCatalogId;
	}

	public CreateCatalogResponse(@JsonProperty("newCatalogId") String newCatalogId) {
		this.newCatalogId = newCatalogId;
	}
}
