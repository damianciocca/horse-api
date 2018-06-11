package com.etermax.spacehorse.core.quest.model;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.QuestCycleEntry;
import com.etermax.spacehorse.core.catalog.model.QuestCycleList;

public class QuestCycleSequenceMapper {
	public List<QuestCycleEntry> mapQuestCyclesFrom(String slotId, String listId, CatalogEntriesCollection<QuestCycleList> questCycleLists) {
		return questCycleLists.getEntries().stream().filter(questCycleList -> isSameDifficultyAndListId(slotId, listId, questCycleList)).findFirst()
				.map(questCycleList -> questCycleList.getEntries()).orElse(emptyList());
	}

	private ArrayList<QuestCycleEntry> emptyList() {
		return new ArrayList<>();
	}

	private boolean isSameDifficultyAndListId(String slotId, String listId, QuestCycleList questCycleList) {
		return questCycleList.getListId().equals(listId) && questCycleList.getDifficulty().equals(slotId);
	}
}
