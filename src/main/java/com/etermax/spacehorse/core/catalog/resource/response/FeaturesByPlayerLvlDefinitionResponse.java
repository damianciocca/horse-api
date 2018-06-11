package com.etermax.spacehorse.core.catalog.resource.response;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FeaturesByPlayerLvlDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("AvailableOnLevel")
	private int availableOnLevel;

	@JsonProperty("FeatureType")
	private String featureType;

	@JsonProperty("CourierMaxSlots")
	private String courierMaxSlots;

	@JsonProperty("QuestMaxDifficulty")
	private String questMaxDifficulty;

	@JsonProperty("DeckMaxSlots")
	private String deckMaxSlots;

	public FeaturesByPlayerLvlDefinitionResponse() {
	}

	public FeaturesByPlayerLvlDefinitionResponse(String id, int availableOnLevel, String featureType, int courierMaxSlots, String questMaxDifficulty,
			int deckMaxSlots) {
		this.id = id;
		this.availableOnLevel = availableOnLevel;
		this.featureType = featureType;
		this.courierMaxSlots = String.valueOf(courierMaxSlots);
		this.questMaxDifficulty = questMaxDifficulty;
		this.deckMaxSlots = String.valueOf(deckMaxSlots);
	}

	public String getId() {
		return id;
	}

	public int getAvailableOnLevel() {
		return availableOnLevel;
	}

	public String getFeatureType() {
		return featureType;
	}

	public int getCourierMaxSlots() {
		return isNotBlank(courierMaxSlots) ? Integer.valueOf(courierMaxSlots) : 0;
	}

	public String getQuestMaxDifficulty() {
		return questMaxDifficulty;
	}

	public int getDeckMaxSlots() {
		return isNotBlank(deckMaxSlots) ? Integer.valueOf(deckMaxSlots) : 0;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
