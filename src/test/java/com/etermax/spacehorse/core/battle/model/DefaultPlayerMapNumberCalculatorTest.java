package com.etermax.spacehorse.core.battle.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.mock.NewPlayerConfigurationBuilder;

public class DefaultPlayerMapNumberCalculatorTest {

	private final String PLAYER_ID = "id";

	private List<MapDefinition> mapsCollection;
	private NewPlayerConfiguration newPlayerConfiguration;

	@Before
	public void setUp() {
		mapsCollection = Arrays.asList(
				buildMapDefinitionWithMmrAndMapNumber("Map1", 0, 0),
				buildMapDefinitionWithMmrAndMapNumber("Map2", 100, 1),
				buildMapDefinitionWithMmrAndMapNumber("Map3", 200, 2),
				buildMapDefinitionWithMmrAndMapNumber("Map4", 300, 3)
		);

		newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
	}

	private MapDefinition buildMapDefinitionWithMmrAndMapNumber(String mapId, int mmr, int mapNumber) {
		return new MapDefinition(mapId, mapNumber, mmr, 0, 0);
	}

	@Test
	public void testGetMapNumberFromMmr() {
		Player player = buildPlayer();

		assertEquals(0, new DefaultPlayerMapNumberCalculator(mapsCollection).getMapNumber(0));
		assertEquals(0, new DefaultPlayerMapNumberCalculator(mapsCollection).getMapNumber(50));
		assertEquals(1, new DefaultPlayerMapNumberCalculator(mapsCollection).getMapNumber(100));
		assertEquals(1, new DefaultPlayerMapNumberCalculator(mapsCollection).getMapNumber(150));
		assertEquals(2, new DefaultPlayerMapNumberCalculator(mapsCollection).getMapNumber(200));
		assertEquals(2, new DefaultPlayerMapNumberCalculator(mapsCollection).getMapNumber(250));
		assertEquals(3, new DefaultPlayerMapNumberCalculator(mapsCollection).getMapNumber(300));
		assertEquals(3, new DefaultPlayerMapNumberCalculator(mapsCollection).getMapNumber(350));
		assertEquals(3, new DefaultPlayerMapNumberCalculator(mapsCollection).getMapNumber(400));
		assertEquals(3, new DefaultPlayerMapNumberCalculator(mapsCollection).getMapNumber(1000));
	}

	private Player buildPlayer() {
		return Player.buildNewPlayer(PLAYER_ID, ABTag.emptyABTag(), 0, newPlayerConfiguration);
	}
}