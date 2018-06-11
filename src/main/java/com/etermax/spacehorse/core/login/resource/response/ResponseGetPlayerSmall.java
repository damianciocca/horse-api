package com.etermax.spacehorse.core.login.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseGetPlayerSmall {
    @JsonProperty("found")
    private boolean found;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("player")
    private PlayerSmallResponse player;

    public ResponseGetPlayerSmall(PlayerSmallResponse player, boolean found, String userId) {
        this.player = player;
        this.found = found;
        this.userId = userId;
    }

    public Boolean getFound() {
        return found;
    }

    public String getUserId() {
        return userId;
    }

    public PlayerSmallResponse getPlayer() {
        return player;
    }
}
