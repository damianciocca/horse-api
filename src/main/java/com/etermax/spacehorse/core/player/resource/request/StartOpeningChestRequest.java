package com.etermax.spacehorse.core.player.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StartOpeningChestRequest {

	@JsonProperty("chestId")
	private Long chestId;

	public StartOpeningChestRequest(@JsonProperty("chestId") Long chestId) {
		this.chestId = chestId;
	}

	public Long getChestId() {
		return chestId;
	}
}
