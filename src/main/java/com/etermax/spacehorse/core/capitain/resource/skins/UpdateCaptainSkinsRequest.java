package com.etermax.spacehorse.core.capitain.resource.skins;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

public class UpdateCaptainSkinsRequest {

	@JsonProperty("captainId")
	private String captainId;

	@JsonProperty("captainSlots")
	private List<CaptainSlotRequest> captainSlots;

	public UpdateCaptainSkinsRequest(@JsonProperty("captainId") String captainId,
			@JsonProperty("captainSlots") List<CaptainSlotRequest> captainSlots) {
		checkArgument(isNotBlank(captainId), "the captain id should not be blank");
		checkArgument(captainSlots != null, "the captain slots should not be null");
		this.captainId = captainId;
		this.captainSlots = captainSlots;
	}

	public String getCaptainId() {
		return captainId;
	}

	public Map<Integer, String> mapToSkinBySlots() {
		Map<Integer, String> slotsOfSkins = Maps.newHashMap();
		captainSlots.forEach(request -> slotsOfSkins.put(request.getSlotNumber(), request.getCaptainSkinId()));
		return slotsOfSkins;
	}
}
