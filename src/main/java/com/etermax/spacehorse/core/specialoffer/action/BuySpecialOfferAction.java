package com.etermax.spacehorse.core.specialoffer.action;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.model.BuySpecialOfferDomainService;

public class BuySpecialOfferAction {

	private final BuySpecialOfferDomainService buySpecialOfferDomainService;
	private final ServerTimeProvider serverTimeProvider;

	public BuySpecialOfferAction(BuySpecialOfferDomainService buySpecialOfferDomainService, ServerTimeProvider serverTimeProvider) {
		this.buySpecialOfferDomainService = buySpecialOfferDomainService;
		this.serverTimeProvider = serverTimeProvider;
	}

	public List<RewardResponse> buy(Player player, String specialOfferId, Catalog catalog) {
		GetRewardConfiguration getRewardConfiguration = GetRewardConfiguration.createBy(catalog, serverTimeProvider);
		ApplyRewardConfiguration applyRewardConfiguration = ApplyRewardConfiguration.createBy(catalog);
		CatalogEntriesCollection<ChestDefinition> chestDefinitions = catalog.getChestDefinitionsCollection();
		return buySpecialOfferDomainService.buy(player, specialOfferId, getRewardConfiguration, applyRewardConfiguration, chestDefinitions);
	}
}
