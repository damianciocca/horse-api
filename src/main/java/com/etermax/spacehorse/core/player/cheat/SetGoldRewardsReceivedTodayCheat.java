package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.resource.response.player.progress.RewardsReceivedTodayResponse;

public class SetGoldRewardsReceivedTodayCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "setGoldRewardsReceivedToday";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {

		int amount = getParameterInt(parameters, 0);
		long serverTime = getParameterLong(parameters, 1);

		if (amount >= 0) {
			player.getRewardsReceivedToday().setGoldRewardsReceivedToday(amount);
			player.getRewardsReceivedToday().setExpirationServerTime(serverTime);
			return new CheatResponse(new RewardsReceivedTodayResponse(player.getRewardsReceivedToday()));
		}
		return null;
	}
}
