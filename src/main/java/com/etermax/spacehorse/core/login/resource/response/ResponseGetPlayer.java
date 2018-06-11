package com.etermax.spacehorse.core.login.resource.response;

import com.etermax.spacehorse.core.player.resource.response.player.PlayerResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseGetPlayer {
    @JsonProperty("found")
    private Boolean found;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("player")
    private PlayerResponse player;

    public ResponseGetPlayer(PlayerResponse player, Boolean found, String userId) {
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

    public PlayerResponse getPlayer() {
        return player;
    }
}
