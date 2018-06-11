package com.etermax.spacehorse.core.login.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetFeatureTogglesResponse {

	@JsonProperty("features")
	private String[] features;

	public GetFeatureTogglesResponse(String[] features) {
		this.features = features;
	}

	public String[] getFeatures() {
		return features;
	}
}
