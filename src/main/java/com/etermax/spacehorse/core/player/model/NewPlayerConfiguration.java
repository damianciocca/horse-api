package com.etermax.spacehorse.core.player.model;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.freechest.model.FreeChestConfiguration;
import com.etermax.spacehorse.core.player.model.deck.DeckConfiguration;
import com.etermax.spacehorse.core.player.model.inventory.InventoryConfiguration;

public class NewPlayerConfiguration {

	private final String catalogId;
	private final DeckConfiguration deckConfiguration;
	private final InventoryConfiguration inventoryConfiguration;
	private final FreeChestConfiguration freeChestConfiguration;

	public NewPlayerConfiguration(String catalogId, DeckConfiguration deckConfiguration, InventoryConfiguration inventoryConfiguration,
			FreeChestConfiguration freeChestConfiguration) {
		this.deckConfiguration = deckConfiguration;
		this.inventoryConfiguration = inventoryConfiguration;
		this.catalogId = catalogId;
		this.freeChestConfiguration = freeChestConfiguration;
	}

	public static NewPlayerConfiguration createBy(Catalog catalog) {
		GameConstants gameConstants = catalog.getGameConstants();

		DeckConfiguration deckConfiguration = new DeckConfiguration(gameConstants.getNumberOfCardsInDeck(),
				gameConstants.getStartingCards());
		InventoryConfiguration inventoryConfiguration = new InventoryConfiguration(gameConstants.getStartingGems(),
				gameConstants.getStartingGold());
		FreeChestConfiguration freeChestConfiguration = new FreeChestConfiguration(gameConstants.getFreeChestId(),
				gameConstants.getTimeBetweenFreeChestsInSeconds(), gameConstants.getMaxFreeChests());
		String catalogId = catalog.getCatalogId();

		return new NewPlayerConfiguration(catalogId, deckConfiguration, inventoryConfiguration, freeChestConfiguration);
	}

	public DeckConfiguration getDeckConfiguration() {
		return deckConfiguration;
	}

	public InventoryConfiguration getInventoryConfiguration() {
		return inventoryConfiguration;
	}

	public FreeChestConfiguration getFreeChestConfiguration() {
		return freeChestConfiguration;
	}

	public String getCatalogId() {
		return catalogId;
	}
}
