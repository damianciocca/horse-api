package com.etermax.spacehorse.core.specialoffer.model;

import com.etermax.spacehorse.core.reward.model.PurchasableRewardItem;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class SpecialOfferPurchasableRewardItem implements PurchasableRewardItem {

	private final String rewardId;
	private final RewardType rewardType;
	private final int quantity;

	public SpecialOfferPurchasableRewardItem(SpecialOfferItem item) {
		this.rewardId = item.getItemRewardTypeId();
		this.rewardType = item.getRewardType();
		this.quantity = item.getQuantity();
	}

	@Override
	public String getRewardId() {
		return rewardId;
	}

	@Override
	public RewardType getRewardType() {
		return rewardType;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}
}
