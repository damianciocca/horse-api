package com.etermax.spacehorse.core.player.action.chest;

import java.util.Date;
import java.util.List;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.achievements.model.observers.SpeedupChestCompletedAchievementObserver;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.player.resource.response.SpeedupOpeningChestResponse;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class SpeedupOpeningChestAction {

	private final PlayerRepository playerRepository;
	private final CatalogRepository catalogRepository;
	private final ServerTimeProvider serverTimeProvider;
	private final GetRewardsDomainService getRewardsDomainService;
	private final ApplyRewardDomainService applyRewardDomainService;
	private final AchievementsFactory achievementsFactory;
	private final CompleteAchievementAction completeAchievementAction;

	public SpeedupOpeningChestAction(PlayerRepository playerRepository, CatalogRepository catalogRepository, ServerTimeProvider serverTimeProvider,
			GetRewardsDomainService getRewardsDomainService, ApplyRewardDomainService applyRewardDomainService,
			AchievementsFactory achievementsFactory, CompleteAchievementAction completeAchievementAction) {
		this.playerRepository = playerRepository;
		this.catalogRepository = catalogRepository;
		this.serverTimeProvider = serverTimeProvider;
		this.getRewardsDomainService = getRewardsDomainService;
		this.applyRewardDomainService = applyRewardDomainService;
		this.achievementsFactory = achievementsFactory;
		this.completeAchievementAction = completeAchievementAction;
	}

	public SpeedupOpeningChestResponse speedup(String playerId, Long chestId) {
		Player player = playerRepository.find(playerId);
		Catalog catalog = catalogRepository.getActiveCatalogWithTag(player.getAbTag());
		Chest chest = getChest(chestId, player);
		int costInGems = forceOpeningChest(catalog, player, chest);
		List<Reward> rewards = getRewards(catalog, player, chest);
		List<RewardResponse> rewardsResponse = applyRewards(catalog, player, chest, rewards);

		AchievementsObserver achievementObserver = createAchievementsObserver(chest, catalog);
		achievementObserver.update(player, catalog.getAchievementsDefinitionsCollection().getEntries());

		return new SpeedupOpeningChestResponse(rewardsResponse, costInGems);
	}

	private Chest getChest(Long chestId, Player player) {
		return player.getInventory().getChests().findChestById(chestId).orElseThrow(() -> new ApiException("Unknown chest"));
	}

	private int forceOpeningChest(Catalog catalog, Player player, Chest chest) {
		final Date now = getServerDate();
		if (!chest.canOpen(now)) {
			throw new ApiException("Can't speedup the chest");
		}
		final int costInGems = chest.getForceOpeningCostInGems(now, catalog.getGameConstants(), catalog.getChestDefinitionsCollection());
		if (costInGems > player.getInventory().getGems().getAmount()) {
			throw new ApiException("Not enough gems");
		}
		chest.forceOpening(now);
		player.getInventory().getGems().remove(costInGems);
		removeChest(player, chest);
		return costInGems;
	}

	private List<Reward> getRewards(Catalog catalog, Player player, Chest chest) {
		ChestDefinition chestDefinition = catalog.getChestDefinitionsCollection().findByIdOrFail(chest.getChestType());
		GetRewardConfiguration configuration = GetRewardConfiguration.createBy(catalog, serverTimeProvider);
		return getRewardsDomainService.getRewards(player, chestDefinition, chest.getMapNumber(), configuration);
	}

	private List<RewardResponse> applyRewards(Catalog catalog, Player player, Chest chest, List<Reward> rewards) {
		ApplyRewardConfiguration configuration = ApplyRewardConfiguration.createBy(catalog);

		List<AchievementsObserver> achievementsObservers = achievementsFactory.createForApplyRewards();
		List<RewardResponse> rewardsResponse = applyRewardDomainService
				.applyRewards(player, rewards, RewardContext.fromChest(chest), configuration, achievementsObservers);

		playerRepository.update(player);
		return rewardsResponse;
	}

	private Date getServerDate() {
		return serverTimeProvider.getDate();
	}

	private void removeChest(Player player, Chest chest) {
		player.getInventory().getChests().getChests().remove(chest);
	}

	private AchievementsObserver createAchievementsObserver(Chest chest, Catalog catalog) {
		return new SpeedupChestCompletedAchievementObserver(completeAchievementAction, catalog.getChestDefinitionsCollection(), chest);
	}
}
