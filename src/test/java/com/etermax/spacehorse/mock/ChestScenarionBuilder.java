package com.etermax.spacehorse.mock;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.google.common.collect.Lists;

public class ChestScenarionBuilder {

	private static final String CHEST_TYPE_ID = "chestTypeId";
	private final ServerTimeProvider timeProvider;
	private final CatalogEntriesCollection<ChestDefinition> chestDefinitionCatalogEntriesCollection;
	private Chest chest;

	public ChestScenarionBuilder(ServerTimeProvider timeProvider, int duration) {
		this.timeProvider = timeProvider;
		Chest aChest = new Chest(1L, CHEST_TYPE_ID, 1, 1);
		List<ChestDefinition> chestDefinitions = Lists.newArrayList(new ChestDefinition("chestTypeId", "name", duration, false, false, false));
		chestDefinitionCatalogEntriesCollection = new CatalogEntriesCollection<>(chestDefinitions);
		this.chest = aChest;
	}

	public ChestScenarionBuilder startOpening() {
		chest.startOpening(timeProvider.getDate(), chestDefinitionCatalogEntriesCollection);
		return this;
	}

	public Chest build() {
		return chest;
	}
}
