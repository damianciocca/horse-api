package com.etermax.spacehorse.core.catalog.model;

import java.util.List;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;

public class ChestList extends CatalogEntry {

	private final List<ChestListEntry> entries;

	public List<ChestListEntry> getEntries() {
		return entries;
	}

	public ChestList(String id, List<ChestListEntry> entries) {
		super(id);
		this.entries = entries;
	}

	@Override
	public void validate(Catalog catalog) {
		if (entries.size() == 0) {
			throw new CatalogException("Invalid empty chest list");
		}
		entries.forEach(chestListEntry -> chestListEntry.validate(catalog, this));
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getSequenceOrder() != i) {
				throw new CatalogException("Invalid Sequence " + entries.get(i).getSequenceOrder() + " in ChestList " + getId());
			}
		}
	}

}
