package com.etermax.spacehorse.core.catalog.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.resource.response.ChestDefinitionResponse;

public class ChestDefinition extends CatalogEntry {

	private final String name;

	private final int duration;

	private final boolean tutorialOnly;

	private final boolean ignoreCardsPerMap;

	private final boolean speedUpFree;

	public boolean isSpeedUpFree() {
		return speedUpFree;
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

	public ChestDefinition(ChestDefinitionResponse dto) {
		super(dto.getId());
		this.name = dto.getName();
		this.duration = dto.getDuration();
		this.tutorialOnly = dto.getTutorialOnly();
		this.ignoreCardsPerMap = dto.getIgnoreCardsPerMap();
		this.speedUpFree = dto.isSpeedUpFree();
	}

	public ChestDefinition(String id, String name, int duration, boolean tutorialOnly, boolean ignoreCardsPerMap, boolean speedUpFree) {
		super(id);
		this.name = name;
		this.duration = duration;
		this.tutorialOnly = tutorialOnly;
		this.ignoreCardsPerMap = ignoreCardsPerMap;
		this.speedUpFree = speedUpFree;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(duration >= 0, "duration < 0");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
