package com.etermax.spacehorse.core.quest.model.unlock;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.unlock.FeatureByPlayerLvlType;
import com.etermax.spacehorse.core.catalog.model.unlock.FeaturesByPlayerLvlDefinition;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;

public class QuestSlotDifficultiesConfiguration {

	private final List<AvailableQuestSlotDifficultiesByPlayerLevel> availableQuestSlotDifficultiesByPlayerLevels;

	public QuestSlotDifficultiesConfiguration(List<AvailableQuestSlotDifficultiesByPlayerLevel> availableQuestSlotDifficultiesByPlayerLevels) {
		this.availableQuestSlotDifficultiesByPlayerLevels = availableQuestSlotDifficultiesByPlayerLevels;

	}

	public static QuestSlotDifficultiesConfiguration create(List<FeaturesByPlayerLvlDefinition> featuresByPlayerLvlDefinitions) {
		List<AvailableQuestSlotDifficultiesByPlayerLevel> availableQuestDifficultyByPlayerLvl = featuresByPlayerLvlDefinitions.stream()
				.filter(byQuestSlotType()) //
				.map(toAvailableQuestSlotsDifficultiesByPlayerLevel())//
				.collect(Collectors.toList());//
		return new QuestSlotDifficultiesConfiguration(availableQuestDifficultyByPlayerLvl);
	}

	private static Predicate<FeaturesByPlayerLvlDefinition> byQuestSlotType() {
		return featuresByPlayerLvlDefinition -> FeatureByPlayerLvlType.QUEST_SLOT.getId().equals(featuresByPlayerLvlDefinition.getFeatureType());
	}

	private static Function<FeaturesByPlayerLvlDefinition, AvailableQuestSlotDifficultiesByPlayerLevel> toAvailableQuestSlotsDifficultiesByPlayerLevel() {
		return slotsDifficultiesByPlayerLvlDef -> new AvailableQuestSlotDifficultiesByPlayerLevel(
				slotsDifficultiesByPlayerLvlDef.getAvailableOnLevel(),
				QuestDifficultyType.getTypesUpTo(slotsDifficultiesByPlayerLvlDef.getQuestMaxDifficulty()));
	}

	public List<AvailableQuestSlotDifficultiesByPlayerLevel> getAvailableQuestDifficultiesByPlayerLvl() {
		return availableQuestSlotDifficultiesByPlayerLevels;
	}
}