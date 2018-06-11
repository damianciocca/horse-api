package com.etermax.spacehorse.core.catalog.resource.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CatalogListResponse {
	@JsonProperty("catalogs")
	private List<CatalogIsActive> catalogs = new ArrayList<>();

	public CatalogListResponse(List<CatalogIsActive> catalogs) {
		this.catalogs = catalogs;
	}

	public List<CatalogIsActive> getCatalogs() {
		return this.catalogs;
	}
}
