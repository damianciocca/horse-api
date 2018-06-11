package com.etermax.spacehorse.core.shop.action;

import com.etermax.spacehorse.configuration.EnviromentType;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.inapps.domain.market.Markets;
import com.etermax.spacehorse.core.inapps.domain.market.android.AndroidMarket;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.signature.SignatureVerifier;
import com.etermax.spacehorse.core.inapps.repository.MarketRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.player.repository.dynamo.PlayerDynamoRepository;
import com.etermax.spacehorse.core.reward.model.PurchasableRewardCollector;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.shop.model.InAppReceiptCreationDomainService;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class ShopBuyInAppItemActionTest {

	public static final int MONEY_SPENT = 90;
	private PlayerRepository playerRepository;
	private ApplyRewardDomainService applyRewardDomainService;
	private ShopBuyInAppItemAction shopBuyInappItemAction;
	private PlayerWinRateRepository playerWinRateRepository;
	private Catalog catalog;

	private static final String PLAYER_ID = "Player1Id";
	private static final int PLAYER_GEMS = 50000;
	private static final String GEMS_ID = "Gems500";
	private static final int GEMS_QUANTITY = 500;
	private static final int MMR = 100;
	private Markets markets;
	private Player player;

	@Before
	public void setUp() {
		ServerTimeProvider serverTimeProvider = new FixedServerTimeProvider();
		playerRepository = mock(PlayerDynamoRepository.class);
		catalog = MockUtils.mockCatalog();
		GetRewardsDomainService getRewardsDomainService = mock(GetRewardsDomainService.class);
		applyRewardDomainService = mock(ApplyRewardDomainService.class);
		playerWinRateRepository = mock(PlayerWinRateRepository.class);
		when(playerWinRateRepository.findOrCrateDefault(any())).thenReturn(new PlayerWinRate(PLAYER_ID, 1,1,1,100));
		when(playerWinRateRepository.findOrCrateDefault(any())).thenReturn(new PlayerWinRate(PLAYER_ID, 1,1,1, MMR));

		ShopBuyItemAction shopBuyItemAction = new ShopBuyItemAction(playerRepository, applyRewardDomainService, new PurchasableRewardCollector(getRewardsDomainService),
				serverTimeProvider, mock(AchievementsFactory.class));

		markets = mock(Markets.class);
		MarketRepository marketRepository = mock(MarketRepository.class);
		InAppReceiptCreationDomainService inAppReceiptCreationDomainService = new InAppReceiptCreationDomainService(markets, marketRepository, EnviromentType.DEVELOPMENT);
		shopBuyInappItemAction = new ShopBuyInAppItemAction(shopBuyItemAction, inAppReceiptCreationDomainService);
		player = new PlayerScenarioBuilder(PLAYER_ID).withGems(PLAYER_GEMS).build();
	}

	@After
	public void tearDown() {
		reset(playerRepository);
	}

	@Test
	public void testBuyInAppItem() {
		SignatureVerifier signatureVerifier = mock(SignatureVerifier.class);
		when(signatureVerifier.verifySignature(any(), any())).thenReturn(true);
		AndroidMarket androidMarket = mock(AndroidMarket.class);
		when(markets.findById(Platform.ANDROID)).thenReturn(Optional.of(androidMarket));
		when(androidMarket.validate(any())).thenReturn(true);
		when(androidMarket.canBeUsedIn(any())).thenReturn(true);

		User user = MockUtils.mockUser(PLAYER_ID, Platform.ANDROID);
		MockUtils.mockGemsRewardsForPlayer(player, GEMS_QUANTITY, playerRepository, applyRewardDomainService);
		String receipt = "{\n" + "\t\"Store\": \"GooglePlay\",\n" + "\t\"TransactionID\": \"GPA.3354-5649-0281-57515\",\n" + "\t\"Payload\": {\n"
				+ "\t\t\"json\": {\n" + "\t\t\t\"orderId\": \"GPA.3354-5649-0281-57515\",\n" + "\t\t\t\"packageName\": \"com.etermax.spacehorse2\",\n"
				+ "\t\t\t\"productId\": \"com.etermax.orbital1.bunch\",\n" + "\t\t\t\"purchaseTime\": 1502128408636,\n" + "\t\t\t\"purchaseState\": 0,\n"
				+ "\t\t\t\"purchaseToken\": \"ekdjmdgdggfcjekeecjohmgi.AO-J1OxhQJqn9xK-OV-nGR2maf5PXRVkp-74fs_2UTZ--08TyGYdlTqN4pdeggykX8mCljioZrRX1HAmbVpf64uZsDTiY9w9JjYkqBE5xDIsV2sg7pOFEkkcqh0g1VqfouHtbCEkm62A\"\n"
				+ "\t\t},\n"
				+ "\t\t\"signature\": \"BJup7haTRYor9tQ45F+62Ua4pWM5FrgGBrbUM\\/Ry53e3\\/NkcJ1wOZ75vUrSWeABD6tyxVlfgNI9nbIJ4CrYYYmJK+8TNds9+dhJDTAF8WPbVWYtPTCFeLrNRg8k9vKFf2XhYetvlggKN+XwJfcc8L1nQeGNNrGtAhRiJDEqVh7G7ehPbQJgCECn9Evxtu+eHZ0mKydz1EuFWlZqtI6\\/0gki9cWoDkyckKq++zxjZAU2VjzBhDMzs9IAjNL6pNVYEYj9m9Ibc7MzTijlBjcnoVNaaZo1qnkgrAUxmF4zkTcRz3VafgnsQbghssHC2tuQ\\/IYp+aIAdeTRptHzMlNDtbg==\"\n"
				+ "\t}\n" + "}";
		List<RewardResponse> rewardResponses = shopBuyInappItemAction.buyInApp(player, user, catalog, GEMS_ID, receipt);
		assertNotNull(rewardResponses);
		assertEquals(1, rewardResponses.size());
		RewardResponse reward = rewardResponses.stream().findFirst().get();
		assertEquals(RewardType.GEMS, reward.getRewardType());
		assertEquals(new Integer(GEMS_QUANTITY), reward.getAmount());
		assertEquals(PLAYER_GEMS, player.getInventory().getGems().getAmount());
		assertEquals(MONEY_SPENT, player.getPlayerStats().getMoneySpentInUsdCents());
	}

}
