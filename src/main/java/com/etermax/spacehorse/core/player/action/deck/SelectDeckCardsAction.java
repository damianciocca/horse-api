package com.etermax.spacehorse.core.player.action.deck;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class SelectDeckCardsAction {

	private final PlayerRepository playerRepository;
	private final CatalogRepository catalogRepository;

	public SelectDeckCardsAction(PlayerRepository playerRepository, CatalogRepository catalogRepository) {
		this.playerRepository = playerRepository;
		this.catalogRepository = catalogRepository;
	}

	public void selectDeckCards(String loginId, Integer cardDeckId, List<Long> selectedCardsIds) {
		Player player = playerRepository.find(loginId);
		Catalog catalog = catalogRepository.getActiveCatalogWithTag(player.getAbTag());
		Deck cardDeck = player.getDeck();
		cardDeck.setSelectedCardsIds(cardDeckId, selectedCardsIds, catalog.getGameConstants());
		playerRepository.update(player);
	}
}
