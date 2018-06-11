package com.etermax.spacehorse.core.catalog.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LeagueRewardsDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("ChestReward")
	private String reward;

	@JsonProperty("MMR")
	private int mmr;

	public LeagueRewardsDefinitionResponse() {
	}

	public LeagueRewardsDefinitionResponse(String id, int mmr, String reward) {
		this.id = id;
		this.reward = reward;
		this.mmr = mmr;
	}

	public String getId() {
		return id;
	}

	public String getReward() {
		return reward;
	}

	public int getMmr() {
		return mmr;
	}
}
