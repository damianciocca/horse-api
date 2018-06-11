package com.etermax.spacehorse.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InAppsAndroidConfiguration {

	@JsonProperty("packageName")
	public String packageName;

	@JsonProperty("signature")
	public String signature;

	public String getPackageName() {
		return packageName;
	}

	public String getSignature() {
		return signature;
	}

}
