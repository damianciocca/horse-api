package com.etermax.spacehorse.core.captain;

import java.util.Map;

import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.player.model.Player;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class InMemoryCaptainCollectionRepository implements CaptainCollectionRepository {

	private Map<String, CaptainsCollection> captainsByUserIds = Maps.newConcurrentMap();

	@Override
	public CaptainsCollection findOrDefaultBy(Player player) {
		if (captainsByUserIds.containsKey(player.getUserId())) {
			return captainsByUserIds.get(player.getUserId());
		}
		return new CaptainsCollection(player.getUserId(), Lists.newArrayList(), "");
	}

	@Override
	public void addOrUpdate(CaptainsCollection captainsCollection) {
		captainsByUserIds.put(captainsCollection.getUserId(), captainsCollection);
	}
}
