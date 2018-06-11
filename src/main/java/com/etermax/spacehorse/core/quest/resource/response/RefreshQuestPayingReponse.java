package com.etermax.spacehorse.core.quest.resource.response;

import com.etermax.spacehorse.core.player.resource.response.player.progress.QuestResponse;
import com.etermax.spacehorse.core.quest.model.QuestWithPrice;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshQuestPayingReponse {

	@JsonProperty("gemsCost")
	private final int gemsCost;

	@JsonProperty("quest")
	private QuestResponse quest;

	public RefreshQuestPayingReponse(QuestWithPrice questWithPrice) {
		this.quest = new QuestResponse(questWithPrice.getQuest());
		this.gemsCost = questWithPrice.getGemsCost();
	}

	public QuestResponse getQuest() {
		return quest;
	}

	public int getGemsCost() {
		return gemsCost;
	}
}
