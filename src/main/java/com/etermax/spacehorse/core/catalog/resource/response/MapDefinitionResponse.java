package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MapDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("MapNumber")
	private int mapNumber;

	@JsonProperty("MMR")
	private int mmr;

	@JsonProperty("VictoryGold")
	private int victoryGold;

	@JsonProperty("VictoryRewardsPerDay")
	private int victoryRewardsPerDay;

	public MapDefinitionResponse() {
	}

	public MapDefinitionResponse(MapDefinition map) {
		this.id = map.getId();
		this.mapNumber = map.getMapNumber();
		this.mmr = map.getMmr();
		this.victoryGold = map.getVictoryGold();
		this.victoryRewardsPerDay = map.getVictoryRewardsPerDay();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public void setMapNumber(int mapNumber) {
		this.mapNumber = mapNumber;
	}

	public int getMmr() {
		return mmr;
	}

	public void setMmr(int mmr) {
		this.mmr = mmr;
	}

	public int getVictoryGold() {
		return victoryGold;
	}

	public void setVictoryGold(int victoryGold) {
		this.victoryGold = victoryGold;
	}

	public int getVictoryRewardsPerDay() {
		return victoryRewardsPerDay;
	}

	public void setVictoryRewardsPerDay(int victoryRewardsPerDay) {
		this.victoryRewardsPerDay = victoryRewardsPerDay;
	}

}
