package com.etermax.spacehorse.core.reward.model.unlock.strategies;

import com.etermax.spacehorse.core.player.model.inventory.chest.Chests;

public interface ChestSlotsInspector {

	boolean hasAvailable(int playerLevel, Chests filledChestSlots);
}
