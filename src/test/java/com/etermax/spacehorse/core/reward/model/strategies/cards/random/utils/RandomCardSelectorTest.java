package com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;

public class RandomCardSelectorTest {

	static private final String CARD_1_ID = "card_1";
	static private final String CARD_2_ID = "card_2";

	static private final double CARD_1_CHANCE = 0.2;
	static private final double CARD_2_CHANCE = 0.8;
	static private final double CHANCE_TOLERANCE = 0.1;

	static private final int ITERATIONS = 10000;


	@Test
	public void testChooseCard() {
		Map<CardDefinition, Double> dropRate = givenCardsDropRate();

		CardDefinition chooseCard = whenChoosingACard(dropRate);

		thenTheChosenCardWasInTheDropRate(dropRate, chooseCard);
	}

	@Test
	public void testChooseCardRandomQuality() {
		Map<CardDefinition, Double> dropRate = givenCardsDropRate();

		Map<String, Integer> selections = new HashMap<>();
		selections.put(CARD_1_ID, 0);
		selections.put(CARD_2_ID, 0);

		for (int i = 0; i < ITERATIONS; i++) {
			CardDefinition chooseCard = whenChoosingACard(dropRate);
			selections.put(chooseCard.getId(), selections.get(chooseCard.getId()) + 1);
		}

		double finalCard1SelectionChance = selections.get(CARD_1_ID) / (double) ITERATIONS;
		double finalCard2SelectionChance = selections.get(CARD_2_ID) / (double) ITERATIONS;

		Assertions.assertThat(finalCard1SelectionChance).isBetween(CARD_1_CHANCE - CHANCE_TOLERANCE, CARD_1_CHANCE + CHANCE_TOLERANCE);
		Assertions.assertThat(finalCard2SelectionChance).isBetween(CARD_2_CHANCE - CHANCE_TOLERANCE, CARD_2_CHANCE + CHANCE_TOLERANCE);
	}

	private void thenTheChosenCardWasInTheDropRate(Map<CardDefinition, Double> dropRate, CardDefinition chooseCard) {
		assertTrue(dropRate.containsKey(chooseCard));
	}

	private CardDefinition whenChoosingACard(Map<CardDefinition, Double> dropRate) {
		return RandomCardSelector.chooseCard(dropRate).get();
	}

	private Map<CardDefinition, Double> givenCardsDropRate() {
		Map<CardDefinition, Double> dropRate = new HashMap<>();
		dropRate.put(new CardDefinition(CARD_1_ID), CARD_1_CHANCE);
		dropRate.put(new CardDefinition(CARD_2_ID), CARD_2_CHANCE);
		return dropRate;
	}
}