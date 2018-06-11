package com.etermax.spacehorse.core.achievements.model.observers.types;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.player.model.Player;

public interface AchievementsObserver {

	void update(Player player, List<AchievementDefinition> definitions);
}
