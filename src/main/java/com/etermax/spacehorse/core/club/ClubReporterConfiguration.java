package com.etermax.spacehorse.core.club;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClubReporterConfiguration {

	@JsonProperty("environmentPrefix")
	private String environmentPrefix;

	@JsonProperty("readTimeoutInSeconds")
	private int readTimeoutInSeconds;

	@JsonProperty("connectTimeoutInSeconds")
	private int connectTimeoutInSeconds;

	@JsonProperty("writeTimeoutInSeconds")
	private int writeTimeoutInSeconds;

	@JsonProperty("serverUrl")
	private String serverUrl;

	public String getEnvironmentPrefix() {
		return environmentPrefix;
	}

	public int getReadTimeoutInSeconds() {
		return readTimeoutInSeconds;
	}

	public int getConnectTimeoutInSeconds() {
		return connectTimeoutInSeconds;
	}

	public int getWriteTimeoutInSeconds() {
		return writeTimeoutInSeconds;
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setEnvironmentPrefix(String environmentPrefix) {
		this.environmentPrefix = environmentPrefix;
	}

	public void setReadTimeoutInSeconds(int readTimeoutInSeconds) {
		this.readTimeoutInSeconds = readTimeoutInSeconds;
	}

	public void setConnectTimeoutInSeconds(int connectTimeoutInSeconds) {
		this.connectTimeoutInSeconds = connectTimeoutInSeconds;
	}

	public void setWriteTimeoutInSeconds(int writeTimeoutInSeconds) {
		this.writeTimeoutInSeconds = writeTimeoutInSeconds;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
}
