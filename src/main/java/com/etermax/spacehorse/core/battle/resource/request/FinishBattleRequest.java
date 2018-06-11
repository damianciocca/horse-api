package com.etermax.spacehorse.core.battle.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FinishBattleRequest {

	@JsonProperty("battleId")
	private String battleId;

	@JsonProperty("winnerLoginId")
	private String winnerLoginId;

	@JsonProperty("isWinFullScore")
	private Boolean isWinFullScore;

	@JsonProperty("team1Stats")
	private TeamStatsRequest team1Stats;

	@JsonProperty("team2Stats")
	private TeamStatsRequest team2Stats;

	public FinishBattleRequest(@JsonProperty("battleId") String battleId, @JsonProperty("winnerLoginId") String winnerLoginId,
			@JsonProperty("isWinFullScore") Boolean isWinFullScore, @JsonProperty("team1Stats") TeamStatsRequest team1Stats,
			@JsonProperty("team2Stats") TeamStatsRequest team2Stats) {
		this.battleId = battleId;
		this.winnerLoginId = winnerLoginId;
		this.isWinFullScore = isWinFullScore;
		this.team1Stats = team1Stats;
		this.team2Stats = team2Stats;
	}

	public String getBattleId() {
		return battleId;
	}

	public String getWinnerLoginId() {
		return winnerLoginId;
	}

	public Boolean getWinFullScore() {
		return isWinFullScore;
	}

	public TeamStatsRequest getTeam1Stats() {
		return team1Stats;
	}

	public TeamStatsRequest getTeam2Stats() {
		return team2Stats;
	}
}
