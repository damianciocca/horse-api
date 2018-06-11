package com.etermax.spacehorse.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InAppsIosConfiguration {

	@JsonProperty("bundleId")
	public String bundleId;

	public String getBundleId() {
		return bundleId;
	}
}
