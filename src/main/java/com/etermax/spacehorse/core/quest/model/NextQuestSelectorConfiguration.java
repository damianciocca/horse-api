package com.etermax.spacehorse.core.quest.model;

import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.QuestChancesList;
import com.etermax.spacehorse.core.catalog.model.QuestCycleList;

public class NextQuestSelectorConfiguration {

	private final CatalogEntriesCollection<QuestCycleList> questCycleLists;
	private final CatalogEntriesCollection<QuestChancesList> questChancesList;

	private final String listId;
	private final int playerMapNumber;

	public NextQuestSelectorConfiguration(CatalogEntriesCollection<QuestCycleList> questCycleLists, CatalogEntriesCollection<QuestChancesList> questChancesList,
			String listId, int playerMapNumber) {
		this.questCycleLists = questCycleLists;
		this.questChancesList = questChancesList;
		this.listId = listId;
		this.playerMapNumber = playerMapNumber;
	}

	public CatalogEntriesCollection<QuestCycleList> getQuestCycleLists() {
		return questCycleLists;
	}

	public CatalogEntriesCollection<QuestChancesList> getQuestChancesList() {
		return questChancesList;
	}

	public String getListId() {
		return listId;
	}

	public int getPlayerMapNumber() {
		return playerMapNumber;
	}
}
