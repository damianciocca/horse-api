package com.etermax.spacehorse.core.ads.videorewards.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAvailableVideoRewardResponse {

	@JsonProperty("hasAvailable")
	private boolean hasAvailable;

	public GetAvailableVideoRewardResponse() {
		// just for jackson
	}

	public GetAvailableVideoRewardResponse(boolean hasAvailable) {
		this.hasAvailable = hasAvailable;
	}

	public boolean hasAvailable() {
		return hasAvailable;
	}
}
