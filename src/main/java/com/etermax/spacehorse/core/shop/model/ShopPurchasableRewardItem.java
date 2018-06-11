package com.etermax.spacehorse.core.shop.model;

import java.util.Map;

import com.etermax.spacehorse.core.catalog.model.ShopItemDefinition;
import com.etermax.spacehorse.core.catalog.model.ShopItemType;
import com.etermax.spacehorse.core.reward.model.PurchasableRewardItem;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.google.common.collect.Maps;

public class ShopPurchasableRewardItem implements PurchasableRewardItem {

	private final String rewardId;
	private final int quantity;
	private final RewardType rewardType;

	private Map<ShopItemType, RewardType> rewards = Maps.newHashMap();
	{
		rewards.put(ShopItemType.GOLD, RewardType.GOLD);
		rewards.put(ShopItemType.CHEST, RewardType.CHEST);
		rewards.put(ShopItemType.GEMS, RewardType.GEMS);
	}

	public ShopPurchasableRewardItem(ShopItemDefinition item, int itemQuantity) {
		this.rewardId = item.getItemId();
		this.quantity = itemQuantity;
		this.rewardType = rewards.get(item.getItemType());
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