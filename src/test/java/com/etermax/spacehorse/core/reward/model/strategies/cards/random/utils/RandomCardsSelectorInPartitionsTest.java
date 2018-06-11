package com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;

public class RandomCardsSelectorInPartitionsTest {

	private static final String CARD_1_ID = "card_fighter";
	private static final String CARD_2_ID = "card_bomber";
	private static final String CARD_3_ID = "card_corvette";

	private Map<CardDefinition, Double> cardsDropRate;
	private Map<CardDefinition, Integer> chosenCards;

	@After
	public void tearDown() {
		cardsDropRate = null;
		chosenCards = null;
	}

	@Test
	public void testChooseCards() {
		givenCardsDropRateWithThreeCards();

		whenChoosingCards(3, 100);

		thenCardsAmountWhereChosen(CARD_1_ID, 10);
		thenCardsAmountWhereChosen(CARD_2_ID, 50);
		thenCardsAmountWhereChosen(CARD_3_ID, 40);
	}

	private void thenCardsAmountWhereChosen(String cardId, int amount) {
		assertThat(chosenCards.get(buildCard(cardId)), equalTo(amount));
	}

	private void whenChoosingCards(int maxPartitions, int amount) {
		chosenCards = RandomCardsSelectorInPartitions.chooseCards(cardsDropRate, maxPartitions, amount);
	}

	private void givenCardsDropRateWithThreeCards() {
		cardsDropRate = new HashMap<>();
		cardsDropRate.put(buildCard(CARD_1_ID), 0.10);
		cardsDropRate.put(buildCard(CARD_2_ID), 0.50);
		cardsDropRate.put(buildCard(CARD_3_ID), 0.40);
	}

	@Test
	public void testChooseCardsShorter() {
		givenCardDropRateWithTwoCards();

		whenChoosingCards(3, 100);

		thenCardsAmountWhereChosen(CARD_1_ID, 20);
		thenCardsAmountWhereChosen(CARD_2_ID, 80);
	}

	private void givenCardDropRateWithTwoCards() {
		cardsDropRate = new HashMap<>();
		cardsDropRate.put(buildCard(CARD_1_ID), 0.10);
		cardsDropRate.put(buildCard(CARD_2_ID), 0.40);
	}

	private CardDefinition buildCard(String id) {
		return new CardDefinition(id);
	}

}