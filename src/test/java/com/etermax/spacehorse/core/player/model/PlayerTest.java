package com.etermax.spacehorse.core.player.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.ChestList;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.NewPlayerConfigurationBuilder;

public class PlayerTest {

	private Player player;

	private Catalog catalog;
	private FixedServerTimeProvider serverTimeProvider;
	private NewPlayerConfiguration newPlayerConfiguration;

	@Before
	public void setUp() {
		this.catalog = MockUtils.mockCatalog();
		serverTimeProvider = new FixedServerTimeProvider();
		newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
		this.player = Player.buildNewPlayer("id", ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
	}

	@Test
	public void testGetChestReward() {
		// Given
		String sequenceId = catalog.getGameConstants().getDefaultFreeGemsCycleSequenceId();
		CatalogEntriesCollection<ChestList> chestsListsCollection = catalog.getChestsListsCollection();
		CatalogEntriesCollection<ChestDefinition> chestDefinitionsCollection = catalog.getChestDefinitionsCollection();
		// When
		ChestDefinition chestDefinition = player.getPlayerRewards()
				.getNextChestReward(sequenceId, chestsListsCollection, chestDefinitionsCollection);
		// Then
		assertNotNull(chestDefinition);
		assertEquals("courier_Silver", chestDefinition.getId());
	}

	@Test
	public void testCycleGetChestReward() {
		// Given
		String sequenceId = catalog.getGameConstants().getDefaultFreeGemsCycleSequenceId();
		CatalogEntriesCollection<ChestList> chestsListsCollection = catalog.getChestsListsCollection();
		CatalogEntriesCollection<ChestDefinition> chestDefinitionsCollection = catalog.getChestDefinitionsCollection();
		player.getPlayerRewards().getNextChestReward(sequenceId, chestsListsCollection, chestDefinitionsCollection);
		player.getPlayerRewards().getNextChestReward(sequenceId, chestsListsCollection, chestDefinitionsCollection);
		// when
		ChestDefinition chestDefinition = player.getPlayerRewards().getNextChestReward(sequenceId, chestsListsCollection, chestDefinitionsCollection);
		// Then
		assertNotNull(chestDefinition);
		assertEquals("courier_Silver", chestDefinition.getId());
	}

}
