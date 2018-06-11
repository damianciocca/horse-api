package com.etermax.spacehorse.core.player.resource.response.player.questboard;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.player.resource.response.player.progress.QuestResponse;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestBoardResponse {

	@JsonProperty("slots")
	private List<QuestSlotResponse> slots;

	@JsonProperty("skipTimeInSeconds")
	private long skipTimeInSeconds;

	@JsonProperty("dailyQuest")
	private QuestResponse dailyQuest;

	public QuestBoardResponse(QuestBoard questBoard) {
		Map<String, QuestSlot> slots = questBoard.getSlots();
		this.slots = slots.entrySet().stream().map(entry -> {
			String slotId = entry.getKey();
			return new QuestSlotResponse(slotId, entry.getValue());
		}).collect(Collectors.toList());
		this.skipTimeInSeconds = questBoard.getSkipTimeInSeconds();
		this.dailyQuest = new QuestResponse(questBoard.getDailyQuest());
	}

}
