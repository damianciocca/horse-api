package com.etermax.spacehorse.core.login.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetPlayerFromGooglePlayRequest {

    @JsonProperty("token")
    private String token;

    public GetPlayerFromGooglePlayRequest(@JsonProperty("token") String token) {
        this.token = token;
    }

    public GetPlayerFromGooglePlayRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
