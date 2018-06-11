package com.etermax.spacehorse.core.catalog.model.unlock;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;

public class FeaturesByPlayerLvlDefinition extends CatalogEntry {

	private final int availableOnLevel;
	private final FeatureByPlayerLvlType type;
	private final int courierMaxSlots;
	private final String questMaxDifficulty;
	private final int deckMaxSlots;

	public FeaturesByPlayerLvlDefinition(String id, int availableOnLevel, FeatureByPlayerLvlType type, int courierMaxSlots, String questMaxDifficulty,
			int deckMaxSlots) {
		super(id);
		this.availableOnLevel = availableOnLevel;
		this.type = type;
		this.courierMaxSlots = courierMaxSlots;
		this.questMaxDifficulty = questMaxDifficulty;
		this.deckMaxSlots = deckMaxSlots;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(availableOnLevel >= 0, "availableOnLevel should not be less than 0");
		if (FeatureByPlayerLvlType.COURIER_SLOT.equals(type)) {
			validateParameter(courierMaxSlots > 0, "courierMaxSlots should not be less than 1");
		}
		if (FeatureByPlayerLvlType.QUEST_SLOT.equals(type)) {
			validateParameter(StringUtils.isNotBlank(questMaxDifficulty), "questMaxDifficulty should not be blank");
		}
		if (FeatureByPlayerLvlType.DECK_SLOT.equals(type)) {
			validateParameter(deckMaxSlots > 0, "deckMaxSlots should not be less than 1");
		}
	}

	public int getAvailableOnLevel() {
		return availableOnLevel;
	}

	public String getFeatureType() {
		return type.getId();
	}

	public int getCourierMaxSlots() {
		return courierMaxSlots;
	}

	public String getQuestMaxDifficulty() {
		return questMaxDifficulty;
	}

	public int getDeckMaxSlots() {
		return deckMaxSlots;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
