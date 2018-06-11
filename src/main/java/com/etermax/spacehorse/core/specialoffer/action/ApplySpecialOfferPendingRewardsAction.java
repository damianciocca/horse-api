package com.etermax.spacehorse.core.specialoffer.action;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.inapps.resource.response.InAppsItemResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.shop.model.ApplyPendingRewardsTemplate;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOffersApplyPendingRewards;
import com.etermax.spacehorse.core.user.model.User;

public class ApplySpecialOfferPendingRewardsAction {

	private final ApplyPendingRewardsTemplate applyPendingRewards;

	public ApplySpecialOfferPendingRewardsAction(SpecialOffersApplyPendingRewards applyPendingRewards) {
		this.applyPendingRewards = applyPendingRewards;
	}

	public List<InAppsItemResponse> applyPendingReceipts(Player player, User user, Catalog catalog, List<Object> receipts) {
		return applyPendingRewards.applyPendingReceipts(player, user, catalog, receipts);
	}
}
