package com.etermax.spacehorse.core.quest.resource.response;

import com.etermax.spacehorse.core.player.resource.response.player.progress.QuestResponse;
import com.etermax.spacehorse.core.player.resource.response.player.progress.QuestsResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestRefreshResponse {

	@JsonProperty("newQuest")
	private QuestResponse questResponse;

	public QuestRefreshResponse() {
	}

	public QuestRefreshResponse(@JsonProperty("newQuest") QuestResponse questResponse) {
		this.questResponse = questResponse;
	}

	public QuestResponse getQuestResponse() {
		return questResponse;
	}

}
