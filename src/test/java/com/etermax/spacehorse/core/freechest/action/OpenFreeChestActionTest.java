package com.etermax.spacehorse.core.freechest.action;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.catalog.model.GemsCycle;
import com.etermax.spacehorse.core.catalog.model.GemsCycleEntry;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.freechest.exception.FreeChestNotReadyException;
import com.etermax.spacehorse.core.freechest.model.FreeChest;
import com.etermax.spacehorse.core.freechest.resource.response.OpenFreeChestResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.PlayerBuilder;
import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.model.progress.PlayerProgress;
import com.etermax.spacehorse.core.player.model.progress.PlayerRewards;
import com.etermax.spacehorse.core.player.model.progress.TutorialProgress;
import com.etermax.spacehorse.core.player.model.stats.PlayerStats;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils.CardDropRateCalculatorConfiguration;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.shop.model.DynamicShop;
import com.etermax.spacehorse.core.shop.model.ShopCards;
import com.google.common.collect.Lists;

public class OpenFreeChestActionTest {

	private static final int MAX_FREE_CHESTS = 5;
	private static final int SECONDS_BETWEEN_FREE_CHESTS = 8 * 60 * 60; //8hs
	private static final String FREE_CHEST_ID = "freeChest";
	private static final String GEMS_CYCLE_SEQ_ID = "ListA";

	private PlayerRepository playerRepository;
	private OpenFreeChestAction openFreeChestAction;
	private CatalogRepository catalogRepository;
	private PlayerWinRateRepository playerWinRateRepository;

	private Player player;
	private Catalog catalog;

	private List<Reward> expectedRewards;
	private List<RewardResponse> expectedRewardsResponse;

	private OpenFreeChestResponse openResponse;

	private Date now;
	private FixedServerTimeProvider timeProvider;
	private GetRewardsDomainService getRewardsDomainService;
	private ApplyRewardDomainService applyRewardDomainService;
	private ArgumentCaptor<GetRewardConfiguration> argumentCaptorGetRewardConf;

	@Before
	public void setUp() {

		timeProvider = new FixedServerTimeProvider();

		player = mock(Player.class);

		playerRepository = mock(PlayerRepository.class);
		catalogRepository = mock(CatalogRepository.class);
		getRewardsDomainService = mock(GetRewardsDomainService.class);
		applyRewardDomainService = mock(ApplyRewardDomainService.class);
		playerWinRateRepository = mock(PlayerWinRateRepository.class);

		expectedRewards = Arrays.asList(new Reward(RewardType.GOLD, 100), new Reward(RewardType.GEMS, 10));
		expectedRewardsResponse = Arrays.asList(new RewardResponse(RewardType.GOLD, 100), new RewardResponse(RewardType.GEMS, 10));

		when(getRewardsDomainService.getRewards(any(), any(), anyInt(), any())).thenReturn(expectedRewards);

		when(applyRewardDomainService.applyRewards(any(), eq(expectedRewards), any(), any(), anyList())).thenReturn(expectedRewardsResponse);
		doNothing().when(playerRepository).update(any());

		catalog = mock(Catalog.class);

		when(catalog.getGameConstants()).thenReturn(mock(GameConstants.class));

		when(catalog.getCardsDropRateCollection()).thenReturn(new CatalogEntriesCollection<>());

		when(catalog.getTutorialProgressCollection()).thenReturn(new CatalogEntriesCollection<>());

		when(catalog.getChestChancesDefinitionsCollection()).thenReturn(new CatalogEntriesCollection<>());

		CatalogEntriesCollection catalogEntriesCollection = getACardDefinitionsCollectionWithTwoCards();
		when(catalog.getCardDefinitionsCollection()).thenReturn(catalogEntriesCollection);

		when(catalog.getChestDefinitionsCollection())
				.thenReturn(new CatalogEntriesCollection<>(Arrays.asList(new ChestDefinition(FREE_CHEST_ID, "", 0, false, false, false))));

		when(catalog.getGemsCycleCollection())
				.thenReturn(new CatalogEntriesCollection<>(Arrays.asList(new GemsCycle(GEMS_CYCLE_SEQ_ID, Arrays.asList(new GemsCycleEntry(0, 2))))));

		when(catalog.getGameConstants().getCardDropRateCalculatorConfiguration()).thenReturn(new CardDropRateCalculatorConfiguration());

		when(catalog.getFeatureByPlayerLvlDefinitionCollection()).thenReturn(new CatalogEntriesCollection());

		when(catalog.getGameConstants().getMaxFreeChests()).thenReturn(MAX_FREE_CHESTS);
		when(catalog.getGameConstants().getTimeBetweenFreeChestsInSeconds()).thenReturn(SECONDS_BETWEEN_FREE_CHESTS);
		when(catalog.getGameConstants().getFreeChestId()).thenReturn(FREE_CHEST_ID);
		when(catalog.getGameConstants().getDefaultFreeGemsCycleSequenceId()).thenReturn(GEMS_CYCLE_SEQ_ID);

		when(catalog.getMapsCollection()).thenReturn(new CatalogEntriesCollection<>());

		when(catalog.getAchievementsDefinitionsCollection()).thenReturn(new CatalogEntriesCollection<>());

		when(playerWinRateRepository.findOrCrateDefault(anyString())).thenReturn(new PlayerWinRate(""));

		openFreeChestAction = new OpenFreeChestAction(playerRepository, catalogRepository, getRewardsDomainService, applyRewardDomainService,
				timeProvider, mock(AchievementsFactory.class));

		now = new Date();
	}

	@After
	public void tearDown() {
		reset(playerRepository);
		reset(catalogRepository);
		reset(getRewardsDomainService);
		player = null;
		openResponse = null;
		expectedRewards = null;
	}

	@Test(expected = FreeChestNotReadyException.class)
	public void tryingToOpenANonReadyFreeChestFails() {
		givenAPlayerWithANonReadyFreeChest();

		whenOpening();
	}

	@Test
	public void aNonReadyFreeChestHasACorrectNextChestTime() {
		givenAPlayerWithANonReadyFreeChest();

		thenNextChestTimeIs(SECONDS_BETWEEN_FREE_CHESTS);
	}

	@Test
	public void tryingToOpenAReadyFreeChestGivesRewards() {
		givenAPlayerWithAReadyFreeChest();

		whenOpening();

		thenRewardsWereReceived();
	}

	@Test
	public void cardsNotActiveAreFilteredWhenFindRewardsForChest() {
		givenAPlayerWithAReadyFreeChest();
		givenAArgumentCaptorGetRewardConfiguration();

		whenOpening();

		thenGetRewardConfigurationIsCreatedWithoutNotActiveCards();
	}

	@Test
	public void openingAllAvailableFreeChestsLeavesNoChestToBeOpeneded() {
		givenAPlayerWhoDidntLoginForAYear();

		openMaxChests();

		thenNoMoreFreeChestsAreAvailable();
	}

	@Test
	public void openingAllAvailableFreeChestsLeavesNextChestTimeAtCorrectValue() {
		givenAPlayerWhoDidntLoginForAYear();

		openMaxChests();

		thenNextChestTimeIs(SECONDS_BETWEEN_FREE_CHESTS);
	}

	@Test
	public void correctRemainingTimeAfterOpeningAChestAndWaitingSomeSeconds() {
		givenAPlayerWhoDidntLoginForAYear();

		thenNextChestTimeIs(0);

		whenOpening();

		waitSomeSeconds(999);

		for (int i = 1; i < MAX_FREE_CHESTS; i++) {
			thenNextChestTimeIs(SECONDS_BETWEEN_FREE_CHESTS - 999);

			whenOpening();
		}

		thenNextChestTimeIs(SECONDS_BETWEEN_FREE_CHESTS - 999);
	}

	private void thenGetRewardConfigurationIsCreatedWithoutNotActiveCards() {
		verify(getRewardsDomainService)
				.getRewards(any(Player.class), any(ChestDefinition.class), anyInt(), argumentCaptorGetRewardConf.capture());
		GetRewardConfiguration getRewardConfigurationCaptured = argumentCaptorGetRewardConf.getValue();

		List<CardDefinition> cardDefinitions = getRewardConfigurationCaptured.getCardDefinition();
		assertThat(cardDefinitions.size(), is(equalTo(1)));
	}

	private void givenAArgumentCaptorGetRewardConfiguration() {
		argumentCaptorGetRewardConf = ArgumentCaptor.forClass(GetRewardConfiguration.class);
	}

	private void waitSomeSeconds(int seconds) {
		now = new Date(now.getTime() + seconds * 1000L);
	}

	private void thenNextChestTimeIs(int seconds) {
		assertThat(player.getFreeChest().getSecondsToNextFreeChest(now, catalog.getGameConstants()), is(equalTo(seconds)));
	}

	private CatalogEntriesCollection getACardDefinitionsCollectionWithTwoCards() {
		CardDefinition cardDefinition1 = mock(CardDefinition.class);
		when(cardDefinition1.isActiveFor(any())).thenReturn(true);

		CardDefinition cardDefinition2 = mock(CardDefinition.class);
		when(cardDefinition2.isActiveFor(any())).thenReturn(false);

		CatalogEntriesCollection catalogEntriesCollection = mock(CatalogEntriesCollection.class);
		when(catalogEntriesCollection.getEntries()).thenReturn(Lists.newArrayList(cardDefinition1, cardDefinition2));
		return catalogEntriesCollection;
	}

	private void thenNoMoreFreeChestsAreAvailable() {
		assertThat(player.getFreeChest().canBeOpened(now, catalog.getGameConstants()), is(equalTo(false)));
	}

	private void thenRewardsWereReceived() {
		assertThat(openResponse.getRewards(), is(equalTo(expectedRewardsResponse)));
	}

	private void givenAPlayerWhoDidntLoginForAYear() {
		Date aYearAgo = new Date(now.getTime() - 365 * 24 * 60 * 60 * 1000L);
		player = new PlayerBuilder().setUserId("").setDeck(new Deck()).setInventory(new Inventory()).setProgress(new PlayerProgress())
				.setPlayerRewards(new PlayerRewards(timeProvider.getTimeNowAsSeconds()))
				.setDynamicShop(new DynamicShop(new ShopCards(new ArrayList<>(), 0))).setFreeChest(new FreeChest(aYearAgo))
				.setTutorialProgress(new TutorialProgress()).setPlayerStats(new PlayerStats()).setAbTag(ABTag.emptyABTag()).createPlayer();
	}

	private void givenAPlayerWithAReadyFreeChest() {
		Date past = new Date(now.getTime() - SECONDS_BETWEEN_FREE_CHESTS * 1000L);
		player = new PlayerBuilder().setUserId("").setDeck(new Deck()).setInventory(new Inventory()).setProgress(new PlayerProgress())
				.setPlayerRewards(new PlayerRewards(timeProvider.getTimeNowAsSeconds()))
				.setDynamicShop(new DynamicShop(new ShopCards(new ArrayList<>(), 0))).setFreeChest(new FreeChest(past))
				.setTutorialProgress(new TutorialProgress()).setPlayerStats(new PlayerStats()).setAbTag(ABTag.emptyABTag()).createPlayer();
	}

	private void whenOpening() {
		openResponse = openFreeChestAction.openFreeChest(player, catalog, now);
	}

	private void givenAPlayerWithANonReadyFreeChest() {
		player = new PlayerBuilder().setUserId("").setDeck(new Deck()).setInventory(new Inventory()).setProgress(new PlayerProgress())
				.setPlayerRewards(new PlayerRewards(timeProvider.getTimeNowAsSeconds()))
				.setDynamicShop(new DynamicShop(new ShopCards(new ArrayList<>(), 0))).setFreeChest(new FreeChest(now))
				.setTutorialProgress(new TutorialProgress()).setPlayerStats(new PlayerStats()).setAbTag(ABTag.emptyABTag()).createPlayer();
	}

	private void openMaxChests() {
		for (int i = 0; i < MAX_FREE_CHESTS; i++) {
			thenAvailableChestsIs(MAX_FREE_CHESTS - (i));
			whenOpening();
			thenAvailableChestsIs(MAX_FREE_CHESTS - (i + 1));
		}
	}

	private void thenAvailableChestsIs(int amount) {
		assertThat(player.getFreeChest().getAvailableChests(now, catalog.getGameConstants()), is(equalTo(amount)));
	}

}