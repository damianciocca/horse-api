package com.etermax.spacehorse.core.quest.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.QuestChancesList;
import com.etermax.spacehorse.core.catalog.model.QuestCycleEntry;
import com.etermax.spacehorse.core.catalog.model.QuestCycleList;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.catalog.model.QuestDefinitionWithChance;
import com.etermax.spacehorse.core.quest.exception.NoQuestAvailableForCurrentSlotAndMap;

public class DefaultNextQuestSelector implements NextQuestSelector {

	@Override
	public QuestDefinition getNextQuest(NextQuestSelectorConfiguration configuration, String slotId, int slotSequence) {

		List<QuestCycleEntry> questCycleEntries = getQuestCycleEntries(slotId, configuration.getListId(), configuration.getQuestCycleLists());

		if (isSequenceEndReached(slotSequence, questCycleEntries.size())) {
			return getRandomQuestDefinition(slotId, configuration.getQuestChancesList(), configuration.getPlayerMapNumber());
		} else {
			return getQuestDefinitionAtCurrentSequence(slotSequence, questCycleEntries);
		}
	}

	@Override
	public QuestDefinition getDailyQuest(DailyQuestSelectorConfiguration configuration) {
		return configuration.findActiveDailyQuestDefinition();
	}

	private QuestDefinition getRandomQuestDefinition(String slotId, CatalogEntriesCollection<QuestChancesList> questChancesList, int mapNumber) {

		List<QuestDefinitionWithChance> questDefinitionsWithChances = getQuestDefinitionsWithChances(slotId, questChancesList, mapNumber);

		List<QuestDefinitionWithChance> questDefinitionsWithChancesAboveZero = questDefinitionsWithChances.stream().filter(x -> x.getChance() > 0)
				.collect(Collectors.toList());

		if (questDefinitionsWithChancesAboveZero.size() == 0) {
			throw new NoQuestAvailableForCurrentSlotAndMap(slotId, mapNumber);
		}

		return selectRandomFromList(questDefinitionsWithChancesAboveZero);
	}

	private QuestDefinition selectRandomFromList(List<QuestDefinitionWithChance> questDefinitions) {
		int chancesSum = questDefinitions.stream().mapToInt(x -> x.getChance()).sum();

		int chance = ThreadLocalRandom.current().nextInt(chancesSum);

		for (QuestDefinitionWithChance questDefinitionWithChance : questDefinitions) {
			if (chance < questDefinitionWithChance.getChance())
				return questDefinitionWithChance.getQuestDefinition();
			chance -= questDefinitionWithChance.getChance();
		}

		return questDefinitions.get(questDefinitions.size() - 1).getQuestDefinition();
	}

	private List<QuestDefinitionWithChance> getQuestDefinitionsWithChances(String slotId, CatalogEntriesCollection<QuestChancesList> questChancesList,
			int mapNumber) {
		return questChancesList.getEntries().stream().filter(chancesList -> isSameSlotAndMapNumber(slotId, mapNumber, chancesList)).findFirst()
				.map(questChanceList -> questChanceList.getQuestDefinitionWithChances())
				.orElseThrow(() -> new NoQuestAvailableForCurrentSlotAndMap(slotId, mapNumber));
	}

	private boolean isSameSlotAndMapNumber(String slotId, int mapNumber, QuestChancesList chancesList) {
		return chancesList.getDifficulty().equals(slotId) && chancesList.getMapNumber() == mapNumber;
	}

	private QuestDefinition getQuestDefinitionAtCurrentSequence(int slotSequence, List<QuestCycleEntry> questCycleEntries) {
		return questCycleEntries.stream().filter(currentSequenceOrder(slotSequence)).findFirst().orElseThrow(throwExceptionWhenQuestIdIsNotFound())
				.getQuestDefinition();
	}

	private boolean isSequenceEndReached(int sequenceOrder, int sequenceSize) {
		return sequenceOrder > (sequenceSize - 1);
	}

	private Predicate<QuestCycleEntry> currentSequenceOrder(int currentSequence) {
		return questCycleEntry -> Objects.equals(questCycleEntry.getSequenceOrder(), currentSequence);
	}

	private Supplier<RuntimeException> throwExceptionWhenQuestIdIsNotFound() {
		return () -> new RuntimeException("Unexpected error when trying to get the quest id in quest cycle configuration");
	}

	public List<QuestCycleEntry> getQuestCycleEntries(String slotId, String listId, CatalogEntriesCollection<QuestCycleList> questCycleLists) {
		return questCycleLists.getEntries().stream().filter(questCycleList -> isSameDifficultyAndListId(slotId, listId, questCycleList)).findFirst()
				.map(questCycleList -> questCycleList.getEntries()).orElse(emptyList());
	}

	private ArrayList<QuestCycleEntry> emptyList() {
		return new ArrayList<>();
	}

	private boolean isSameDifficultyAndListId(String slotId, String listId, QuestCycleList questCycleList) {
		return questCycleList.getListId().equals(listId) && questCycleList.getDifficulty().equals(slotId);
	}

}
