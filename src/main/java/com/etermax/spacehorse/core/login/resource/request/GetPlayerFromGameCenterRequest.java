package com.etermax.spacehorse.core.login.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetPlayerFromGameCenterRequest {

    @JsonProperty("gameCenterId")
    private String gameCenterId;

    public GetPlayerFromGameCenterRequest(@JsonProperty("gameCenterId") String gameCenterId) {
        this.gameCenterId = gameCenterId;
    }

    public GetPlayerFromGameCenterRequest() {
    }

    public String getGameCenterId() {
        return gameCenterId;
    }

    public void setGameCenterId(String gameCenterId) {
        this.gameCenterId = gameCenterId;
    }

}
