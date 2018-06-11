package com.etermax.spacehorse.core.achievements.model;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;

public class AchievementsDefinitionsSelector {

	public List<AchievementDefinition> selectByType(List<AchievementDefinition> definitions, AchievementType type) {
		return definitions.stream().filter(byAchievementType(type)).collect(Collectors.toList());
	}

	private Predicate<AchievementDefinition> byAchievementType(AchievementType type) {
		return achievementDefinition -> type.getTypeAsTxt().equals(achievementDefinition.getAchievementTypeAsTxt());
	}
}
