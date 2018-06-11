package com.etermax.spacehorse.core.player.action.deck;

import java.util.List;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class GetSelectedCardsFromDeckAction {

	private final PlayerRepository playerRepository;

	public GetSelectedCardsFromDeckAction(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	public List<Long> getSelectedCards(String loginId) {
		Player player = playerRepository.find(loginId);
		Deck cardDeck = player.getDeck();
		return cardDeck.getSelectedCardsIds();
	}
}
