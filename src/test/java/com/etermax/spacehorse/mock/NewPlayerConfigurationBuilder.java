package com.etermax.spacehorse.mock;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.etermax.spacehorse.core.freechest.model.FreeChestConfiguration;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.deck.DeckConfiguration;
import com.etermax.spacehorse.core.player.model.inventory.InventoryConfiguration;

public class NewPlayerConfigurationBuilder {

	private static final int DEFAULT_MAX_FREE_CHESTS = 2;
	private static final String DEFAULT_FREE_CHEST_ID = "courier_Free";
	private static final int DEFAULT_STARTING_GEMS = 100;
	private static final int DEFAULT_STARTING_GOLD = 100;
	private static final int DEFAULT_TIME_BETWEEN_FREE_CHESTS_IN_SECONDS = 14400;
	private static final int DEFAULT_NUMBER_OF_CARDS_IN_DECK = 8;
	private static final String CATALOG_ID = "1507298083884";

	private NewPlayerConfiguration newPlayerConfiguration;

	public NewPlayerConfigurationBuilder() {
		DeckConfiguration deckConfiguration = new DeckConfiguration(DEFAULT_NUMBER_OF_CARDS_IN_DECK, getDefaultStartingCardTypes());
		InventoryConfiguration inventoryConfiguration = new InventoryConfiguration(DEFAULT_STARTING_GEMS, DEFAULT_STARTING_GOLD);
		FreeChestConfiguration freeChestConfiguration = new FreeChestConfiguration(DEFAULT_FREE_CHEST_ID, DEFAULT_TIME_BETWEEN_FREE_CHESTS_IN_SECONDS,
				DEFAULT_MAX_FREE_CHESTS);

		newPlayerConfiguration = new NewPlayerConfiguration(CATALOG_ID, deckConfiguration, inventoryConfiguration, freeChestConfiguration);
	}

	public NewPlayerConfiguration build() {
		return newPlayerConfiguration;
	}

	private List<String> getDefaultStartingCardTypes() {
		return newArrayList( //
				"card_enforcer",//
				"card_electric_duo", //
				"card_mastodon", //
				"card_punk_shooter", //
				"card_laser", //
				"card_flying_headstrongs", //
				"card_spitter", //
				"card_headstrongs");
	}

}
