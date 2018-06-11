package com.etermax.spacehorse.core.login.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseFromLoginApi {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("password")
    private String password;

    public ResponseFromLoginApi() {
    }

    public ResponseFromLoginApi(@JsonProperty("userId") String userId, @JsonProperty("password") String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
