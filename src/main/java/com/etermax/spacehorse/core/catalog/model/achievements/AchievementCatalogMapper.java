package com.etermax.spacehorse.core.catalog.model.achievements;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.AchievementDefinitionResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;

public class AchievementCatalogMapper {

	public EntryContainerResponse<AchievementDefinitionResponse> mapFrom(Catalog catalog) {
		List<AchievementDefinitionResponse> responses = catalog.getAchievementsDefinitionsCollection().getEntries().stream()
				.map(toAchievementDefinitionResponse()).collect(Collectors.toList());
		return new EntryContainerResponse<>(responses);
	}

	public List<AchievementDefinition> mapFrom(CatalogResponse catalogResponse) {
		return catalogResponse.getAchievementsCollection().getEntries().stream().map(toAchievementDefinition()).collect(Collectors.toList());
	}

	private Function<AchievementDefinitionResponse, AchievementDefinition> toAchievementDefinition() {
		return achievementDefinitionResponse -> new AchievementDefinition(//
				achievementDefinitionResponse.getId(), //
				achievementDefinitionResponse.getAchievementType(),//
				achievementDefinitionResponse.getCoinsReward(), //
				achievementDefinitionResponse.getGemsReward(),//
				achievementDefinitionResponse.getGoalAmount(),//
				achievementDefinitionResponse.isAllowedToCompleteByClient());
	}

	private Function<AchievementDefinition, AchievementDefinitionResponse> toAchievementDefinitionResponse() {
		return achievementDefinition -> new AchievementDefinitionResponse( //
				achievementDefinition.getId(), //
				achievementDefinition.getAchievementTypeAsTxt(), //
				achievementDefinition.getGoldReward(),//
				achievementDefinition.getGemsReward(),//
				achievementDefinition.getGoalAmount(),//
				achievementDefinition.isAllowedToCompleteByClient());
	}

}
