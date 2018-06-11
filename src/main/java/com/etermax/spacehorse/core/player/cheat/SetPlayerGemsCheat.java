package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;

public class SetPlayerGemsCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "setPlayerGems";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		int gems = getParameterInt(parameters, 0);
		if (gems >= 0) {
			player.getInventory().getGems().setAmount(gems);
			return new CheatResponse(gems);
		}
		return null;
	}
}
