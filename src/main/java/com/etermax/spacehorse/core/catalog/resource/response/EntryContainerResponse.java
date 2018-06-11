package com.etermax.spacehorse.core.catalog.resource.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntryContainerResponse<T> {

	@JsonProperty("entries")
	private List<T> entries;

	public EntryContainerResponse() {
		entries = new ArrayList<>();
	}

	public EntryContainerResponse(List<T> entries) {
		this.entries = entries;
	}

	public List<T> getEntries() {
		return entries;
	}

	public void addEntry(T entry) {
		this.entries.add(entry);
	}
}
