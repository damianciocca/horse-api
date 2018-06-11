package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.PowerUpDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PowerUpDefinitionResponse {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("PowerUpType")
    private int powerUpType;

    @JsonProperty("NestedPowerUpIds")
    private List<String> nestedPowerUpIds = new ArrayList<>();

    @JsonProperty("OnlyOnce")
    private boolean onlyOnce;

    @JsonProperty("Width")
    private FintResponse width;

    @JsonProperty("Radius")
    private FintResponse radius;

    @JsonProperty("Boost")
    private FintResponse boost;

    @JsonProperty("Speed")
    private FintResponse speed;

    @JsonProperty("AttackDelay")
    private FintResponse attackDelay;

    @JsonProperty("TargetTeam")
    private int targetTeam;

    @JsonProperty("TrackObjectId")
    private String trackObjectId;

    @JsonProperty("UnitId")
    private String unitId;

    @JsonProperty("ProjectileId")
    private String projectileId;

    @JsonProperty("Amount")
    private int amount;

    public String getId() {
        return id;
    }

    public int getPowerUpType() {
        return powerUpType;
    }

    public List<String> getNestedPowerUpIds() {
        return nestedPowerUpIds;
    }

    public boolean getOnlyOnce() {
        return onlyOnce;
    }

    public FintResponse getWidth() {
        return width;
    }

    public FintResponse getRadius() {
        return radius;
    }

    public FintResponse getBoost() {
        return boost;
    }

    public FintResponse getSpeed() {
        return speed;
    }

    public FintResponse getAttackDelay() {
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

    public PowerUpDefinitionResponse() {
    }

    public PowerUpDefinitionResponse(PowerUpDefinition powerUpDefinition) {
        this.id = powerUpDefinition.getId();
        this.powerUpType = powerUpDefinition.getPowerUpType();
        this.nestedPowerUpIds = powerUpDefinition.getNestedPowerUpIds();
        this.onlyOnce = powerUpDefinition.getOnlyOnce();
        this.width = new FintResponse(powerUpDefinition.getWidth());
        this.radius = new FintResponse(powerUpDefinition.getRadius());
        this.boost = new FintResponse(powerUpDefinition.getBoost());
        this.speed = new FintResponse(powerUpDefinition.getSpeed());
        this.attackDelay = new FintResponse(powerUpDefinition.getAttackDelay());
        this.targetTeam = powerUpDefinition.getTargetTeam();
        this.trackObjectId = powerUpDefinition.getTrackObjectId();
        this.unitId = powerUpDefinition.getUnitId();
        this.projectileId = powerUpDefinition.getProjectileId();
        this.amount = powerUpDefinition.getAmount();
    }
}