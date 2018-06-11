package com.etermax.spacehorse.core.player.action.chest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.model.inventory.chest.ChestState;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class StartOpeningChestActionTest {

	private static final int TIME_TO_OPEN_CHEST = 43200;
	private static final String CHEST_TYPE = "courier_Mysterious";
	private static final String LOGIN_ID = "10";
	private static final long CHEST_ID = 1L;

	private PlayerRepository playerRepository;
	private CatalogRepository catalogRepository;
	private FixedServerTimeProvider timeProvider;
	private Player player;

	@Before
	public void setUp() throws Exception {
		Catalog catalog = MockUtils.mockCatalog();
		player = new PlayerScenarioBuilder(LOGIN_ID).withDefaultChest().build();
		playerRepository = aPlayerRepository(player);
		catalogRepository = aCatalogRepository(catalog);
		timeProvider = new FixedServerTimeProvider();
	}

	@Test
	public void givenAChestInStateClosedWhenStartOpeningThenTheChestShouldBeInOpeningState() throws Exception {
		// Given
		StartOpeningChestAction startOpeningChestAction = new StartOpeningChestAction(catalogRepository, playerRepository, timeProvider);
		// When
		Chest chest = startOpeningChestAction.start(LOGIN_ID, CHEST_ID);
		// Then
		assertThatChestIsInStateOpening(chest);
	}

	@Test
	public void givenAChestInStateOpeningWhenTheTimePassThenTheChestShouldBeInOpenedState() throws Exception {
		// Given
		StartOpeningChestAction startOpeningChestAction = new StartOpeningChestAction(catalogRepository, playerRepository, timeProvider);
		Chest chest = startOpeningChestAction.start(LOGIN_ID, CHEST_ID);
		// When
		increaseTimeInSeconds(TIME_TO_OPEN_CHEST);
		// Then
		assertThatChestIsInStateOpened(chest);
	}

	@Test
	public void givenAChestInStateOpeningWhenTriesToOpenAgainThenThrowException() throws Exception {
		// Given
		StartOpeningChestAction startOpeningChestAction = new StartOpeningChestAction(catalogRepository, playerRepository, timeProvider);
		startOpeningChestAction.start(LOGIN_ID, CHEST_ID);
		// When - Then
		assertThatThrownBy(() -> startOpeningChestAction.start(LOGIN_ID, CHEST_ID)).isInstanceOf(ApiException.class)
				.hasMessageContaining("Chest can't be started to open");
	}

	private void assertThatChestIsInStateOpened(Chest chest) {
		assertThat(chest.getChestState(getServerDate())).isEqualTo(ChestState.OPENED);
		assertChestType(chest);
	}

	private void assertThatChestIsInStateOpening(Chest chest) {
		assertThat(chest.getChestState(getServerDate())).isEqualTo(ChestState.OPENING);
		assertChestType(chest);
	}

	private void assertChestType(Chest chest) {
		assertThat(chest.getChestType()).isEqualTo(CHEST_TYPE);
		assertThat(player.getInventory().getChests().getChests()).extracting(Chest::getChestType).containsOnly(CHEST_TYPE);
	}

	private void increaseTimeInSeconds(int seconds) {
		DateTime timeIncreased = timeProvider.getDateTime().plusSeconds(seconds);
		timeProvider.changeTime(timeIncreased);
	}

	private Date getServerDate() {
		return timeProvider.getDateTime().toDate();
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
