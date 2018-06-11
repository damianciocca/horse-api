package com.etermax.spacehorse.core.freechest.cheat;

import java.util.Date;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.ServerTime;

public class SetFreeChestOpeningTimeCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "setFreeChestOpeningTime";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		Integer openingServerTime = getParameterInt(parameters, 0);
		player.getFreeChest().setLastChestOpeningDate(ServerTime.toDate(openingServerTime));
		return new CheatResponse(openingServerTime);
	}

}
