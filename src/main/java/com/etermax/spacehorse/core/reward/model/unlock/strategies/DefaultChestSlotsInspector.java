package com.etermax.spacehorse.core.reward.model.unlock.strategies;

import com.etermax.spacehorse.core.player.model.inventory.chest.Chests;

public class DefaultChestSlotsInspector implements ChestSlotsInspector {

	private final int maxChestSlots;

	public DefaultChestSlotsInspector(int maxChestSlots) {
		this.maxChestSlots = maxChestSlots;
	}

	@Override
	public boolean hasAvailable(int playerLevel, Chests filledChestSlots) {
		return slotsAreNotFull(filledChestSlots);
	}

	private boolean slotsAreNotFull(Chests filledSlots) {
		return !(filledSlots.getChests().size() >= maxChestSlots);
	}

}
