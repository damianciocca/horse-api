package com.etermax.spacehorse.core.reward.model.unlock;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.unlock.FeatureByPlayerLvlType;
import com.etermax.spacehorse.core.catalog.model.unlock.FeaturesByPlayerLvlDefinition;

public class ChestSlotsConfiguration {

	private final int maxChestSlots;
	private final List<MaxChestSlotsByPlayerLevel> maxChestSlotsByPlayerLevels;

	public ChestSlotsConfiguration(int maxChestSlots, List<MaxChestSlotsByPlayerLevel> maxChestSlotsByPlayerLevels) {
		this.maxChestSlots = maxChestSlots;
		this.maxChestSlotsByPlayerLevels = maxChestSlotsByPlayerLevels;
	}

	public static ChestSlotsConfiguration create(List<FeaturesByPlayerLvlDefinition> featuresByPlayerLvlDefinitions, int maxChestSlots) {
		List<MaxChestSlotsByPlayerLevel> maxAvailableChestSlotsByPlayerLvl = featuresByPlayerLvlDefinitions.stream().filter(byCourierSlotType()) //
				.map(toMaxAvailableChestSlotsByPlayerLevel())//
				.collect(Collectors.toList());//
		return new ChestSlotsConfiguration(maxChestSlots, maxAvailableChestSlotsByPlayerLvl);
	}

	private static Predicate<FeaturesByPlayerLvlDefinition> byCourierSlotType() {
		return featuresByPlayerLvlDefinition -> FeatureByPlayerLvlType.COURIER_SLOT.getId().equals(featuresByPlayerLvlDefinition.getFeatureType());
	}

	private static Function<FeaturesByPlayerLvlDefinition, MaxChestSlotsByPlayerLevel> toMaxAvailableChestSlotsByPlayerLevel() {
		return chestSlotsByPlayerLvlDef -> new MaxChestSlotsByPlayerLevel(chestSlotsByPlayerLvlDef.getAvailableOnLevel(),
				chestSlotsByPlayerLvlDef.getCourierMaxSlots());
	}

	public int getMaxChestSlots() {
		return maxChestSlots;
	}

	public List<MaxChestSlotsByPlayerLevel> getMaxAvailableChestSlotsByPlayerLevel() {
		return maxChestSlotsByPlayerLevels;
	}
}