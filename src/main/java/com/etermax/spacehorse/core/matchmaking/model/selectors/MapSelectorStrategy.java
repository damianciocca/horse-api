package com.etermax.spacehorse.core.matchmaking.model.selectors;

import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import org.apache.commons.lang3.Validate;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapSelectorStrategy {

	public MapDefinition getBestMap(int mmr1, int mmr2, Collection<MapDefinition> mapsCollection) {
		return getBestMap(Math.max(mmr1, mmr2), mapsCollection);
	}

	public MapDefinition getBestMap(int mmr, Collection<MapDefinition> mapsCollection) {

		Validate.notNull(mapsCollection);
		Validate.notEmpty(mapsCollection);

		List<MapDefinition> sortedMaps = mapsCollection.stream()
				.sorted(Comparator.comparingInt(MapDefinition::getMmr).reversed()).collect(Collectors.toList());

		Optional<MapDefinition> highestMmrMap = sortedMaps.stream().filter(m -> m.getMmr() <= mmr).findFirst();

		MapDefinition map = highestMmrMap.orElseGet(() -> sortedMaps.get(sortedMaps.size() - 1));

		return map;
	}

}
