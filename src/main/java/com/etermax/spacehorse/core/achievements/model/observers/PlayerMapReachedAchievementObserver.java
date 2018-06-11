package com.etermax.spacehorse.core.achievements.model.observers;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.action.FindAchievementAction;
import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.model.observers.base.BaseAchievementByGoalAmountObserver;
import com.etermax.spacehorse.core.player.model.Player;

public class PlayerMapReachedAchievementObserver extends BaseAchievementByGoalAmountObserver {

	public PlayerMapReachedAchievementObserver(FindAchievementAction findAchievementAction, CompleteAchievementAction completeAchievementAction) {
		super(findAchievementAction, completeAchievementAction);
	}

	@Override
	public AchievementType getAchievementType() {
		return AchievementType.REACH_STADIUM;
	}

	@Override
	public boolean achievementIsReached(Player player, Achievement achievement) {
		return player.getMapNumber() >= achievement.getGoalAmount();
	}

	@Override
	public String getErrorMsg() {
		return "====> Unexpected error when trying to update achievements for player map achievement type";
	}
}
