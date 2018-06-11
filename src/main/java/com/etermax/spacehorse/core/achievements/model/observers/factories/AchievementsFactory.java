package com.etermax.spacehorse.core.achievements.model.observers.factories;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.action.FindAchievementAction;
import com.etermax.spacehorse.core.achievements.model.observers.BattleFriendlyPlaysReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.CardsUnlockedReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.battle.model.Battle;

public class AchievementsFactory {

	private final FindAchievementAction findAchievementAction;
	private final CompleteAchievementAction completeAchievementAction;

	public AchievementsFactory(FindAchievementAction findAchievementAction, CompleteAchievementAction completeAchievementAction) {
		this.findAchievementAction = findAchievementAction;
		this.completeAchievementAction = completeAchievementAction;
	}

	public List<AchievementsObserver> createForApplyRewards() {
		return newArrayList(new CardsUnlockedReachedAchievementObserver(findAchievementAction, completeAchievementAction));
	}

	public AchievementsObserver createBattleFriendlyPlaysReached(Battle battle) {
		return new BattleFriendlyPlaysReachedAchievementObserver(completeAchievementAction, battle);
	}
}
