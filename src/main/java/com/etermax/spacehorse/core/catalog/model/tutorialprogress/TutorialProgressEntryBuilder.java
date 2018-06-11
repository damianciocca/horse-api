package com.etermax.spacehorse.core.catalog.model.tutorialprogress;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CardWithAmount;
import com.etermax.spacehorse.core.player.model.deck.Card;

public class TutorialProgressEntryBuilder {
	private String tutorialId = "";
	private String chestId = "";
	private int gold = 0;
	private int gems = 0;
	private List<CardWithAmount> cardsWithAmount = new ArrayList<>();
	private int randomEpic = 0;
	private List<Card> botCards = new ArrayList<>();
	private int fixedMMR = 0;
	private String type = "";

	public TutorialProgressEntryBuilder setTutorialId(String tutorialId) {
		this.tutorialId = tutorialId;
		return this;
	}

	public TutorialProgressEntryBuilder setChestId(String chestId) {
		this.chestId = chestId;
		return this;
	}

	public TutorialProgressEntryBuilder setGold(int gold) {
		this.gold = gold;
		return this;
	}

	public TutorialProgressEntryBuilder setGems(int gems) {
		this.gems = gems;
		return this;
	}

	public TutorialProgressEntryBuilder setCardsWithAmount(List<CardWithAmount> cardsWithAmount) {
		this.cardsWithAmount = cardsWithAmount;
		return this;
	}

	public TutorialProgressEntryBuilder setRandomEpic(int randomEpic) {
		this.randomEpic = randomEpic;
		return this;
	}

	public TutorialProgressEntryBuilder setBotCards(List<Card> botCards) {
		this.botCards = botCards;
		return this;
	}

	public TutorialProgressEntryBuilder setFixedMMR(int fixedMMR) {
		this.fixedMMR = fixedMMR;
		return this;
	}

	public void setType(String type) {
		this.type = type;
	}

	public TutorialProgressEntry create() {
		return new TutorialProgressEntry(tutorialId, chestId, gold, gems, cardsWithAmount, randomEpic, botCards, fixedMMR, type);
	}
}