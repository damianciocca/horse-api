package com.etermax.spacehorse.core.player.action.deck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class GetSelectedCardsFromDeckActionTest {

	private static final String PLAYER_ID = "100";
	private Player player;
	private GetSelectedCardsFromDeckAction action;

	@Before
	public void setup() {
		player = new PlayerScenarioBuilder("100").build();
		PlayerRepository playerRepository = aPlayerRepository();
		action = new GetSelectedCardsFromDeckAction(playerRepository);
	}

	@Test
	public void whenGetSelectedCardsThenTheSelectedCardsShouldBeFound() {

		List<Long> selectedCardIds = action.getSelectedCards(PLAYER_ID);

		thenTheSelectedCardIdsAreEigth(selectedCardIds);
	}

	private void thenTheSelectedCardIdsAreEigth(List<Long> selectedCardIds) {
		assertThat(selectedCardIds).containsExactly(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L);
	}

	private PlayerRepository aPlayerRepository() {
		PlayerRepository playerRepository = mock(PlayerRepository.class);
		when(playerRepository.find(PLAYER_ID)).thenReturn(player);
		return playerRepository;
	}
}
