package com.etermax.spacehorse.core.catalog.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.resource.response.BotDefinitionResponse;
import com.etermax.spacehorse.core.player.model.deck.Card;

public class BotDefinition extends CatalogEntry {

	private final String name;

	private final int level;

	private final int minMMR;

	private final int maxMMR;

	private final List<Card> cards;

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getMinMMR() {
		return minMMR;
	}

	public int getMaxMMR() {
		return maxMMR;
	}

	public List<Card> getCards() {
		return cards;
	}

	public BotDefinition(String id, String name, int level, int minMMR, int maxMMR, List<Card> cards) {
		super(id);
		this.name = name;
		this.level = level;
		this.minMMR = minMMR;
		this.maxMMR = maxMMR;
		this.cards = cards;
	}

	public BotDefinition(BotDefinitionResponse cardDefinitionResponse) {
		super(cardDefinitionResponse.getId());
		this.name = cardDefinitionResponse.getName();
		this.level = cardDefinitionResponse.getLevel();
		this.minMMR = cardDefinitionResponse.getMinMMR();
		this.maxMMR = cardDefinitionResponse.getMaxMMR();
		this.cards = buildCardsFromString(cardDefinitionResponse.getCards());
	}

	private List<Card> buildCardsFromString(String cardsString) {
		//cardsString format: cardId1:level1,cardId2:level2,etc..
		List<Card> cards;

		try {
			cards = Arrays.stream(cardsString.split(",")).map(x -> x.trim()).filter(x -> x.length() >= 3).map(cardStr -> cardStr.split(":"))
					.map(cardStrs -> new Card(0L, cardStrs[0], Integer.parseInt(cardStrs[1]))).collect(Collectors.toList());

			for (int i = 0; i < cards.size(); i++)
				cards.get(i).setId((long) i);
		} catch (Exception ex) {
			throw new CatalogException("Wrong cards configuration in bot " + getId());
		}

		return cards;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(level >= 0, "level < 0");
		validateParameter(level < catalog.getPlayerLevelsCollection().getMaxLevel(), "level > maxLevel");
		validateParameter(minMMR >= 0, "minMMR < 0");
		validateParameter(maxMMR >= 0, "maxMMR < 0");
		validateParameter(minMMR < maxMMR, "minMMR >= maxMMR");
		validateParameter(name.length() > 0, "Empty name");
		validateParameter(cards.size() > 0, "Empty cards");
		validateParameter(cards.size() <= catalog.getGameConstants().getNumberOfCardsInDeck(), "number of cards > max number of cards in deck");

		cards.stream().forEach(card -> {
			validateParameter(catalog.getCardDefinitionsCollection().findById(card.getCardType()).isPresent(),
					"Invalid card id " + card.getCardType());
			validateParameter(card.hasLevel(card.getLevel(), catalog), "Invalid level " + card.getLevel() + " for card id " + card.getCardType());
		});
	}

}
