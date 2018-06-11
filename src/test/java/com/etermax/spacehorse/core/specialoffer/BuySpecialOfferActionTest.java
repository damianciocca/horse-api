package com.etermax.spacehorse.core.specialoffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.function.Predicate;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.reward.model.PurchasableRewardCollector;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.action.BuySpecialOfferAction;
import com.etermax.spacehorse.core.specialoffer.model.BuySpecialOfferDomainService;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;
import com.etermax.spacehorse.mock.MockCatalog;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.etermax.spacehorse.mock.SpecialOfferScenarioBuilder;

public class BuySpecialOfferActionTest {

	private static final int INITIAL_GOLD = 300;
	private static final String SPECIAL_OFFER_ID = "special_offer_epic_chest_0";
	private ServerTimeProvider timeProvider;
	private PlayerRepository playerRepository;
	private ApplyRewardDomainService applyRewardDomainService;
	private Catalog catalog;
	private SpecialOfferBoardRepository specialOfferBoardRepository;
	private Player player;
	private BuySpecialOfferAction action;

	@Before
	public void setUp() throws Exception {
		timeProvider = new FixedServerTimeProvider();
		playerRepository = mock(PlayerRepository.class);
		applyRewardDomainService = new ApplyRewardDomainService();
		catalog = MockCatalog.buildCatalog();
		specialOfferBoardRepository = new InMemorySpecialOfferBoardRepository(timeProvider);
		player = new PlayerScenarioBuilder("10").withGold(INITIAL_GOLD).build();

		BuySpecialOfferDomainService buySpecialOfferDomainService = new BuySpecialOfferDomainService(specialOfferBoardRepository, playerRepository,
				applyRewardDomainService, new PurchasableRewardCollector(new GetRewardsDomainService()), mock(AchievementsFactory.class));

		action = new BuySpecialOfferAction(buySpecialOfferDomainService, timeProvider);
	}

	@Test
	public void whenBuyAnOfferOfAnEpicChestByMapThenTheGoldOfPlayerDecreaseAndTheNewRewardsShouldBeApplied() throws Exception {

		givenAnSpecialOfferRepositoryWithOneOffer();

		List<RewardResponse> rewards = action.buy(player, SPECIAL_OFFER_ID, catalog);

		assertThatTheGoldsWereDecreaseAndRewardsWereApplied(rewards);
	}

	private void assertThatTheGoldsWereDecreaseAndRewardsWereApplied(List<RewardResponse> rewards) {
		assertThat(rewards).extracting(RewardResponse::getRewardType)
				.allMatch((Predicate<RewardType>) rewardType -> rewardType == RewardType.CARD_PARTS);
		assertThat(rewards.stream().mapToInt(RewardResponse::getAmount).sum()).isEqualTo(7); // sale del catalogo chestChantes Chances_courier_Epic_0
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(100);
	}

	private void givenAnSpecialOfferRepositoryWithOneOffer() {
		SpecialOfferBoard specialOfferBoard = new SpecialOfferBoard(timeProvider);
		SpecialOffer specialOffer = new SpecialOfferScenarioBuilder(SPECIAL_OFFER_ID).withChestItem("gloriousChestId").build();
		specialOfferBoard.put(specialOffer);
		specialOfferBoardRepository.addOrUpdate(player, specialOfferBoard);
	}
}
