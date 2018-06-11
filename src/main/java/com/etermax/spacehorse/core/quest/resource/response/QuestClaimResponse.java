package com.etermax.spacehorse.core.quest.resource.response;

import java.util.List;

import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestClaimResponse {

	@JsonProperty("rewardResponses")
	private List<RewardResponse> rewardResponses;

	@JsonProperty("refreshTimeInSeconds")
	private long refreshTimeInSeconds;

	public QuestClaimResponse(List<RewardResponse> rewardResponses, long refreshTimeInSeconds) {
		this.rewardResponses = rewardResponses;
		this.refreshTimeInSeconds = refreshTimeInSeconds;
	}

	public List<RewardResponse> getRewardResponses() {
		return rewardResponses;
	}

	public void setRewardResponses(List<RewardResponse> rewardResponses) {
		this.rewardResponses = rewardResponses;
	}

}
