package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.GemsCycleEntryResponse;

public class GemsCycleEntry {

	private final int sequenceOrder;

	private final int freeGemsAmount;

	public GemsCycleEntry(int sequenceOrder, int freeGemsAmount) {
		this.sequenceOrder = sequenceOrder;
		this.freeGemsAmount = freeGemsAmount;
	}

	public GemsCycleEntry(GemsCycleEntryResponse dto) {
		this.sequenceOrder = dto.getSequence();
		this.freeGemsAmount = dto.getFreeGemsAmount();
	}

	public int getSequenceOrder() {
		return sequenceOrder;
	}

	public int getFreeGemsAmount() {
		return freeGemsAmount;
	}

}
