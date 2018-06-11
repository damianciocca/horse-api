package com.etermax.spacehorse.core.catalog.model.achievements;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;

public class AchievementDefinition extends CatalogEntry {

	private final String achievementType;

	private final int goldReward;

	private final int gemsReward;

	private final int goalAmount;

	private final boolean allowedToCompleteByClient;

	public AchievementDefinition(String id, String achievementType, int goldReward, int gemsReward, int goalAmount, boolean allowedToCompleteByClient) {
		super(id);
		this.achievementType = achievementType;
		this.goldReward = goldReward;
		this.gemsReward = gemsReward;
		this.goalAmount = goalAmount;
		this.allowedToCompleteByClient = allowedToCompleteByClient;
	}

	public String getAchievementTypeAsTxt() {
		return achievementType;
	}

	public int getGoldReward() {
		return goldReward;
	}

	public int getGemsReward() {
		return gemsReward;
	}

	public int getGoalAmount() {
		return goalAmount;
	}

	public boolean isAllowedToCompleteByClient() {
		return allowedToCompleteByClient;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(isNotBlank(achievementType), "achievement type should not be blank");
	}
}
