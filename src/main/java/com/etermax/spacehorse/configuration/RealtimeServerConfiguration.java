package com.etermax.spacehorse.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RealtimeServerConfiguration {

	private static final String SEPARATOR = ":";

	@JsonProperty("host")
	public String host;

	@JsonProperty("port")
	public int port;

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@JsonIgnore
	public String getServer() {
		StringBuilder server = new StringBuilder();
		server.append(getHost());
		server.append(SEPARATOR);
		server.append(getPort());
		return server.toString();
	}

}
