package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.UnitLevelDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitLevelDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("UnitId")
	private String unitId;

	@JsonProperty("Level")
	private int level;

	@JsonProperty("Health")
	private FintResponse health;

	@JsonProperty("MoveSpeed")
	private FintResponse moveSpeed;

	@JsonProperty("AttackSplashDamageRadius")
	private FintResponse attackSplashDamageRadius;

	@JsonProperty("AttackDamage")
	private FintResponse attackDamage;

	@JsonProperty("MainShipsAttackDamage")
	private FintResponse mainShipsAttackDamage = new FintResponse(0);

	@JsonProperty("AttackDelay")
	private FintResponse attackDelay;

	@JsonProperty("AttackRange")
	private FintResponse attackRange;

	@JsonProperty("ViewRange")
	private FintResponse viewRange;

	@JsonProperty("Lifetime")
	private FintResponse lifetime;

	@JsonProperty("ProjectileSpeed")
	private FintResponse projectileSpeed;

	public UnitLevelDefinitionResponse() {
	}

	public UnitLevelDefinitionResponse(UnitLevelDefinition unitLevelDefinition) {
		this.id = unitLevelDefinition.getId();
		this.unitId = unitLevelDefinition.getUnitId();
		this.level = unitLevelDefinition.getLevel();
		this.health = new FintResponse(unitLevelDefinition.getHealth());
		this.moveSpeed = new FintResponse(unitLevelDefinition.getMoveSpeed());
		this.attackSplashDamageRadius = new FintResponse(unitLevelDefinition.getAttackSplashDamageRadius());
		this.attackDamage = new FintResponse(unitLevelDefinition.getAttackDamage());
		this.mainShipsAttackDamage = new FintResponse(unitLevelDefinition.getMainShipsAttackDamage());
		this.attackDelay = new FintResponse(unitLevelDefinition.getAttackDelay());
		this.attackRange = new FintResponse(unitLevelDefinition.getAttackRange());
		this.viewRange = new FintResponse(unitLevelDefinition.getViewRange());
		this.projectileSpeed = new FintResponse(unitLevelDefinition.getProjectileSpeed());
		this.lifetime = new FintResponse(unitLevelDefinition.getLifetime());
	}

	public String getId() {
		return id;
	}

	public String getUnitId() {
		return unitId;
	}

	public int getLevel() {
		return level;
	}

	public FintResponse getHealth() {
		return health;
	}

	public FintResponse getMoveSpeed() {
		return moveSpeed;
	}

	public FintResponse getAttackSplashDamageRadius() {
		return attackSplashDamageRadius;
	}

	public FintResponse getAttackDamage() {
		return attackDamage;
	}

	public FintResponse getMainShipsAttackDamage() {
		return mainShipsAttackDamage;
	}

	public FintResponse getAttackDelay() {
		return attackDelay;
	}

	public FintResponse getAttackRange() {
		return attackRange;
	}

	public FintResponse getViewRange() {
		return viewRange;
	}

	public FintResponse getProjectileSpeed() {
		return projectileSpeed;
	}

	public FintResponse getLifetime() {
		return lifetime;
	}
}
