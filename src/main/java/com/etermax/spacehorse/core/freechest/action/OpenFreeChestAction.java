package com.etermax.spacehorse.core.freechest.action;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.freechest.exception.FreeChestNotReadyException;
import com.etermax.spacehorse.core.freechest.model.FreeChest;
import com.etermax.spacehorse.core.freechest.model.FreeChestConstants;
import com.etermax.spacehorse.core.freechest.resource.response.OpenFreeChestResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class OpenFreeChestAction {

	private final PlayerRepository playerRepository;
	private final CatalogRepository catalogRepository;
	private final GetRewardsDomainService getRewardsDomainService;
	private final ApplyRewardDomainService applyRewardDomainService;
	private final ServerTimeProvider serverTimeProvider;
	private final AchievementsFactory achievementsFactory;

	public OpenFreeChestAction(PlayerRepository playerRepository, CatalogRepository catalogRepository,
			GetRewardsDomainService getRewardsDomainService, ApplyRewardDomainService applyRewardDomainService, ServerTimeProvider serverTimeProvider,
			AchievementsFactory achievementsFactory) {
		this.playerRepository = playerRepository;
		this.catalogRepository = catalogRepository;
		this.getRewardsDomainService = getRewardsDomainService;
		this.applyRewardDomainService = applyRewardDomainService;
		this.serverTimeProvider = serverTimeProvider;
		this.achievementsFactory = achievementsFactory;
	}

	public OpenFreeChestResponse openFreeChest(String loginId) {
		Player player = playerRepository.find(loginId);
		Catalog catalog = catalogRepository.getActiveCatalogWithTag(player.getAbTag());
		return openFreeChest(player, catalog, new Date());
	}

	public OpenFreeChestResponse openFreeChest(Player player, Catalog catalog, Date now) {
		FreeChest freeChest = player.getFreeChest();

		openFreeChest(catalog.getGameConstants(), freeChest, now);

		int playerMapNumber = player.getMapNumber();

		ChestDefinition freeChestDefinition = catalog.getChestDefinitionsCollection().findByIdOrFail(catalog.getGameConstants().getFreeChestId());

		GetRewardConfiguration getRewardConfiguration = GetRewardConfiguration.createBy(catalog, serverTimeProvider);
		List<Reward> rewards = getRewardsDomainService.getRewards(player, freeChestDefinition, playerMapNumber, getRewardConfiguration);

		findFreeChestGemsReward(player, catalog).ifPresent(gemsReward -> addFreeChestGemsReward(rewards, gemsReward));

		List<RewardResponse> rewardsResponse = applyRewards(player, catalog, playerMapNumber, rewards);

		return new OpenFreeChestResponse(rewardsResponse, ServerTime.fromDate(freeChest.getLastChestOpeningDate()));
	}

	private void openFreeChest(FreeChestConstants constants, FreeChest freeChest, Date now) {
		if (!freeChest.canBeOpened(now, constants)) {
			throw new FreeChestNotReadyException("Can't open the free chest");
		}
		freeChest.open(now, constants);
	}

	private Optional<Reward> findFreeChestGemsReward(Player player, Catalog catalog) {
		int freeGemsAmount = player.getPlayerRewards().getNextFreeChestGemsRewardAmount(catalog);
		if (freeGemsAmount > 0) {
			return Optional.of(new Reward(RewardType.GEMS, freeGemsAmount));
		}
		return Optional.empty();
	}

	private void addFreeChestGemsReward(List<Reward> rewards, Reward gemsReward) {
		Optional<Reward> optGems = findExistingGemsReward(rewards);
		if (optGems.isPresent()) {
			optGems.get().addAmount(gemsReward.getAmount());
		} else {
			rewards.add(gemsReward);
		}
	}

	private Optional<Reward> findExistingGemsReward(List<Reward> rewards) {
		return rewards.stream().filter(reward -> reward.getRewardType().equals(RewardType.GEMS)).findFirst();
	}

	private List<RewardResponse> applyRewards(Player player, Catalog catalog, int playerMapNumber, List<Reward> rewards) {
		ApplyRewardConfiguration configuration = ApplyRewardConfiguration.createBy(catalog);
		List<AchievementsObserver> achievementsObservers = achievementsFactory.createForApplyRewards();

		List<RewardResponse> rewardsResponse = applyRewardDomainService
				.applyRewards(player, rewards, RewardContext.fromMapNumber(playerMapNumber), configuration, achievementsObservers);
		playerRepository.update(player);
		return rewardsResponse;
	}
}
