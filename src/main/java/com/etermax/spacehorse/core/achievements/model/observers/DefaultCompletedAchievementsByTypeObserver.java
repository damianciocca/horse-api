package com.etermax.spacehorse.core.achievements.model.observers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.model.AchievementsDefinitionsSelector;
import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.player.model.Player;

public class DefaultCompletedAchievementsByTypeObserver implements AchievementsObserver {

	private static final Logger logger = LoggerFactory.getLogger(DefaultCompletedAchievementsByTypeObserver.class);

	private final CompleteAchievementAction completeAchievementAction;
	private final AchievementType achievementType;
	private final AchievementsDefinitionsSelector achievementsDefinitionsSelector;

	public DefaultCompletedAchievementsByTypeObserver(CompleteAchievementAction completeAchievementAction, AchievementType achievementType) {
		this.completeAchievementAction = completeAchievementAction;
		this.achievementType = achievementType;
		this.achievementsDefinitionsSelector = new AchievementsDefinitionsSelector();
	}

	public void update(Player player, List<AchievementDefinition> definitions) {
		try {
			List<AchievementDefinition> definitionsGroupedByType = achievementsDefinitionsSelector.selectByType(definitions, achievementType);
			definitionsGroupedByType.forEach(achievementDefinition -> completeAchievementAction.complete(player, achievementDefinition.getId()));
		} catch (Exception e) {
			logger.error("====> Unexpected error when trying to update default achievements", e);
		}
	}
}
