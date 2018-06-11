package com.etermax.spacehorse.core.quest.exception;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class NoQuestAvailableForCurrentSlotAndMap extends ApiException {
	public NoQuestAvailableForCurrentSlotAndMap(String slotId, int mapNumber) {
		super("No quest available for slot " + slotId + " and map " + mapNumber + ", please check quest chances configuration");
	}
}
