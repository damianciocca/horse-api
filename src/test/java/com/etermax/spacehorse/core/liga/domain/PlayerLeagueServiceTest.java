package com.etermax.spacehorse.core.liga.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.liga.domain.PlayerSeason;
import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.etermax.spacehorse.core.liga.domain.LeagueConfiguration;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTime;

public class PlayerLeagueServiceTest {

	public static final String USER_ID = "1";
	private FixedServerTimeProvider timeProvider;
	private DateTime currentDate;
	private DateTime expiredDate;
	private Player player;
	private PlayerLeagueRepository playerLeagueRepository;
	private PlayerWinRateRepository playerWinRateRepository;
	private PlayerLeagueService playerLeagueService;
	private LeagueConfiguration leagueConfiguration;
	private Optional<PlayerLeague> playerLeagueOptional;
	private PlayerSeason currentPlayerSeason;
	private DateTime seasonStartDate;

	@Before
	public void setUp() throws Exception {
		timeProvider = new FixedServerTimeProvider(this.currentDate);
		currentDate = ServerTime.roundToStartOfDay("2018-04-13");
		timeProvider.changeTime(currentDate);

		expiredDate = ServerTime.roundToStartOfDay("2018-03-01");
		seasonStartDate = ServerTime.roundToStartOfDay("2018-04-01");

		leagueConfiguration = mock(LeagueConfiguration.class);
		when(leagueConfiguration.getInitialMmr()).thenReturn(3000);
		when(leagueConfiguration.isLeagueEnabled()).thenReturn(true);

		player = mock(Player.class);
		when(player.getUserId()).thenReturn(USER_ID);

	}

	@Test
	public void whenPlayerMmrIsHigherTo3000ThenNewSeasonIsCreated() {
		//si el mmr es >= 3000 y se detecta que no tiene temporada current se crea una
		givenAEmptyPlayerLeague();
		givenPlayerSeasonsRepositoryThatReturnPlayerLeague();
		givenAPlayerWinRateRepositoryThatReturnPlayerWinRateWith(3000);
		givenSeasonService();

		whenAddOrUpdatePlayerSeasons();

		assertThat(playerLeagueOptional).isNotEmpty();
		PlayerLeague playerLeagueUpdated = playerLeagueOptional.get();
		assertThat(playerLeagueUpdated.getCurrent()).isNotEmpty();
		PlayerSeason currentPlayerSeason = playerLeagueUpdated.getCurrent().get();
		assertThat(currentPlayerSeason.getMmr()).isEqualTo(3000);
		assertThat(currentPlayerSeason.getInitialDateTime()).isEqualTo(seasonStartDate);
		assertThat(currentPlayerSeason.isRewardPending()).isFalse();
	}

	@Test
	public void whenPlayerMmrIsLowerTo3000ThenReturnEmpty() {
		//si el mmr es < 3000 y se detecta que no tiene temporada current ni previa, entonces devuelve empty
		givenAEmptyPlayerLeague();
		givenPlayerSeasonsRepositoryThatReturnPlayerLeague();
		givenAPlayerWinRateRepositoryThatReturnPlayerWinRateWith(2800);
		givenSeasonService();

		whenAddOrUpdatePlayerSeasons();

		assertThat(playerLeagueOptional).isEmpty();
	}

	@Test
	public void whenPlayerMmrIsHigherToMaximumCurrentSeasonMmrThenThisIsUpdated() {
		//si el mmr es >= 3000 y se detecta que tiene temporada current se actualiza
		givenACurrentPlayerSeason(3200, seasonStartDate);
		givenAPlayerSeasons();
		givenPlayerSeasonsRepositoryThatReturnPlayerLeague();
		givenAPlayerWinRateRepositoryThatReturnPlayerWinRateWith(3500);
		givenSeasonService();

		whenAddOrUpdatePlayerSeasons();

		assertThat(playerLeagueOptional).isNotEmpty();
		PlayerLeague playerLeagueUpdated = playerLeagueOptional.get();
		assertThat(playerLeagueUpdated.getCurrent()).isNotEmpty();
		PlayerSeason playerSeason = playerLeagueUpdated.getCurrent().get();
		assertThat(playerSeason.getMmr()).isEqualTo(3500);
		assertThat(playerSeason.getInitialDateTime()).isEqualTo(seasonStartDate);
		assertThat(playerSeason.isRewardPending()).isFalse();
	}

	@Test
	public void whenPlayerMmrIsMinorToMaximumCurrentSeasonMmrThenThisIsNotUpdated() {
		//si el mmr es >= 3000 y se detecta que tiene temporada current, pero el mmr del player es < al maximo alcanzado en la current, entonces no se
		// actualiza
		givenACurrentPlayerSeason(3500, seasonStartDate);
		givenAPlayerSeasons();
		givenPlayerSeasonsRepositoryThatReturnPlayerLeague();
		givenAPlayerWinRateRepositoryThatReturnPlayerWinRateWith(3100);
		givenSeasonService();

		whenAddOrUpdatePlayerSeasons();

		assertThat(playerLeagueOptional).isNotEmpty();
		PlayerLeague playerLeagueUpdated = playerLeagueOptional.get();
		assertThat(playerLeagueUpdated.getCurrent()).usingFieldByFieldValueComparator().contains(currentPlayerSeason);
	}

	@Test
	public void whenCurrentSeasonIsExpiredThenPlayerMmrIsUpdatedTo3000() {
		//si el mmr es >= 3000 y se detecta que la temporada current esta expirada, entonces se reinicia a el
		// mmr del player a 3000
		givenACurrentPlayerSeason(3200, expiredDate);
		givenAPlayerSeasons();
		givenPlayerSeasonsRepositoryThatReturnPlayerLeague();
		givenAPlayerWinRateRepositoryThatReturnPlayerWinRateWith(3100);
		givenSeasonService();

		whenAddOrUpdatePlayerSeasons();

		ArgumentCaptor<PlayerWinRate> playerWinRateArgumentCaptor = ArgumentCaptor.forClass(PlayerWinRate.class);
		verify(playerWinRateRepository).update(playerWinRateArgumentCaptor.capture());
		PlayerWinRate playerWinRate = playerWinRateArgumentCaptor.getValue();
		assertThat(playerWinRate.getMmr()).isEqualTo(3000);
	}

	@Test
	public void whenCurrentSeasonIsExpiredThenPreviousSeasonsAreUpdated() {
		//si el mmr es < 3000 y se detecta que la temporada current esta expirada, se actualiza la temporada previa y se crea una nueva
		givenACurrentPlayerSeason(3200, expiredDate);
		givenAPlayerSeasons();
		givenPlayerSeasonsRepositoryThatReturnPlayerLeague();
		givenAPlayerWinRateRepositoryThatReturnPlayerWinRateWith(3100);
		givenSeasonService();

		whenAddOrUpdatePlayerSeasons();

		assertThat(playerLeagueOptional).isNotEmpty();
		PlayerLeague playerLeagueUpdated = playerLeagueOptional.get();
		assertThat(playerLeagueUpdated.getCurrent()).isNotEmpty();
		PlayerSeason playerSeason = playerLeagueUpdated.getCurrent().get();
		assertThat(playerSeason.getMmr()).isEqualTo(3000);
		assertThat(playerSeason.getInitialDateTime()).isEqualTo(seasonStartDate);
		assertThat(playerSeason.isRewardPending()).isFalse();

		assertThat(playerLeagueUpdated.getPrevious()).isNotEmpty();
		playerSeason = playerLeagueUpdated.getPrevious().get();
		assertThat(playerSeason.getMmr()).isEqualTo(3200);
		assertThat(playerSeason.getInitialDateTime()).isEqualTo(expiredDate);
		assertThat(playerSeason.isRewardPending());
	}

	@Test
	public void whenPlayerMmrIsLessThan3000ThenCurrentSeasonsIsNotUpdated() {
		//si el mmr es < 3000 y tiene temporada current, devuelve la misma sin actualizarla
		givenACurrentPlayerSeason(3200, seasonStartDate);
		givenAPlayerSeasons();
		givenPlayerSeasonsRepositoryThatReturnPlayerLeague();
		givenAPlayerWinRateRepositoryThatReturnPlayerWinRateWith(2800);
		givenSeasonService();

		whenAddOrUpdatePlayerSeasons();

		assertThat(playerLeagueOptional).isNotEmpty();
		PlayerLeague playerLeagueUpdated = playerLeagueOptional.get();
		assertThat(playerLeagueUpdated.getCurrent()).isNotEmpty();
		PlayerSeason playerSeason = playerLeagueUpdated.getCurrent().get();
		assertThat(playerSeason.getMmr()).isEqualTo(3200);
		assertThat(playerSeason.getInitialDateTime()).isEqualTo(seasonStartDate);
		assertThat(playerSeason.isRewardPending()).isFalse();
		assertThat(playerLeagueUpdated.getPrevious()).isEmpty();
	}

	@Test
	public void whenPlayerUpdateSeasonsOneTimeByEachMonthThemOnlyUpdateCurrentAndPreviousSeasons() {

		givenAEmptyPlayerLeague();
		givenAPlayerSeasons();
		givenPlayerSeasonsRepositoryThatReturnPlayerLeague();
		givenAPlayerWinRateRepositoryThatReturnPlayerWinRateWith(3000);
		givenSeasonService();
		givenUpdateSeasonsTwoTimesInTwoMonths();

		whenAddOrUpdatePlayerSeasons();

		assertThat(playerLeagueOptional).isNotEmpty();
		PlayerLeague playerLeagueUpdated = playerLeagueOptional.get();
		assertThat(playerLeagueUpdated.getCurrent()).isNotEmpty();
		PlayerSeason playerSeason = playerLeagueUpdated.getCurrent().get();
		DateTime startDateCurrentSeason = ServerTime.roundToStartOfDay("2018-06-01");
		assertThat(playerSeason.getMmr()).isEqualTo(3000);
		assertThat(playerSeason.getInitialDateTime()).isEqualTo(startDateCurrentSeason);
		assertThat(playerSeason.isRewardPending()).isFalse();

		assertThat(playerLeagueUpdated.getPrevious()).isNotEmpty();
		playerSeason = playerLeagueUpdated.getPrevious().get();
		assertThat(playerSeason.getMmr()).isEqualTo(3000);
		DateTime startDatePreviousSeason = ServerTime.roundToStartOfDay("2018-05-01");
		assertThat(playerSeason.getInitialDateTime()).isEqualTo(startDatePreviousSeason);
		assertThat(playerSeason.isRewardPending()).isTrue();

	}

	@Test
	public void whenPlayerMmrIsLessThan3000AndCurrentSeasonIsExpiredThenPreviousSeasonsIsUpdated() {
		//si el mmr es < 3000 y tiene temporada current expirada, devuelve la misma sin actualizarla
		givenACurrentPlayerSeason(3200, expiredDate);
		givenAPlayerSeasons();
		givenPlayerSeasonsRepositoryThatReturnPlayerLeague();
		givenAPlayerWinRateRepositoryThatReturnPlayerWinRateWith(2800);
		givenSeasonService();

		whenAddOrUpdatePlayerSeasons();

		assertThat(playerLeagueOptional).isNotEmpty();
		PlayerLeague playerLeagueUpdated = playerLeagueOptional.get();
		assertThat(playerLeagueUpdated.getCurrent()).isEmpty();
		PlayerSeason playerSeason = playerLeagueUpdated.getPrevious().get();
		assertThat(playerSeason.getMmr()).isEqualTo(3200);
		assertThat(playerSeason.getInitialDateTime()).isEqualTo(expiredDate);
		assertThat(playerSeason.isRewardPending()).isTrue();
	}

	private void givenUpdateSeasonsTwoTimesInTwoMonths() {
		playerLeagueOptional = playerLeagueService.addOrUpdatePlayerSeasons(player, leagueConfiguration);
		when(playerLeagueRepository.findBy(USER_ID)).thenReturn(playerLeagueOptional);

		DateTime previousStartDate = ServerTime.roundToStartOfDay("2018-05-13");
		timeProvider.changeTime(previousStartDate);

		playerLeagueOptional = playerLeagueService.addOrUpdatePlayerSeasons(player, leagueConfiguration);
		when(playerLeagueRepository.findBy(USER_ID)).thenReturn(playerLeagueOptional);

		currentDate = ServerTime.roundToStartOfDay("2018-06-13");
		timeProvider.changeTime(currentDate);
	}

	private void givenAPlayerSeasons() {
		playerLeagueOptional = Optional.of(new PlayerLeague(currentPlayerSeason));
	}

	private void givenACurrentPlayerSeason(int mmr, DateTime startDate) {
		currentPlayerSeason = new PlayerSeason(startDate, mmr);
	}

	private void givenSeasonService() {
		playerLeagueService = new PlayerLeagueService(playerLeagueRepository, timeProvider, playerWinRateRepository);
	}

	private void givenAEmptyPlayerLeague() {
		playerLeagueOptional = Optional.empty();
	}

	private void whenAddOrUpdatePlayerSeasons() {
		playerLeagueOptional = playerLeagueService.addOrUpdatePlayerSeasons(player, leagueConfiguration);
	}

	private void givenAPlayerWinRateRepositoryThatReturnPlayerWinRateWith(int mmr) {
		playerWinRateRepository = mock(PlayerWinRateRepository.class);
		when(playerWinRateRepository.find(USER_ID)).thenReturn(Optional.of(new PlayerWinRate(USER_ID, 10, 10, 10, mmr)));
	}

	private void givenPlayerSeasonsRepositoryThatReturnPlayerLeague() {
		playerLeagueRepository = mock(PlayerLeagueRepository.class);
		when(playerLeagueRepository.findBy(USER_ID)).thenReturn(playerLeagueOptional);
	}
}
