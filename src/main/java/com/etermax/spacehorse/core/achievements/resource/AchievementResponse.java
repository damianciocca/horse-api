package com.etermax.spacehorse.core.achievements.resource;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AchievementResponse {

	@JsonProperty("id")
	private String achievementId;

	@JsonProperty("state")
	private String state;

	public AchievementResponse(Achievement achievement) {
		this.achievementId = achievement.getAchievementId();
		this.state = achievement.getStateAsTxt();
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
