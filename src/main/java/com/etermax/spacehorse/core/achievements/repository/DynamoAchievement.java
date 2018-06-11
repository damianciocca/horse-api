package com.etermax.spacehorse.core.achievements.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;

@DynamoDBDocument
public class DynamoAchievement {

	@DynamoDBAttribute(attributeName = "achievementId")
	private String achievementId;

	@DynamoDBAttribute(attributeName = "state")
	private String state;

	public DynamoAchievement() {
		// just for dynamo mapper
	}

	public DynamoAchievement(String achievementId, String state) {
		this.achievementId = achievementId;
		this.state = state;
	}

	static Achievement mapToAchievement(DynamoAchievement dynamoAchievement, Catalog catalog) {
		AchievementDefinition definition = getDefinitionFor(catalog, dynamoAchievement.getAchievementId());
		Achievement.AchievementState state = Achievement.AchievementState.typeOf(dynamoAchievement.getState());
		return Achievement.restore(definition, dynamoAchievement.getAchievementId(), state);
	}

	static DynamoAchievement mapToDynamoAchievement(Achievement achievement) {
		return new DynamoAchievement(achievement.getAchievementId(), achievement.getStateAsTxt());
	}

	public String getAchievementId() {
		return achievementId;
	}

	public void setAchievementId(String achievementId) {
		this.achievementId = achievementId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	private static AchievementDefinition getDefinitionFor(Catalog catalog, String achievementId) {
		return catalog.getAchievementsDefinitionsCollection().findByIdOrFail(achievementId);
	}
}
