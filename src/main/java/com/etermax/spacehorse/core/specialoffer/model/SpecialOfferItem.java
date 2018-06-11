package com.etermax.spacehorse.core.specialoffer.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferItemDefinition;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class SpecialOfferItem {

	private final String itemRewardTypeId; // si es chest, este es el ID del chestDefinition
	private final RewardType rewardType;
	private final int quantity;

	public SpecialOfferItem(SpecialOfferItemDefinition itemDefinition) {
		this.itemRewardTypeId = itemDefinition.getItemRewardTypeId();
		this.rewardType = itemDefinition.getRewardType();
		this.quantity = itemDefinition.getQuantity();
	}

	public String getItemRewardTypeId() {
		return itemRewardTypeId;
	}

	public RewardType getRewardType() {
		return rewardType;
	}

	public int getQuantity() {
		return quantity;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
