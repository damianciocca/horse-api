package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChestDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("Duration")
	private int duration;

	@JsonProperty("TutorialOnly")
	private boolean tutorialOnly;

	@JsonProperty("IgnoreCardsPerMap")
	private boolean ignoreCardsPerMap;

	@JsonProperty("SpeedUpFree")
	private boolean speedUpFree;

	public boolean isTutorialOnly() {
		return tutorialOnly;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}

	public boolean getTutorialOnly() {
		return tutorialOnly;
	}

	public boolean getIgnoreCardsPerMap() {
		return ignoreCardsPerMap;
	}

	public ChestDefinitionResponse() {
	}

	public ChestDefinitionResponse(ChestDefinition chest) {
		this.id = chest.getId();
		this.name = chest.getName();
		this.duration = chest.getDuration();
		this.tutorialOnly = chest.getTutorialOnly();
		this.ignoreCardsPerMap = chest.getIgnoreCardsPerMap();
		this.speedUpFree = chest.isSpeedUpFree();
	}

	public boolean isSpeedUpFree() {
		return speedUpFree;
	}
}
