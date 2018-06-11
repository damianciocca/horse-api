package com.etermax.spacehorse.core.cheat.resource.request;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CheatRequest {

	@JsonProperty("cheatId")
	private String cheatId;

	@JsonProperty("parameters")
	private String[] parameters;

	private CheatRequest() {
	}

	public CheatRequest(@JsonProperty("cheatId") String cheatId, @JsonProperty("parameters") String[] parameters) {
		this.cheatId = cheatId;
		this.parameters = parameters;
	}

	public String getCheatId() {
		return cheatId;
	}

	public String[] getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
