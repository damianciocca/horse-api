package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.ABTesterEntry;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ABTesterResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("Segment")
	private String segment;

	@JsonProperty("Deltas")
	private String deltas;

	@JsonProperty("BattleCompatible")
	private boolean battleCompatible;

	public ABTesterResponse() {
	}

	public ABTesterResponse(ABTesterEntry abTesterEntry) {

		if (abTesterEntry.getId().split("-").length == 2)
			this.id = abTesterEntry.getId().split("-")[0];
		else
			this.id = abTesterEntry.getId();
		this.segment = abTesterEntry.getSegment();
		this.deltas = abTesterEntry.getDeltas();
		this.battleCompatible = abTesterEntry.getBattleCompatible();
	}

	public String getId() {
		return id;
	}

	public String getSegment() {
		return segment;
	}

	public String getDeltas() {
		return deltas;
	}

	public boolean getBattleCompatible() {
		return battleCompatible;
	}
}
