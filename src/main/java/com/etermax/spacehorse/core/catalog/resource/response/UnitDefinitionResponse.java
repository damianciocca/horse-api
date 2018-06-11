package com.etermax.spacehorse.core.catalog.resource.response;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.catalog.model.UnitDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UnitDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("UnitType")
	private int unitType;

	@JsonProperty("TargetTeam")
	private int targetTeam;

	@JsonProperty("Layer")
	private int layer;

	@JsonProperty("AttackLayerTargets")
	private List<Integer> attackLayerTargets;

	@JsonProperty("GroupId")
	private String groupId;

	@JsonProperty("Size")
	private FintResponse size;

	@JsonProperty("AttackAnimationDuration")
	private FintResponse attackAnimationDuration;

	@JsonProperty("AttackType")
	private int attackType;

	@JsonProperty("AttackBehavior")
	private int attackBehavior;

	@JsonProperty("AttackTargets")
	private List<Integer> attackTargets = new ArrayList<>();

	@JsonProperty("AttackGroupPriorities")
	private List<String> attackGroupPriorities = new ArrayList<>();

	@JsonProperty("AttackDamageType")
	private int attackDamageType;

	@JsonProperty("SpawnPowerUpId")
	private String spawnPowerUpId;

	@JsonProperty("AttackPowerUpId")
	private String attackPowerUpId;

	@JsonProperty("ProjectilePowerUpId")
	private String projectilePowerUpId;

	@JsonProperty("DeathPowerUpId")
	private String deathPowerUpId;

	public UnitDefinitionResponse() {
	}

	public UnitDefinitionResponse(UnitDefinition unitDefinition) {
		this.id = unitDefinition.getId();
		this.groupId = unitDefinition.getGroupId();
		this.attackDamageType = unitDefinition.getAttackDamageType();
		this.attackType = unitDefinition.getAttackType();
		this.attackBehavior = unitDefinition.getAttackBehavior();
		this.attackTargets = unitDefinition.getAttackTargets();
		this.attackGroupPriorities = unitDefinition.getAttackGroupPriorities();
		this.size = new FintResponse(unitDefinition.getSize());
		this.attackAnimationDuration = new FintResponse(unitDefinition.getAttackAnimationDuration());
		this.unitType = unitDefinition.getUnitType();
		this.targetTeam = unitDefinition.getTargetTeam();
		this.layer = unitDefinition.getLayer();
		this.attackLayerTargets = unitDefinition.getAttackLayerTargets();
		this.spawnPowerUpId = unitDefinition.getSpawnPowerUpId();
		this.attackPowerUpId = unitDefinition.getAttackPowerUpId();
		this.projectilePowerUpId = unitDefinition.getProjectilePowerUpId();
		this.deathPowerUpId = unitDefinition.getDeathPowerUpId();
	}

	public String getId() {
		return id;
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

	public FintResponse getSize() {
		return size;
	}

	public FintResponse getAttackAnimationDuration() {
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
}
