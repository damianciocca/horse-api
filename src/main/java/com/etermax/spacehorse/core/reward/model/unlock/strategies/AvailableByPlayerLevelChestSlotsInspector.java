package com.etermax.spacehorse.core.reward.model.unlock.strategies;

import static java.util.Comparator.comparing;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import com.etermax.spacehorse.core.player.model.inventory.chest.Chests;
import com.etermax.spacehorse.core.reward.model.unlock.ChestSlotsConfiguration;
import com.etermax.spacehorse.core.reward.model.unlock.MaxChestSlotsByPlayerLevel;

public class AvailableByPlayerLevelChestSlotsInspector implements ChestSlotsInspector {

	private final ChestSlotsConfiguration configuration;

	public AvailableByPlayerLevelChestSlotsInspector(ChestSlotsConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public boolean hasAvailable(int playerLevel, Chests filledChestSlots) {
		return slotsAreNotFull(filledChestSlots) && existAnyAvailableSlotsFor(playerLevel, filledChestSlots);
	}

	private boolean existAnyAvailableSlotsFor(int playerLevel, Chests filledChestSlots) {
		Optional<MaxChestSlotsByPlayerLevel> maxChestSlotsByPlayerLevel = getMaxAvailableSlotsByLvlFromConfiguration(playerLevel);
		return maxChestSlotsByPlayerLevel.isPresent() //
				&& areNotReachedTheMaxAvailableSlotsPerLevel(filledChestSlots, maxChestSlotsByPlayerLevel.get());
	}

	private boolean areNotReachedTheMaxAvailableSlotsPerLevel(Chests filledChestSlots, MaxChestSlotsByPlayerLevel maxChestSlotsByPlayerLevel) {
		return filledChestSlots.getChests().size() < maxChestSlotsByPlayerLevel.getMaxSlotsAvailable();
	}

	private boolean slotsAreNotFull(Chests filledSlots) {
		return !(filledSlots.getChests().size() >= configuration.getMaxChestSlots());
	}

	private Optional<MaxChestSlotsByPlayerLevel> getMaxAvailableSlotsByLvlFromConfiguration(int playerLevel) {
		return configuration.getMaxAvailableChestSlotsByPlayerLevel().stream()//
				.filter(byLessOrEqualsThan(playerLevel)) //
				.sorted(byPlayerLevelDesc())//
				.findFirst();
	}

	private Comparator<MaxChestSlotsByPlayerLevel> byPlayerLevelDesc() {
		return comparing(MaxChestSlotsByPlayerLevel::getPlayerLevel).reversed();
	}

	private Predicate<MaxChestSlotsByPlayerLevel> byLessOrEqualsThan(int playerLevel) {
		return maxChestSlotsByPlayerLvl -> maxChestSlotsByPlayerLvl.getPlayerLevel() <= playerLevel;
	}
}
