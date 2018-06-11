package com.etermax.spacehorse.core.shop.action;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.freechest.model.FreeChest;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.PlayerBuilder;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.shop.exception.ExpiredShopCardsException;
import com.etermax.spacehorse.core.shop.exception.InvalidShopCardException;
import com.etermax.spacehorse.core.shop.exception.NotEnoughGemsException;
import com.etermax.spacehorse.core.shop.exception.NotEnoughGoldException;
import com.etermax.spacehorse.core.shop.exception.ShopCardNotFoundException;
import com.etermax.spacehorse.core.shop.model.DynamicShop;
import com.etermax.spacehorse.core.shop.model.PriceType;
import com.etermax.spacehorse.core.shop.model.ShopCard;
import com.etermax.spacehorse.core.shop.model.ShopCards;

public class ShopBuyCardActionTest {

	public static final String SHOP_CARD_ID = "aa";
	public static final String CARD_TYPE = "cardTypeA";
	public static final int CARD_TOTAL = 10;
	public static final int CARD_BASE_PRICE = 100;
	public static final int CARD_PRICE_INCREASE_PER_PURCHASE = 50;
	public static final int PLAYER_GOLD = 999999;
	public static final int PLAYER_GEMS = 888888;

	private PlayerRepository playerRepository;
	private ShopBuyCardAction shopBuyCardAction;
	private Player player;

	private ShopCard shopCard;
	private ServerTimeProvider timeProvider;

	@Before
	public void setUp() {
		playerRepository = mock(PlayerRepository.class);
		timeProvider = new FixedServerTimeProvider();
		shopBuyCardAction = new ShopBuyCardAction(playerRepository, timeProvider, newArrayList());
	}

	@After
	public void tearDown() {
		reset(playerRepository);
	}

	@Test(expected = ShopCardNotFoundException.class)
	public void tryingToBuyAnUnavailableCardFails() {

		givenADefaultShopCard();
		givenAPlayerWithoutShopCards();

		whenBuyingCard();
	}

	@Test(expected = InvalidShopCardException.class)
	public void tryingToBuyAnExhaustedCardFails() {
		givenAnExhaustedShopCard();
		givenAPlayerWithShopCard();

		whenBuyingCard();
	}

	@Test(expected = ExpiredShopCardsException.class)
	public void tryingToBuyFromAnExpiredCardsShopFails() {
		givenADefaultShopCard();
		givenAPlayerWithAnExpiredShop();

		whenBuyingCard();
	}

	@Test(expected = NotEnoughGoldException.class)
	public void tryingToBuyWithoutEnoughGoldFails() {
		givenADefaultShopCard();
		givenAPlayerWithShopCard();

		whenBuyingCard();
	}

	@Test(expected = NotEnoughGemsException.class)
	public void tryingToBuyWithoutEnoughGemsFails() {
		givenAGemsShopCard();
		givenAPlayerWithShopCard();

		whenBuyingCard();
	}

	@Test
	public void buyingACardGivesTheCardPart() {
		givenADefaultShopCard();
		givenAPlayerWithShopCardAndEnoughMoney();

		whenBuyingCard();

		thenThePlayerHasTheCardPart();
	}

	@Test
	public void buyingACardRemovesThePurchasePrice() {
		givenADefaultShopCard();
		givenAPlayerWithShopCardAndEnoughMoney();

		int price = shopCard.getCurrentPrice();
		whenBuyingCard();

		thenTheGoldPriceWasRemved(price);
	}

	@Test
	public void buyingAGemsCardRemovesThePurchasePrice() {
		givenAGemsShopCard();
		givenAPlayerWithShopCardAndEnoughMoney();

		int price = shopCard.getCurrentPrice();
		whenBuyingCard();

		thenTheGemsPriceWasRemved(price);
	}

	@Test
	public void buyingACardUnlocksTheCard() {
		givenADefaultShopCard();
		givenAPlayerWithShopCardAndEnoughMoney();

		whenBuyingCard();

		thenTheCardWasUnlocked();
	}

	@Test
	public void buyingACardReducesTheAmountOfRemainingCards() {
		givenADefaultShopCard();
		givenAPlayerWithShopCardAndEnoughMoney();

		whenBuyingCard();

		thenTheAmountOfRemainingCardsWasDecreased();
	}

	private void thenTheAmountOfRemainingCardsWasDecreased() {
		assertEquals(shopCard.getRemaining(), CARD_TOTAL - 1);
	}

	@Test
	public void buyingACardUpdatedThePlayerInTheRepository() {
		givenADefaultShopCard();
		givenAPlayerWithShopCardAndEnoughMoney();

		whenBuyingCard();

		thenThePlayerWasUpdatedInTheRepository();
	}

	private void thenThePlayerWasUpdatedInTheRepository() {
		verify(playerRepository).update(player);
	}

	private void thenTheCardWasUnlocked() {
		assertNotNull(player.getDeck().findCardByCardType(CARD_TYPE));
	}

	private void thenTheGemsPriceWasRemved(int price) {
		assertEquals(player.getInventory().getGems().getAmount(), PLAYER_GEMS - price);
	}

	private void thenTheGoldPriceWasRemved(int price) {
		assertEquals(player.getInventory().getGold().getAmount(), PLAYER_GOLD - price);
	}

	private void thenThePlayerHasTheCardPart() {
		assertEquals(player.getInventory().getCardParts().getAmount(shopCard.getCardType()), 1);
	}

	private void givenAPlayerWithShopCardAndEnoughMoney() {
		givenAPlayerWithShopCard();
		player.getInventory().getGold().setAmount(PLAYER_GOLD);
		player.getInventory().getGems().setAmount(PLAYER_GEMS);

	}

	private void givenAGemsShopCard() {
		shopCard = new ShopCard(SHOP_CARD_ID, CARD_TYPE, CARD_TOTAL, CARD_TOTAL, CARD_BASE_PRICE, CARD_PRICE_INCREASE_PER_PURCHASE, PriceType.GEMS);
	}

	private void givenAPlayerWithAnExpiredShop() {
		player = new PlayerBuilder().setDynamicShop(new DynamicShop(new ShopCards(Arrays.asList(shopCard), 0))).setFreeChest(new FreeChest())
				.createPlayer();
	}

	private void givenAPlayerWithShopCard() {
		player = new PlayerBuilder().setDynamicShop(new DynamicShop(new ShopCards(Arrays.asList(shopCard), Long.MAX_VALUE))).createPlayer();
	}

	private void givenAnExhaustedShopCard() {
		shopCard = new ShopCard(SHOP_CARD_ID, CARD_TYPE, CARD_TOTAL, 0, CARD_BASE_PRICE, CARD_PRICE_INCREASE_PER_PURCHASE, PriceType.GOLD);
	}

	private void whenBuyingCard() {
		shopBuyCardAction.buy(player, SHOP_CARD_ID, newArrayList());
	}

	private void givenADefaultShopCard() {
		shopCard = new ShopCard(SHOP_CARD_ID, CARD_TYPE, CARD_TOTAL, CARD_TOTAL, CARD_BASE_PRICE, CARD_PRICE_INCREASE_PER_PURCHASE, PriceType.GOLD);
	}

	private void givenAPlayerWithoutShopCards() {
		player = new PlayerBuilder().createPlayer();
	}
}