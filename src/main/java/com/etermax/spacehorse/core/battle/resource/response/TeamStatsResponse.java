package com.etermax.spacehorse.core.battle.resource.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TeamStatsResponse {

	@JsonProperty("score")
	private Integer score = 0;

	@JsonProperty("motherShipDamaged")
	private Boolean motherShipDamaged = false;

	@JsonProperty("usedCards")
	private List<UsedCardResponse> usedCards;

	public TeamStatsResponse() {
		this.score = 0;
		this.motherShipDamaged = false;
		this.usedCards = new ArrayList<>();
	}

	public TeamStatsResponse(Integer score, Boolean motherShipDamaged, List<UsedCardResponse> usedCards) {
		this.score = score;
		this.motherShipDamaged = motherShipDamaged;
		this.usedCards = usedCards;
	}

	public Integer getScore() {
		return score;
	}

	public Boolean getMotherShipDamaged() {
		return motherShipDamaged;
	}

	public List<UsedCardResponse> getUsedCards() {
		return usedCards;
	}

	public static TeamStatsResponse fromTeamStats(TeamStats teamStats) {
		return new TeamStatsResponse(teamStats.getScore(), teamStats.getMotherShipDamaged(),
				teamStats.getUsedCards().stream().map(x -> UsedCardResponse.fromUsedCardInfo(x)).collect(Collectors.toList()));
	}
}
