package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.PlayerLevelResponse;

public class PlayerLevel extends CatalogEntry {

	private final int level;

	private final int xp;

	public int getLevel() {
		return level;
	}

	public int getXp() {
		return xp;
	}

	public PlayerLevel(PlayerLevelResponse playerLevelResponse) {
		super(playerLevelResponse.getId());
		this.level = playerLevelResponse.getLevel();
		this.xp = playerLevelResponse.getXp();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(level >= 0, "level < 0");
		validateParameter(xp >= 0, "xp < 0");
	}

}
