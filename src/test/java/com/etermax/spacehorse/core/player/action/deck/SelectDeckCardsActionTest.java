package com.etermax.spacehorse.core.player.action.deck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.mock.MockCatalog;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.google.common.collect.Lists;

public class SelectDeckCardsActionTest {

	private static final long INVALID_CARD_ID = 700L;
	private static final String NEW_CARD_TYPE = "NEW-CARD-TYPE";
	private static final long NEW_CARD_ID = 9L;
	private static final int CARD_DECK_ID = 2;
	private static final String PLAYER_ID = "100";

	private Player player;
	private SelectDeckCardsAction action;

	@Before
	public void setup() {
		Catalog catalog = MockCatalog.buildCatalog();
		player = new PlayerScenarioBuilder(PLAYER_ID).build();
		PlayerRepository playerRepository = aPlayerRepository();
		CatalogRepository catalogRepository = aCatalogRepository(catalog, player.getAbTag());
		action = new SelectDeckCardsAction(playerRepository, catalogRepository);
	}

	@Test
	public void whenAPlayerSelectNewCardsInCardDeckTwoThenTheNewCardsShouldBeFoundInCardDeckTwo() {
		// Given
		givenAPlayerWithANewOwnedCard();
		List<Long> selectedCardsIds = givenASelectedCardsWithNewCardId();
		// When
		whenSelectCardsForCardDeck(selectedCardsIds, CARD_DECK_ID);
		// Then
		thenTheSelectedCardIdsShouldContainsNewCardId();
	}

	@Test
	public void whenTryToSelectAnInvalidNumberOfCardsThenAnExceptionShouldbeThrown() {
		// Given
		givenAPlayerWithANewOwnedCard();
		List<Long> selectedCardsIds = givenAnInvalidSelectedCardsSize();
		// When - then
		assertThatThrownBy(() -> whenSelectCardsForCardDeck(selectedCardsIds, CARD_DECK_ID)).isInstanceOf(ApiException.class)
				.hasMessageContaining("Invalid number of selected cards");

	}

	@Test
	public void whenTryToSelectAnInvalidCardIdThenAnExceptionShouldBeThrown() {
		// Given
		List<Long> selectedCardsIds = givenAnInvalidSelectedCardIds();
		// When - then
		assertThatThrownBy(() -> whenSelectCardsForCardDeck(selectedCardsIds, CARD_DECK_ID)).isInstanceOf(ApiException.class)
				.hasMessageContaining("Invalid cards ids");

	}

	private List<Long> givenASelectedCardsWithNewCardId() {
		return Lists.newArrayList(1L, 2L, 3L, 4L, 5L, 6L, 7L, NEW_CARD_ID);
	}

	private List<Long> givenAnInvalidSelectedCardsSize() {
		return Lists.newArrayList(1L, 2L, 3L, 4L);
	}

	private List<Long> givenAnInvalidSelectedCardIds() {
		return Lists.newArrayList(1L, 2L, 3L, 4L, 5L, 6L, INVALID_CARD_ID, 8L);
	}

	private void givenAPlayerWithANewOwnedCard() {
		player.getDeck().addNewCard(NEW_CARD_TYPE);
	}

	private void whenSelectCardsForCardDeck(List<Long> selectedCardsIds, int cardDeckId) {
		action.selectDeckCards(PLAYER_ID, cardDeckId, selectedCardsIds);
	}

	private void thenTheSelectedCardIdsShouldContainsNewCardId() {
		assertThat(player.getDeck().getSelectedCardsIds()).containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L, NEW_CARD_ID);
	}

	private CatalogRepository aCatalogRepository(Catalog catalog, ABTag abTag) {
		CatalogRepository catalogRepository = mock(CatalogRepository.class);
		when(catalogRepository.getActiveCatalogWithTag(abTag)).thenReturn(catalog);
		return catalogRepository;
	}

	private PlayerRepository aPlayerRepository() {
		PlayerRepository playerRepository = mock(PlayerRepository.class);
		when(playerRepository.find(PLAYER_ID)).thenReturn(player);
		return playerRepository;
	}
}
