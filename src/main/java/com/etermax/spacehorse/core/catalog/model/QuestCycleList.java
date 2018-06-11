package com.etermax.spacehorse.core.catalog.model;

import java.util.List;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;

public class QuestCycleList extends CatalogEntry {

	private final List<QuestCycleEntry> entries;
	private final String difficulty;
	private final String listId;

	public QuestCycleList(String listId, String difficulty, List<QuestCycleEntry> entries) {
		super(listId + "_" + difficulty);
		this.listId = listId;
		this.difficulty = difficulty;
		this.entries = entries;
	}

	public String getListId() {
		return listId;
	}

	public List<QuestCycleEntry> getEntries() {
		return entries;
	}

	public String getDifficulty() {
		return difficulty;
	}

	@Override
	public void validate(Catalog catalog) {
		if (entries.size() == 0) {
			throw new CatalogException("Invalid Quest Cycle list");
		}

		try {
			QuestDifficultyType.valueOf(difficulty);
		} catch (IllegalArgumentException ex) {
			throw new CatalogException("Invalid difficuly " + difficulty + " in Quest Cycle list " + getId());
		}

		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).getSequenceOrder() != i) {
				throw new CatalogException("Invalid Sequence " + entries.get(i).getSequenceOrder() + " in Quest Cycle list " + getId());
			}
		}
	}

}
