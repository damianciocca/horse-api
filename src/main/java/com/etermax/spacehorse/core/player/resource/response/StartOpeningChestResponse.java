package com.etermax.spacehorse.core.player.resource.response;

import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StartOpeningChestResponse {
	@JsonProperty("chest")
	private ChestResponse chest;

	public ChestResponse getChest() {
		return chest;
	}

	public StartOpeningChestResponse(ChestResponse chest) {
		this.chest = chest;
	}
}
