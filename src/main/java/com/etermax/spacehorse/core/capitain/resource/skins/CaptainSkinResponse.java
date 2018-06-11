package com.etermax.spacehorse.core.capitain.resource.skins;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptainSkinResponse {

	@JsonProperty("captainSkinId")
	private final String captainSkinId;

	public CaptainSkinResponse(String captainSkinId) {
		this.captainSkinId = captainSkinId;
	}
}
