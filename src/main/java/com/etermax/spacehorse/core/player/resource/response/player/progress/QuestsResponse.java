package com.etermax.spacehorse.core.player.resource.response.player.progress;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestsResponse {

    @JsonProperty("activeQuest")
    private QuestResponse activeQuest;

    public void QuestsResponse() {
    }

    public QuestsResponse(@JsonProperty("activeQuest") QuestResponse questResponse) {
        this.activeQuest = questResponse;
    }

    public QuestResponse getActiveQuest() {
        return activeQuest;
    }

}
