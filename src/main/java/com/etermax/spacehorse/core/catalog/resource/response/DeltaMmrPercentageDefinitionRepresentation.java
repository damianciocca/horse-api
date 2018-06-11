package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.DeltaMmrPercentageDefinition;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeltaMmrPercentageDefinitionRepresentation {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("MinMmr")
	private int minMmr;

	@JsonProperty("MaxMmr")
	private int maxMmr;

	@JsonProperty("PercentageByVictory")
	private int percentageByVictory;

	@JsonProperty("PercentageByLose")
	private int percentageByLose;

	public DeltaMmrPercentageDefinitionRepresentation(){
		// just for jackson library
	}

	public DeltaMmrPercentageDefinitionRepresentation(DeltaMmrPercentageDefinition deltaMmrPercentageDefinition){
		id = deltaMmrPercentageDefinition.getId();
		minMmr = deltaMmrPercentageDefinition.getMinMmr();
		maxMmr = deltaMmrPercentageDefinition.getMaxMmr();
		percentageByLose = deltaMmrPercentageDefinition.getPercentageByLose();
		percentageByVictory = deltaMmrPercentageDefinition.getPercentageByVictory();
	}

	public String getId() {
		return id;
	}

	public int getMinMmr() {
		return minMmr;
	}

	public int getMaxMmr() {
		return maxMmr;
	}

	public int getPercentageByVictory() {
		return percentageByVictory;
	}

	public int getPercentageByLose() {
		return percentageByLose;
	}
}
