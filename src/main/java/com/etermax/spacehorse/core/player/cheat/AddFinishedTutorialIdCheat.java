package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class AddFinishedTutorialIdCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "addFinishedTutorial";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		String tutorialId = getParameterString(parameters, 0);
		Boolean tutorialIdActive = player.isTutorialIdActive(tutorialId);
		if (tutorialIdActive) {
			player.finishActiveTutorial();
		} else {
			player.addFinishedTutorial(tutorialId);
		}
		return new CheatResponse(tutorialId);
	}
}
