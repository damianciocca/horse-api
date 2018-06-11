package com.etermax.spacehorse.core.matchmaking.resource.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FriendlyMatchmakingRequest {

	@JsonProperty("mapId")
	private String mapId;

	@JsonProperty("opponentId")
	private String opponentId;

	@JsonProperty("extraData")
	private String extraData;

	public FriendlyMatchmakingRequest(@JsonProperty("mapId") String mapId, @JsonProperty("opponentId") String opponentId,
			@JsonProperty("extraData") String extraData) {
		this.mapId = mapId;
		this.opponentId = opponentId;
		this.extraData = extraData;
	}

	public String getMapId() {
		return mapId;
	}

	public String getOpponentId() {
		return opponentId;
	}

	public String getExtraData() {
		return extraData;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	public boolean isValid() {
		return mapId != null && !mapId.isEmpty() && opponentId != null && !opponentId.isEmpty();
	}
}
