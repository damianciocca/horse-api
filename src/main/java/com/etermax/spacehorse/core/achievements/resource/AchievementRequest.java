package com.etermax.spacehorse.core.achievements.resource;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AchievementRequest {

	@JsonProperty("achievementId")
	private String achievementId;

	public AchievementRequest(@JsonProperty("achievementId") String achievementId) {
		checkArgument(isNotBlank(achievementId), "the achievement id should not be blank");
		this.achievementId = achievementId;
	}

	public String getAchievementId() {
		return this.achievementId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
