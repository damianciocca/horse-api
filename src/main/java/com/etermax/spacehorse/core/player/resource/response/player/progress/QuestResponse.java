package com.etermax.spacehorse.core.player.resource.response.player.progress;

import com.etermax.spacehorse.core.quest.model.Quest;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestResponse {

	@JsonProperty("id")
	public String id;

	@JsonProperty("currentProgress")
	public Long currentProgress;

	@JsonProperty("state")
	public String state;

	@JsonProperty("refreshTimeInSeconds")
	public long refreshTimeInSeconds;

	public QuestResponse(Quest quest) {
		this.id = quest.getQuestId();
		this.currentProgress = quest.getCurrentProgress();
		this.state = quest.getState();
		this.refreshTimeInSeconds = quest.getRefreshTimeInSeconds();
	}

	public String getId() {
		return id;
	}

	public Long getCurrentProgress() {
		return currentProgress;
	}

	public String getState() {
		return state;
	}

	public long getRefreshTimeInSeconds() {
		return refreshTimeInSeconds;
	}
}
