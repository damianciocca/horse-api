package com.etermax.spacehorse.core.player.action.chest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.player.resource.response.SpeedupOpeningChestResponse;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class SpeedupOpeningChestActionTest {

	private static final int TIME_TO_OPEN_CHEST = 43200;
	private static final String LOGIN_ID = "10";
	private static final long CHEST_ID = 1L;
	private static final String CHEST_ID_WITH_SPEED_UP_FREE = "courier_tutorial_step_3";
	private PlayerRepository playerRepository;
	private FixedServerTimeProvider timeProvider;
	private Player player;
	private StartOpeningChestAction startOpeningChestAction;
	private SpeedupOpeningChestAction speedupOpeningChestAction;
	private SpeedupOpeningChestResponse response;

	@Before
	public void setUp() throws Exception {
		Catalog catalog = MockUtils.mockCatalog();
		CatalogRepository catalogRepository = aCatalogRepository(catalog);
		GetRewardsDomainService getRewardsDomainService = new GetRewardsDomainService();
		ApplyRewardDomainService applyRewardDomainService = new ApplyRewardDomainService();
		player = new PlayerScenarioBuilder(LOGIN_ID).withDefaultChest().build();
		playerRepository = aPlayerRepository(player);
		timeProvider = new FixedServerTimeProvider();
		startOpeningChestAction = new StartOpeningChestAction(catalogRepository, playerRepository, timeProvider);

		speedupOpeningChestAction = new SpeedupOpeningChestAction(playerRepository, catalogRepository, timeProvider, getRewardsDomainService,
				applyRewardDomainService, mock(AchievementsFactory.class), mock(CompleteAchievementAction.class));
	}

	@Test
	public void givenAChestWithSpeedUpFreeAndSpeedUpThenChestShouldBeRemovedAndGemsCostIsZero() {
		//given
		givenAPlayerWithChestWithSpeedUpFree();
		// When
		whenSpeedUp();
		//then
		thenRewardWereAppliedAndGemsCostIsZero();
	}

	@Test
	public void givenAChestInStateClosedWhenSpeedupThenChestShouldBeRemoved() {
		// When
		whenSpeedUp();
		// Then
		assertThatRewardsWereApplied(1);
	}

	@Test
	public void givenAChestInStateOpeningWhenSpeedupThenChestShouldBeRemoved() {
		// Given
		givenAChestOpening();
		// When
		whenSpeedUp();
		// Then
		assertThatRewardsWereApplied(2);
	}

	@Test
	public void givenAChestInStateOpenedWhenSpeedupThenThrowException() {
		// Given
		givenAChestOpened();
		// When - Then
		assertThatThrownBy(() -> speedupOpeningChestAction.speedup(LOGIN_ID, CHEST_ID)).isInstanceOf(ApiException.class)
				.hasMessageContaining("Can't speedup the chest");
	}

	private void whenSpeedUp() {
		response = speedupOpeningChestAction.speedup(LOGIN_ID, CHEST_ID);
	}

	private void thenRewardWereAppliedAndGemsCostIsZero() {
		assertThat(player.getInventory().getChests().getChests()).isEmpty();
		verify(playerRepository, times(1)).update(eq(player));
		assertThat(response.getCostInGems()).isEqualTo(0);
		assertThat(response.getRewards()).isNotEmpty();
	}

	private void givenAPlayerWithChestWithSpeedUpFree() {
		player = new PlayerScenarioBuilder(LOGIN_ID).withChest(CHEST_ID_WITH_SPEED_UP_FREE).build();
		when(playerRepository.find(LOGIN_ID)).thenReturn(player);
	}

	private void givenAChestOpened() {
		givenAChestOpening();
		increaseTimeInSeconds(TIME_TO_OPEN_CHEST);
	}

	private void givenAChestOpening() {
		startOpeningChestAction.start(LOGIN_ID, CHEST_ID);
	}

	private void assertThatRewardsWereApplied(int numberOfInvocations) {
		assertThat(player.getInventory().getChests().getChests()).isEmpty();
		verify(playerRepository, times(numberOfInvocations)).update(eq(player));
		assertThat(response.getCostInGems()).isGreaterThan(0);
		assertThat(response.getRewards()).isNotEmpty();
	}

	private void increaseTimeInSeconds(int seconds) {
		DateTime timeIncreased = timeProvider.getDateTime().plusSeconds(seconds);
		timeProvider.changeTime(timeIncreased);
	}

	private CatalogRepository aCatalogRepository(Catalog catalog) {
		CatalogRepository catalogRepository = mock(CatalogRepository.class);
		when(catalogRepository.getActiveCatalogWithTag(any())).thenReturn(catalog);
		return catalogRepository;
	}

	private PlayerRepository aPlayerRepository(Player player) {
		PlayerRepository playerRepository = mock(PlayerRepository.class);
		when(playerRepository.find(LOGIN_ID)).thenReturn(player);
		return playerRepository;
	}

}