package com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.CardsDropRate;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.model.inventory.cardparts.CardParts;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.mock.MockUtils;

public class CardDropRateCalculatorTest {

	public static double ERROR = 0.01;

	public static double ERROR_DROP_RATE = 0.001;

	private CardDropRateCalculatorConfiguration cardDropRateCalculatorConfiguration;

	private String userId = "id";

	private Player player;

	private List<CardDefinition> cardDefinitionsCollection;

	private List<CardsDropRate> cardsDropRateCollection;

	public CardDropRateCalculatorTest() {
		this.cardDropRateCalculatorConfiguration = new CardDropRateCalculatorConfiguration(2, 2, 5, 45, 3);
		PlayerRepository playerRepository = mock(PlayerRepository.class);
		this.player = MockUtils.mockPlayerWithGems(userId, 500, playerRepository);
		Catalog catalog = MockUtils.mockCatalog();
		cardDefinitionsCollection = catalog.getCardDefinitionsCollection().getEntries();
		cardsDropRateCollection = catalog.getCardsDropRateCollection().getEntries();
	}

	@Test
	public void testDropDiffCards() {
		int cardAmount = 300;
		int intendedCards = 155;

		double dropDiff = CardDropRateCalculator.calculateDropDiffCards(cardDropRateCalculatorConfiguration, cardAmount, intendedCards);

		assertTrue(dropDiff < 0);
		assertTrue(Math.abs(0.75 - Math.abs(dropDiff)) < ERROR);
	}

	@Test
	public void testDropWinRatioMakesNoEffect() {
		double winLoseRatio = 0.50;
		boolean selected = false;

		double dropWinRatio = CardDropRateCalculator.calculateDropWinRatio(cardDropRateCalculatorConfiguration, winLoseRatio, selected);

		assertTrue(Math.abs(ERROR - dropWinRatio) <= ERROR);
	}

	@Test
	public void testDropWinRatioMakesEffectOnSelection() {
		double winLoseRatio = 0.30;
		boolean selected = true;

		double dropWinRatio = CardDropRateCalculator.calculateDropWinRatio(cardDropRateCalculatorConfiguration, winLoseRatio, selected);

		assertTrue(Math.abs(0.04 - dropWinRatio) < ERROR);
	}

	@Test
	public void testDropWinRatioMakesNoEffectOnSelection() {
		double winLoseRatio = 0.30;
		boolean selected = false;

		double dropWinRatio = CardDropRateCalculator.calculateDropWinRatio(cardDropRateCalculatorConfiguration, winLoseRatio, selected);

		assertTrue(Math.abs(0.29 - dropWinRatio) < ERROR);
	}

	@Test
	public void testDropRate() {
		Inventory inventory = mock(Inventory.class);
		when(player.getInventory()).thenReturn(inventory);
		CardParts cardParts = new CardParts();
		when(inventory.getCardParts()).thenReturn(cardParts);

		cardParts.add("card_fighter", 900);
		cardParts.add("card_bomber", 60);
		cardParts.add("card_corvette", 900);
		cardParts.add("card_corvette_bomber", 155);
		cardParts.add("card_frigate", 1300);
		cardParts.add("card_cruiser", 150);
		cardParts.add("card_laser", 50);
		cardParts.add("card_missile", 50);
		cardParts.add("card_decoy", 300);

		Deck deck = mock(Deck.class);
		when(player.getDeck()).thenReturn(deck);
		when(deck.isCardSelected("card_fighter")).thenReturn(true);
		when(deck.isCardSelected("card_bomber")).thenReturn(false);
		when(deck.isCardSelected("card_corvette")).thenReturn(true);
		when(deck.isCardSelected("card_corvette_bomber")).thenReturn(true);
		when(deck.isCardSelected("card_frigate")).thenReturn(false);
		when(deck.isCardSelected("card_cruiser")).thenReturn(true);
		when(deck.isCardSelected("card_laser")).thenReturn(true);
		when(deck.isCardSelected("card_missile")).thenReturn(false);
		when(deck.isCardSelected("card_decoy")).thenReturn(false);

		List<CardsDropRate> cardsDropRateList = new ArrayList();
		cardsDropRateList.add(new CardsDropRate("1", "card_fighter", 0, 980));
		cardsDropRateList.add(new CardsDropRate("2", "card_bomber", 0, 50));
		cardsDropRateList.add(new CardsDropRate("3", "card_corvette", 0, 980));
		cardsDropRateList.add(new CardsDropRate("4", "card_corvette_bomber", 0, 155));
		cardsDropRateList.add(new CardsDropRate("5", "card_frigate", 0, 980));
		cardsDropRateList.add(new CardsDropRate("6", "card_cruiser", 0, 155));
		cardsDropRateList.add(new CardsDropRate("7", "card_laser", 0, 50));
		cardsDropRateList.add(new CardsDropRate("8", "card_missile", 0, 50));
		cardsDropRateList.add(new CardsDropRate("9", "card_playerWinRatedecoy", 0, 155));

		List<CardDefinition> cardsDefinitionsList = MockUtils.mockCardsDefinitionsList(CardRarity.COMMON);

		Map<CardDefinition, Double> dropRate = CardDropRateCalculator
				.calculateCardsDropRate(player, player.getMapNumber(), cardDropRateCalculatorConfiguration, cardsDefinitionsList, cardsDropRateList);

		assertTrue(Math.abs(0.0675 - dropRate.get(buildCard("card_fighter"))) < ERROR_DROP_RATE);
		assertTrue(Math.abs(0.1711 - dropRate.get(buildCard("card_bomber"))) < ERROR_DROP_RATE);
		assertTrue(Math.abs(0.0675 - dropRate.get(buildCard("card_corvette"))) < ERROR_DROP_RATE);
		assertTrue(Math.abs(0.0022 - dropRate.get(buildCard("card_corvette_bomber"))) < ERROR_DROP_RATE);
		assertTrue(Math.abs(0.0698 - dropRate.get(buildCard("card_frigate"))) < ERROR_DROP_RATE);
		assertTrue(Math.abs(0.0280 - dropRate.get(buildCard("card_cruiser"))) < ERROR_DROP_RATE);
		assertTrue(Math.abs(0.0022 - dropRate.get(buildCard("card_laser"))) < ERROR_DROP_RATE);
		assertTrue(Math.abs(0.3311 - dropRate.get(buildCard("card_missile"))) < ERROR_DROP_RATE);
		assertTrue(Math.abs(0.0100 - dropRate.get(buildCard("card_decoy"))) < ERROR_DROP_RATE);
	}

	private CardDefinition buildCard(String id) {
		return new CardDefinition(id);
	}

	@Test
	public void testMap0() {
		Inventory inventory = mock(Inventory.class);
		when(player.getInventory()).thenReturn(inventory);
		CardParts cardParts = new CardParts();
		when(inventory.getCardParts()).thenReturn(cardParts);

		Deck deck = mock(Deck.class);
		when(player.getDeck()).thenReturn(deck);
		when(deck.isCardSelected(anyString())).thenReturn(false);

		Map<CardDefinition, Double> dropRate = CardDropRateCalculator
				.calculateCardsDropRate(player, player.getMapNumber(), cardDropRateCalculatorConfiguration, getCardsAvailableInMap(0),
						cardsDropRateCollection);

		assertEquals(13, dropRate.entrySet().size());
		assertNotNull(dropRate.get(buildCard("card_buzzer")));
		assertNull(dropRate.get(buildCard("card_chaser")));
		assertNull(dropRate.get(buildCard("card_biters")));
	}

	@Test
	public void testMap1() {
		Inventory inventory = mock(Inventory.class);
		when(player.getInventory()).thenReturn(inventory);
		CardParts cardParts = new CardParts();
		when(inventory.getCardParts()).thenReturn(cardParts);

		Deck deck = mock(Deck.class);
		when(player.getDeck()).thenReturn(deck);
		when(deck.isCardSelected(anyString())).thenReturn(false);

		when(player.getMapNumber()).thenReturn(1);

		Map<CardDefinition, Double> dropRate = CardDropRateCalculator
				.calculateCardsDropRate(player, player.getMapNumber(), cardDropRateCalculatorConfiguration, getCardsAvailableInMap(1),
						cardsDropRateCollection);

		assertEquals(18, dropRate.entrySet().size());
		assertNotNull(dropRate.get(buildCard("card_buzzer")));
		assertNotNull(dropRate.get(buildCard("card_chaser")));
		assertNull(dropRate.get(buildCard("card_biters")));
	}

	@Test
	public void testMap2() {
		Inventory inventory = mock(Inventory.class);
		when(player.getInventory()).thenReturn(inventory);
		CardParts cardParts = new CardParts();
		when(inventory.getCardParts()).thenReturn(cardParts);

		Deck deck = mock(Deck.class);
		when(player.getDeck()).thenReturn(deck);
		when(deck.isCardSelected(anyString())).thenReturn(false);

		when(player.getMapNumber()).thenReturn(2);

		Map<CardDefinition, Double> dropRate = CardDropRateCalculator
				.calculateCardsDropRate(player, player.getMapNumber(), cardDropRateCalculatorConfiguration, getCardsAvailableInMap(2),
						cardsDropRateCollection);

		assertEquals(22, dropRate.entrySet().size());
		assertNotNull(dropRate.get(buildCard("card_buzzer")));
		assertNotNull(dropRate.get(buildCard("card_chaser")));
		assertNotNull(dropRate.get(buildCard("card_biters")));
	}

	private List<CardDefinition> getCardsAvailableInMap(int mapNumber) {
		return cardDefinitionsCollection.stream().filter(card -> isCardAvailableInMap(mapNumber, card)).collect(Collectors.toList());
	}

	private static boolean isCardAvailableInMap(int mapNumber, CardDefinition card) {
		return card.getEnabled() && mapNumber >= card.getAvailableFromMapId();
	}

	@Test
	public void testIntendedCardsForHigherMap() {
		Inventory inventory = mock(Inventory.class);
		when(player.getInventory()).thenReturn(inventory);
		CardParts cardParts = new CardParts();
		when(inventory.getCardParts()).thenReturn(cardParts);

		Deck deck = mock(Deck.class);
		when(player.getDeck()).thenReturn(deck);
		when(deck.isCardSelected(anyString())).thenReturn(false);

		cardParts.add("card_buzzer", 100);

		List<CardsDropRate> cardsDropRateList = new ArrayList();
		cardsDropRateList.add(new CardsDropRate("1", "card_buzzer", 0, 100));
		cardsDropRateList.add(new CardsDropRate("2", "card_buzzer", 1, 500));

		when(player.getMapNumber()).thenReturn(0);

		Map<CardDefinition, Double> dropRate0 = CardDropRateCalculator
				.calculateCardsDropRate(player, player.getMapNumber(), cardDropRateCalculatorConfiguration, cardDefinitionsCollection,
						cardsDropRateList);

		when(player.getMapNumber()).thenReturn(1);

		Map<CardDefinition, Double> dropRate1 = CardDropRateCalculator
				.calculateCardsDropRate(player, player.getMapNumber(), cardDropRateCalculatorConfiguration, cardDefinitionsCollection,
						cardsDropRateList);

		double value0 = dropRate0.get(buildCard("card_buzzer"));
		double value1 = dropRate1.get(buildCard("card_buzzer"));

		assertThat(value1, Matchers.greaterThan(value0));
	}

}
