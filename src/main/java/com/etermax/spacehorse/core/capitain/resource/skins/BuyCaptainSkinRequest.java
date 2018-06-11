package com.etermax.spacehorse.core.capitain.resource.skins;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BuyCaptainSkinRequest {

	@JsonProperty("captainId")
	private String captainId;

	@JsonProperty("captainSkinId")
	private String captainSkinId;

	public BuyCaptainSkinRequest(@JsonProperty("captainId") String captainId, @JsonProperty("captainSkinId") String captainSkinId) {
		checkArgument(isNotBlank(captainId), "the captain id should not be blank");
		checkArgument(isNotBlank(captainSkinId), "the captain skin id should not be blank");
		this.captainId = captainId;
		this.captainSkinId = captainSkinId;
	}

	public String getCaptainSkinId() {
		return this.captainSkinId;
	}

	public String getCaptainId() {
		return captainId;
	}
}
