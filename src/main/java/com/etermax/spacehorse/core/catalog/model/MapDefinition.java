package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.MapDefinitionResponse;

public class MapDefinition extends CatalogEntry implements Comparable<MapDefinition> {

	private final int mapNumber;

	private final int mmr;

	private final int victoryGold;

	private final int victoryRewardsPerDay;

	public MapDefinition(String id, int mmr) {
		super(id);
		this.mmr = mmr;
		this.mapNumber = 0;
		this.victoryGold = 0;
		this.victoryRewardsPerDay = 0;
	}

	public MapDefinition(MapDefinitionResponse mapDefinitionResponse) {
		super(mapDefinitionResponse.getId());
		this.mapNumber = mapDefinitionResponse.getMapNumber();
		this.mmr = mapDefinitionResponse.getMmr();
		this.victoryGold = mapDefinitionResponse.getVictoryGold();
		this.victoryRewardsPerDay = mapDefinitionResponse.getVictoryRewardsPerDay();
	}

	public MapDefinition(String id, int mapNumber, int mmr, int victoryGold, int victoryRewardsPerDay) {
		super(id);
		this.mapNumber = mapNumber;
		this.mmr = mmr;
		this.victoryGold = victoryGold;
		this.victoryRewardsPerDay = victoryRewardsPerDay;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public int getMmr() {
		return mmr;
	}

	public int getVictoryGold() {
		return victoryGold;
	}

	public int getVictoryRewardsPerDay() {
		return victoryRewardsPerDay;
	}

	public void validate(Catalog catalog) {
		validateParameter(mapNumber >= 0, "mapNumber < 0");
		validateParameter(mmr >= 0, "mmr < 0");
		validateParameter(victoryGold >= 0, "victoryGold < 0");
		validateParameter(victoryRewardsPerDay >= 0, "victoryRewardsPerDay < 0");
	}

	@Override
	public int compareTo(MapDefinition o) {
		return this.getMmr() - o.getMmr();
	}

}
