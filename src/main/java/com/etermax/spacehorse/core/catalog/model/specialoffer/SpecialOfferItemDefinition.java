package com.etermax.spacehorse.core.catalog.model.specialoffer;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class SpecialOfferItemDefinition extends CatalogEntry {

	private final String itemRewardTypeId; // si es chest, este es el ID del chestDefinition
	private final RewardType rewardType;
	private final int quantity;
	private final String specialOfferId;

	public SpecialOfferItemDefinition(String id, String itemRewardTypeId, RewardType rewardType, int quantity, String specialOfferId) {
		super(id);
		this.itemRewardTypeId = itemRewardTypeId;
		this.rewardType = rewardType;
		this.quantity = quantity;
		this.specialOfferId = specialOfferId;
	}

	@Override
	public void validate(Catalog catalog) {
		List<String> specialOfferIds = catalog.getSpecialOfferDefinitionsCollection().getEntries().stream().map(CatalogEntry::getId)
				.collect(Collectors.toList());
		if (!specialOfferIds.contains(getSpecialOfferId())) {
			throw new CatalogException("The refrence of special offer id in the special offer item [ " + getId() + " ] "
					+ " was not found");
		}
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

	public String getSpecialOfferId() {
		return specialOfferId;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
