package com.etermax.spacehorse.core.catalog.resource.response;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AchievementDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("AchievementType")
	private String achievementType;

	@JsonProperty("GoldReward")
	private int coinsReward;

	@JsonProperty("GemsReward")
	private int gemsReward;

	@JsonProperty("GoalAmount")
	private int goalAmount;

	@JsonProperty("AllowedToCompleteByClient")
	private boolean allowedToCompleteByClient;

	public AchievementDefinitionResponse() {
	}

	public AchievementDefinitionResponse(String id, String achievementType, int coinsReward, int gemsReward, int goalAmount,
			boolean allowedToCompleteByClient) {
		this.id = id;
		this.achievementType = achievementType;
		this.coinsReward = coinsReward;
		this.gemsReward = gemsReward;
		this.goalAmount = goalAmount;
		this.allowedToCompleteByClient = allowedToCompleteByClient;
	}

	public String getId() {
		return id;
	}

	public String getAchievementType() {
		return achievementType;
	}

	public int getCoinsReward() {
		return coinsReward;
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
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
