package com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;

public class RandomCardSelector {

	public static Optional<CardDefinition> chooseCard(Map<CardDefinition, Double> cardsDropRate) {

		if (cardsDropRate.isEmpty()) {
			return Optional.empty();
		}

		double cardsDropRateSum = cardsDropRate.values().stream().mapToDouble(dropRate -> dropRate).sum();

		double random = ThreadLocalRandom.current().nextDouble() * cardsDropRateSum;

		for (Map.Entry<CardDefinition, Double> entry : cardsDropRate.entrySet()) {
			if (random <= entry.getValue()) {
				return Optional.of(entry.getKey());
			}
			random -= entry.getValue();
		}

		//Since floating point operations have precision limits, we leave this fallback that should happen only when trying to select the last entry
		//in the collection.
		CardDefinition lastCardDefinition = getLastCardDefinition(cardsDropRate);

		return Optional.ofNullable(lastCardDefinition);
	}

	private static CardDefinition getLastCardDefinition(Map<CardDefinition, Double> cardsDropRate) {
		return cardsDropRate.entrySet().stream().reduce((a, b) -> b).map(Map.Entry::getKey).orElse(null);
	}
}
