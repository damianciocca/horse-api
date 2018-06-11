package com.etermax.spacehorse.mock;

import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.BattlePlayerBuilder;

public class BattlePlayerScenarioBuilder {

	private static final String CATALOG_ID = "1507298083884";
	private static final int DEFAULT_LEVEL = 1;
	private static final String DEFUALT_AB_TAG = "";

	private BattlePlayer battlePlayer;

	public BattlePlayerScenarioBuilder(String battlePlayerId) {
		String name = "pepe";
		battlePlayer = new BattlePlayerBuilder() //
				.setUserId(battlePlayerId)//
				.setCatalogId(CATALOG_ID)//
				.setAbTag(DEFUALT_AB_TAG)//
				.setName(name)//
				.setLevel(DEFAULT_LEVEL)//
				.createBattlePlayer();
	}

	public BattlePlayer build() {
		return battlePlayer;
	}

}
