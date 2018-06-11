package com.etermax.spacehorse.core.player.action;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.catalog.repository.dynamo.CatalogDynamoRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.player.model.Gender;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.stats.PlayerStats;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.player.repository.dynamo.PlayerDynamoRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class PlayerActionTest {

	CatalogRepository catalogRepository = mock(CatalogDynamoRepository.class);
	Catalog catalog = mock(Catalog.class);
	PlayerRepository playerRepository = mock(PlayerDynamoRepository.class);
	Player player = mock(Player.class);
	PlayerStats playerStats = new PlayerStats();
	String loginId = "loginId";
	private PlayerRepository nullPlayerRepository = mock(PlayerDynamoRepository.class);
	private FixedServerTimeProvider serverTimeProvider;
	private boolean actionResult;

	@Before
	public void setup() {
		serverTimeProvider = new FixedServerTimeProvider();
		when(catalogRepository.getActiveCatalogWithTag(any())).thenReturn(catalog);
		when(player.getUserId()).thenReturn(loginId);
		when(player.getPlayerStats()).thenReturn(playerStats);
		doNothing().when(player).checkAndFixIntegrity(catalog, serverTimeProvider.getTimeNowAsSeconds());
		when(playerRepository.find(anyString())).thenReturn(player);
		when(nullPlayerRepository.find(anyString())).thenReturn(null);
	}

	@Test
	public void testFindByLoginId() {
		PlayerAction playerAction = new PlayerAction(playerRepository, catalogRepository);
		Optional<Player> playerOpt = playerAction.findByLoginId(loginId);

		assertThat(playerOpt.isPresent()).isEqualTo(true);
		assertThat(playerOpt.get()).hasFieldOrPropertyWithValue("userId", this.loginId);
	}

	@Test
	public void testFindByLoginIdWhenPlayerIsNull() {
		PlayerAction playerAction = new PlayerAction(nullPlayerRepository, catalogRepository);
		Optional<Player> playerOpt = playerAction.findByLoginId(loginId);

		assertThat(playerOpt.isPresent()).isEqualTo(false);
	}

	@Test
	public void testFindByLoginIdOrFail() {
		PlayerAction playerAction = new PlayerAction(playerRepository, catalogRepository);
		Player returnedPlayer = playerAction.findByLoginIdOrFail(this.loginId);

		assertThat(returnedPlayer).isEqualTo(this.player);
	}

	@Test
	public void testFindByLoginIdOrFailWhenFails() {
		PlayerAction playerAction = new PlayerAction(nullPlayerRepository, catalogRepository);
		Throwable thrown = catchThrowable(() -> playerAction.findByLoginIdOrFail(this.loginId));
		assertThat(thrown).isInstanceOf(ApiException.class).hasMessageContaining("Player loginId not found: " + this.loginId);
	}

	@Test
	public void UpdatingAPlayerProfileShouldIncrementNameChangesAndReturnTrue() {
		givenAPlayerByName(null);
		doNothing().when(nullPlayerRepository).update(anyObject());
		whenUpdatePlayerProfile("playerName", null, 0);
		thenActionWasSucceedAndNameChangesIncrementedByOne();
	}

	@Test
	public void UpdatingAPlayerProfileWithSameNameShouldNotIncrementNameChangesAndReturnFalse() {
		givenAPlayerByName("playerName");
		doNothing().when(nullPlayerRepository).update(anyObject());
		whenUpdatePlayerProfile("playerName", null, 0);
		thenActionWasFailedAndNameChangesWasNotIncremented();
	}

	private void givenAPlayerByName(String playerName) {
		doNothing().when(player).setName(anyString());
		when(player.getName()).thenReturn(playerName);
	}

	private void whenUpdatePlayerProfile(String name, Gender gender, int age) {
		PlayerAction playerAction = new PlayerAction(playerRepository, catalogRepository);
		playerAction.updatePlayerProfile(player, name, gender, age);
	}

	private void thenActionWasSucceedAndNameChangesIncrementedByOne() {
		assertUpdatePlayerProfileAction(true, 1);
	}

	private void thenActionWasFailedAndNameChangesWasNotIncremented() {
		assertUpdatePlayerProfileAction(false, 0);
	}

	private void assertUpdatePlayerProfileAction(boolean expectedResult, int expectedNameChanges) {
		assertThat(playerStats.getNameChanges()).isEqualTo(expectedNameChanges);
	}
}
