package com.etermax.spacehorse.core.shop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.InAppDefinition;
import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.catalog.model.ShopInAppDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.shop.action.ShopBuyItemAction;

public class ShopItemApplyPendingsRewards extends ApplyPendingRewardsTemplate {

	private final ShopBuyItemAction shopBuyItemAction;

	public ShopItemApplyPendingsRewards(InAppReceiptCreationDomainService inAppReceiptCreationDomainService, ShopBuyItemAction shopBuyItemAction) {
		super(inAppReceiptCreationDomainService);
		this.shopBuyItemAction = shopBuyItemAction;
	}

	@Override
	protected List<InAppDefinition> mapInAppDefinitions(Catalog catalog) {
		return new ArrayList<>(catalog.getShopInAppCollection().getEntries());
	}

	@Override
	protected List<RewardResponse> applyPendingRewards(Player player, MarketType marketType, Catalog catalog, String productId) {
		Optional<ShopInAppDefinition> optionalInApp = catalog.getShopInAppCollection().findByProductIdAndMarketType(productId, marketType);
		return optionalInApp.map(inapp -> shopBuyItemAction.buy(player, catalog, inapp.getItemId(), true)).orElse(new ArrayList<>());
	}
}
