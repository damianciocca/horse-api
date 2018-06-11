package com.etermax.spacehorse.core.player.action.deck;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardParameterLevel;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class UpgradeCardAction {

	private final PlayerRepository playerRepository;
	private final CatalogRepository catalogRepository;
	private final AchievementsObserver achievementsObserver;

	public UpgradeCardAction(PlayerRepository playerRepository, CatalogRepository catalogRepository, AchievementsObserver achievementsObserver) {
		this.playerRepository = playerRepository;
		this.catalogRepository = catalogRepository;
		this.achievementsObserver = achievementsObserver;
	}

	public Card upgradeCard(String loginId, Long cardId) {
		Player player = playerRepository.find(loginId);
		Catalog catalog = catalogRepository.getActiveCatalogWithTag(player.getAbTag());
		return upgradeCard(player, catalog, cardId);
	}

	private Card upgradeCard(Player player, Catalog catalog, Long cardId) {
		Card card = getDeckCard(player, cardId);
		validateCardUpgradeRequirements(card, catalog, player);
		removeCardUpgradeRequirements(card, catalog, player);
		giveCardUpgradeRewards(card, catalog, player);
		card.upgrade();
		playerRepository.update(player);
		achievementsObserver.update(player, catalog.getAchievementsDefinitionsCollection().getEntries());
		return card;
	}

	private void giveCardUpgradeRewards(Card card, Catalog catalog, Player player) {
		CardParameterLevel levelParameter = getCardParameterNextLevel(card, catalog);
		player.getProgress().addXp(levelParameter.getUpgradeRewardXP(), catalog.getPlayerLevelsCollection());
	}

	private Card getDeckCard(Player player, Long cardId) {
		return player.getDeck().findCardById(cardId).orElseThrow(() -> new ApiException(("Unknown card")));
	}

	private void removeCardUpgradeRequirements(Card card, Catalog catalog, Player player) {
		CardParameterLevel levelParameter = getCardParameterNextLevel(card, catalog);
		player.getInventory().getGold().remove(levelParameter.getUpgradeCostGold());
		player.getInventory().getCardParts().remove(card.getCardType(), levelParameter.getUpgradeCostCardParts());
	}

	private CardParameterLevel getCardParameterNextLevel(Card card, Catalog catalog) {
		CardDefinition cardDefinition = card.getDefinition(catalog);
		return catalog.getCardParameterLevelsCollection().findByRarityAndLevel(cardDefinition.getCardRarity(), card.getLevel() + 1)
				.orElseThrow(() -> new ApiException("Missing card level parameter"));
	}

	private void validateCardUpgradeRequirements(Card card, Catalog catalog, Player player) {
		validateCardMaxLevel(card, catalog);
		CardParameterLevel levelParameter = getCardParameterNextLevel(card, catalog);
		if (player.getInventory().getGold().getAmount() < levelParameter.getUpgradeCostGold()) {
			throw new ApiException("Not enough gold");
		}
		if (player.getInventory().getCardParts().getAmount(card.getCardType()) < levelParameter.getUpgradeCostCardParts()) {
			throw new ApiException("Not enough card parts");
		}
	}

	private void validateCardMaxLevel(Card card, Catalog catalog) {
		CardDefinition cardDefinition = card.getDefinition(catalog);
		if (card.getLevel() + 1 >= catalog.getCardParameterLevelsCollection().getMaxCardLevelByRarity(cardDefinition.getCardRarity())) {
			throw new ApiException("Card max level reached");
		}
	}
}
