package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;

public class SetPlayerGoldCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "setPlayerGold";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		int gold = getParameterInt(parameters, 0);
		if (gold >= 0) {
			player.getInventory().getGold().setAmount(gold);
			return new CheatResponse(gold);
		}
		return null;
	}
}
