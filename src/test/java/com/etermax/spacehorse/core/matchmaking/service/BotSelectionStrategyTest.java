package com.etermax.spacehorse.core.matchmaking.service;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import com.etermax.spacehorse.core.catalog.model.BotDefinition;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.matchmaking.model.selectors.BotSelectionStrategy;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.mock.MockCatalog;
import com.google.common.collect.Lists;

public class BotSelectionStrategyTest {

	private int mmr;
	private Collection<BotDefinition> botsCollection;
	private BotDefinition foundBot;
	private BotSelectionStrategy strategy;
	private ServerTimeProvider serverTime;

	@Before
	public void setUp() {
		serverTime = new FixedServerTimeProvider();
		Catalog catalog = MockCatalog.buildCatalog();
		strategy = new BotSelectionStrategy(catalog, serverTime);
	}

	@After
	public void tearDown() {
		strategy = null;
		foundBot = null;
		botsCollection = null;
	}

	@Test(expected = IllegalArgumentException.class)
	public void anEmptyMapsCollectionFails() {
		givenEmptyMmr();
		givenAnEmptyBotCollection();

		whenFindingBestBot();
	}

	@Test
	public void aBotsCollectionWithASingleEntryAlwaysReturnsThatEntry() {
		givenASingleBotCollection(0, 0);

		givenEmptyMmr();
		whenFindingBestBot();
		thenABotIsFound();

		givenMmr(10);
		whenFindingBestBot();
		thenABotIsFound();

		givenMmr(-10);
		whenFindingBestBot();
		thenABotIsFound();
	}

	@Test
	public void theClosestBotIsAlwaysReturned() {
		givenThreeBots();

		whenFindingBestBotWithMmr(0);
		thenABotWithAValidRangeIsFound();

		whenFindingBestBotWithMmr(50);
		thenABotWithAValidRangeIsFound();

		whenFindingBestBotWithMmr(99);
		thenABotWithAValidRangeIsFound();

		whenFindingBestBotWithMmr(100);
		thenABotWithAValidRangeIsFound();

		whenFindingBestBotWithMmr(199);
		thenABotWithAValidRangeIsFound();

		whenFindingBestBotWithMmr(200);
		thenABotWithAValidRangeIsFound();

		whenFindingBestBotWithMmr(299);
		thenABotWithAValidRangeIsFound();
	}

	@Test
	public void botWithNotActiveCardIsFilteredAndReturnBotWithDefaultCard() {
		givenABotWithDefaultCardAndOtherWithNotActiveCard();

		whenFindingBestBotWithMmr(150);

		thenBotFoundHasExpectedId("bot2");
	}

	private void thenBotFoundHasExpectedId(String botIdExpected) {
		assertThat(foundBot.getId()).isEqualTo(botIdExpected);
	}

	private void givenABotWithDefaultCardAndOtherWithNotActiveCard() {
		Card notActiveCard = getNotActiveCard();
		Card defaultCard = getDefaultCard();
		botsCollection = Lists.newArrayList(buildBotDefinition(0, 200, notActiveCard, "bot1"), buildBotDefinition(0, 200, defaultCard, "bot2"));
	}

	private Card getNotActiveCard() {
		CardDefinition cardDefinition = mock(CardDefinition.class);
		DateTime dateTime = serverTime.getDateTime().plusHours(1);
		when(cardDefinition.getActivationTime()).thenReturn(Optional.of(dateTime));
		Card card1 = mock(Card.class);
		when(card1.getDefinition(Matchers.any())).thenReturn(cardDefinition);
		return card1;
	}

	private void whenFindingBestBotWithMmr(int mmr) {
		givenMmr(mmr);
		whenFindingBestBot();
	}

	private void thenABotWithAValidRangeIsFound() {
		thenABotIsFound();
		assertThat(mmr).isGreaterThanOrEqualTo(foundBot.getMinMMR());
		assertThat(mmr).isLessThan(foundBot.getMaxMMR());
	}

	private void givenThreeBots() {
		Card card = getDefaultCard();
		botsCollection = Lists.newArrayList(buildBotDefinition(0, 100, card, "bot"), buildBotDefinition(100, 200, card, "bot"),
				buildBotDefinition(200, 300, card, "bot"));
	}

	private Card getDefaultCard() {
		CardDefinition cardDefinition = mock(CardDefinition.class);
		when(cardDefinition.isActiveFor(any())).thenReturn(true);
		Card card = mock(Card.class);
		when(card.getDefinition(Matchers.any())).thenReturn(cardDefinition);
		return card;
	}

	private BotDefinition buildBotDefinition(int minMMR, int maxMMR, Card card, String botId) {
		return new BotDefinition(botId, "bot", 0, minMMR, maxMMR, Lists.newArrayList(card));
	}

	private void givenASingleBotCollection(int minMMR, int maxMMR) {
		Card card = getDefaultCard();
		botsCollection = Arrays.asList(new BotDefinition("bot", "bot", 0, minMMR, maxMMR, Lists.newArrayList(card)));
	}

	private void thenABotIsFound() {
		assertThat(foundBot).isNotNull();
	}

	private void givenMmr(int mmr) {
		this.mmr = mmr;
	}

	private void whenFindingBestBot() {
		foundBot = strategy.getBestBot(mmr, botsCollection);
	}

	private void givenAnEmptyBotCollection() {
		botsCollection = new ArrayList<>();
	}

	private void givenEmptyMmr() {
		mmr = 0;
	}
}