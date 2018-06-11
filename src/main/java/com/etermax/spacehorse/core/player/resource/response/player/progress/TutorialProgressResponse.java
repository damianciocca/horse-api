package com.etermax.spacehorse.core.player.resource.response.player.progress;

import com.etermax.spacehorse.core.player.model.progress.TutorialProgress;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TutorialProgressResponse {
    @JsonProperty("activeTutorialId")
    private String activeTutorial;

    @JsonProperty("finishedTutorials")
    private List<String> finishedTutorials;

    public TutorialProgressResponse(TutorialProgress tutorialProgress) {
        this.activeTutorial = tutorialProgress.getActiveTutorial();
        this.finishedTutorials = tutorialProgress.getFinishedTutorials();
    }

    public String getActiveTutorial() {
        return activeTutorial;
    }

    public List<String> getFinishedTutorials() {
        return finishedTutorials;
    }
}
