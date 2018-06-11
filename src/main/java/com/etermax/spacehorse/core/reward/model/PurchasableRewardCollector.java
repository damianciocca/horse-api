package com.etermax.spacehorse.core.reward.model;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.strategies.gems.FixedGemsRewardStrategy;
import com.etermax.spacehorse.core.reward.model.strategies.gold.FixedGoldRewardStrategy;
import com.google.common.collect.Lists;

public class PurchasableRewardCollector {

	private final GetRewardsDomainService getRewardsDomainService;

	public PurchasableRewardCollector(GetRewardsDomainService getRewardsDomainService) {
		this.getRewardsDomainService = getRewardsDomainService;
	}

	public List<Reward> collect(Player player, CatalogEntriesCollection<ChestDefinition> chestDefinitions, GetRewardConfiguration configuration,
			PurchasableRewardItem purchasableRewardItem, RewardContext rewardContext) {

		List<Reward> rewards = Lists.newArrayList();

		if (purchasableRewardItem.getRewardType() == RewardType.GEMS) {
			rewards.addAll(new FixedGemsRewardStrategy(purchasableRewardItem.getQuantity()).getRewards());
		}
		if (purchasableRewardItem.getRewardType() == RewardType.GOLD) {
			rewards.addAll(new FixedGoldRewardStrategy(purchasableRewardItem.getQuantity()).getRewards());
		}
		if (purchasableRewardItem.getRewardType() == RewardType.CHEST) {
			rewards.addAll(getRewardsDomainService
					.getRewards(player, getChestDefinition(chestDefinitions, purchasableRewardItem.getRewardId()), rewardContext.getMapNumber(),
							configuration));
		}

		return rewards;
	}

	private ChestDefinition getChestDefinition(CatalogEntriesCollection<ChestDefinition> chestDefinitions, String id) {
		return chestDefinitions.findByIdOrFail(id);
	}

}
