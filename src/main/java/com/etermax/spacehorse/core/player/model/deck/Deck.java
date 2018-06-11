package com.etermax.spacehorse.core.player.model.deck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.common.exception.ApiException;

@DynamoDBDocument
public class Deck {

	private final int NUMBER_OF_STOCKS = 3;

	@DynamoDBAttribute(attributeName = "stocks")
	private List<Stock> stocks;

	@DynamoDBAttribute(attributeName = "selectedStockId")
	private int selectedStockId;

	@DynamoDBAttribute(attributeName = "ownedCards")
	private List<Card> ownedCards;

	public Deck() {
		ownedCards = new ArrayList<>();
		stocks = new ArrayList<>();
	}

	public void cheatRemoveCard(Card cardToRemove) {
		if (stocks.stream().anyMatch(stock -> stock.getSelectedCardsIds().contains(cardToRemove.getId()))) {
			throw new ApiException("Can't remove a selected card");
		}
		ownedCards.removeIf(card -> card.getId() == cardToRemove.getId());
	}

	public void checkAndFixIntegrity(Catalog catalog) {
		validateOwnedCards(catalog);

		validateAmountOfSelectedCards(catalog);

		fixCardsLevel(catalog);
	}

	private void fixCardsLevel(Catalog catalog) {
		for (Card card: ownedCards) {
			card.checkAndFixIntegrity(catalog);
		}
	}

	private void validateAmountOfSelectedCards(Catalog catalog) {
		if (hasInvalidAmountOfSelectedCards(catalog)) {
			throw new ApiException(
					"Amount of selected cards doesn't match the required value. Selected: " + getSelectedCardsIds().size() + ", " + "expected: "
							+ catalog.getGameConstants().getNumberOfCardsInDeck());
		}
	}

	private void validateOwnedCards(Catalog catalog) {
		if (hasInvalidOwnedCards(catalog)) {
			throw new ApiException("Some owned cards are invalid: " + getOwnedCardsListAsString());
		}
	}

	private String getOwnedCardsListAsString() {
		return getOwnedCards().stream().map(x -> x.getCardType()).collect(Collectors.joining(","));
	}

	public static Deck buildNewPlayerDeck(DeckConfiguration configuration) {
		Deck deck = new Deck();
		configuration.getStartingCardTypes().forEach(deck::addNewCard);
		deck.initializeHandsCardsIds(configuration.getNumberOfCardsInDeck());
		return deck;
	}

	public List<Card> getOwnedCards() {
		return ownedCards;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public void setOwnedCards(List<Card> ownedCards) {
		this.ownedCards = ownedCards;
	}

	public int getSelectedStockId() {
		return selectedStockId;
	}

	public void setSelectedStockId(int selectedStockId) {
		//TODO: separar en modelo y vista
		//		if (stocks.stream().noneMatch(x -> x.getStockId().equals(selectedStockId))) {
		//			throw new ApiException("Invalid stock id");
		//		}

		this.selectedStockId = selectedStockId;
	}

	public Optional<Card> findCardById(long id) {
		return getOwnedCards().stream().filter(card -> card.getId() == id).findFirst();
	}

	public boolean isCardSelected(String cardId) {
		return this.getSelectedCards().stream().anyMatch(card -> card.getCardType().equals(cardId));
	}

	@DynamoDBIgnore
	public List<Long> getSelectedCardsIds() {
		return stocks.stream().filter(stock -> stock.getStockId() == selectedStockId).findFirst().get().getSelectedCardsIds();
	}

	@DynamoDBIgnore
	public List<Card> getSelectedCards() {
		return getSelectedCardsIds().stream()
				.map(id -> findCardById(id).orElseThrow(() -> new ApiException("Invalid cards ids in selectedCards collection")))
				.collect(Collectors.toList());
	}

	@DynamoDBIgnore
	public void setSelectedCardsIds(int stockId, List<Long> selectedCardsIds, GameConstants gameConstants) {

		if (selectedCardsIds == null) {
			throw new ApiException("Invalid cards ids");
		}

		if (hasDuplicatedIds(selectedCardsIds)) {
			throw new ApiException("Duplicated cards ids");
		}

		if (hasInvalidIds(selectedCardsIds)) {
			throw new ApiException("Invalid cards ids");
		}

		if (selectedCardsIds.size() != gameConstants.getNumberOfCardsInDeck()) {
			throw new ApiException("Invalid number of selected cards");
		}

		setSelectedStockId(stockId);
		stocks.removeIf(stock -> stock.getStockId() == stockId);
		stocks.add(new Stock(stockId, selectedCardsIds));
	}

	public Card addNewCard(String cardType) {
		if (findCardByCardType(cardType) != null) {
			throw new RuntimeException("Duplicated cardType " + cardType);
		}

		Card card = new Card(getNextCardId(), cardType, 0);

		ownedCards.add(card);

		return card;
	}

	public Card findCardByCardType(String cardType) {
		return getOwnedCards().stream().filter(card -> card.getCardType().equals(cardType)).findFirst().orElse(null);
	}

	private boolean hasInvalidIds(List<Long> selectedCardsIds) {
		return !selectedCardsIds.stream().allMatch(id -> findCardById(id).isPresent());
	}

	private boolean hasDuplicatedIds(List<Long> selectedCardsIds) {
		return selectedCardsIds.stream().anyMatch(i -> Collections.frequency(selectedCardsIds, i) > 1);
	}

	@DynamoDBIgnore
	private long getNextCardId() {
		OptionalLong optionalMax = getOwnedCards().stream().mapToLong(Card::getId).max();

		if (optionalMax.isPresent()) {
			return optionalMax.getAsLong() + 1;
		} else {
			return 1L;
		}
	}

	private void initializeHandsCardsIds(int numberOfCardsInDeck) {
		List<Long> cardsIds = ownedCards.stream().map(Card::getId).limit(numberOfCardsInDeck).collect(Collectors.toList());

		stocks.clear();

		for (int i = 0; i < NUMBER_OF_STOCKS; i++) {
			Stock stock = new Stock(i, cardsIds);
			stocks.add(stock);
		}

		setSelectedStockId(0);
	}

	private boolean hasInvalidAmountOfSelectedCards(Catalog catalog) {
		return getSelectedCardsIds().size() != catalog.getGameConstants().getNumberOfCardsInDeck();
	}

	private void resetDeck(Catalog catalog) {
		ownedCards.clear();
		catalog.getCardDefinitionsCollection().getEntries().stream().map(CardDefinition::getId).forEach(this::addNewCard);
		initializeHandsCardsIds(catalog.getGameConstants().getNumberOfCardsInDeck());
	}

	private boolean hasInvalidOwnedCards(Catalog catalog) {
		return ownedCards.stream().anyMatch(card -> !catalog.getCardDefinitionsCollection().findById(card.getCardType()).isPresent());
	}
}
