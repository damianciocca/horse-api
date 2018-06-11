package com.etermax.spacehorse.core.quest.model;

public class QuestWithPrice {

	private final Quest quest;
	private final int gemsCost;

	public QuestWithPrice(Quest quest, int gemsCost) {
		this.quest = quest;
		this.gemsCost = gemsCost;
	}

	public Quest getQuest() {
		return quest;
	}

	public int getGemsCost() {
		return gemsCost;
	}
}
