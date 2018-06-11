package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.PowerUpLevelDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PowerUpLevelDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("PowerUpId")
	private String powerUpId;

	@JsonProperty("Level")
	private int level;

	@JsonProperty("TargetLevel")
	private int targetLevel;

	@JsonProperty("Duration")
	private FintResponse duration;

	@JsonProperty("Damage")
	private FintResponse damage;

	@JsonProperty("MainShipsDamage")
	private FintResponse mainShipsDamage = new FintResponse(0);

	@JsonProperty("Health")
	private FintResponse health;

	public String getId() {
		return id;
	}

	public String getPowerUpId() {
		return powerUpId;
	}

	public int getLevel() {
		return level;
	}

	public int getTargetLevel() {
		return targetLevel;
	}

	public FintResponse getDuration() {
		return duration;
	}

	public FintResponse getDamage() {
		return damage;
	}

	public FintResponse getMainShipsDamage() {
		return mainShipsDamage;
	}

	public FintResponse getHealth() {
		return health;
	}

	public PowerUpLevelDefinitionResponse() {
	}

	public PowerUpLevelDefinitionResponse(PowerUpLevelDefinition powerUpLevelDefinition) {
		this.id = powerUpLevelDefinition.getId();
		this.powerUpId = powerUpLevelDefinition.getPowerUpId();
		this.level = powerUpLevelDefinition.getLevel();
		this.targetLevel = powerUpLevelDefinition.getTargetLevel();
		this.duration = new FintResponse(powerUpLevelDefinition.getDuration());
		this.damage = new FintResponse(powerUpLevelDefinition.getDamage());
		this.mainShipsDamage = new FintResponse(powerUpLevelDefinition.getMainShipsDamage());
		this.health = new FintResponse(powerUpLevelDefinition.getHealth());
	}
}