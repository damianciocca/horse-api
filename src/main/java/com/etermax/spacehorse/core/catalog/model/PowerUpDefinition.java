package com.etermax.spacehorse.core.catalog.model;

import java.util.List;

import com.etermax.spacehorse.core.catalog.resource.response.PowerUpDefinitionResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PowerUpDefinition extends CatalogEntry {

	private final int powerUpType;

	private final List<String> nestedPowerUpIds;

	private final boolean onlyOnce;

	private final long width;

	private final long radius;

	private final long boost;

	private final long speed;

	private final long attackDelay;

	private final int targetTeam;

	private final String trackObjectId;

	private final String unitId;

	private final String projectileId;

	private final int amount;

	public int getPowerUpType() {
		return powerUpType;
	}

	public List<String> getNestedPowerUpIds() {
		return nestedPowerUpIds;
	}

	public boolean getOnlyOnce() {
		return onlyOnce;
	}

	public long getWidth() {
		return width;
	}

	public long getRadius() {
		return radius;
	}

	public long getBoost() {
		return boost;
	}

	public long getSpeed() {
		return speed;
	}

	public long getAttackDelay() {
		return attackDelay;
	}

	public int getTargetTeam() {
		return targetTeam;
	}

	public String getTrackObjectId() {
		return trackObjectId;
	}

	public String getUnitId() {
		return unitId;
	}

	public String getProjectileId() {
		return projectileId;
	}

	public int getAmount() {
		return amount;
	}

	public PowerUpDefinition(PowerUpDefinitionResponse powerUpDefinitionResponse) {
		super(powerUpDefinitionResponse.getId());
		this.powerUpType = powerUpDefinitionResponse.getPowerUpType();
		this.nestedPowerUpIds = powerUpDefinitionResponse.getNestedPowerUpIds();
		this.onlyOnce = powerUpDefinitionResponse.getOnlyOnce();
		this.width = powerUpDefinitionResponse.getWidth().getRaw();
		this.radius = powerUpDefinitionResponse.getRadius().getRaw();
		this.boost = powerUpDefinitionResponse.getBoost().getRaw();
		this.speed = powerUpDefinitionResponse.getSpeed().getRaw();
		this.attackDelay = powerUpDefinitionResponse.getAttackDelay().getRaw();
		this.targetTeam = powerUpDefinitionResponse.getTargetTeam();
		this.trackObjectId = powerUpDefinitionResponse.getTrackObjectId();
		this.unitId = powerUpDefinitionResponse.getUnitId();
		this.projectileId = powerUpDefinitionResponse.getProjectileId();
		this.amount = powerUpDefinitionResponse.getAmount();
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(width >= 0, "width < 0");
		validateParameter(radius >= 0, "radius < 0");
		validateParameter(attackDelay >= 0, "attackDelay < 0");
	}

}
