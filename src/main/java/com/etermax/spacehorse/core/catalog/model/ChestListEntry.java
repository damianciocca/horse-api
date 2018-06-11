package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.resource.response.ChestListEntryResponse;

public class ChestListEntry implements CatalogEntryWithChestInformation {

	private final int sequenceOrder;

	private final String chestId;

	public ChestListEntry(ChestListEntryResponse dto) {
		this.sequenceOrder = dto.getSequence();
		this.chestId = dto.getChestId();
	}

	public void validate(Catalog catalog, ChestList chestList) {
		if (!catalog.getChestDefinitionsCollection().findById(getChestId()).isPresent()) {
			throw new CatalogException("Invalid ChestId " + getChestId() + " in ChestList " + chestList.getId());
		}
	}

	public int getSequenceOrder() {
		return sequenceOrder;
	}

	public String getChestId() {
		return chestId;
	}

}
