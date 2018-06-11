package com.etermax.spacehorse.core.achievements.model.observers.base;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.action.FindAchievementAction;
import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.model.AchievementsDefinitionsSelector;
import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsInLoginObserver;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.player.model.Player;

public abstract class BaseAchievementByGoalAmountObserver implements AchievementsInLoginObserver {

	private static final Logger logger = LoggerFactory.getLogger(BaseAchievementByGoalAmountObserver.class);

	private final FindAchievementAction findAchievementAction;
	private final CompleteAchievementAction completeAchievementAction;
	private final AchievementsDefinitionsSelector achievementsDefinitionsSelector;

	public BaseAchievementByGoalAmountObserver(FindAchievementAction findAchievementAction, CompleteAchievementAction completeAchievementAction) {
		this.findAchievementAction = findAchievementAction;
		this.completeAchievementAction = completeAchievementAction;
		this.achievementsDefinitionsSelector = new AchievementsDefinitionsSelector();
	}

	public abstract AchievementType getAchievementType();

	public abstract boolean achievementIsReached(Player player, Achievement achievement);

	public abstract String getErrorMsg();

	@Override
	public void update(Player player, List<AchievementDefinition> definitions) {
		try {
			List<AchievementDefinition> definitionsGroupedByType = filterDefinitionsByType(definitions);
			definitionsGroupedByType.forEach(achievementDefinition -> {
				Achievement achievement = findAchievementAction.findBy(player, achievementDefinition.getId());
				updatedIfAchievementIsReached(player, achievement);
			});
		} catch (Exception e) {
			logger.error(getErrorMsg(), e);
		}
	}

	@Override
	public void update(Player player, Achievement achievement) {
		try {
			updatedIfAchievementIsReached(player, achievement);
		} catch (Exception e) {
			logger.error(getErrorMsg(), e);
		}
	}

	public List<AchievementDefinition> filterDefinitionsByType(List<AchievementDefinition> definitions) {
		return achievementsDefinitionsSelector.selectByType(definitions, getAchievementType());
	}

	private void updatedIfAchievementIsReached(Player player, Achievement achievement) {
		if (achievementIsReached(player, achievement) && achievement.isNotAccomplished()) {
			completeAchievementAction.complete(player, achievement.getAchievementId());
		}
	}

}
