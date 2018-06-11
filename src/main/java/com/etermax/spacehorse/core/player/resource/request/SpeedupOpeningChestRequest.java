package com.etermax.spacehorse.core.player.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpeedupOpeningChestRequest {
	@JsonProperty("chestId")
	private Long chestId;

	public SpeedupOpeningChestRequest(@JsonProperty("chestId") Long chestId) {
		this.chestId = chestId;
	}

	public Long getChestId() {
		return chestId;
	}

}
