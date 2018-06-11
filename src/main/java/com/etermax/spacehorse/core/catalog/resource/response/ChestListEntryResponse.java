package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.ChestList;
import com.etermax.spacehorse.core.catalog.model.ChestListEntry;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChestListEntryResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("ListId")
	private String listId;

	@JsonProperty("Sequence")
	private int sequence;

	@JsonProperty("ChestId")
	private String chestId;

	public String getId() {
		return id;
	}

	public String getListId() {
		return listId;
	}

	public int getSequence() {
		return sequence;
	}

	public String getChestId() {
		return chestId;
	}

	public ChestListEntryResponse() {
	}

	public ChestListEntryResponse(ChestList chestList, ChestListEntry entry) {
		this.id = chestList.getId() + "-" + entry.getSequenceOrder();
		this.listId = chestList.getId();
		this.sequence = entry.getSequenceOrder();
		this.chestId = entry.getChestId();
	}

}
