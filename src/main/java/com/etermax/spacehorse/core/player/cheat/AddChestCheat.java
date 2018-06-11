package com.etermax.spacehorse.core.player.cheat;

import java.util.Optional;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.etermax.spacehorse.core.reward.model.unlock.ChestSlotsConfiguration;
import com.etermax.spacehorse.core.reward.model.unlock.strategies.ChestSlotInspectorFactory;
import com.etermax.spacehorse.core.reward.model.unlock.strategies.ChestSlotsInspector;

public class AddChestCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "addChest";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		String chestId = getParameterString(parameters, 0);
		int mapNumber = parameters.length > 1 ? getParameterInt(parameters, 1) : 0;
		Optional<ChestDefinition> chestDefinition = catalog.getChestDefinitionsCollection().findById(chestId);
		if (chestDefinition.isPresent()) {
			ChestSlotsInspector chestSlotsInspector = newChestSlotsInspector(catalog);
			Chest chest = player.getInventory().getChests()
					.addChest(chestId, mapNumber, catalog.getGameConstants(), getPlayerLevelOf(player), chestSlotsInspector);
			if (chest != null) {
				return new CheatResponse(new ChestResponse(chest));
			}
		}
		return null;
	}

	private ChestSlotsInspector newChestSlotsInspector(Catalog catalog) {
		ChestSlotsConfiguration configuration = ChestSlotsConfiguration
				.create(catalog.getFeatureByPlayerLvlDefinitionCollection().getEntries(), catalog.getGameConstants().getMaxChests());
		return new ChestSlotInspectorFactory().create(catalog.getGameConstants().isUseFeaturesByPlayerLvl(), configuration);
	}

	private int getPlayerLevelOf(Player player) {
		return player.getProgress().getLevel();
	}
}
