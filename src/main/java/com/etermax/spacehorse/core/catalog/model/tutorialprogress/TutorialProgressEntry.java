package com.etermax.spacehorse.core.catalog.model.tutorialprogress;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.CardWithAmount;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;
import com.etermax.spacehorse.core.catalog.model.CatalogEntryWithChestInformation;
import com.etermax.spacehorse.core.catalog.resource.response.TutorialProgressCatalogResponse;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.google.common.collect.Lists;

public class TutorialProgressEntry extends CatalogEntry implements CatalogEntryWithChestInformation {

	private final String chestId;
	private final int gold;
	private final int gems;
	private final List<CardWithAmount> cardsWithAmount;
	private final int randomEpic;
	private final List<Card> botCards;
	private final int fixedMMR;
	private final String type;

	public TutorialProgressEntry(TutorialProgressCatalogResponse tutorialProgressCatalogResponse) {
		super(tutorialProgressCatalogResponse.getTutorialId());
		this.type = tutorialProgressCatalogResponse.getType();
		this.chestId = tutorialProgressCatalogResponse.getChestId();
		this.gold = tutorialProgressCatalogResponse.getGold();
		this.gems = tutorialProgressCatalogResponse.getGems();
		this.cardsWithAmount = buildCardsFromString(tutorialProgressCatalogResponse.getCards());
		this.randomEpic = tutorialProgressCatalogResponse.getRandomEpic();
		this.botCards = buildBotCardsFromString(tutorialProgressCatalogResponse.getBotCards());
		this.fixedMMR = tutorialProgressCatalogResponse.getFixedMMR();
	}

	public TutorialProgressEntry(String tutorialId, String chestId, int gold, int gems, List<CardWithAmount> cardsWithAmount, int randomEpic,
			List<Card> botCards, int fixedMMR, String type) {
		super(tutorialId);
		this.chestId = chestId;
		this.gold = gold;
		this.gems = gems;
		this.cardsWithAmount = cardsWithAmount;
		this.botCards = botCards;
		this.randomEpic = randomEpic;
		this.fixedMMR = fixedMMR;
		this.type = type;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(hasType(), "Empty type");

		if (Objects.equals(type, TutorialProgressType.SIMPLE_STEP.getId())) {
			validateSimpleStepEntry(catalog);
		}

		if (Objects.equals(type, TutorialProgressType.BATTLE_STEP.getId())) {
			validateBattleStepEntry(catalog);
		}

	}

	private boolean hasType() {
		return type != null && type.length() > 0;
	}

	private void validateSimpleStepEntry(Catalog catalog) {
		validateParameter(chestId == null || chestId.length() == 0, "ChestId must be empty");
		validateParameter(gold == 0, "Gold must be empty");
		validateParameter(gems == 0, "Gems must be empty");
		validateParameter(cardsWithAmount.size() == 0, "Cards must be empty");
		validateParameter(randomEpic == 0, "RandomEpic must be empty");
		validateParameter(botCards.size() == 0, "BotCards must be empty");
		validateParameter(fixedMMR == 0, "FixedMMR must be empty");
	}

	private void validateBattleStepEntry(Catalog catalog) {
		validateParameter(cardsWithAmount.size() > 0, "Empty cards");
		validateParameter(cardsWithAmount.size() <= catalog.getGameConstants().getNumberOfCardsInDeck(),
				"number of cards > max number of cards in deck");

		cardsWithAmount.stream()
				.forEach(card -> validateParameter(isValidCardId(catalog, card.getCard()), "Invalid card type " + card.getCard().getCardType()));

		botCards.stream().forEach(card -> validateParameter(isValidCardId(catalog, card) && isValidCardLevel(catalog, card),
				"Invalid card type or level, " + card.getCardType() + " " + card.getLevel()));

		if (chestId != null && chestId.length() > 0) {
			validateParameter(catalog.getChestDefinitionsCollection().findById(chestId).isPresent(), "Invalid chest id " + chestId);
		}
	}

	private boolean isValidCardLevel(Catalog catalog, Card card) {
		return catalog.getCardParameterLevelsCollection()
				.findByRarityAndLevel(catalog.getCardDefinitionsCollection().findById(card.getCardType()).get().getCardRarity(), card.getLevel())
				.isPresent();
	}

	private boolean isValidCardId(Catalog catalog, Card card) {
		return catalog.getCardDefinitionsCollection().findById(card.getCardType()).isPresent();
	}

	private List<CardWithAmount> buildCardsFromString(String cardsString) {
		if (!Objects.equals(type, TutorialProgressType.BATTLE_STEP.getId())) {
			return Lists.newArrayList();
		}

		try {
			return assignUniqueCardsIdsToCardWithAmount(
					Arrays.stream(cardsString.split(",")).map(String::trim).filter(x -> x.length() >= 3).map(cardStr -> cardStr.split(":"))
							.map(cardStrs -> {
								int level = 0;
								long id = 0;
								Card card = new Card(id, cardStrs[0], level);
								Integer amount = Integer.valueOf(cardStrs[1]);
								return new CardWithAmount(card, amount);
							}).collect(Collectors.toList()));
		} catch (Exception ex) {
			throw new CatalogException("Wrong cards configuration in bot " + getId());
		}
	}

	static private List<CardWithAmount> assignUniqueCardsIdsToCardWithAmount(List<CardWithAmount> cards) {
		for (long id = 0; id < cards.size(); id++) {
			cards.get((int) id).getCard().setId(id);
		}
		return cards;
	}

	private List<Card> buildBotCardsFromString(String cardsString) {
		if (!Objects.equals(type, TutorialProgressType.BATTLE_STEP.getId())) {
			return Lists.newArrayList();
		}

		try {
			return assignUniqueCardsIdsToCards(
					Arrays.stream(cardsString.split(",")).map(String::trim).filter(x -> x.length() >= 3).map(cardStr -> cardStr.split(":"))
							.map(cardStrs -> {
								String cardType = cardStrs[0];
								int level = Integer.valueOf(cardStrs[1]);
								long id = 0;
								return new Card(id, cardType, level);
							}).collect(Collectors.toList()));
		} catch (Exception ex) {
			throw new CatalogException("Wrong cards configuration in bot " + getId());
		}
	}

	static private List<Card> assignUniqueCardsIdsToCards(List<Card> cards) {
		for (long id = 0; id < cards.size(); id++) {
			cards.get((int) id).setId(id);
		}
		return cards;
	}

	public String getChestId() {
		return chestId;
	}

	public int getGold() {
		return gold;
	}

	public int getGems() {
		return gems;
	}

	public int getRandomEpic() {
		return randomEpic;
	}

	public List<CardWithAmount> getCardsWithAmount() {
		return cardsWithAmount;
	}

	public List<Card> getBotCards() {
		return botCards;
	}

	public int getFixedMMR() {
		return fixedMMR;
	}

	public String getType() {
		return type;
	}
}
