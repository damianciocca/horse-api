package com.etermax.spacehorse.core.quest.model;

import java.util.List;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class QuestRewardAssigner {

	private final GetRewardsDomainService getRewardsDomainService;
	private final PlayerRepository playerRepository;
	private final ApplyRewardDomainService applyRewardDomainService;
	private final ServerTimeProvider serverTimeProvider;
	private final AchievementsFactory achievementsFactory;

	public QuestRewardAssigner(PlayerRepository playerRepository, ApplyRewardDomainService applyRewardDomainService,
			GetRewardsDomainService getRewardsDomainService, ServerTimeProvider serverTimeProvider,
			AchievementsFactory achievementsFactory) {
		this.getRewardsDomainService = getRewardsDomainService;
		this.playerRepository = playerRepository;
		this.applyRewardDomainService = applyRewardDomainService;
		this.serverTimeProvider = serverTimeProvider;
		this.achievementsFactory = achievementsFactory;
	}

	public List<RewardResponse> applyRewardsToPlayer(Player player, Catalog catalog, QuestDefinition questDefinition) {
		int mapNumber = player.getMapNumber();
		List<Reward> rewards = collectRewards(player, mapNumber, catalog, questDefinition);
		List<RewardResponse> rewardResponses = applyRewardsToPlayer(player, catalog, mapNumber, rewards);
		playerRepository.update(player);
		return rewardResponses;
	}

	private List<Reward> collectRewards(Player player, int mapNumber, Catalog catalog, QuestDefinition questDefinition) {
		ChestDefinition chestDefinition = catalog.getChestDefinitionsCollection().findByIdOrFail(questDefinition.getChestId());
		GetRewardConfiguration configuration = GetRewardConfiguration.createBy(catalog, serverTimeProvider);
		return getRewardsDomainService.getRewards(player, chestDefinition, mapNumber, configuration);
	}

	private List<RewardResponse> applyRewardsToPlayer(Player player, Catalog catalog, int mapNumber, List<Reward> rewards) {
		ApplyRewardConfiguration configuration = ApplyRewardConfiguration.createBy(catalog);
		List<AchievementsObserver> achievementsObservers = achievementsFactory.createForApplyRewards();

		return applyRewardDomainService.applyRewards(player, rewards, RewardContext.fromMapNumber(mapNumber), configuration, achievementsObservers);
	}
}
