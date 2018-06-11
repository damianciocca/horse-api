package com.etermax.spacehorse.core.achievements.action;

import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class ClaimAchievementRewardAction {

	private final AchievementCollectionRepository repository;
	private final PlayerRepository playerRepository;

	public ClaimAchievementRewardAction(AchievementCollectionRepository repository, PlayerRepository playerRepository) {
		this.repository = repository;
		this.playerRepository = playerRepository;
	}

	public void claimRewards(Player player, String achievementId, AchievementDefinition definition) {
		AchievementCollection achievementCollection = repository.findOrDefaultBy(player);
		achievementCollection.claimAchievementRewards(achievementId);

		updatePlayerRewards(player, definition);

		repository.addOrUpdate(player.getUserId(), achievementCollection);
		playerRepository.update(player);
	}

	private void updatePlayerRewards(Player player, AchievementDefinition definition) {
		player.getInventory().getGold().add(definition.getGoldReward());
		player.getInventory().getGems().add(definition.getGemsReward());
	}
}
