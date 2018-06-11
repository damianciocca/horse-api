package com.etermax.spacehorse.core.player.resource.response.player.progress;

import com.etermax.spacehorse.core.player.model.progress.PlayerRewards;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerRewardsResponse {

	@JsonProperty("rewardsReceivedToday")
	private RewardsReceivedTodayResponse rewardsReceivedToday;

	public RewardsReceivedTodayResponse getRewardsReceivedToday() {
		return rewardsReceivedToday;
	}

	public PlayerRewardsResponse() {
	}

	public PlayerRewardsResponse(PlayerRewards playerRewards) {
		this.rewardsReceivedToday = new RewardsReceivedTodayResponse(playerRewards.getRewardsReceivedToday());

	}
}
