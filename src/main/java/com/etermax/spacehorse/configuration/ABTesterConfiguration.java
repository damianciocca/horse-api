package com.etermax.spacehorse.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ABTesterConfiguration {

    @JsonProperty("readTimeoutInSeconds")
    private int readTimeoutInSeconds;

    @JsonProperty("connectTimeoutInSeconds")
    private int connectTimeoutInSeconds;

    @JsonProperty("writeTimeoutInSeconds")
    private int writeTimeoutInSeconds;

    @JsonProperty("requestTimeoutInSeconds")
    private int requestTimeoutInSeconds;

    @JsonProperty("baseUrl")
    private String baseUrl;

    @JsonProperty("gameCode")
    private String gameCode;

    public int getReadTimeoutInSeconds() { return readTimeoutInSeconds; }

    public int getConnectTimeoutInSeconds() {
        return connectTimeoutInSeconds;
    }

    public int getWriteTimeoutInSeconds() {
        return writeTimeoutInSeconds;
    }

    public int getRequestTimeoutInSeconds() { return requestTimeoutInSeconds; }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getGameCode() {
        return gameCode;
    }
}
