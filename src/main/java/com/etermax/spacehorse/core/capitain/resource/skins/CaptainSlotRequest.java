package com.etermax.spacehorse.core.capitain.resource.skins;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptainSlotRequest {

	@JsonProperty("slotNumber")
	private int slotNumber;

	@JsonProperty("captainSkinId")
	private String captainSkinId;

	public CaptainSlotRequest(@JsonProperty("slotNumber") int slotNumber, @JsonProperty("captainSkinId") String captainSkinId) {
		checkArgument(isNotBlank(captainSkinId), "the captain id should not be blank");
		checkArgument(slotNumber >= 0, "the slot number should not be negative");
		this.slotNumber = slotNumber;
		this.captainSkinId = captainSkinId;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public String getCaptainSkinId() {
		return captainSkinId;
	}
}
