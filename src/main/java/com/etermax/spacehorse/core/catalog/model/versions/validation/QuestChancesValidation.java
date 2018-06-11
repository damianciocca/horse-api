package com.etermax.spacehorse.core.catalog.model.versions.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.QuestChancesList;
import com.etermax.spacehorse.core.catalog.model.QuestDefinitionWithChance;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;

public class QuestChancesValidation implements CatalogValidation {

	@Override
	public boolean validate(Catalog catalog) {

		List<MapDefinition> maps = catalog.getMapsCollection().getEntries();
		List<QuestChancesList> questChancesLists = catalog.getQuestChancesListCollection().getEntries();

		List<QuestDifficultyType> difficulties = Arrays.asList(QuestDifficultyType.values());

		for (MapDefinition map : maps) {
			for (QuestDifficultyType difficulty : difficulties) {
				if (!existsQuestChancesForDifficultyAndMap(questChancesLists, difficulty, map)) {
					throw new CatalogException("No quest chances exists for map " + map.getMapNumber() + " and difficulty " + difficulty);
				}
			}
		}

		return true;
	}

	private boolean existsQuestChancesForDifficultyAndMap(List<QuestChancesList> questChancesLists, QuestDifficultyType difficulty,
			MapDefinition map) {
		List<QuestDefinitionWithChance> definitionsWithChances = questChancesLists.stream()
				.filter(x -> isSameMapNumberAndDifficulty(difficulty, map, x)).findFirst().
						map(x -> x.getQuestDefinitionWithChances()).orElse(new ArrayList<>());

		List<QuestDefinitionWithChance> definitionsWithChancesAboveZero = definitionsWithChances.stream().filter(x -> x.getChance() > 0)
				.collect(Collectors.toList());

		return definitionsWithChancesAboveZero.size() > 0;
	}

	private boolean isSameMapNumberAndDifficulty(QuestDifficultyType difficulty, MapDefinition map, QuestChancesList x) {
		return x.getMapNumber() == map.getMapNumber() && x.getDifficulty().equals(difficulty.toString());
	}
}
