package com.etermax.spacehorse.core.specialoffer.model;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
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

public class BuySpecialOfferDomainService {

	private final PlayerRepository playerRepository;
	private final SpecialOfferBoardRepository specialOfferBoardRepository;
	private final ApplyRewardDomainService applyRewardDomainService;
	private final PurchasableRewardCollector rewardCollector;
	private final AchievementsFactory achievementsFactory;

	public BuySpecialOfferDomainService(SpecialOfferBoardRepository specialOfferBoardRepository, PlayerRepository playerRepository,
			ApplyRewardDomainService applyRewardDomainService, PurchasableRewardCollector rewardCollector,
			AchievementsFactory achievementsFactory) {
		this.specialOfferBoardRepository = specialOfferBoardRepository;
		this.playerRepository = playerRepository;
		this.applyRewardDomainService = applyRewardDomainService;
		this.rewardCollector = rewardCollector;
		this.achievementsFactory = achievementsFactory;
	}

	public List<RewardResponse> buy(Player player, String specialOfferId, GetRewardConfiguration getRewardConfiguration,
			ApplyRewardConfiguration applyRewardConfiguration, CatalogEntriesCollection<ChestDefinition> chestDefinitionsCollection) {

		SpecialOfferBoard specialOfferBoard = specialOfferBoardRepository.findBy(player);
		SpecialOffer specialOffer = specialOfferBoard.get(specialOfferId);

		PaymentMethodUtil.payWith(player, specialOffer.getGemsPrice(), specialOffer.getGoldPrice());

		List<RewardResponse> rewards = applyRewards(player, specialOffer, getRewardConfiguration, applyRewardConfiguration,
				chestDefinitionsCollection);

		specialOfferBoard.consume(specialOffer.getId());

		playerRepository.update(player);
		specialOfferBoardRepository.addOrUpdate(player, specialOfferBoard);

		return rewards;
	}

	private List<RewardResponse> applyRewards(Player player, SpecialOffer specialOffer, GetRewardConfiguration getRewardConfiguration,
			ApplyRewardConfiguration applyRewardConfiguration, CatalogEntriesCollection<ChestDefinition> chestDefinitionsCollection) {
		RewardContext rewardContext = buildRewardContext(player, specialOffer);

		List<Reward> rewards = newArrayList();

		specialOffer.getItems().forEach(specialOfferItem -> rewards.addAll(rewardCollector
				.collect(player, chestDefinitionsCollection, getRewardConfiguration, new SpecialOfferPurchasableRewardItem(specialOfferItem),
						rewardContext)));

		List<AchievementsObserver> achievementsObservers = achievementsFactory.createForApplyRewards();
		return applyRewardDomainService.applyRewards(player, rewards, rewardContext, applyRewardConfiguration, achievementsObservers);
	}

	private RewardContext buildRewardContext(Player player, SpecialOffer specialOffer) {
		if (specialOffer.hasFixedMapNumber())
			return RewardContext.fromMapNumber(specialOffer.getFixedMapNumber());
		return RewardContext.fromMapNumber(player.getMapNumber());
	}

}
