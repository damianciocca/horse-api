package com.etermax.spacehorse.core.shop.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import com.etermax.spacehorse.core.battle.model.DefaultPlayerMapNumberCalculator;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.ShopCardDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.shop.exception.ShopCardsNotExpiredException;
import com.etermax.spacehorse.core.shop.model.ShopCard;
import com.etermax.spacehorse.core.shop.model.ShopCards;

public class ShopRefreshCardsAction {

	private final PlayerRepository playerRepository;
	private final ServerTimeProvider timeProvider;

	public ShopRefreshCardsAction(PlayerRepository playerRepository, ServerTimeProvider timeProvider) {
		this.playerRepository = playerRepository;
		this.timeProvider = timeProvider;
	}

	public ShopCards refresh(Player player, CatalogEntriesCollection<CardDefinition> cardsCatalog,
			CatalogEntriesCollection<ShopCardDefinition> shopCardsCatalog, CatalogEntriesCollection<MapDefinition> mapsCatalog) {

		int mapNumber = player.getMapNumber();

		Validate.notNull(player);
		if (player.getDynamicShop().getShopCards().isAvailable(timeProvider.getTimeNowAsSeconds())) {
			throw new ShopCardsNotExpiredException("Shop cards don't expired yet, unable to refresh");
		}

		//TODO: Get client gmt offset from somewhere!
		ShopCards shopCards = getNewShopCards(mapNumber, cardsCatalog, shopCardsCatalog, 0);
		player.getDynamicShop().getShopCards().refresh(shopCards);
		playerRepository.update(player);

		return shopCards;
	}

	private ShopCards getNewShopCards(int mapNumber, CatalogEntriesCollection<CardDefinition> cardsCatalog,
			CatalogEntriesCollection<ShopCardDefinition> shopCardsCatalog, long clientGmtOffsetInSeconds) {

		List<ShopCard> cards = new ArrayList<>();

		shopCardsCatalog.getEntries().forEach(shopCardDefinition -> buildShopCard(shopCardDefinition, mapNumber, cardsCatalog).ifPresent(cards::add));

		long expirationTime = ServerTime.roundToEndOfDay(timeProvider.getTimeNowAsSeconds()) + clientGmtOffsetInSeconds;

		return new ShopCards(cards, expirationTime);
	}

	private Optional<ShopCard> buildShopCard(ShopCardDefinition shopCardDefinition, int mapNumber,
			CatalogEntriesCollection<CardDefinition> cardsCatalog) {

		List<CardDefinition> cardsWithRarity = findCardsByRarityAndMapNumber(cardsCatalog, shopCardDefinition.getCardRarity(), mapNumber);

		if (cardsWithRarity.isEmpty()) {
			return Optional.empty();
		}

		String cardType = getRandomCardType(cardsWithRarity);

		ShopCard shopCard = buildShopCard(shopCardDefinition, cardType);

		return Optional.of(shopCard);
	}

	private ShopCard buildShopCard(ShopCardDefinition shopCardDefinition, String cardType) {
		return new ShopCard(shopCardDefinition.getId(), cardType, shopCardDefinition.getAmount(), shopCardDefinition.getAmount(),
				shopCardDefinition.getBasePrice(), shopCardDefinition.getPriceIncreasePerPurchase(), shopCardDefinition.getPriceType());
	}

	private String getRandomCardType(List<CardDefinition> cardsWithRarity) {
		return cardsWithRarity.get(ThreadLocalRandom.current().nextInt(cardsWithRarity.size())).getId();
	}

	private List<CardDefinition> findCardsByRarityAndMapNumber(CatalogEntriesCollection<CardDefinition> cardsCatalog, CardRarity cardRarity,
			int mapNumber) {
		return cardsCatalog.getEntries().stream().filter(x -> x.getEnabled() && x.isActiveFor(timeProvider.getDateTime()))
				.filter(x -> x.getCardRarity().equals(cardRarity) && mapNumber >= x.getAvailableFromMapId()).collect(Collectors.toList());
	}

}
