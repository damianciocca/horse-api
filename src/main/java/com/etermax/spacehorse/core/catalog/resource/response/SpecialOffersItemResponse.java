package com.etermax.spacehorse.core.catalog.resource.response;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferItemDefinition;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialOffersItemResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("ItemRewardTypeId")
	private String itemRewardTypeId;

	@JsonProperty("RewardType")
	private String rewardType;

	@JsonProperty("Quantity")
	private int quantity;

	@JsonProperty("SpecialOfferId")
	private String specialOfferId;

	public SpecialOffersItemResponse() {
	}

	public SpecialOffersItemResponse(SpecialOfferItemDefinition definition) {
		this.id = definition.getId();
		this.itemRewardTypeId = definition.getItemRewardTypeId();
		this.rewardType = definition.getRewardType().toString();
		this.quantity = definition.getQuantity();
		this.specialOfferId = definition.getSpecialOfferId();
	}

	public String getId() {
		return id;
	}

	public String getItemRewardTypeId() {
		return itemRewardTypeId;
	}

	public String getRewardType() {
		return rewardType;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getSpecialOfferId() {
		return specialOfferId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
