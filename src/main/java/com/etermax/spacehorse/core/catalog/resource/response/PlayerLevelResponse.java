package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.PlayerLevel;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerLevelResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("Level")
	private int level;

	@JsonProperty("XP")
	private int xp;

	public String getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getXp() {
		return xp;
	}

	public PlayerLevelResponse() {
	}

	public PlayerLevelResponse(PlayerLevel playerLevel) {
		this.id = playerLevel.getId();
		this.level = playerLevel.getLevel();
		this.xp = playerLevel.getXp();
	}

}
