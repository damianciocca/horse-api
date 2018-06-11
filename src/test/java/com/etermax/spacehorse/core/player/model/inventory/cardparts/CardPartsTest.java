package com.etermax.spacehorse.core.player.model.inventory.cardparts;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Test;

import com.etermax.spacehorse.core.player.model.inventory.cardparts.CardParts;

public class CardPartsTest {

	static final String CARD_TYPE_A = "cardTypeA";
	static final String CARD_TYPE_B = "cardTypeB";

	private CardParts cardsParts;

	@After
	public void after() {
		cardsParts = null;
	}

	@Test
	public void aNewCardsPartsIsEmpty() {
		givenANewCardsParts();

		thenItsEmpty();
	}

	@Test
	public void newAddedCardsAreStored() {
		givenANewCardsParts();

		whenAddingCard(CARD_TYPE_A, 2);
		whenAddingCard(CARD_TYPE_A, 3);

		whenAddingCard(CARD_TYPE_B, 4);
		whenAddingCard(CARD_TYPE_B, 3);

		thenTheAmountOfCardsIs(CARD_TYPE_A, 2 + 3);
		thenTheAmountOfCardsIs(CARD_TYPE_B, 4 + 3);
	}

	@Test
	public void removedCardsAreRemoved() {
		givenANewCardsParts();

		whenAddingCard(CARD_TYPE_A, 5);
		whenRemovingCard(CARD_TYPE_A, 2);

		thenTheAmountOfCardsIs(CARD_TYPE_A, 5 - 2);
	}

	@Test
	public void removingMoreCardsThanAvaiableShouldNotBecomeNegative() {
		givenANewCardsParts();

		whenAddingCard(CARD_TYPE_A, 5);
		whenRemovingCard(CARD_TYPE_A, 99);

		thenTheAmountOfCardsIs(CARD_TYPE_A, 0);
		thenItsEmpty();
	}

	@Test
	public void requestingAmountOfUnknownCardReturnsZero() {
		givenANewCardsParts();

		thenTheAmountOfCardsIs(CARD_TYPE_A, 0);
		thenTheAmountOfCardsIs(CARD_TYPE_B, 0);
	}

	private void thenItsEmpty() {
		assertThat(cardsParts.getCardsTypes().size(), equalTo(0));
	}

	private void givenANewCardsParts() {
		cardsParts = new CardParts();
	}

	private void thenTheAmountOfCardsIs(String cardType, int amount) {
		assertThat(cardsParts.getAmount(cardType), equalTo(amount));
	}

	private void whenAddingCard(String cardType, int amount) {
		cardsParts.add(cardType, amount);
	}

	private void whenRemovingCard(String cardType, int amount) {
		cardsParts.remove(cardType, amount);
	}

}
