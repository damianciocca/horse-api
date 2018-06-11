package com.etermax.spacehorse.core.shop.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.player.repository.dynamo.PlayerDynamoRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.reward.model.PurchasableRewardCollector;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.shop.exception.NotEnoughGemsException;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class ShopBuyItemActionTest {

	private static final String PLAYER_ID = "Player1Id";
	private static final int PLAYER_GEMS = 50000;
	private static final String GOLD_ID = "Gold800";
	private static final int GOLD_QUANTITY = 800;
	private static final int GOLD_PRICE = 50;
	private static final String GEMS_ID = "Gems500";
	private static final int GEMS_QUANTITY = 500;
	private static final String CHEST_ID = "ChestGlorious";
	private static final int CHEST_PRICE = 400;
	private PlayerRepository playerRepository;
	private ShopBuyItemAction shopBuyItemAction;
	private GetRewardsDomainService getRewardsDomainService;
	private ApplyRewardDomainService applyRewardDomainService;
	private Catalog catalog;

	@Before
	public void setUp() {
		ServerTimeProvider timeProvider = new FixedServerTimeProvider();
		playerRepository = mock(PlayerDynamoRepository.class);
		PlayerWinRateRepository playerWinRateRepository = mock(PlayerWinRateRepository.class);
		catalog = MockUtils.mockCatalog();
		getRewardsDomainService = new GetRewardsDomainService();
		applyRewardDomainService = new ApplyRewardDomainService();
		when(playerWinRateRepository.findOrCrateDefault(any())).thenReturn(new PlayerWinRate(""));

		shopBuyItemAction = new ShopBuyItemAction(playerRepository, applyRewardDomainService,
				new PurchasableRewardCollector(getRewardsDomainService), timeProvider, mock(AchievementsFactory.class));
	}

	@After
	public void tearDown() {
		reset(playerRepository);
	}

	@Test(expected = NullPointerException.class)
	public void testInvalidBuyRequest() {
		shopBuyItemAction.buy(null, catalog, GOLD_ID);
	}

	@Test(expected = NotEnoughGemsException.class)
	public void testNotEnoughGemsBuy() {
		Player player = MockUtils.mockPlayerWithGems(PLAYER_ID, 0, playerRepository);
		shopBuyItemAction.buy(player, catalog, GOLD_ID);
	}

	@Test
	public void testSuccessfulGoldBuy() {
		// Given
		Player player = new PlayerScenarioBuilder(PLAYER_ID).withGems(PLAYER_GEMS).withGold(GOLD_QUANTITY).build();
		// When
		List<RewardResponse> rewardResponses = shopBuyItemAction.buy(player, catalog, GOLD_ID);
		// Then
		assertNotNull(rewardResponses);
		assertEquals(1, rewardResponses.size());
		RewardResponse reward = rewardResponses.stream().findFirst().get();
		assertEquals(RewardType.GOLD, reward.getRewardType());
		assertEquals(new Integer(GOLD_QUANTITY), reward.getAmount());
		assertEquals(new Integer(PLAYER_GEMS) - new Integer(GOLD_PRICE), player.getInventory().getGems().getAmount());
	}

	@Test
	public void testSuccessfulGemsBuy() {
		// Given
		Player player = new PlayerScenarioBuilder(PLAYER_ID).withGold(0).withGems(PLAYER_GEMS).build();
		// When
		List<RewardResponse> rewardResponses = shopBuyItemAction.buy(player, catalog, GEMS_ID);
		// Then
		assertNotNull(rewardResponses);
		assertEquals(1, rewardResponses.size());
		RewardResponse reward = rewardResponses.stream().findFirst().get();
		assertEquals(RewardType.GEMS, reward.getRewardType());
		assertEquals(new Integer(GEMS_QUANTITY), reward.getAmount());
		assertEquals(50500, player.getInventory().getGems().getAmount());
	}

	@Test
	public void testSuccessfulChestBuy() {
		// Given
		Player player = new PlayerScenarioBuilder(PLAYER_ID).withGems(PLAYER_GEMS).build();
		// when
		List<RewardResponse> rewardResponses = shopBuyItemAction.buy(player, catalog, CHEST_ID);
		// then
		assertNotNull(rewardResponses);
		assertThat(rewardResponses.size()).isGreaterThanOrEqualTo(2);
		assertThat(rewardResponses).extracting(RewardResponse::getRewardType).contains(RewardType.GOLD, RewardType.CARD_PARTS);
		assertEquals(new Integer(PLAYER_GEMS) - new Integer(CHEST_PRICE), player.getInventory().getGems().getAmount());
	}

}
