package com.etermax.spacehorse.core.capitain.actions;

import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.player.model.Player;

public class SelectCaptainAction {

	private final CaptainCollectionRepository captainCollectionRepository;

	public SelectCaptainAction(CaptainCollectionRepository captainCollectionRepository) {
		this.captainCollectionRepository = captainCollectionRepository;
	}

	public void selectCaptain(Player player, String captainId) {
		CaptainsCollection captainsCollection = captainCollectionRepository.findOrDefaultBy(player);
		captainsCollection.selectCaptain(captainId);
		captainCollectionRepository.addOrUpdate(captainsCollection);
	}
}
