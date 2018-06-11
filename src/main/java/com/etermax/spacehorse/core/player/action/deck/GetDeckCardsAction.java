package com.etermax.spacehorse.core.player.action.deck;

import java.util.List;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class GetDeckCardsAction {

	private final PlayerRepository playerRepository;

	public GetDeckCardsAction(PlayerRepository playerRepository) {
		this.playerRepository = playerRepository;
	}

	public List<Card> getDeckCards(String loginId) {
		Player player = playerRepository.find(loginId);
		Deck cardDeck = player.getDeck();
		return cardDeck.getOwnedCards();
	}

}
