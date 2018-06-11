package com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;

public class RandomCardsSelectorInPartitions {

	public static Map<CardDefinition, Integer> chooseCards(Map<CardDefinition, Double> cardsDropRate, int cardMaxPartitions, int cardAmount) {

		if (cardsDropRate.isEmpty() || cardMaxPartitions <= 0 || cardAmount <= 0) {
			return Collections.emptyMap();
		}

		int partitions = capPartitionsToAvailableCards(cardsDropRate, cardMaxPartitions);

		Map<CardDefinition, Double> chosenCardsWithDropRate = chooseCards(cardsDropRate, partitions);

		Map<CardDefinition, Integer> chosenCardsAmount = calculateChosenCardsAmount(chosenCardsWithDropRate, cardAmount);

		return chosenCardsAmount;
	}

	private static Map<CardDefinition, Integer> calculateChosenCardsAmount(Map<CardDefinition, Double> chosenCardsWithDropRate, int totalAmount) {

		//By normalizing all the drop rates to the 0..1 range, we can simply multiply the normalized rate of each card with totalAmount to get the
		//number of cards that should be chosen.
		Map<CardDefinition, Double> normalizedDropRate = normalizeDropRates(chosenCardsWithDropRate);

		Map<CardDefinition, Integer> chosenCardsAmount = normalizedDropRate.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, keyValue -> (int) (keyValue.getValue() * totalAmount)));

		if (!chosenCardsAmount.isEmpty()) {
			// Handle truncation errors by adding remaining amount to the card with the highest amount
			int chosenAmount = chosenCardsAmount.entrySet().stream().mapToInt(Map.Entry::getValue).sum();
			if (chosenAmount != totalAmount) {
				CardDefinition cardWithMaxAmount = chosenCardsAmount.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get()
						.getKey();
				int cardWithMaxAmountAmount = chosenCardsAmount.get(cardWithMaxAmount);
				chosenCardsAmount.put(cardWithMaxAmount, cardWithMaxAmountAmount + (totalAmount - chosenAmount));
			}
		}
		return chosenCardsAmount;
	}

	private static int capPartitionsToAvailableCards(Map<CardDefinition, Double> cardsDropRate, int cardMaxPartitions) {
		return Math.min(cardMaxPartitions, cardsDropRate.keySet().size());
	}

	private static Map<CardDefinition, Double> chooseCards(Map<CardDefinition, Double> cardsDropRate, int partitions) {
		Map<CardDefinition, Double> rouletteCards = new HashMap();
		Map<CardDefinition, Double> auxDropRate = new HashMap(cardsDropRate);

		for (int i = 0; i < partitions; i++) {
			RandomCardSelector.chooseCard(auxDropRate).ifPresent(card -> {
				auxDropRate.remove(card);
				rouletteCards.put(card, cardsDropRate.get(card));
			});
		}
		return rouletteCards;
	}

	private static Map<CardDefinition, Double> normalizeDropRates(Map<CardDefinition, Double> dropRates) {
		double sum = dropRates.entrySet().stream().mapToDouble(x -> x.getValue()).sum();
		Map<CardDefinition, Double> returnVals = new HashMap<>();
		for (Map.Entry<CardDefinition, Double> entry : dropRates.entrySet()) {
			returnVals.put(entry.getKey(), entry.getValue() / sum);
		}
		return returnVals;
	}

}
