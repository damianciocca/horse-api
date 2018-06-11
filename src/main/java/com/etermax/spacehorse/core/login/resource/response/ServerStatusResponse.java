package com.etermax.spacehorse.core.login.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerStatusResponse {

	static public final String UNDER_MAINTENANCE = "maintenance";

	@JsonProperty("status")
	private String status = "";

	@JsonProperty("statusDuration")
	private Integer statusDuration = 0;

	@JsonProperty("statusDescription")
	private String statusDescription = "";

	public ServerStatusResponse(String status, Integer statusDuration, String statusDescription) {
		this.status = status;
		this.statusDuration = statusDuration;
		this.statusDescription = statusDescription;
	}

	public String getStatus() {
		return status;
	}

	public Integer getStatusDuration() {
		return statusDuration;
	}

	public String getStatusDescription() {
		return statusDescription;
	}
}
