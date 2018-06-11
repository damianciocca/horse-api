package com.etermax.spacehorse.core.specialoffer.action;

import static com.google.common.collect.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.InAppDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.PurchasableRewardCollector;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.shop.model.InAppPlayerMoneySpentDomainService;
import com.etermax.spacehorse.core.shop.model.InAppPurchaseReceipt;
import com.etermax.spacehorse.core.shop.model.InAppReceiptCreationDomainService;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferPurchasableRewardItem;
import com.etermax.spacehorse.core.user.model.User;

public class BuyInAppSpecialOfferAction {

	private final PlayerRepository playerRepository;
	private final SpecialOfferBoardRepository specialOfferBoardRepository;
	private final ApplyRewardDomainService applyRewardDomainService;
	private final InAppReceiptCreationDomainService inAppReceiptCreationDomainService;
	private final PurchasableRewardCollector rewardCollector;
	private final ServerTimeProvider serverTimeProvider;
	private final AchievementsFactory achievementsFactory;

	public BuyInAppSpecialOfferAction(PlayerRepository playerRepository, SpecialOfferBoardRepository specialOfferBoardRepository,
			ApplyRewardDomainService applyRewardDomainService, PurchasableRewardCollector rewardCollector,
			InAppReceiptCreationDomainService inAppReceiptCreationDomainService, ServerTimeProvider serverTimeProvider,
			AchievementsFactory achievementsFactory) {
		this.playerRepository = playerRepository;
		this.specialOfferBoardRepository = specialOfferBoardRepository;
		this.applyRewardDomainService = applyRewardDomainService;
		this.rewardCollector = rewardCollector;
		this.inAppReceiptCreationDomainService = inAppReceiptCreationDomainService;
		this.serverTimeProvider = serverTimeProvider;
		this.achievementsFactory = achievementsFactory;
	}

	public List<RewardResponse> buy(User user, Player player, String specialOfferId, Catalog catalog, Object receipt) {

		SpecialOfferBoard specialOfferBoard = specialOfferBoardRepository.findBy(player);
		SpecialOffer specialOffer = specialOfferBoard.get(specialOfferId);

		List<InAppDefinition> inAppDefinitions = mapInAppDefinitions(catalog);
		InAppPurchaseReceipt inAppPurchaseReceipt = inAppReceiptCreationDomainService
				.createReceipt(user, inAppDefinitions, specialOffer.getShopItemIdInApp(), receipt);

		InAppPlayerMoneySpentDomainService.accumulate(player, specialOffer.getShopItemIdInApp(), inAppDefinitions);

		List<RewardResponse> rewards = applyRewards(player, catalog, specialOffer);

		specialOfferBoard.consume(specialOffer.getId());

		playerRepository.update(player);
		specialOfferBoardRepository.addOrUpdate(player, specialOfferBoard);
		inAppReceiptCreationDomainService.persistReceipt(inAppPurchaseReceipt);

		return rewards;
	}

	private List<InAppDefinition> mapInAppDefinitions(Catalog catalog) {
		return new ArrayList<>(catalog.getSpecialOfferInAppDefinitionsCollection().getEntries());
	}

	private List<RewardResponse> applyRewards(Player player, Catalog catalog, SpecialOffer specialOffer) {
		RewardContext rewardContext = buildRewardContext(player, specialOffer);

		List<Reward> rewards = newArrayList();

		specialOffer.getItems().forEach(specialOfferItem -> rewards.addAll(rewardCollector
				.collect(player, catalog.getChestDefinitionsCollection(), GetRewardConfiguration.createBy(catalog, serverTimeProvider),
						new SpecialOfferPurchasableRewardItem(specialOfferItem), rewardContext)));

		List<AchievementsObserver> achievementsObservers = achievementsFactory.createForApplyRewards();
		return applyRewardDomainService
				.applyRewards(player, rewards, rewardContext, ApplyRewardConfiguration.createBy(catalog), achievementsObservers);
	}

	private RewardContext buildRewardContext(Player player, SpecialOffer specialOffer) {
		if (specialOffer.hasFixedMapNumber()) {
			return RewardContext.fromMapNumber(specialOffer.getFixedMapNumber());
		}
		return RewardContext.fromMapNumber(player.getMapNumber());
	}
}
