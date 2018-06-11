package com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardsDropRate;
import com.etermax.spacehorse.core.player.model.Player;

public class CardDropRateCalculator {

	private static final double WINLOSE_BASE = 100.0;

	private static final double MIN_VALUE = 0.01;

	public static Map<CardDefinition, Double> calculateCardsDropRate(Player player, int mapNumber, CardDropRateCalculatorConfiguration configuration,
			List<CardDefinition> cards, List<CardsDropRate> cardsDropRate) {

		if (invalidParameters(player, configuration, cards, cardsDropRate)) {
			return Collections.emptyMap();
		}

		Map<CardDefinition, Double> dropRates = cards.stream()
				.map(card -> calculateCardDropRate(player, mapNumber, configuration, cardsDropRate, card))
				.collect(Collectors.toMap(keyValue -> keyValue.getKey(), keyValue -> keyValue.getValue()));

		return dropRates;
	}

	public static Map.Entry<CardDefinition, Double> calculateCardDropRate(Player player, int mapNumber,
			CardDropRateCalculatorConfiguration configuration, List<CardsDropRate> cardsDropRate, CardDefinition card) {

		int intendedCards = getIntendedCards(mapNumber, cardsDropRate, card);

		if (intendedCards <= 0) {
			return new AbstractMap.SimpleEntry<>(card, MIN_VALUE);
		}

		int alreadyOwnedCardsAmount = getAlreadyOwnedCardsAmount(player, card);

		// TODO: ADD WIN LOSE RATE IN FOLLOWING ITERATION
		double winLoseRatio = 0;//playerWinRate.getWinRate();

		boolean selected = player.getDeck().isCardSelected(card.getId());

		double dropRate = calculateCardDropRate(configuration, intendedCards, alreadyOwnedCardsAmount, winLoseRatio, selected);

		return new AbstractMap.SimpleEntry<>(card, dropRate);
	}

	private static double calculateCardDropRate(CardDropRateCalculatorConfiguration configuration, int intendedCards, int ownedCards,
			double winLoseRatio, boolean selected) {

		double dropDiffCards = calculateDropDiffCards(configuration, ownedCards, intendedCards);

		double dropWinRatio = calculateDropWinRatio(configuration, winLoseRatio, selected);

		return capDropRateToMinValue(dropDiffCards + dropWinRatio);
	}

	private static boolean invalidParameters(Player player, CardDropRateCalculatorConfiguration configuration,
			List<CardDefinition> cardDefinitionCatalog, List<CardsDropRate> cardsDropRateCatalog) {
		return player == null || configuration == null || cardDefinitionCatalog == null || cardsDropRateCatalog == null;
	}

	private static int getAlreadyOwnedCardsAmount(Player player, CardDefinition cardDefinition) {
		return player.getInventory().getCardParts().getAmount(cardDefinition.getId());
	}

	private static int getIntendedCards(int mapNumber, List<CardsDropRate> cardsDropRate, CardDefinition cardDefinition) {
		return findCardDropRateForCardAndMapNumber(cardsDropRate, cardDefinition, mapNumber).map(dr -> dr.getIntendedCards()).orElse(0);
	}

	private static Optional<CardsDropRate> findCardDropRateForCardAndMapNumber(List<CardsDropRate> cardsDropRate, CardDefinition cardDefinition,
			int mapNumber) {
		return cardsDropRate.stream().filter(dr -> dr.getCardId().equals(cardDefinition.getId()) && dr.getMapNumber() == mapNumber).findFirst();
	}

	public static double calculateDropDiffCards(CardDropRateCalculatorConfiguration configuration, int cardAmount, int intendedCards) {

		if (intendedCards == 0) {
			return 0;
		}

		return (((1 - (double) cardAmount / (double) intendedCards) * Math.pow(configuration.getDropDiffBase(), configuration.getDropDiffExp()))
				/ configuration.getDropDiffReducer());
	}

	public static double calculateDropWinRatio(CardDropRateCalculatorConfiguration configuration, double winLoseRatio, boolean selected) {
		double reference = (double) configuration.getWinLoseReference() / WINLOSE_BASE;
		if (winLoseRatio > reference) {
			return 0;
		}
		double ratioResult = (-1 / (1 + Math.exp(-(winLoseRatio - 0.5) * 10)) + 1);
		if (selected) {
			return (1 - ratioResult) / configuration.getWinLoseReducer();
		} else {
			return ratioResult / configuration.getWinLoseReducer();
		}
	}

	private static double capDropRateToMinValue(double totalDrop) {
		if (totalDrop < 0) {
			return MIN_VALUE;
		}
		return totalDrop;
	}

}
