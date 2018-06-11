package com.etermax.spacehorse.core.achievements.model.observers;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.action.FindAchievementAction;
import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.model.observers.base.BaseAchievementByGoalAmountObserver;
import com.etermax.spacehorse.core.player.model.Player;

public class BattlePlaysReachedAchievementObserver extends BaseAchievementByGoalAmountObserver {

	public BattlePlaysReachedAchievementObserver(FindAchievementAction findAchievementAction, CompleteAchievementAction completeAchievementAction) {
		super(findAchievementAction, completeAchievementAction);
	}

	@Override
	public AchievementType getAchievementType() {
		return AchievementType.PLAY_BATTLE;
	}

	@Override
	public boolean achievementIsReached(Player player, Achievement achievement) {
		return player.getPlayerStats().getMatchStats().getMultiplayerPlayed() >= achievement.getGoalAmount();
	}

	@Override
	public String getErrorMsg() {
		return "====> Unexpected error when trying to update achievements for play battle achievement type";
	}
}
