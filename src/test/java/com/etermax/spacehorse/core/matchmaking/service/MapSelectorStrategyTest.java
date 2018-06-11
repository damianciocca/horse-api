package com.etermax.spacehorse.core.matchmaking.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.matchmaking.model.selectors.MapSelectorStrategy;

public class MapSelectorStrategyTest {

	private int mmr1;
	private int mmr2;

	private Collection<MapDefinition> mapsCollection;
	private MapDefinition foundMap;
	private MapSelectorStrategy strategy;

	@Before
	public void setUp() {
		strategy = new MapSelectorStrategy();
	}

	@After
	public void tearDown() {
		strategy = null;
		foundMap = null;
		mapsCollection = null;
	}

	@Test(expected = IllegalArgumentException.class)
	public void anEmptyMapsCollectionFails() {
		givenEmptyMmrs();
		givenAnEmptyMapsCollection();

		whenFindingABestMap();
	}

	@Test
	public void aMapsCollectionWithASingleEntryAlwaysReturnsThatEntry() {
		givenASingleMapCollection(0);

		givenEmptyMmrs();
		whenFindingABestMap();
		thenAMapIsFound();

		givenMmrs(10, 10);
		whenFindingABestMap();
		thenAMapIsFound();

		givenMmrs(-10, 10);
		whenFindingABestMap();
		thenAMapIsFound();

		givenMmrs(-10, -10);
		whenFindingABestMap();
		thenAMapIsFound();
	}

	@Test
	public void theClosestMapIsAlwaysReturned() {
		givenAMapsCollection(0, 10, 20);

		givenMmrs(-5, -5);
		whenFindingABestMap();
		thenAMapWithMmrIsFound(0);

		givenMmrs(0, 0);
		whenFindingABestMap();
		thenAMapWithMmrIsFound(0);

		givenMmrs(0, 5);
		whenFindingABestMap();
		thenAMapWithMmrIsFound(0);

		givenMmrs(5, 5);
		whenFindingABestMap();
		thenAMapWithMmrIsFound(0);

		givenMmrs(5, 10);
		whenFindingABestMap();
		thenAMapWithMmrIsFound(10);

		givenMmrs(10, 10);
		whenFindingABestMap();
		thenAMapWithMmrIsFound(10);

		givenMmrs(15, 15);
		whenFindingABestMap();
		thenAMapWithMmrIsFound(10);

		givenMmrs(20, 20);
		whenFindingABestMap();
		thenAMapWithMmrIsFound(20);

		givenMmrs(20, 25);
		whenFindingABestMap();
		thenAMapWithMmrIsFound(20);

		givenMmrs(25, 25);
		whenFindingABestMap();
		thenAMapWithMmrIsFound(20);
	}

	private void thenAMapWithMmrIsFound(int mmr) {
		thenAMapIsFound();
		assertThat(mmr, is(equalTo(foundMap.getMmr())));
	}

	private void givenAMapsCollection(int... mmrs) {

		Stream<MapDefinition> mapDefinitionStream = Arrays.stream(mmrs).mapToObj(mmr -> {
			return new MapDefinition("MapWithMmr" + mmr, mmr);
		});

		mapsCollection = mapDefinitionStream.collect(Collectors.toList());

	}

	private void thenAMapIsFound() {
		assertThat(foundMap, is(notNullValue()));
	}

	private void givenASingleMapCollection(int mmr) {
		MapDefinition mapDefinition = new MapDefinition("map1", mmr);
		mapsCollection = Arrays.asList(mapDefinition);
	}

	private void givenEmptyMmrs() {
		mmr1 = 0;
		mmr2 = 0;
	}

	private void givenMmrs(int mmr1, int mmr2) {
		this.mmr1 = mmr1;
		this.mmr2 = mmr2;
	}

	private void whenFindingABestMap() {
		foundMap = strategy.getBestMap(mmr1, mmr2, mapsCollection);
	}

	private void givenAnEmptyMapsCollection() {
		mapsCollection = new ArrayList<>();
	}

}