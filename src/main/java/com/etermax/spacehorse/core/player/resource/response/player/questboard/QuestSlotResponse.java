package com.etermax.spacehorse.core.player.resource.response.player.questboard;

import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.player.resource.response.player.progress.QuestResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestSlotResponse {

	@JsonProperty("slotId")
	String slotId;

	@JsonProperty("quest")
	QuestResponse questResponse;

	public QuestSlotResponse(String slotId, QuestSlot questSlot) {
		this.slotId = slotId;
		this.questResponse = new QuestResponse(questSlot.getActiveQuest());
	}
}
