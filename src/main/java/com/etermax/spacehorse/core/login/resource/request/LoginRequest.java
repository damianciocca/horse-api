package com.etermax.spacehorse.core.login.resource.request;

import com.etermax.spacehorse.core.user.model.Platform;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

public class LoginRequest {

	@JsonProperty("loginId")
	private String loginId;

	@JsonProperty("password")
	private String password;

	@JsonProperty("clientVersion")
    @NotNull
	private Integer clientVersion;

	@JsonProperty("platform")
	@NotNull
	private Platform platform;

	public LoginRequest(@JsonProperty("loginId") String loginId,
			@JsonProperty("password") String password,
			@JsonProperty("clientVersion") Integer clientVersion,
			@JsonProperty("platform") Platform platform) {
		this.loginId = loginId;
		this.password = password;
		this.clientVersion = clientVersion;
		this.platform = platform;
	}

	public LoginRequest(Integer clientVersion, Platform platform) {
		this.clientVersion = clientVersion;
		this.platform = platform;
	}

	private LoginRequest() {
	}

	public String getLoginId() {
		return loginId;
	}

	public String getPassword() {
		return password;
	}

	public Integer getClientVersion() {
		return clientVersion;
	}

	public Platform getPlatform() {
		return platform;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
