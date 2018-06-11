package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;

public class SetPlayerBotMmrCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "setPlayerBotMmr";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		int mmr = getParameterInt(parameters, 0);
		player.setBotMmr(mmr);
		return new CheatResponse(mmr);
	}
}
