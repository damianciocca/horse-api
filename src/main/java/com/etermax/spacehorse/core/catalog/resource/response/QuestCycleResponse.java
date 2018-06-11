package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.QuestCycleList;
import com.etermax.spacehorse.core.catalog.model.QuestCycleEntry;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestCycleResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("ListId")
	private String listId;

	@JsonProperty("Difficulty")
	private String difficulty;

	@JsonProperty("Sequence")
	private int sequence;

	@JsonProperty("QuestDefinitionId")
	private String questDefinitionId;

	public QuestCycleResponse() {
	}

	public QuestCycleResponse(QuestCycleList list, QuestCycleEntry entry) {
		this.id = entry.getId();
		this.listId = list.getListId();
		this.difficulty = list.getDifficulty();
		this.sequence = entry.getSequenceOrder();
		this.questDefinitionId = entry.getQuestDefinition().getId();
	}

	public String getId() {
		return id;
	}

	public String getListId() {
		return listId;
	}

	public int getSequence() {
		return sequence;
	}

	public String getQuestDefinitionId() {
		return questDefinitionId;
	}

	public String getDifficulty() {
		return difficulty;
	}
}
