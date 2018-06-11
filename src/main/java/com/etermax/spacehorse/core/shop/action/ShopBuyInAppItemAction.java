package com.etermax.spacehorse.core.shop.action;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.InAppDefinition;
import com.etermax.spacehorse.core.inapps.resource.response.InAppsItemResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.shop.model.ApplyPendingRewardsTemplate;
import com.etermax.spacehorse.core.shop.model.InAppPlayerMoneySpentDomainService;
import com.etermax.spacehorse.core.shop.model.InAppPurchaseReceipt;
import com.etermax.spacehorse.core.shop.model.InAppReceiptCreationDomainService;
import com.etermax.spacehorse.core.shop.model.ShopItemApplyPendingsRewards;
import com.etermax.spacehorse.core.user.model.User;

public class ShopBuyInAppItemAction {

	private final InAppReceiptCreationDomainService inAppReceiptCreationDomainService;
	private final ShopBuyItemAction shopBuyItemAction;
	private final ApplyPendingRewardsTemplate applyPendingRewards;

	public ShopBuyInAppItemAction(ShopBuyItemAction shopBuyItemAction, InAppReceiptCreationDomainService inAppReceiptCreationDomainService) {
		this.shopBuyItemAction = shopBuyItemAction;
		this.inAppReceiptCreationDomainService = inAppReceiptCreationDomainService;
		this.applyPendingRewards = new ShopItemApplyPendingsRewards(inAppReceiptCreationDomainService, shopBuyItemAction);
	}

	public List<RewardResponse> buyInApp(Player player, User user, Catalog catalog, String shopItemId, Object receipt) {
		List<InAppDefinition> inAppDefinitions = mapInAppDefinitions(catalog);
		InAppPurchaseReceipt inAppPurchaseReceipt = inAppReceiptCreationDomainService.createReceipt(user, inAppDefinitions, shopItemId, receipt);
		InAppPlayerMoneySpentDomainService.accumulate(player, shopItemId, inAppDefinitions);
		List<RewardResponse> rewards = shopBuyItemAction.buy(player, catalog, shopItemId, true);
		inAppReceiptCreationDomainService.persistReceipt(inAppPurchaseReceipt);
		return rewards;
	}

	public List<InAppsItemResponse> applyPendingReceipts(Player player, User user, Catalog catalog, List<Object> receipts) {
		return applyPendingRewards.applyPendingReceipts(player, user, catalog, receipts);
	}

	private List<InAppDefinition> mapInAppDefinitions(Catalog catalog) {
		return new ArrayList<>(catalog.getShopInAppCollection().getEntries());
	}
}
