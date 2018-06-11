package com.etermax.spacehorse.core.reward.model.strategies.cards.random;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;

public class CardsRewardConfiguration {
	private final CardRarity rarity;
	private final List<CardDefinition> cards;

	private int amount;
	private int partitions;

	public CardRarity getRarity() {
		return rarity;
	}

	public List<CardDefinition> getCards() {
		return cards;
	}

	public int getAmount() {
		return amount;
	}

	public int getPartitions() {
		return partitions;
	}

	public void setPartitions(int partitions) {
		this.partitions = partitions;
	}

	public CardsRewardConfiguration(CardRarity rarity, List<CardDefinition> cards, int amount, int partitions) {
		this.rarity = rarity;
		this.cards = cards;

		this.amount = amount;
		this.partitions = partitions;
	}

	public boolean isValid() {
		return getAmount() > 0 && getPartitions() > 0 && getCards().size() > 0;
	}
}
