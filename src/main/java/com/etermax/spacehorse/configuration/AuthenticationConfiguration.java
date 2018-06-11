package com.etermax.spacehorse.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthenticationConfiguration {

	@JsonProperty
	private boolean enabled;

	@JsonProperty
	private Long maxCacheSize;

	@JsonProperty
	private String defaultAdminLoginId;

	@JsonProperty
	private String defaultAdminPassword;

	@JsonProperty
	private String defaultSupportLoginId;

	@JsonProperty
	private String defaultSupportPassword;

	public Long getMaxCacheSize() {
		return maxCacheSize;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getDefaultAdminLoginId() {
		return defaultAdminLoginId;
	}

	public String getDefaultAdminPassword() {
		return defaultAdminPassword;
	}

	public String getDefaultSupportLoginId() {
		return defaultSupportLoginId;
	}

	public String getDefaultSupportPassword() {
		return defaultSupportPassword;
	}
}
