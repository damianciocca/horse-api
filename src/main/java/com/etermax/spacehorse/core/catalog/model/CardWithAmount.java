package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.player.model.deck.Card;

public class CardWithAmount {
	private final Card card;
	private final int amount;

	public CardWithAmount(Card card, int amount) {
		this.card = card;
		this.amount = amount;
	}

	public Card getCard() {
		return card;
	}

	public int getAmount() {
		return amount;
	}
}
