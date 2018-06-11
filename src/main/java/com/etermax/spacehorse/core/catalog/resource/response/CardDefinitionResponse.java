package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("EnergyCost")
	private int energyCost;

	@JsonProperty("CardAction")
	private int cardAction;

	@JsonProperty("CardRarity")
	public int cardRarity;

	@JsonProperty("CardArchetype")
	public int cardArchetype = 0; //TODO: Borrar despues de incorporar este parametro en los catalogs de test

	@JsonProperty("CardTarget")
	private int cardTarget;

	@JsonProperty("CardTargetRadius")
	private FintResponse cardTargetRadius;

	@JsonProperty("TargetTeam")
	private int targetTeam;

	@JsonProperty("CastTime")
	private FintResponse castTime;

	@JsonProperty("UnitId")
	private String unitId;

	@JsonProperty("PowerUpId")
	private String powerUpId;

	@JsonProperty("Enabled")
	private boolean enabled;

	@JsonProperty("AvailableFromMapId")
	private int availableFromMapId;

	@JsonProperty("ActivationTime")
	private String activationTime;

	public String getId() {
		return id;
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

	public FintResponse getCardTargetRadius() {
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

	public FintResponse getCastTime() {
		return castTime;
	}

	public int getCardRarity() {
		return cardRarity;
	}

	public int getCardArchetype() {
		return cardArchetype;
	}

	public boolean getEnabled() {
		return enabled;
	}

	public int getAvailableFromMapId() {
		return availableFromMapId;
	}

	public CardDefinitionResponse() {
	}

	public String getActivationTime() {
		return activationTime;
	}

	public CardDefinitionResponse(CardDefinition cardDefinition) {
		this.id = cardDefinition.getId();
		this.energyCost = cardDefinition.getEnergyCost();
		this.cardAction = cardDefinition.getCardAction();
		this.cardTarget = cardDefinition.getCardTarget();
		this.cardTargetRadius = new FintResponse(cardDefinition.getCardTargetRadius());
		this.targetTeam = cardDefinition.getTargetTeam();
		this.unitId = cardDefinition.getUnitId();
		this.powerUpId = cardDefinition.getPowerUpId();
		this.castTime = new FintResponse(cardDefinition.getCastTime());
		this.cardRarity = CardRarityResponse.fromCardRarityEnum(cardDefinition.getCardRarity());
		this.cardArchetype = CardArchetypeResponse.fromCardArchetypeEnum(cardDefinition.getCardArchetype());
		this.enabled = cardDefinition.getEnabled();
		this.availableFromMapId = cardDefinition.getAvailableFromMapId();
		this.activationTime = cardDefinition.getActivationTime().toString();
	}

	public CardDefinitionResponse(String id, int energyCost, int cardAction, int cardRarity, int cardArchetype, int cardTarget,
			FintResponse cardTargetRadius, int targetTeam, FintResponse castTime, String unitId, String powerUpId, boolean enabled,
			int availableFromMapId, String activationTime) {
		this.id = id;
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
}
