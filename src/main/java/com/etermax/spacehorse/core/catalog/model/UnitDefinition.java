package com.etermax.spacehorse.core.catalog.model;

import java.util.List;

import com.etermax.spacehorse.core.catalog.resource.response.UnitDefinitionResponse;

public class UnitDefinition extends CatalogEntry {

	private final int unitType;

	private final int targetTeam;

	private final int layer;

	private final List<Integer> attackLayerTargets;

	private final String groupId;

	private final long size;

	private final long attackAnimationDuration;

	private final int attackType;

	private final int attackBehavior;

	private final List<Integer> attackTargets;

	private final List<String> attackGroupPriorities;

	private final int attackDamageType;

	private final String spawnPowerUpId;

	private final String attackPowerUpId;

	private final String projectilePowerUpId;

	private final String deathPowerUpId;

	public UnitDefinition(UnitDefinitionResponse unitDefinitionResponse) {
		super(unitDefinitionResponse.getId());
		this.groupId = unitDefinitionResponse.getGroupId();
		this.attackDamageType = unitDefinitionResponse.getAttackDamageType();
		this.attackType = unitDefinitionResponse.getAttackType();
		this.attackBehavior = unitDefinitionResponse.getAttackBehavior();
		this.attackTargets = unitDefinitionResponse.getAttackTargets();
		this.attackGroupPriorities = unitDefinitionResponse.getAttackGroupPriorities();
		this.size = unitDefinitionResponse.getSize().getRaw();
		this.attackAnimationDuration = unitDefinitionResponse.getAttackAnimationDuration().getRaw();
		this.unitType = unitDefinitionResponse.getUnitType();
		this.targetTeam = unitDefinitionResponse.getTargetTeam();
		this.layer = unitDefinitionResponse.getLayer();
		this.attackLayerTargets = unitDefinitionResponse.getAttackLayerTargets();
		this.spawnPowerUpId = unitDefinitionResponse.getSpawnPowerUpId();
		this.attackPowerUpId = unitDefinitionResponse.getAttackPowerUpId();
		this.projectilePowerUpId = unitDefinitionResponse.getProjectilePowerUpId();
		this.deathPowerUpId = unitDefinitionResponse.getDeathPowerUpId();
	}

	public String getGroupId() {
		return groupId;
	}

	public int getAttackDamageType() {
		return attackDamageType;
	}

	public int getAttackType() {
		return attackType;
	}

	public int getAttackBehavior() {
		return attackBehavior;
	}

	public List<Integer> getAttackTargets() {
		return attackTargets;
	}

	public List<String> getAttackGroupPriorities() {
		return attackGroupPriorities;
	}

	public long getSize() {
		return size;
	}

	public long getAttackAnimationDuration() {
		return attackAnimationDuration;
	}

	public int getUnitType() {
		return unitType;
	}

	public int getTargetTeam() {
		return targetTeam;
	}

	public int getLayer() {
		return layer;
	}

	public List<Integer> getAttackLayerTargets() {
		return attackLayerTargets;
	}

	public String getSpawnPowerUpId() {
		return spawnPowerUpId;
	}

	public String getAttackPowerUpId() {
		return attackPowerUpId;
	}

	public String getProjectilePowerUpId() {
		return projectilePowerUpId;
	}

	public String getDeathPowerUpId() {
		return deathPowerUpId;
	}

	@Override
	public void validate(Catalog catalog) {
	}

}