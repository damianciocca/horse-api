package com.etermax.spacehorse.core.capitain.resource.skins;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptainSlotResponse {

	@JsonProperty("slotNumber")
	private int slotNumber;

	@JsonProperty("captainSkin")
	private CaptainSkinResponse captainSkin;

	public CaptainSlotResponse(int slotNumber, String captainSkinId) {
		this.slotNumber = slotNumber;
		this.captainSkin = new CaptainSkinResponse(captainSkinId);
	}

}
