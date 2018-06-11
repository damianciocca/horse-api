package com.etermax.spacehorse.core.catalog.model.league;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;

public class LeagueRewardsDefinition extends CatalogEntry {

	private final int mmr;
	private final String reward;

	public LeagueRewardsDefinition(String id, int mmr, String reward) {
		super(id);
		this.mmr = mmr;
		this.reward = reward;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(isNotBlank(reward), "courier reward should not be blank");
	}

	public String getReward() {
		return reward;
	}

	public int getMmr() {
		return mmr;
	}
}
