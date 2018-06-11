package com.etermax.spacehorse.core.player.action.deck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class GetDeckCardsActionTest {

	private static final String PLAYER_ID = "100";
	private Player player;
	private GetDeckCardsAction action;

	@Before
	public void setup() {
		player = new PlayerScenarioBuilder("100").build();
		PlayerRepository playerRepository = aPlayerRepository();
		action = new GetDeckCardsAction(playerRepository);
	}

	@Test
	public void whenGetDeckCardsThenTheDeckCardsShouldBeFound() {

		List<Card> deckCards = whenGetDeckCards();

		thenTheDeckCardsAreEight(deckCards);
	}

	private void thenTheDeckCardsAreEight(List<Card> deckCards) {
		assertThat(deckCards).hasSize(8);
		assertThat(deckCards).extracting(Card::getCardType).containsExactly(expectedCards());
	}

	private List<Card> whenGetDeckCards() {
		return action.getDeckCards(PLAYER_ID);
	}

	private String[] expectedCards() {
		return new String[] { //
				"card_enforcer", //
				"card_electric_duo", //
				"card_mastodon", //
				"card_punk_shooter", //
				"card_laser", //
				"card_flying_headstrongs", //
				"card_spitter", //
				"card_headstrongs" };
	}

	private PlayerRepository aPlayerRepository() {
		PlayerRepository playerRepository = mock(PlayerRepository.class);
		when(playerRepository.find(PLAYER_ID)).thenReturn(player);
		return playerRepository;
	}
}
