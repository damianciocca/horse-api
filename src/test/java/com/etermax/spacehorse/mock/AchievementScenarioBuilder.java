package com.etermax.spacehorse.mock;

import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;

public class AchievementScenarioBuilder {

	private static final String ACHIEVEMENT_PURCHASE_CAPTAIN_ID = "achievement_PurchaseCaptain"; // equals to mock catalog json

	private Achievement achievement;

	public AchievementScenarioBuilder() {
		this.achievement = new Achievement(ACHIEVEMENT_PURCHASE_CAPTAIN_ID, aAchievementDefinition());
	}

	public Achievement buildDefaultAchievement() {
		return achievement;
	}

	public Achievement buidReadyToClaimAchievement() {
		this.achievement = Achievement
				.restore(aAchievementDefinition(), ACHIEVEMENT_PURCHASE_CAPTAIN_ID, Achievement.AchievementState.READY_TO_CLAIM);
		return achievement;
	}

	private AchievementDefinition aAchievementDefinition() {
		return new AchievementDefinition(ACHIEVEMENT_PURCHASE_CAPTAIN_ID, AchievementType.PURCHASE_CAPTAIN.getTypeAsTxt(), 100, 400, 50, false);
	}

}
