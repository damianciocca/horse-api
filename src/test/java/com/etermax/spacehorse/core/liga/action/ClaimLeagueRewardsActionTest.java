package com.etermax.spacehorse.core.liga.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueRepository;
import com.etermax.spacehorse.core.liga.domain.PlayerSeason;
import com.etermax.spacehorse.core.liga.exception.ClaimLeagueRewardsException;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.mock.MockCatalog;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.google.common.collect.Lists;

public class ClaimLeagueRewardsActionTest {

	public static final String USER_ID = "10";
	private static final int INITIAL_GOLD = 300;
	private ServerTimeProvider timeProvider;
	private PlayerRepository playerRepository;
	private ApplyRewardDomainService applyRewardDomainService;
	private Catalog catalog;
	private Player player;
	private ClaimLeagueRewardsAction action;
	private PlayerLeagueRepository playerLeagueRepository;
	private GetRewardsDomainService getRewardsDomainService;
	private PlayerLeague playerLeague;
	private List<RewardResponse> rewardResponses;

	@Before
	public void setUp() throws Exception {
		timeProvider = new FixedServerTimeProvider();
		playerRepository = mock(PlayerRepository.class);
		applyRewardDomainService = new ApplyRewardDomainService();
		catalog = MockCatalog.buildCatalog();
		player = new PlayerScenarioBuilder(USER_ID).withGold(INITIAL_GOLD).build();
		getRewardsDomainService = mock(GetRewardsDomainService.class);
		List<Reward> rewards = Lists.newArrayList(new Reward(RewardType.GOLD, 100), new Reward(RewardType.CARD_PARTS, "card_enforcer", 4));
		when(getRewardsDomainService.getRewards(any(), any(), anyInt(), any())).thenReturn(
				rewards);
		playerLeagueRepository = mock(PlayerLeagueRepository.class);
		action = new ClaimLeagueRewardsAction(getRewardsDomainService, applyRewardDomainService, timeProvider, playerRepository,
				playerLeagueRepository);

	}

	@Test
	public void whenClaimWithAPreviousSeasonWith3100ThenRewardsAreApplyAndReturned() {
		givenAPlayerLeagueWithAPreviousPlayerSeasonWith(true, 3100);

		whenClaim();

		assertThat(rewardResponses).isNotEmpty();
		assertThat(rewardResponses).hasSize(2);
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(400);
		chestDefinitionIdAppliedIs("courier_Silver");
	}

	@Test
	public void whenClaimWithAPreviousSeasonWith3600ThenRewardsAreApplyAndReturned() {
		givenAPlayerLeagueWithAPreviousPlayerSeasonWith(true, 3600);

		whenClaim();

		assertThat(rewardResponses).isNotEmpty();
		assertThat(rewardResponses).hasSize(2);
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(400);
		chestDefinitionIdAppliedIs("courier_Diamond");
	}

	@Test
	public void whenClaimWithAPreviousSeasonWith4000ThenRewardsAreApplyAndReturned() {
		givenAPlayerLeagueWithAPreviousPlayerSeasonWith(true, 4000);

		whenClaim();

		assertThat(rewardResponses).isNotEmpty();
		assertThat(rewardResponses).hasSize(2);
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(400);
		chestDefinitionIdAppliedIs("courier_Epic");
	}

	@Test
	public void whenClaimWithAPreviousPlayerSeasonAlreadyClaimedThenExceptionIsThrown() {
		givenAPlayerLeagueWithAPreviousPlayerSeasonWith(false, 3100);

		Throwable exception = catchThrowable(this::whenClaim);

		Assertions.assertThat(exception).isInstanceOf(IllegalArgumentException.class).hasMessage("League Reward is already claimed");
	}

	@Test
	public void whenClaimWithoutPreviousPlayerSeasonThenExceptionIsThrown() {
		givenAPlayerLeagueWithoutPreviousPlayerSeason();

		Throwable exception = catchThrowable(this::whenClaim);

		Assertions.assertThat(exception).isInstanceOf(ClaimLeagueRewardsException.class).hasMessage("Previous player league not found");
	}

	@Test
	public void whenClaimWithoutPlayerLeagueThenExceptionIsThrown() {
		givenAPlayerLeagueWithoutPlayerLeague();

		Throwable exception = catchThrowable(this::whenClaim);

		Assertions.assertThat(exception).isInstanceOf(ClaimLeagueRewardsException.class).hasMessage("Player League not found");
	}

	private void chestDefinitionIdAppliedIs(String courierId) {
		ArgumentCaptor<ChestDefinition> chestDefinitionArgumentCaptor = ArgumentCaptor.forClass(ChestDefinition.class);
		verify( getRewardsDomainService, times(1)).getRewards(any(Player.class), chestDefinitionArgumentCaptor.capture(), anyInt(), any
				(GetRewardConfiguration
				.class));
		ChestDefinition chestDefinition = chestDefinitionArgumentCaptor.getValue();
		assertThat(chestDefinition.getId()).isEqualTo(courierId);
	}

	private void whenClaim() {
		rewardResponses = action.claim(player, catalog);
	}

	private void givenAPlayerLeagueWithAPreviousPlayerSeasonWith(boolean rewardPending, int mmr) {
		playerLeague = new PlayerLeague(null, new PlayerSeason(timeProvider.getDateTime(), mmr, rewardPending));
		when(playerLeagueRepository.findBy(USER_ID)).thenReturn(Optional.of(playerLeague));
	}

	private void givenAPlayerLeagueWithoutPreviousPlayerSeason() {
		playerLeague = new PlayerLeague();
		when(playerLeagueRepository.findBy(USER_ID)).thenReturn(Optional.of(playerLeague));
	}

	private void givenAPlayerLeagueWithoutPlayerLeague() {
		when(playerLeagueRepository.findBy(USER_ID)).thenReturn(Optional.empty());
	}

}
