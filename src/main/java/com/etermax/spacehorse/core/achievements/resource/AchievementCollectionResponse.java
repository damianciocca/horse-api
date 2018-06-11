package com.etermax.spacehorse.core.achievements.resource;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AchievementCollectionResponse {

	@JsonProperty("achievements")
	private List<AchievementResponse> achievements;

	public AchievementCollectionResponse() {
		// just for jackson
	}

	public AchievementCollectionResponse(AchievementCollection achievementCollection) {
		this.achievements = achievementCollection.getAchievements().stream().map(AchievementResponse::new).collect(Collectors.toList());
	}


	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
