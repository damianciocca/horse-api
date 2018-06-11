package com.etermax.spacehorse.core.catalog.model.unlock;

public enum FeatureByPlayerLvlType {

	COURIER_SLOT("CourierSlot"), //
	QUEST_SLOT("QuestSlot"), //
	DECK_SLOT("DeckSlot"), //
	CLUB("Club");

	private final String id;

	FeatureByPlayerLvlType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}