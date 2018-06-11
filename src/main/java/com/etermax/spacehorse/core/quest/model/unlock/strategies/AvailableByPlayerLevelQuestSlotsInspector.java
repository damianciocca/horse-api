package com.etermax.spacehorse.core.quest.model.unlock.strategies;

import static java.util.Comparator.comparing;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.model.unlock.AvailableQuestSlotDifficultiesByPlayerLevel;
import com.etermax.spacehorse.core.quest.model.unlock.QuestSlotDifficultiesConfiguration;

public class AvailableByPlayerLevelQuestSlotsInspector implements QuestSlotsInspector {

	private final QuestSlotDifficultiesConfiguration configuration;

	public AvailableByPlayerLevelQuestSlotsInspector(QuestSlotDifficultiesConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public boolean isAvailable(int playerLevel, QuestDifficultyType currentQuestDifficultyType) {
		Optional<AvailableQuestSlotDifficultiesByPlayerLevel> availableQuestSlotsDifficultiesByPlayerLvl = getAvailableSlotsDifficultiesByLvlFromConfiguration(
				playerLevel);
		return availableQuestSlotsDifficultiesByPlayerLvl.isPresent() //
				&& isAvailableSlotsPerLevel(currentQuestDifficultyType, availableQuestSlotsDifficultiesByPlayerLvl.get());
	}

	private boolean isAvailableSlotsPerLevel(QuestDifficultyType currentQuestDifficultyType,
			AvailableQuestSlotDifficultiesByPlayerLevel availableQuestDifficultiesByPlayerLvl) {
		return availableQuestDifficultiesByPlayerLvl.contains(currentQuestDifficultyType);
	}

	private Optional<AvailableQuestSlotDifficultiesByPlayerLevel> getAvailableSlotsDifficultiesByLvlFromConfiguration(int playerLevel) {
		return configuration.getAvailableQuestDifficultiesByPlayerLvl().stream()//
				.filter(byLessOrEqualsThan(playerLevel)) //
				.sorted(byPlayerLevelDesc())//
				.findFirst();
	}

	private Comparator<AvailableQuestSlotDifficultiesByPlayerLevel> byPlayerLevelDesc() {
		return comparing(AvailableQuestSlotDifficultiesByPlayerLevel::getPlayerLevel).reversed();
	}

	private Predicate<AvailableQuestSlotDifficultiesByPlayerLevel> byLessOrEqualsThan(int playerLevel) {
		return availableQuestSlotsDifficultiesByPlayerLvl -> availableQuestSlotsDifficultiesByPlayerLvl.getPlayerLevel() <= playerLevel;
	}
}
