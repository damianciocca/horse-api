package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;

public class SetPlayerXpCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "setPlayerXp";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		int xp = getParameterInt(parameters, 0);
		if (xp >= 0) {
			player.getProgress().cheatSetXp(xp, catalog.getPlayerLevelsCollection());
			return new CheatResponse(player.getProgress().getXp());
		}
		return null;
	}
}
