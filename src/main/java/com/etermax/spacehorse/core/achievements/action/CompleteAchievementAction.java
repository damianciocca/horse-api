package com.etermax.spacehorse.core.achievements.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.player.model.Player;

public class CompleteAchievementAction {

	private static final Logger logger = LoggerFactory.getLogger(CompleteAchievementAction.class);

	private final AchievementCollectionRepository repository;

	public CompleteAchievementAction(AchievementCollectionRepository repository) {
		this.repository = repository;
	}

	public void complete(Player player, String achievementId) {
		AchievementCollection achievementCollection = repository.findOrDefaultBy(player);
		if (achievementIsNotAccomplished(achievementCollection, achievementId)) {
			achievementCollection.achievementAccomplished(achievementId);
			repository.addOrUpdate(player.getUserId(), achievementCollection);
			logger.info("======================> achievement completed! ID [ " + achievementId + " ]");
		}
	}

	private boolean achievementIsNotAccomplished(AchievementCollection achievementCollection, String achievementId) {
		Achievement achievement = achievementCollection.getAchievementById(achievementId);
		return achievement.isNotAccomplished();
	}
}
