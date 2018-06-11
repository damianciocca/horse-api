package com.etermax.spacehorse.core.catalog.model.versions.validation;

import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.catalog.model.QuestCycleList;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogValidation;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;

public class GameConstantsValidation implements CatalogValidation {

	@Override
	public boolean validate(Catalog catalog) {
		GameConstants gameConstants = catalog.getGameConstants();
		if (gameConstants.getNumberOfCardsInDeck() <= 0) {
			throw new CatalogException("The number of cards in deck must be >= 1");
		}
		if (gameConstants.getStartingGold() < 0) {
			throw new CatalogException("Starting gold must be >= 0");
		}
		if (gameConstants.getStartingGems() < 0) {
			throw new CatalogException("Starting gems must be >= 0");
		}
		if (gameConstants.getStartingCards().size() < gameConstants.getNumberOfCardsInDeck()) {
			throw new CatalogException("The number of starting cards must be at least equal to the number of cards in deck");
		}
		List<String> invalidStartingCards = gameConstants.getStartingCards().stream()
				.filter(cardId -> catalog.getCardDefinitionsCollection().findById(cardId) == null).collect(Collectors.toList());
		if (invalidStartingCards.size() > 0) {
			throw new CatalogException("Invalid starting cards: " + String.join(",", invalidStartingCards));
		}
		if (!catalog.getChestsListsCollection().findById(gameConstants.getDefaultChestRewardSequenceId()).isPresent()) {
			throw new CatalogException("Invalid default chest reward sequence id: " + gameConstants.getDefaultChestRewardSequenceId());
		}
		if (!catalog.getChestDefinitionsCollection().findById(gameConstants.getFreeChestId()).isPresent()) {
			throw new CatalogException("Invalid free chest id: " + gameConstants.getFreeChestId());
		}
		if (catalog.getGameConstants().getTimeBetweenFreeChestsInSeconds() <= 0) {
			throw new CatalogException("Invalid time between free chests in seconds: " + gameConstants.getTimeBetweenFreeChestsInSeconds());
		}
		if (catalog.getGameConstants().getMaxFreeChests() < 1) {
			throw new CatalogException("Invalid amount of max free chests: " + gameConstants.getMaxFreeChests());
		}

		List<QuestCycleList> questCycleLists = catalog.getQuestCycleListCollection().getEntries().stream()
				.filter(questCycleList -> questCycleList.getListId().equals(gameConstants.getDefaultQuestCycleSequenceId()))
				.collect(Collectors.toList());

		for (QuestDifficultyType difficulty : QuestDifficultyType.values()) {
			if (questCycleLists.stream().noneMatch(x -> x.getDifficulty().equals(difficulty.toString()))) {
				throw new CatalogException(
						"Missing entries for difficuly " + difficulty + " in quest cycle list " + gameConstants.getDefaultQuestCycleSequenceId());
			}
		}

		validateCurrentDailyQuestId(catalog);

		gameConstants.getCardDropRateCalculatorConfiguration().validate();

		gameConstants.getBotMmrAlgorithmConfiguration().validate();

		validateStartingCaptains(catalog, gameConstants.getStartingCaptains());

		return false;
	}

	private void validateCurrentDailyQuestId(Catalog catalog) {
		String currentDailyQuestId = catalog.getGameConstants().getCurrentDailyQuestId();
		if(!catalog.getDailyQuestCollection().findById(currentDailyQuestId).isPresent()){
			throw new CatalogException("CurrentDailyQuestId no match with any dailyQuestDefinition id");
		}
	}

	private void validateStartingCaptains(Catalog catalog, List<String> startingCaptains) {
		if (startingCaptains.size() < 1) {
			throw new CatalogException("The number of starting captains must be at least 1");
		}
		startingCaptains.forEach(captainId -> catalog.getCaptainDefinitionsCollection().findByIdOrFail(captainId));
	}
}
