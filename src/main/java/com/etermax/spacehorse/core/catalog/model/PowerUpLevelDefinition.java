package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.PowerUpLevelDefinitionResponse;

public class PowerUpLevelDefinition extends CatalogEntry {

	private final String powerUpId;

	private final int level;

	private final int targetLevel;

	private final long duration;

	private final long damage;

	private final long mainShipsDamage;

	private final long health;

	public String getPowerUpId() {
		return powerUpId;
	}

	public int getLevel() {
		return level;
	}

	public int getTargetLevel() {
		return targetLevel;
	}

	public long getDuration() {
		return duration;
	}

	public long getDamage() {
		return damage;
	}

	public long getMainShipsDamage() {
		return mainShipsDamage;
	}

	public long getHealth() {
		return health;
	}

	public PowerUpLevelDefinition(PowerUpLevelDefinitionResponse powerUpLevelDefinitionResponse) {
		super(powerUpLevelDefinitionResponse.getId());
		this.powerUpId = powerUpLevelDefinitionResponse.getPowerUpId();
		this.level = powerUpLevelDefinitionResponse.getLevel();
		this.targetLevel = powerUpLevelDefinitionResponse.getTargetLevel();
		this.duration = powerUpLevelDefinitionResponse.getDuration().getRaw();
		this.damage = powerUpLevelDefinitionResponse.getDamage().getRaw();
		this.mainShipsDamage = powerUpLevelDefinitionResponse.getMainShipsDamage().getRaw();
		this.health = powerUpLevelDefinitionResponse.getHealth().getRaw();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(level >= 0, "level < 0");
		validateParameter(targetLevel >= 0, "targetLevel < 0");
		validateParameter(duration >= 0, "duration < 0");
		validateParameter(damage >= 0, "damage < 0");
		validateParameter(mainShipsDamage >= 0, "damage < 0");
		validateParameter(health >= 0, "health < 0");
	}

}
