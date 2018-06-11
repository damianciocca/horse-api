package com.etermax.spacehorse.core.player.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishOpeningChestRequest {
	@JsonProperty("chestId")
	private Long chestId;

	public FinishOpeningChestRequest(@JsonProperty("chestId") Long chestId) {
		this.chestId = chestId;
	}

	public Long getChestId() {
		return chestId;
	}

}
