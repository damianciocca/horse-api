package com.etermax.spacehorse.core.player.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.player.action.deck.GetDeckCardsAction;
import com.etermax.spacehorse.core.player.action.deck.GetSelectedCardsFromDeckAction;
import com.etermax.spacehorse.core.player.action.deck.SelectDeckCardsAction;
import com.etermax.spacehorse.core.player.action.deck.UpgradeCardAction;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.resource.request.SelectedCardsRequest;
import com.etermax.spacehorse.core.player.resource.request.UpgradeCardRequest;
import com.etermax.spacehorse.core.player.resource.response.UpgradeCardResponse;
import com.etermax.spacehorse.mock.MockUtils;
import com.google.common.collect.Lists;

public class PlayerDeckResourceTest {

	private static final Integer CARD_DECK_ID = 1;
	private static final Long CARD_ID = 1l;
	private static final String LOGIN_ID = "loginId";

	private PlayerDeckResource playerDeckResource;
	private HttpServletRequest transport;

	@Before
	public void setUp() throws Exception {
		UpgradeCardAction upgradeCardAction = anUpgradeCardAction();
		SelectDeckCardsAction selectDeckCardsAction = aSelectDeckCardsAction();
		GetSelectedCardsFromDeckAction getSelectedCardsFromDeckAction = aGetSelectedCardsFromDeckAction();
		GetDeckCardsAction getDeckCardsAction = aGetDeckCardsAction();
		transport = MockUtils.mockHttpServletRequest(LOGIN_ID);
		playerDeckResource = new PlayerDeckResource(selectDeckCardsAction, getSelectedCardsFromDeckAction, getDeckCardsAction, upgradeCardAction);
	}

	@Test
	public void testPostDeckSelectedCards() {
		// given
		List<Long> selectedCardsIds = aSelectedDeckCards();
		SelectedCardsRequest request = new SelectedCardsRequest(selectedCardsIds, CARD_DECK_ID);
		// when
		Response response = playerDeckResource.postDeckSelectedCards(transport, request);
		// then
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
	}

	@Test
	public void testPostDeckUpgradeCard() {
		// given
		UpgradeCardRequest updateCardRequest = new UpgradeCardRequest(CARD_ID);
		// when
		Response response = playerDeckResource.postDeckUpgradeCard(transport, updateCardRequest);
		// then
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		assertThat(response.getEntity()).isInstanceOf(UpgradeCardResponse.class);
	}

	@Test
	public void testGetDeckCardsSelected() {
		// given - when
		Response response = playerDeckResource.getDeckCardsSelected(transport);
		// then
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		assertThat(response.getEntity()).asList().containsExactly(1l, 2l);
	}

	@Test
	public void testGetDeckCards() {
		// given
		// when
		Response response = playerDeckResource.getDeckCards(transport);
		// then
		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
	}

	private GetDeckCardsAction aGetDeckCardsAction() {
		GetDeckCardsAction getDeckCardsAction = mock(GetDeckCardsAction.class);
		List<Card> deckCards = aDeckCards();
		when(getDeckCardsAction.getDeckCards(anyString())).thenReturn(deckCards);
		return getDeckCardsAction;
	}

	private GetSelectedCardsFromDeckAction aGetSelectedCardsFromDeckAction() {
		GetSelectedCardsFromDeckAction getSelectedCardsFromDeckAction = mock(GetSelectedCardsFromDeckAction.class);
		List<Long> selectedCardsIds = aSelectedDeckCards();
		when(getSelectedCardsFromDeckAction.getSelectedCards(anyString())).thenReturn(selectedCardsIds);
		return getSelectedCardsFromDeckAction;
	}

	private SelectDeckCardsAction aSelectDeckCardsAction() {
		SelectDeckCardsAction selectDeckCardsAction = mock(SelectDeckCardsAction.class);
		doNothing().when(selectDeckCardsAction).selectDeckCards(anyString(), anyInt(), anyObject());
		return selectDeckCardsAction;
	}

	private UpgradeCardAction anUpgradeCardAction() {
		UpgradeCardAction upgradeCardAction = mock(UpgradeCardAction.class);
		Card card = mock(Card.class);
		when(upgradeCardAction.upgradeCard(LOGIN_ID, CARD_ID)).thenReturn(card);
		return upgradeCardAction;
	}

	private List<Long> aSelectedDeckCards() {
		List<Long> selectedCardsIds = Lists.newArrayList();
		selectedCardsIds.add(1l);
		selectedCardsIds.add(2l);
		return selectedCardsIds;
	}

	private List<Card> aDeckCards() {
		List<Card> deckCards = Lists.newArrayList();
		Card card = mock(Card.class);
		deckCards.add(card);
		return deckCards;
	}
}
