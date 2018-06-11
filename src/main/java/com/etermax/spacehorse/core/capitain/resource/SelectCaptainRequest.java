package com.etermax.spacehorse.core.capitain.resource;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SelectCaptainRequest {

	@JsonProperty("captainId")
	private String captainId;

	public SelectCaptainRequest(@JsonProperty("captainId") String captainId) {
		checkArgument(isNotBlank(captainId), "the captain id should not be blank");
		this.captainId = captainId;
	}

	public String getCaptainId() {
		return this.captainId;
	}

}
