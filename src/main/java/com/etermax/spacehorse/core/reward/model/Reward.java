package com.etermax.spacehorse.core.reward.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.common.exception.ApiException;

public class Reward {

	private final RewardType rewardType;
	private final String rewardId;
	private int amount;

	public RewardType getRewardType() {
		return rewardType;
	}

	public String getRewardId() {
		return rewardId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void addAmount(int amount) {
		this.amount += amount;
	}

	public Reward(Reward reward) {
		this.rewardType = reward.rewardType;
		this.rewardId = reward.rewardId;
		this.amount = reward.amount;
	}

	public Reward(RewardType rewardType, int amount) {
		this.rewardType = rewardType;
		this.rewardId = "";
		this.amount = amount;
	}

	public Reward(RewardType rewardType, String rewardId, int amount) {
		this.rewardType = rewardType;
		this.rewardId = rewardId;
		this.amount = amount;
	}

	public Reward(RewardType rewardType, String rewardId) {
		this.rewardType = rewardType;
		this.rewardId = rewardId;
		this.amount = 1;
	}

	public boolean isValid(CatalogEntriesCollection<ChestDefinition> chestDefinitionCatalog,
			CatalogEntriesCollection<CardDefinition> cardDefinitionCatalog) {
		switch (rewardType) {
			case GEMS:
				return amount > 0;
			case GOLD:
				return amount > 0;
			case CHEST:
				return amount > 0 && rewardId != null && chestDefinitionCatalog.findById(rewardId).isPresent();
			case CARD_PARTS:
				return amount > 0 && rewardId != null && cardDefinitionCatalog.findById(rewardId).isPresent();
			case NEXT_CHEST:
				return amount > 0;
			default:
				throw new ApiException("Unknown rewardType " + rewardType);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Reward reward = (Reward) o;
		return new EqualsBuilder().append(rewardType, reward.rewardType).append(rewardId, reward.rewardId).append(amount, reward.amount).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(rewardType).append(rewardId).append(amount).toHashCode();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
