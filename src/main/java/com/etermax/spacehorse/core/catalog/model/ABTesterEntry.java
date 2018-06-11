package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.ABTesterResponse;

public class ABTesterEntry extends CatalogEntry {

	private final String segment;
	private final String deltas;
	private final boolean battleCompatible;

	public ABTesterEntry(ABTesterResponse abTesterResponse) {
		super(abTesterResponse.getId() + "-" + abTesterResponse.getSegment());
		this.segment = abTesterResponse.getSegment();
		this.deltas = abTesterResponse.getDeltas();
		this.battleCompatible = abTesterResponse.getBattleCompatible();
	}

	@Override
	public void validate(Catalog catalog) {

	}

	public String getDeltas() {
		return deltas;
	}

	public String getSegment() {
		return segment;
	}

	public boolean getBattleCompatible() {
		return battleCompatible;
	}
}
