package com.etermax.spacehorse.core.battle.resource.response;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BattleParametersResponse {

	@JsonProperty("duration")
	private Integer duration;

	@JsonProperty("suddenDeathDuration")
	private Integer suddenDeathDuration;

	@JsonProperty("lastMinuteDuration")
	private String lastMinuteDuration;

	@JsonProperty("energyPerSecond")
	private String energyPerSecond;

	public BattleParametersResponse(Catalog catalog) {
		this.duration = catalog.getGameConstants().getBattleDuration();
		this.suddenDeathDuration = catalog.getGameConstants().getSuddenDeathDuration();
		this.lastMinuteDuration = Float.toString(catalog.getGameConstants().getLastMinuteDuration().toFloat());
		this.energyPerSecond = Float.toString(catalog.getGameConstants().getEnergyPerSecond().toFloat());
	}

	public Integer getDuration() {
		return duration;
	}

	public Integer getSuddenDeathDuration() {
		return suddenDeathDuration;
	}

	public String getEnergyPerSecond() {
		return energyPerSecond;
	}

	public String getLastMinuteDuration() {
		return lastMinuteDuration;
	}

	public BattleParametersResponse() {
	}

	public BattleParametersResponse(Integer duration, Integer suddenDeathDuration, String lastMinuteDuration, String energyPerSecond) {
		this.duration = duration;
		this.suddenDeathDuration = suddenDeathDuration;
		this.lastMinuteDuration = lastMinuteDuration;
		this.energyPerSecond = energyPerSecond;
	}
}
