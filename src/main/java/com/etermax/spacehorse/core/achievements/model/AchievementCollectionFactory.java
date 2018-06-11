package com.etermax.spacehorse.core.achievements.model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.google.common.collect.Maps;

public class AchievementCollectionFactory {

	public AchievementCollection create(String userId, Catalog catalog) {
		Map<String, Achievement> achievementByTypes = Maps.newHashMap();

		List<AchievementDefinition> achievementDefinitions = catalog.getAchievementsDefinitionsCollection().getEntries();
		List<Achievement> achievements = achievementDefinitions.stream().map(toAchievement()).collect(Collectors.toList());

		achievements.forEach(achievement -> achievementByTypes.put(achievement.getAchievementId(), achievement));

		return new AchievementCollection(userId, achievementByTypes);
	}

	public Achievement createAchievementFrom(AchievementDefinition achievementDefinition) {
		return new Achievement(achievementDefinition.getId(), achievementDefinition);
	}

	private Function<AchievementDefinition, Achievement> toAchievement() {
		return this::createAchievementFrom;
	}
}

