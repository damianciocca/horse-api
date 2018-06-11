package com.etermax.spacehorse.core.catalog.filter;

import com.etermax.spacehorse.core.abtest.model.ABTag;

public class RequestCatalogInfo {
	private String catalogId;
	private String clientPlatform;
	private String clientVersion;
	private ABTag abTag;

	public String getCatalogId() {
		return catalogId;
	}

	public String getClientPlatform() {
		return clientPlatform;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public ABTag getABTag() { return abTag; }

	public RequestCatalogInfo(String catalogId, String clientPlatform, String clientVersion, ABTag abTag) {
		this.catalogId = catalogId;
		this.clientPlatform = clientPlatform;
		this.clientVersion = clientVersion;
		this.abTag = abTag;
	}

}
