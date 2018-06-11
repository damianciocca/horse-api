package com.etermax.spacehorse.core.player.resource.response.player.inventory.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.player.model.inventory.chest.Chests;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChestsResponse {

	@JsonProperty("chests")
	private List<ChestResponse> chests = new ArrayList<>();

	public List<ChestResponse> getChests() {
		return chests;
	}

	public ChestsResponse(Chests chests) {
		this.chests = chests.getChests().stream().map(ChestResponse::new).collect(Collectors.toList());
	}

	public ChestsResponse(List<ChestResponse> chests) {
		this.chests = chests;
	}

	public ChestsResponse() {
	}

}
