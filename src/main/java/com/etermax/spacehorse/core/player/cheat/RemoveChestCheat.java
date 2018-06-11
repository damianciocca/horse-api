package com.etermax.spacehorse.core.player.cheat;

import java.util.Optional;

import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.model.Player;

public class RemoveChestCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "removeChest";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		long id = getParameterLong(parameters, 0);

		Optional<Chest> chestOptional = player.getInventory().getChests().findChestById(id);

		if (chestOptional.isPresent()) {
			Chest chest = chestOptional.get();
			player.getInventory().getChests().removeChest(chest);
			return new CheatResponse(new ChestResponse(chest));
		}
		return null;
	}
}
