package com.etermax.spacehorse.core.shop.action;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.player.model.PlayerBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ShopCardDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.shop.exception.ShopCardsNotExpiredException;
import com.etermax.spacehorse.core.shop.model.DynamicShop;
import com.etermax.spacehorse.core.shop.model.PriceType;
import com.etermax.spacehorse.core.shop.model.ShopCards;

public class ShopRefreshCardsActionTest {

	public static final String CARD_COMMON_ID = "cardCommon";
	public static final String CARD_RARE_ID = "cardRare";
	public static final String CARD_EPIC_ID = "cardEpic";

	private PlayerRepository playerRepository;
	private PlayerWinRateRepository playerWinRateRepository;
	private ShopRefreshCardsAction shopRefreshCardsAction;
	private Player player;
	private CatalogEntriesCollection<CardDefinition> cardsCatalog;
	private CatalogEntriesCollection<ShopCardDefinition> shopCardsCatalog;
	private CatalogEntriesCollection<MapDefinition> mapsCatalog;
	private FixedServerTimeProvider timeProvider;

	@Before
	public void setUp() {

		timeProvider = new FixedServerTimeProvider();

		playerRepository = mock(PlayerRepository.class);
		playerWinRateRepository = mock(PlayerWinRateRepository.class);

		when(playerWinRateRepository.findOrCrateDefault(any())).thenReturn(new PlayerWinRate("user"));
		shopRefreshCardsAction = new ShopRefreshCardsAction(playerRepository, timeProvider);

		cardsCatalog = new CatalogEntriesCollection<>();

		cardsCatalog.addEntry(new CardDefinition(CARD_COMMON_ID, CardRarity.COMMON, 0));
		cardsCatalog.addEntry(new CardDefinition(CARD_RARE_ID, CardRarity.RARE, 0));
		cardsCatalog.addEntry(new CardDefinition(CARD_EPIC_ID, CardRarity.EPIC, 0));

		shopCardsCatalog = new CatalogEntriesCollection<>();

		shopCardsCatalog.addEntry(new ShopCardDefinition("common", CardRarity.COMMON, 10, 100, 200, PriceType.GOLD));
		shopCardsCatalog.addEntry(new ShopCardDefinition("rare", CardRarity.RARE, 10, 100, 200, PriceType.GOLD));
		shopCardsCatalog.addEntry(new ShopCardDefinition("epic", CardRarity.EPIC, 10, 100, 200, PriceType.GOLD));

		mapsCatalog = new CatalogEntriesCollection<>();
		mapsCatalog.addEntry(new MapDefinition("map", 0, 0, 1, 1));
	}

	@After
	public void tearDown() {
		reset(playerRepository);
		reset(playerWinRateRepository);
	}

	@Test(expected = ShopCardsNotExpiredException.class)
	public void tryingToRefreshANonExpiredCardsShopFails() {
		givenAPlayerWithANonExpiredCardsShop();

		whenRefreshing();
	}

	@Test
	public void refreshingACardsShopProducesAShopThatExpiredAtMidnight() {
		givenAPlayerWithAnExpiredCardsShop();

		whenRefreshing();

		thenTheCardsShopExpiresAtMidnight();
	}

	@Test
	public void refreshingACardsShopAddCardsToTheShop() {
		givenAPlayerWithAnExpiredCardsShop();

		whenRefreshing();

		thenTheShopHasCards();
	}

	private void thenTheShopHasCards() {
		assertThat(player.getDynamicShop().getShopCards().getCards().size(), equalTo(3));

		assertThat(player.getDynamicShop().getShopCards().getCards().get(0).getCardType(), equalTo(CARD_COMMON_ID));
		assertThat(player.getDynamicShop().getShopCards().getCards().get(1).getCardType(), equalTo(CARD_RARE_ID));
		assertThat(player.getDynamicShop().getShopCards().getCards().get(2).getCardType(), equalTo(CARD_EPIC_ID));
	}

	@Test
	public void refreshingACardsShopUpdatedThePlayerInTheRepository() {
		givenAPlayerWithAnExpiredCardsShop();

		whenRefreshing();

		thenThePlayerWasUpdatedInTheRepository();
	}


	private void thenThePlayerWasUpdatedInTheRepository() {
		verify(playerRepository).update(player);
	}

	private void thenTheCardsShopExpiresAtMidnight() {
		assertTrue(player.getDynamicShop().getShopCards().isAvailable(timeProvider.getTimeNowAsSeconds()));

		Calendar expirationTime = ServerTime.toCalendar(player.getDynamicShop().getShopCards().getExpirationServerTime());

		assertThat(expirationTime.get(Calendar.MINUTE), equalTo(59));
		assertThat(expirationTime.get(Calendar.SECOND), equalTo(59));
		assertThat(expirationTime.get(Calendar.HOUR_OF_DAY), equalTo(23));
	}

	private void whenRefreshing() {
		shopRefreshCardsAction.refresh(player, cardsCatalog, shopCardsCatalog, mapsCatalog);
	}

	private void givenAPlayerWithANonExpiredCardsShop() {
		player = new PlayerBuilder().setDynamicShop(new DynamicShop(new ShopCards(new ArrayList<>(), Long.MAX_VALUE))).createPlayer();
	}

	private void givenAPlayerWithAnExpiredCardsShop() {
		player = new PlayerBuilder().setDynamicShop(new DynamicShop(new ShopCards(new ArrayList<>(), 0))).createPlayer();
	}
}