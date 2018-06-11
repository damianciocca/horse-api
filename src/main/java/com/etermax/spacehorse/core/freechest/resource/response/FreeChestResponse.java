package com.etermax.spacehorse.core.freechest.resource.response;

import com.etermax.spacehorse.core.freechest.model.FreeChest;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FreeChestResponse {

	@JsonProperty("lastChestOpeningServerTime")
	private long lastChestOpeningServerTime;

	public long getLastChestOpeningServerTime() {
		return lastChestOpeningServerTime;
	}

	public FreeChestResponse(FreeChest freeChest) {
		this.lastChestOpeningServerTime = ServerTime.fromDate(freeChest.getLastChestOpeningDate());
	}
}
