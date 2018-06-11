package com.etermax.spacehorse.core.player.model.deck;

import static com.google.common.collect.ImmutableList.copyOf;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class DeckConfiguration {

	private final int numberOfCardsInDeck;
	private final List<String> startingCardTypes;

	public DeckConfiguration(int numberOfCardsInDeck, List<String> startingCardTypes) {
		this.numberOfCardsInDeck = numberOfCardsInDeck;
		this.startingCardTypes = startingCardTypes;
	}

	public int getNumberOfCardsInDeck() {
		return numberOfCardsInDeck;
	}

	public List<String> getStartingCardTypes() {
		return copyOf(startingCardTypes);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
}
