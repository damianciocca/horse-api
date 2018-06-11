package com.etermax.spacehorse.core.achievements.model.observers.types;

import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.player.model.Player;

/**
 * This interface should be use if the achievement needs to be executed in login process due to some retro-compatibility with old users or users
 * that have already reached the achievements.
 */
public interface AchievementsInLoginObserver extends AchievementsObserver {

	void update(Player player, Achievement achievement);
}
