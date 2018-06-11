package com.etermax.spacehorse.core.liga.domain;

import com.etermax.spacehorse.core.catalog.model.Catalog;

public class LeagueConfiguration {

	private int initialMmr;
	private boolean leagueEnabled;

	public LeagueConfiguration(Catalog catalog) {
		String idLeagueMap = catalog.getGameConstants().getLeagueMapId();
		this.initialMmr = catalog.getMapsCollection().findByIdOrFail(idLeagueMap).getMmr();
		this.leagueEnabled = catalog.getGameConstants().isLeagueEnabled();
	}

	public int getInitialMmr() {
		return initialMmr;
	}

	public boolean isLeagueEnabled() {
		return leagueEnabled;
	}
}
