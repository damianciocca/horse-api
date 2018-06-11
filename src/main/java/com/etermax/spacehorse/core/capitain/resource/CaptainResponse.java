package com.etermax.spacehorse.core.capitain.resource;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.capitain.model.slots.CaptainSlot;
import com.etermax.spacehorse.core.capitain.resource.skins.CaptainSlotResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptainResponse {

	@JsonProperty("captainId")
	private String captainId;

	@JsonProperty("ownedSkinsById")
	private List<String> ownedSkinsByIds;

	@JsonProperty("captainSlots")
	private List<CaptainSlotResponse> captainSlots;



	public CaptainResponse(Captain captain) {
		this.captainId = captain.getCaptainId();
		this.captainSlots = captain.getCaptainSlots().stream().map(toCaptainSlotResponse()).collect(Collectors.toList());
		this.ownedSkinsByIds = captain.getOwnedSkins().stream().map(CaptainSkin::getCaptainSkinId).collect(Collectors.toList());
	}

	private Function<CaptainSlot, CaptainSlotResponse> toCaptainSlotResponse() {
		return captainSlot -> new CaptainSlotResponse(captainSlot.getSlotNumber(), captainSlot.getCaptainSkinId());
	}
}
