package com.etermax.spacehorse.core.login.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayServicesAuthRequestIncome {

    @JsonProperty("token")
    private String token;

    public PlayServicesAuthRequestIncome(@JsonProperty("token") String token) {
        this.token = token;
    }

    public PlayServicesAuthRequestIncome() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }

}
