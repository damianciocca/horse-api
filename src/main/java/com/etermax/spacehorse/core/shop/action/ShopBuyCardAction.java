package com.etermax.spacehorse.core.shop.action;

import java.util.List;

import org.apache.commons.lang3.Validate;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.model.inventory.PaymentMethodUtil;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.shop.exception.ExpiredShopCardsException;
import com.etermax.spacehorse.core.shop.exception.InvalidShopCardException;
import com.etermax.spacehorse.core.shop.exception.ShopCardNotFoundException;
import com.etermax.spacehorse.core.shop.model.ShopCard;

public class ShopBuyCardAction {

	private final PlayerRepository playerRepository;
	private final ServerTimeProvider serverTimeProvider;
	private final List<AchievementsObserver> achievementsObservers;

	public ShopBuyCardAction(PlayerRepository playerRepository, ServerTimeProvider serverTimeProvider, List<AchievementsObserver> achievementsObservers) {
		this.playerRepository = playerRepository;
		this.serverTimeProvider = serverTimeProvider;
		this.achievementsObservers = achievementsObservers;
	}

	public Card buy(Player player, String shopCardId, List<AchievementDefinition> achievementDefinitions) {
		Validate.notNull(player);
		Validate.notNull(shopCardId);

		ShopCard shopCard = player.getDynamicShop().getShopCards().findById(shopCardId)
				.orElseThrow(() -> new ShopCardNotFoundException("Invalid shop card id " + shopCardId));

		if (!player.getDynamicShop().getShopCards().isAvailable(serverTimeProvider.getTimeNowAsSeconds())) {
			throw new ExpiredShopCardsException("Shop cards expired, must be refreshed");
		}

		if (!shopCard.isAvailable()) {
			throw new InvalidShopCardException("Shop card not available");
		}

		Card card = this.buyShopCard(shopCard, player);

		achievementsObservers.forEach(achievementsObserver -> achievementsObserver.update(player, achievementDefinitions));

		return card;
	}

	private Card buyShopCard(ShopCard shopCard, Player player) {
		removePrice(player, shopCard);
		giveCardPart(player, shopCard);
		shopCard.consumeRemaining();
		Card unlockedCard = unlockPlayerCardIfRequired(player, shopCard);
		playerRepository.update(player);
		return unlockedCard;
	}

	private Card unlockPlayerCardIfRequired(Player player, ShopCard shopCard) {
		if (player.getDeck().findCardByCardType(shopCard.getCardType()) == null) {
			return player.getDeck().addNewCard(shopCard.getCardType());
		}
		return null;
	}

	private void giveCardPart(Player player, ShopCard shopCard) {
		player.getInventory().getCardParts().add(shopCard.getCardType(), 1);
	}

	private void removePrice(Player player, ShopCard shopCard) {
		switch (shopCard.getPriceType()) {
			case GOLD:
				PaymentMethodUtil.payWithGold(player, shopCard.getCurrentPrice());
				break;

			case GEMS:
				PaymentMethodUtil.payWithGems(player, shopCard.getCurrentPrice());
				break;
		}
	}
}
