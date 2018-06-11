package com.etermax.spacehorse.core.achievements.model.observers.base;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.model.AchievementsDefinitionsSelector;
import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.player.model.Player;

public abstract class BaseAchievementObserver implements AchievementsObserver {

	private static final Logger logger = LoggerFactory.getLogger(BaseAchievementObserver.class);

	private final CompleteAchievementAction completeAchievementAction;
	private final AchievementsDefinitionsSelector achievementsDefinitionsSelector;

	public BaseAchievementObserver(CompleteAchievementAction completeAchievementAction) {
		this.completeAchievementAction = completeAchievementAction;
		achievementsDefinitionsSelector = new AchievementsDefinitionsSelector();
	}

	public abstract AchievementType getAchievementType();

	public abstract boolean achievementIsReached(Player player);

	public abstract String getErrorMsg();

	@Override
	public void update(Player player, List<AchievementDefinition> definitions) {
		try {
			List<AchievementDefinition> definitionsGroupedByType = achievementsDefinitionsSelector.selectByType(definitions, getAchievementType());
			definitionsGroupedByType.forEach(achievementDefinition -> updateIfAchievementIsCompleted(player, achievementDefinition));
		} catch (Exception e) {
			logger.error(getErrorMsg(), e);
		}
	}

	private void updateIfAchievementIsCompleted(Player player, AchievementDefinition achievementDefinition) {
		if (achievementIsReached(player)) {
			completeAchievementAction.complete(player, achievementDefinition.getId());
		}
	}
}
