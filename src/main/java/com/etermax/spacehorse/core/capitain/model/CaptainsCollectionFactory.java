package com.etermax.spacehorse.core.capitain.model;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;

public class CaptainsCollectionFactory {

	private static final String EMPTY_SELECTED_ID = "";
	private final Random random;
	private final CaptainFactory captainFactory;

	public CaptainsCollectionFactory() {
		this.random = ThreadLocalRandom.current();
		captainFactory = new CaptainFactory();
	}

	public CaptainsCollection create(String playerId, Catalog catalog) {
		if (startingCaptainsIsNotEmpty(catalog)) {
			String startingCaptainId = chooseRandomStartingCaptainId(catalog);
			return new CaptainsCollection(playerId, getUnlockedCaptains(startingCaptainId, catalog), startingCaptainId);
		}

		return new CaptainsCollection(playerId, getEmptyUnlockedCaptains(), EMPTY_SELECTED_ID);
	}

	private String chooseRandomStartingCaptainId(Catalog catalog) {
		List<String> startingCaptainsIds = catalog.getGameConstants().getStartingCaptains();
		int randomIndex = random.nextInt(startingCaptainsIds.size());
		return startingCaptainsIds.get(randomIndex);
	}

	private boolean startingCaptainsIsNotEmpty(Catalog catalog) {
		return catalog.getGameConstants().getStartingCaptains().size() > 0;
	}

	private List<Captain> getUnlockedCaptains(String captainId, Catalog catalog) {
		CaptainDefinition captainDefinition = catalog.getCaptainDefinitionsCollection().findByIdOrFail(captainId);
		List<CaptainSkinDefinition> captainSkinDefinitions = catalog.getCaptainSkinDefinitionsCollection().getEntries();
		return newArrayList(captainFactory.createWithDefaultSkin(captainId, captainDefinition, captainSkinDefinitions));
	}

	private List<Captain> getEmptyUnlockedCaptains() {
		return newArrayList();
	}
}
