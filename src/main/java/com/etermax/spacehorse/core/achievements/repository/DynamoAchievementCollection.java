package com.etermax.spacehorse.core.achievements.repository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;

@DynamoDBTable(tableName = "achievementsCollection")
public class DynamoAchievementCollection implements AbstractDynamoDAO {

	@DynamoDBHashKey(attributeName = "userId")
	private String userId;

	@DynamoDBAttribute(attributeName = "achievements")
	private List<DynamoAchievement> achievements;

	public DynamoAchievementCollection() {
		// just for dynamo mapper
	}

	public DynamoAchievementCollection(String userId, List<DynamoAchievement> achievements) {
		this.userId = userId;
		this.achievements = achievements;
	}

	static DynamoAchievementCollection mapToDynamoAchievementCollection(String userId, AchievementCollection achievementCollection) {
		List<DynamoAchievement> dynamoAchievements = achievementCollection.getAchievements().stream().map(DynamoAchievement::mapToDynamoAchievement)
				.collect(Collectors.toList());
		return new DynamoAchievementCollection(userId, dynamoAchievements);
	}

	static AchievementCollection mapToAchievementCollection(DynamoAchievementCollection dynamoAchievementCollection, Catalog catalog, String userId) {
		List<Achievement> achievements = dynamoAchievementCollection.getAchievements().stream().map(toAchievement(catalog))
				.collect(Collectors.toList());
		collectNewConfiguredAchievementsFrom(catalog, achievements);
		return AchievementCollection.restore(userId, achievements);
	}

	@Override
	public String getId() {
		return getUserId();
	}

	@Override
	public void setId(String id) {
		setUserId(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<DynamoAchievement> getAchievements() {
		return achievements;
	}

	public void setAchievements(List<DynamoAchievement> achievements) {
		this.achievements = achievements;
	}

	private static void collectNewConfiguredAchievementsFrom(Catalog catalog, List<Achievement> achievements) {
		List<AchievementDefinition> achievementDefinitions = catalog.getAchievementsDefinitionsCollection().getEntries();
		achievementDefinitions.forEach(definition -> {
			createNewAchievementIfNotExist(achievements, definition);
		});
	}

	private static void createNewAchievementIfNotExist(List<Achievement> achievements, AchievementDefinition definition) {
		boolean newAchievement = achievements.stream().noneMatch(achievement -> achievement.getAchievementId().equals(definition.getId()));
		if (newAchievement) {
			achievements.add(new AchievementCollectionFactory().createAchievementFrom(definition));
		}
	}

	private static Function<DynamoAchievement, Achievement> toAchievement(Catalog catalog) {
		return dynamoAchievement -> DynamoAchievement.mapToAchievement(dynamoAchievement, catalog);
	}

}
