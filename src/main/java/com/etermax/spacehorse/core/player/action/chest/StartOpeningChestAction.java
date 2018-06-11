package com.etermax.spacehorse.core.player.action.chest;

import java.util.Date;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.model.inventory.chest.ChestState;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chests;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class StartOpeningChestAction {

	private final CatalogRepository catalogRepository;
	private final PlayerRepository playerRepository;
	private final ServerTimeProvider serverTimeProvider;

	public StartOpeningChestAction(CatalogRepository catalogRepository, PlayerRepository playerRepository, ServerTimeProvider serverTimeProvider) {
		this.catalogRepository = catalogRepository;
		this.playerRepository = playerRepository;
		this.serverTimeProvider = serverTimeProvider;
	}

	public Chest start(String playerId, Long chestId) {
		Player player = playerRepository.find(playerId);
		Catalog catalog = catalogRepository.getActiveCatalogWithTag(player.getAbTag());
		Chest chest = getChest(chestId, player);
		startOpeningChest(player.getInventory().getChests(), chest, getServerDate(), catalog.getGameConstants(),
				catalog.getChestDefinitionsCollection());
		playerRepository.update(player);
		return chest;
	}

	private void startOpeningChest(Chests chests, Chest chest, Date serverDate, GameConstants gameConstants,
			CatalogEntriesCollection<ChestDefinition> chestDefinitionsCatalog) {
		if (!canStartOpeningChest(chests, chest, serverDate, gameConstants)) {
			throw new ApiException("Chest can't be started to open");
		}
		chest.startOpening(serverDate, chestDefinitionsCatalog);
	}

	private boolean canStartOpeningChest(Chests chests, Chest chest, Date serverDate, GameConstants gameConstants) {
		if (!chest.isClosed()) {
			return false;
		}
		long openingChestsCount = chests.getChests().stream().filter(chestFilter -> chestFilter.getChestState(serverDate).equals(ChestState.OPENING))
				.count();
		return openingChestsCount < gameConstants.getMaxOpeningChests();
	}

	private Chest getChest(Long chestId, Player player) {
		return player.getInventory().getChests().findChestById(chestId).orElseThrow(() -> new ApiException("Unknown chest"));
	}

	private Date getServerDate() {
		return serverTimeProvider.getDate();
	}

}

