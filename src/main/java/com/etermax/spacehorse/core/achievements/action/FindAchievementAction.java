package com.etermax.spacehorse.core.achievements.action;

import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.player.model.Player;

public class FindAchievementAction {

	private final AchievementCollectionRepository repository;

	public FindAchievementAction(AchievementCollectionRepository repository) {
		this.repository = repository;
	}

	public Achievement findBy(Player player, String achievementId) {
		AchievementCollection achievementCollection = repository.findOrDefaultBy(player);
		return achievementCollection.getAchievementById(achievementId);
	}
}
