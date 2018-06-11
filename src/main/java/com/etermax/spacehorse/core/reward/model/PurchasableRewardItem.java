package com.etermax.spacehorse.core.reward.model;

public interface PurchasableRewardItem {

	String getRewardId();

	RewardType getRewardType();

	int getQuantity();
}
