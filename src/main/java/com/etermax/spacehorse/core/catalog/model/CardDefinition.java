package com.etermax.spacehorse.core.catalog.model;

import java.util.Optional;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

public class CardDefinition extends CatalogEntry {

	private final int energyCost;

	private final int cardAction;

	private final CardRarity cardRarity;

	private final CardArchetype cardArchetype;

	private final int cardTarget;

	private final long cardTargetRadius;

	private final int targetTeam;

	private final long castTime;

	private final String unitId;

	private final String powerUpId;

	private final boolean enabled;

	private final int availableFromMapId;

	private DateTime activationTime;

	public CardDefinition(String id, int energyCost, int cardAction, CardRarity cardRarity, CardArchetype cardArchetype, int cardTarget,
			long cardTargetRadius, int targetTeam, long castTime, String unitId, String powerUpId, boolean enabled, int availableFromMapId,
			DateTime activationTime) {
		super(id);
		this.energyCost = energyCost;
		this.cardAction = cardAction;
		this.cardRarity = cardRarity;
		this.cardArchetype = cardArchetype;
		this.cardTarget = cardTarget;
		this.cardTargetRadius = cardTargetRadius;
		this.targetTeam = targetTeam;
		this.castTime = castTime;
		this.unitId = unitId;
		this.powerUpId = powerUpId;
		this.enabled = enabled;
		this.availableFromMapId = availableFromMapId;
		this.activationTime = activationTime;
	}

	public CardDefinition(String id) {
		this(id, CardArchetype.FIGHTER);
	}

	public CardDefinition(String id, CardArchetype cardArchetype) {
		super(id);
		this.cardArchetype = cardArchetype;
		this.cardRarity = CardRarity.COMMON;
		this.enabled = true;
		this.availableFromMapId = 0;
		this.energyCost = 0;
		this.cardAction = 0;
		this.cardTarget = 0;
		this.cardTargetRadius = 0;
		this.targetTeam = 0;
		this.castTime = 0;
		this.unitId = "";
		this.powerUpId = "";
		this.activationTime = null;
	}

	public CardDefinition(String id, CardRarity cardRarity, int availableFromMapId) {
		this(id, cardRarity, availableFromMapId, 0);
	}

	public CardDefinition(String id, CardRarity cardRarity, int availableFromMapId, int energyCost) {
		super(id);
		this.cardRarity = cardRarity;
		this.enabled = true;
		this.availableFromMapId = availableFromMapId;
		this.energyCost = energyCost;
		this.cardAction = 0;
		this.cardArchetype = CardArchetype.FIGHTER;
		this.cardTarget = 0;
		this.cardTargetRadius = 0;
		this.targetTeam = 0;
		this.castTime = 0;
		this.unitId = "";
		this.powerUpId = "";
		this.activationTime = null;
	}

	public int getEnergyCost() {
		return energyCost;
	}

	public int getCardAction() {
		return cardAction;
	}

	public int getCardTarget() {
		return cardTarget;
	}

	public long getCardTargetRadius() {
		return cardTargetRadius;
	}

	public int getTargetTeam() {
		return targetTeam;
	}

	public String getUnitId() {
		return unitId;
	}

	public String getPowerUpId() {
		return powerUpId;
	}

	public long getCastTime() {
		return castTime;
	}

	public CardRarity getCardRarity() {
		return cardRarity;
	}

	public CardArchetype getCardArchetype() {
		return cardArchetype;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public int getAvailableFromMapId() {
		return availableFromMapId;
	}

	public Optional<DateTime> getActivationTime() {
		return Optional.ofNullable(activationTime);
	}

	public boolean isActiveFor(DateTime dateTime) {
		return getActivationTime().map(activationTime -> {
			if (activationTime.isBefore(dateTime)) {
				return true;
			}
			return false;
		}).orElse(true);
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(energyCost > 0, "energyCost <= 0");
		validateParameter(castTime >= 0, "castTime < 0");
		validateParameter(getUnitId() == null || getUnitId().isEmpty() || catalog.getUnitDefinitionsCollection().findById(getUnitId()).isPresent(),
				"Invalid UnitId %s", getUnitId());
		validateParameter(
				getPowerUpId() == null || getPowerUpId().isEmpty() || catalog.getPowerUpDefinitionsCollection().findById(getPowerUpId()).isPresent(),
				"Invalid PowerUpId %s", getPowerUpId());
		validateParameter(!enabled || availableFromMapId >= 0, "availableFromMapId < 0 or null when card enabled");
		validateParameter(
				!enabled || enabled && (getUnitId() == null || getUnitId().isEmpty()) || enabled && getUnitId() != null && !getUnitId().isEmpty()
						&& getNumberOfExistingUnitLevels(catalog) - getFirstLevelDefinitionUnitLevel(catalog) >= getMaxCardLevelByRarity(catalog),
				String.format("The card expects %d unit levels, but the unit %s has %d levels", getMaxCardLevelByRarity(catalog), getUnitId(),
						getNumberOfExistingUnitLevels(catalog)));
	}

	public int getMaxCardLevelByRarity(Catalog catalog) {
		return catalog.getCardParameterLevelsCollection().getMaxCardLevelByRarity(cardRarity);
	}

	public int getFirstLevelDefinitionUnitLevel(Catalog catalog) {
		return catalog.getCardLevelDefinitionsCollection().getEntries().stream().filter(x -> x.getCardId().equals(getId())).findFirst()
				.map(x -> x.getUnitsLevel()).orElse(0);
	}

	private int getNumberOfExistingUnitLevels(Catalog catalog) {
		return catalog.getUnitLevelDefinitionsCollection().getEntries().stream().filter(x -> x.getUnitId().equals(getUnitId()))
				.collect(Collectors.toList()).size();
	}
}