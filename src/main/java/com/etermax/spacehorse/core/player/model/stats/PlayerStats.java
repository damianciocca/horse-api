package com.etermax.spacehorse.core.player.model.stats;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.catalog.model.Catalog;

@DynamoDBDocument
public class PlayerStats {

	@DynamoDBAttribute(attributeName = "maxMMR")
	private int maxMMR;

	@DynamoDBAttribute(attributeName = "nameChanges")
	private int nameChanges;

	@DynamoDBAttribute(attributeName = "moneySpentInUsdCents")
	private int moneySpentInUsdCents;

	@DynamoDBAttribute(attributeName = "matchStats")
	private MatchStats matchStats;

	@DynamoDBAttribute(attributeName = "daysSinceJoin")
	private long daysSinceJoin;

	@DynamoDBAttribute(attributeName = "cumulativeDays")
	private int cumulativeDays;

	public PlayerStats() {
		matchStats = new MatchStats();
	}

	public int getMaxMMR() {
		return maxMMR;
	}

	public void setMaxMMR(int maxMMR) {
		this.maxMMR = maxMMR;
	}

	public int getNameChanges() {
		return nameChanges;
	}

	public void setNameChanges(int nameChanges) {
		this.nameChanges = nameChanges;
	}

	public int getMoneySpentInUsdCents() {
		return moneySpentInUsdCents;
	}

	public void setMoneySpentInUsdCents(int moneySpentInUsdCents) {
		this.moneySpentInUsdCents = moneySpentInUsdCents;
	}

	public MatchStats getMatchStats() {
		return matchStats;
	}

	public void setMatchStats(MatchStats matchStats) {
		this.matchStats = matchStats;
	}

	public long getDaysSinceJoin() {
		return daysSinceJoin;
	}

	public void setDaysSinceJoin(long daysSinceJoin) {
		this.daysSinceJoin = daysSinceJoin;
	}

	public int getCumulativeDays() {
		return cumulativeDays;
	}

	public void setCumulativeDays(int cumulativeDays) {
		this.cumulativeDays = cumulativeDays;
	}

	public void checkAndFixIntegrity(Catalog catalog) {
		matchStats.checkAndFixIntegrity(catalog);
	}

	public void updateMaxMMR(Integer mmr) {
		if (mmr > maxMMR)
			maxMMR = mmr;
	}

	public void incrementNameChanges() {
		this.nameChanges++;
	}

	public void accumulateMoneySpent(int priceInUsdCents) {
			moneySpentInUsdCents += priceInUsdCents;
	}

	public void incrementCumulativeDays() {
		this.cumulativeDays++;
	}
}
