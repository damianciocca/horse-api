package com.etermax.spacehorse.core.battle.resource.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TeamStatsRequest {

	@JsonProperty("score")
	private int score;

	@JsonProperty("motherShipDamaged")
	private boolean motherShipDamaged;

	@JsonProperty("usedCards")
	private List<UsedCardRequest> usedCards;

	public TeamStatsRequest(@JsonProperty("score") int score, @JsonProperty("motherShipDamaged") boolean motherShipDamaged,
			@JsonProperty("usedCards") List<UsedCardRequest> usedCards) {
		this.score = score;
		this.motherShipDamaged = motherShipDamaged;
		this.usedCards = usedCards;
	}

	public int getScore() {
		return score;
	}

	public boolean isMotherShipDamaged() {
		return motherShipDamaged;
	}

	public List<UsedCardRequest> getUsedCards() {
		return usedCards;
	}
}
