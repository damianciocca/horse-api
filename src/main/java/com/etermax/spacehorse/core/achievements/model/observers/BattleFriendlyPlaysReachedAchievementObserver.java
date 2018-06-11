package com.etermax.spacehorse.core.achievements.model.observers;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.model.observers.base.BaseAchievementObserver;
import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.player.model.Player;

public class BattleFriendlyPlaysReachedAchievementObserver extends BaseAchievementObserver {

	private final Battle battle;

	public BattleFriendlyPlaysReachedAchievementObserver(CompleteAchievementAction completeAchievementAction, Battle battle) {
		super(completeAchievementAction);
		this.battle = battle;
	}

	@Override
	public AchievementType getAchievementType() {
		return AchievementType.PLAY_AT_LEAST_ONE_FRIENDLY_BATTLE;
	}

	@Override
	public boolean achievementIsReached(Player player) {
		return battle.getMatchType().equals(MatchType.FRIENDLY);
	}

	@Override
	public String getErrorMsg() {
		return "====> Unexpected error when trying to update achievements for play friendly battle achievement type";
	}
}
