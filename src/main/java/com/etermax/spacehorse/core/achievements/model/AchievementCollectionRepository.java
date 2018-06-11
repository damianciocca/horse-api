package com.etermax.spacehorse.core.achievements.model;

import com.etermax.spacehorse.core.player.model.Player;

public interface AchievementCollectionRepository {

	AchievementCollection findOrDefaultBy(Player userId);

	void addOrUpdate(String userId, AchievementCollection achievementCollection);
}
