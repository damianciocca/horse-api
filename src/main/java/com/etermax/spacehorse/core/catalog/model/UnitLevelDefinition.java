package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.UnitLevelDefinitionResponse;

public class UnitLevelDefinition extends CatalogEntry {

	private final String unitId;

	private final int level;

	private final long health;

	private final long moveSpeed;

	private final long attackSplashDamageRadius;

	private final long attackDamage;

	private final long mainShipsAttackDamage;

	private final long attackDelay;

	private final long attackRange;

	private final long viewRange;

	private final long lifetime;

	private final long projectileSpeed;

	public UnitLevelDefinition(UnitLevelDefinitionResponse unitLevelDefinitionResponse) {
		super(unitLevelDefinitionResponse.getId());
		this.unitId = unitLevelDefinitionResponse.getUnitId();
		this.level = unitLevelDefinitionResponse.getLevel();
		this.health = unitLevelDefinitionResponse.getHealth().getRaw();
		this.moveSpeed = unitLevelDefinitionResponse.getMoveSpeed().getRaw();
		this.attackSplashDamageRadius = unitLevelDefinitionResponse.getAttackSplashDamageRadius().getRaw();
		this.attackDamage = unitLevelDefinitionResponse.getAttackDamage().getRaw();
		this.mainShipsAttackDamage = unitLevelDefinitionResponse.getMainShipsAttackDamage().getRaw();
		this.attackDelay = unitLevelDefinitionResponse.getAttackDelay().getRaw();
		this.attackRange = unitLevelDefinitionResponse.getAttackRange().getRaw();
		this.viewRange = unitLevelDefinitionResponse.getViewRange().getRaw();
		this.projectileSpeed = unitLevelDefinitionResponse.getProjectileSpeed().getRaw();
		this.lifetime = unitLevelDefinitionResponse.getLifetime().getRaw();
	}

	public String getUnitId() {
		return unitId;
	}

	public int getLevel() {
		return level;
	}

	public long getHealth() {
		return health;
	}

	public long getMoveSpeed() {
		return moveSpeed;
	}

	public long getAttackSplashDamageRadius() {
		return attackSplashDamageRadius;
	}

	public long getAttackDamage() {
		return attackDamage;
	}

	public long getMainShipsAttackDamage() {
		return mainShipsAttackDamage;
	}

	public long getAttackDelay() {
		return attackDelay;
	}

	public long getAttackRange() {
		return attackRange;
	}

	public long getViewRange() {
		return viewRange;
	}

	public long getProjectileSpeed() {
		return projectileSpeed;
	}

	public long getLifetime() {
		return lifetime;
	}

	@Override
	public void validate(Catalog catalog) {
	}

}