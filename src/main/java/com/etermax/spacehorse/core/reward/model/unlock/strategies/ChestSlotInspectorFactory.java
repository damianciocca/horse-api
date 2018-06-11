package com.etermax.spacehorse.core.reward.model.unlock.strategies;

import com.etermax.spacehorse.core.reward.model.unlock.ChestSlotsConfiguration;

public class ChestSlotInspectorFactory {

	public ChestSlotsInspector create(boolean enableFeaturesByPlayerLvl, ChestSlotsConfiguration configuration) {
		if (enableFeaturesByPlayerLvl) {
			return new AvailableByPlayerLevelChestSlotsInspector(configuration);
		}
		return new DefaultChestSlotsInspector(configuration.getMaxChestSlots());
	}
}
