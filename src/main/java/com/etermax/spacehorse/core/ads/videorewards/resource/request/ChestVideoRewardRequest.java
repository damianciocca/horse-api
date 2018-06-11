package com.etermax.spacehorse.core.ads.videorewards.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChestVideoRewardRequest {

	@JsonProperty("chestId")
	private long chestId;

	public ChestVideoRewardRequest(@JsonProperty("chestId") long chestId) {
		this.chestId = chestId;
	}

	public Long getChestId() {
		return chestId;
	}
}
