package com.etermax.spacehorse.core.capitain.model;

import com.etermax.spacehorse.core.player.model.Player;

public interface CaptainCollectionRepository {

	CaptainsCollection findOrDefaultBy(Player player);

	void addOrUpdate(CaptainsCollection captainsCollection);
}
