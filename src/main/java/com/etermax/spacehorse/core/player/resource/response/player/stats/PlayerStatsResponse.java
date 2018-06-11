package com.etermax.spacehorse.core.player.resource.response.player.stats;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.player.model.stats.PlayerStats;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerStatsResponse {

	@JsonProperty("maxMMR")
	private int maxMMR;

	@JsonProperty("nameChanges")
	private int nameChanges;

	@JsonProperty("moneySpentInUsdCents")
	private int moneySpentInUsdCents;

	@JsonProperty("matchStats")
	private MatchStatsResponse matchStats;

	@JsonProperty("daysSinceJoin")
	private long daysSinceJoin;

	@JsonProperty("cumulativeDays")
	private int cumulativeDays;

	public PlayerStatsResponse() {
	}

	public PlayerStatsResponse(PlayerStats playerStats) {
		this.maxMMR = playerStats.getMaxMMR();
		this.nameChanges = playerStats.getNameChanges();
		this.moneySpentInUsdCents = playerStats.getMoneySpentInUsdCents();
		this.matchStats = new MatchStatsResponse(playerStats.getMatchStats());
		this.daysSinceJoin = playerStats.getDaysSinceJoin();
		this.cumulativeDays = playerStats.getCumulativeDays();
	}

	public int getMaxMMR() {
		return maxMMR;
	}

	public int getNameChanges() {
		return nameChanges;
	}

	public int getMoneySpentInUsdCents() {
		return moneySpentInUsdCents;
	}

	public MatchStatsResponse getMatchStats() {
		return matchStats;
	}

	public long getDaysSinceJoin() {
		return daysSinceJoin;
	}

	public long getCumulativeDays() {
		return cumulativeDays;
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
