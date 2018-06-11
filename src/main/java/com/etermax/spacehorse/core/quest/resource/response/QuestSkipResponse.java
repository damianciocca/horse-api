package com.etermax.spacehorse.core.quest.resource.response;

import com.etermax.spacehorse.core.player.resource.response.player.progress.QuestResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestSkipResponse {

	@JsonProperty("newQuest")
	private QuestResponse questResponse;

	@JsonProperty("skipTimeInSeconds")
	private long skipTimeInSeconds;

	public QuestSkipResponse(QuestResponse questResponse, long skipTimeInSeconds) {
		this.questResponse = questResponse;
		this.skipTimeInSeconds = skipTimeInSeconds;
	}

	public QuestResponse getQuestResponse() {
		return questResponse;
	}

	public long getSkipTimeInSeconds() {
		return skipTimeInSeconds;
	}
}
