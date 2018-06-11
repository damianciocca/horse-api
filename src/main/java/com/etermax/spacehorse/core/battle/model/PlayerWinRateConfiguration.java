package com.etermax.spacehorse.core.battle.model;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;

public class PlayerWinRateConfiguration {

	private final int cappedMmr;
	private final boolean cappedEnabled;

	public static PlayerWinRateConfiguration create(int cappedMmr, boolean cappedEnabled) {
		return new PlayerWinRateConfiguration(cappedMmr, cappedEnabled);
	}

	public static PlayerWinRateConfiguration create(Catalog catalog) {
		return new PlayerWinRateConfiguration(catalog.getGameConstants().getCappedMmr(), !catalog.getGameConstants().isLeagueEnabled());
	}

	public static PlayerWinRateConfiguration create(BattlePlayer battlePlayer, CatalogRepository catalogRepository) {
		Catalog catalog = catalogRepository.getActiveCatalogWithTag(new ABTag(battlePlayer.getAbTag()));
		return PlayerWinRateConfiguration.create(catalog);
	}

	private PlayerWinRateConfiguration(int cappedMmr, boolean cappedEnabled) {
		this.cappedEnabled = cappedEnabled;
		this.cappedMmr = cappedMmr;
	}

	public boolean isCappedEnabled() {
		return cappedEnabled;
	}

	public int getCappedMmr() {
		return cappedMmr;
	}
}
