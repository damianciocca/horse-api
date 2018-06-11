package com.etermax.spacehorse.core.player.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StartTutorialRequest {
    @JsonProperty("id")
    private String tutorialId;

    public StartTutorialRequest() {
    }

    public StartTutorialRequest(String tutorialId) {
        this.tutorialId = tutorialId;
    }

    public String getTutorialId() {
        return tutorialId;
    }
}
