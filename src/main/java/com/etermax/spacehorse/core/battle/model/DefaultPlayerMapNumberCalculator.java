package com.etermax.spacehorse.core.battle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.etermax.spacehorse.core.catalog.model.MapDefinition;

public class DefaultPlayerMapNumberCalculator implements PlayerMapNumberCalculator {

	private final List<MapDefinition> mapsCollection;

	public DefaultPlayerMapNumberCalculator(List<MapDefinition> mapsCollection) {
		this.mapsCollection = mapsCollection;
	}

	@Override
	public int getMapNumber(int mmr) {
		return getMapNumber(mapsCollection, mmr);
	}

	private int getMapNumber(List<MapDefinition> mapsCollection, int mmr) {
		Validate.notNull(mapsCollection);

		//Clone before sorting
		List<MapDefinition> sortedMapsCollection = new ArrayList<>(mapsCollection);
		Collections.sort(sortedMapsCollection);

		int mapNumber = -1;

		Iterator<MapDefinition> it = mapsCollection.iterator();
		while (it.hasNext()) {
			MapDefinition map = it.next();
			if (mmr >= map.getMmr()) {
				mapNumber = map.getMapNumber();
			} else {
				break;
			}
		}
		return mapNumber;
	}
}
