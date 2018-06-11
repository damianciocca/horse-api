package com.etermax.spacehorse.core.matchmaking.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class MatchmakingRequest {

	@JsonProperty("mapId")
	private String mapId;

	@JsonProperty("extraData")
	private String extraData;

	public MatchmakingRequest(@JsonProperty("mapId") String mapId, @JsonProperty("extraData") String extraData) {
		this.mapId = mapId;
		this.extraData = extraData;
	}

	public String getMapId() {
		return mapId;
	}

	public String getExtraData() {
		return extraData;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	public boolean isValid() {
		return mapId != null && !mapId.isEmpty();
	}
}
