package com.etermax.spacehorse.core.specialoffer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.InAppDefinition;
import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinitionSelector;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.shop.model.InAppReceiptCreationDomainService;
import com.etermax.spacehorse.core.shop.model.ApplyPendingRewardsTemplate;

public class SpecialOffersApplyPendingRewards extends ApplyPendingRewardsTemplate {

	private final BuySpecialOfferDomainService buySpecialOfferDomainService;
	private final ServerTimeProvider serverTimeProvider;

	public SpecialOffersApplyPendingRewards(InAppReceiptCreationDomainService inAppReceiptCreationDomainService,
			BuySpecialOfferDomainService buySpecialOfferDomainService, ServerTimeProvider serverTimeProvider) {
		super(inAppReceiptCreationDomainService);
		this.buySpecialOfferDomainService = buySpecialOfferDomainService;
		this.serverTimeProvider = serverTimeProvider;
	}

	@Override
	protected List<InAppDefinition> mapInAppDefinitions(Catalog catalog) {
		return new ArrayList<>(catalog.getSpecialOfferInAppDefinitionsCollection().getEntries());
	}

	@Override
	protected List<RewardResponse> applyPendingRewards(Player player, MarketType marketType, Catalog catalog, String productId) {
		Optional<SpecialOfferDefinition> specialOfferDefinition = SpecialOfferDefinitionSelector
				.selectFromInAppProductId(catalog, productId, marketType);

		GetRewardConfiguration getRewardConfiguration = GetRewardConfiguration.createBy(catalog, serverTimeProvider);
		ApplyRewardConfiguration applyRewardConfiguration = ApplyRewardConfiguration.createBy(catalog);
		CatalogEntriesCollection<ChestDefinition> chestDefinitions = catalog.getChestDefinitionsCollection();

		return specialOfferDefinition.map(specialOffer -> buySpecialOfferDomainService
				.buy(player, specialOffer.getId(), getRewardConfiguration, applyRewardConfiguration, chestDefinitions)).orElse(new ArrayList<>());
	}
}
