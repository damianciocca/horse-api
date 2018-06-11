package com.etermax.spacehorse.core.player.resource.response.player.stats;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.player.model.stats.MatchStats;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchStatsResponse {
	@JsonProperty("multiplayerWonFullScore")
	private int multiplayerWonFullScore;

	@JsonProperty("multiplayerPlayed")
	private int multiplayerPlayed;

	@JsonProperty("multiplayerWon")
	private int multiplayerWon;

	@JsonProperty("multiplayerLost")
	private int multiplayerLost;

	@JsonProperty("opponentShipsDestroyed")
	private int opponentShipsDestroyed;

	public MatchStatsResponse() {
	}

	public MatchStatsResponse(MatchStats matchStats) {
		this.multiplayerPlayed = matchStats.getMultiplayerPlayed();
		this.multiplayerWon = matchStats.getMultiplayerWon();
		this.multiplayerLost = matchStats.getMultiplayerLost();
		this.multiplayerWonFullScore = matchStats.getMultiplayerWonFullScore();
		this.opponentShipsDestroyed = matchStats.getOpponentShipsDestroyed();
	}

	public int getMultiplayerPlayed() {
		return multiplayerPlayed;
	}

	public int getMultiplayerWon() {
		return multiplayerWon;
	}

	public int getMultiplayerLost() {
		return multiplayerLost;
	}

	public int getMultiplayerWonFullScore() {
		return multiplayerWonFullScore;
	}

	public int getOpponentShipsDestroyed() {
		return opponentShipsDestroyed;
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}
