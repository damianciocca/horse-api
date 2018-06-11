package com.etermax.spacehorse.core.shop.action;

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ShopItemDefinition;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.PaymentMethodUtil;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.PurchasableRewardCollector;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.shop.model.ShopPurchasableRewardItem;

public class ShopBuyItemAction {

	private final PlayerRepository playerRepository;
	private final ApplyRewardDomainService applyRewardDomainService;
	private final PurchasableRewardCollector rewardCollector;
	private final ServerTimeProvider serverTimeProvider;
	private final AchievementsFactory achievementsFactory;

	public ShopBuyItemAction(PlayerRepository playerRepository, ApplyRewardDomainService applyRewardDomainService,
			PurchasableRewardCollector rewardCollector, ServerTimeProvider serverTimeProvider,
			AchievementsFactory achievementsFactory) {
		this.playerRepository = playerRepository;
		this.applyRewardDomainService = applyRewardDomainService;
		this.rewardCollector = rewardCollector;
		this.serverTimeProvider = serverTimeProvider;
		this.achievementsFactory = achievementsFactory;
	}

	public List<RewardResponse> buy(Player player, Catalog catalog, String shopItemId) {
		return buy(player, catalog, shopItemId, true);
	}

	public List<RewardResponse> buy(Player player, Catalog catalog, String shopItemId, boolean canBeInApp) {
		Validate.notNull(player);
		Validate.notNull(catalog);
		Validate.notNull(shopItemId);
		CatalogEntriesCollection<ShopItemDefinition> shopItemCollection = catalog.getShopItemsCollection();
		ShopItemDefinition shopItem = shopItemCollection.findById(shopItemId)
				.orElseThrow(() -> new ApiException("Invalid shop item id " + shopItemId));
		if (!canBeInApp && shopItem.getInAppPurchase()) {
			throw new ApiException("Received shop item id is an in-app item " + shopItemId);
		}
		return this.buyShopItem(shopItem, catalog, player);
	}

	private List<RewardResponse> buyShopItem(ShopItemDefinition shopItem, Catalog catalog, Player player) {
		int mapNumber = player.getMapNumber();

		PaymentMethodUtil.payWithGems(player, shopItem.getItemGemPrice(mapNumber));

		List<Reward> rewards = rewardCollector
				.collect(player, catalog.getChestDefinitionsCollection(), GetRewardConfiguration.createBy(catalog, serverTimeProvider),
						new ShopPurchasableRewardItem(shopItem, shopItem.getItemQuantity(mapNumber)), RewardContext.fromMapNumber(mapNumber));

		return applyRewards(catalog, player, rewards);
	}

	private List<RewardResponse> applyRewards(Catalog catalog, Player player, List<Reward> rewards) {
		ApplyRewardConfiguration configuration = ApplyRewardConfiguration.createBy(catalog);

		List<AchievementsObserver> achievementObservers = achievementsFactory.createForApplyRewards();
		List<RewardResponse> rewardResponses = applyRewardDomainService
				.applyRewards(player, rewards, RewardContext.empty(), configuration, achievementObservers);

		playerRepository.update(player);
		return rewardResponses;
	}
}
