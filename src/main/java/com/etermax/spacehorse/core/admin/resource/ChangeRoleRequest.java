package com.etermax.spacehorse.core.admin.resource;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.authenticator.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeRoleRequest {

	@JsonProperty("adminLoginId")
	private String adminLoginId;

	@JsonProperty("adminPassword")
	private String adminPassword;

	@JsonProperty("playerId")
	private String playerId;

	@JsonProperty("role")
	private Role role;

	public ChangeRoleRequest(@JsonProperty("adminLoginId") String adminLoginId, @JsonProperty("adminPassword") String adminPassword,
			@JsonProperty("playerId") String playerId, @JsonProperty("role") Role role) {
		this.adminLoginId = adminLoginId;
		this.adminPassword = adminPassword;
		this.playerId = playerId;
		this.role = role;
	}

	public String getAdminLoginId() {
		return adminLoginId;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public String getPlayerId() {
		return playerId;
	}

	public Role getRole() {
		return role;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
