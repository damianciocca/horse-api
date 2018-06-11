package com.etermax.spacehorse.core.player.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

import java.util.Optional;

public class SetChestOpeningTime extends Cheat {

	private final ServerTimeProvider timeProvider;

	@Override
	public String getCheatId() {
		return "setChestOpeningTime";
	}

	public SetChestOpeningTime(ServerTimeProvider timeProvider) {
		this.timeProvider = timeProvider;
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		long chestId = getParameterLong(parameters, 0);
		long openingTime = getParameterLong(parameters, 1);
		Optional<Chest> chest = player.getInventory().getChests().findChestById(chestId);
		if (chest.isPresent()) {
			if (openingTime < 0) {
				chest.get().setChestOpeningStartDate(null);
				chest.get().setChestOpeningEndDate(null);
			} else {
				final long serverTime = timeProvider.getTimeNowAsSeconds();
				chest.get().setChestOpeningStartDate(ServerTime.toDate(serverTime));
				chest.get().setChestOpeningEndDate(ServerTime.toDate(serverTime + openingTime));
			}
			return new CheatResponse(new ChestResponse(chest.get()));
		}
		return null;
	}

}
