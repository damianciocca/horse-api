package com.etermax.spacehorse.core.player.action.chest;

import java.util.Date;
import java.util.List;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class FinishOpeningChestAction {

	private final PlayerRepository playerRepository;
	private final CatalogRepository catalogRepository;
	private final ServerTimeProvider serverTimeProvider;
	private final GetRewardsDomainService getRewardsDomainService;
	private final ApplyRewardDomainService applyRewardDomainService;
	private final AchievementsFactory achievementsFactory;

	public FinishOpeningChestAction(PlayerRepository playerRepository, CatalogRepository catalogRepository, ServerTimeProvider serverTimeProvider,
			GetRewardsDomainService getRewardsDomainService, ApplyRewardDomainService applyRewardDomainService,
			AchievementsFactory achievementsFactory) {
		this.playerRepository = playerRepository;
		this.catalogRepository = catalogRepository;
		this.serverTimeProvider = serverTimeProvider;
		this.getRewardsDomainService = getRewardsDomainService;
		this.applyRewardDomainService = applyRewardDomainService;
		this.achievementsFactory = achievementsFactory;
	}

	public List<RewardResponse> finish(String playerId, Long chestId) {
		Player player = playerRepository.find(playerId);
		Catalog catalog = catalogRepository.getActiveCatalogWithTag(player.getAbTag());
		Chest chest = getChest(chestId, player);
		finishOpeningChest(player, chest);
		List<Reward> rewards = getRewards(catalog, player, chest);
		List<RewardResponse> rewardResponses = applyRewards(catalog, player, chest, rewards);
		playerRepository.update(player);
		return rewardResponses;
	}

	private void finishOpeningChest(Player player, Chest chest) {
		final Date now = getServerDate();
		if (!chest.canFinishOpening(now)) {
			throw new ApiException("Can't finish the chest");
		}
		chest.finishOpening(now);
		removeChest(player, chest);
	}

	private List<Reward> getRewards(Catalog catalog, Player player, Chest chest) {
		ChestDefinition chestDefinition = catalog.getChestDefinitionsCollection().findByIdOrFail(chest.getChestType());
		GetRewardConfiguration configuration = GetRewardConfiguration.createBy(catalog, serverTimeProvider);
		return getRewardsDomainService.getRewards(player, chestDefinition, chest.getMapNumber(), configuration);
	}

	private List<RewardResponse> applyRewards(Catalog catalog, Player player, Chest chest, List<Reward> rewards) {
		ApplyRewardConfiguration configuration = ApplyRewardConfiguration.createBy(catalog);

		List<AchievementsObserver> achievementsObservers = achievementsFactory.createForApplyRewards();
		return applyRewardDomainService.applyRewards(player, rewards, RewardContext.fromChest(chest), configuration, achievementsObservers);
	}

	private Chest getChest(Long chestId, Player player) {
		return player.getInventory().getChests().findChestById(chestId).orElseThrow(() -> new ApiException("Unknown chest"));
	}

	private Date getServerDate() {
		return serverTimeProvider.getDate();
	}

	private void removeChest(Player player, Chest chest) {
		player.getInventory().getChests().getChests().remove(chest);
	}

}
