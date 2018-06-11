package com.etermax.spacehorse.core.battle.resource.response;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishBattlePlayerResponse {
	@JsonProperty("rewards")
	private List<RewardResponse> rewards;

	@JsonProperty("mmr")
	private Integer mmr;

	@JsonProperty("botMmr")
	private Integer botMmr;

	@JsonProperty("winnerId")
	private String winnerId;

	@JsonProperty("isWinFullScore")
	private Boolean isWinFullScore;

	@JsonProperty("team1Stats")
	private TeamStatsResponse team1Stats;

	@JsonProperty("team2Stats")
	private TeamStatsResponse team2Stats;

	public FinishBattlePlayerResponse() {
		this.rewards = new ArrayList<>();
		this.mmr = 0;
		this.botMmr = 0;
		this.winnerId = "";
		this.isWinFullScore = Boolean.FALSE;
		this.team1Stats = new TeamStatsResponse();
		this.team2Stats = new TeamStatsResponse();
	}

	public FinishBattlePlayerResponse(List<RewardResponse> rewards, Integer mmr, Integer botMmr, String winnerId, Boolean isWinFullScore,
			TeamStatsResponse team1Stats, TeamStatsResponse team2Stats) {
		this.rewards = rewards;
		this.mmr = mmr;
		this.botMmr = botMmr;
		this.winnerId = winnerId;
		this.isWinFullScore = isWinFullScore;
		this.team1Stats = team1Stats;
		this.team2Stats = team2Stats;
	}

	public void setMmr(Integer mmr) {
		this.mmr = mmr;
	}

	public void setBotMmr(Integer botMmr) {
		this.botMmr = botMmr;
	}

	public List<RewardResponse> getRewards() {
		return rewards;
	}

	public Integer getMmr() {
		return mmr;
	}

	public Integer getBotMmr() {
		return botMmr;
	}

	public String getWinnerId() {
		return winnerId;
	}

	public Boolean getWinFullScore() {
		return isWinFullScore;
	}

	public TeamStatsResponse getTeam1Stats() {
		return team1Stats;
	}

	public TeamStatsResponse getTeam2Stats() {
		return team2Stats;
	}
}
