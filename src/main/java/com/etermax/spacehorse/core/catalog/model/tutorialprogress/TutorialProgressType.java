package com.etermax.spacehorse.core.catalog.model.tutorialprogress;

public enum TutorialProgressType {
	SIMPLE_STEP("SimpleStep"),
	BATTLE_STEP("BattleStep");

	private final String id;

	TutorialProgressType(final String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return id;
	}

	public String getId() {
		return id;
	}
}
