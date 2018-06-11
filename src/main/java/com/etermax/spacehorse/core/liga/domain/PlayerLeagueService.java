package com.etermax.spacehorse.core.liga.domain;

import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class PlayerLeagueService {

	private static Logger logger = LoggerFactory.getLogger(PlayerLeagueService.class);

	private PlayerLeagueRepository playerLeagueRepository;
	private ServerTimeProvider serverTimeProvider;
	private PlayerWinRateRepository playerWinRateRepository;

	public PlayerLeagueService(PlayerLeagueRepository playerLeagueRepository, ServerTimeProvider serverTimeProvider,
			PlayerWinRateRepository playerWinRateRepository) {
		this.playerLeagueRepository = playerLeagueRepository;
		this.serverTimeProvider = serverTimeProvider;
		this.playerWinRateRepository = playerWinRateRepository;
	}

	// Es necesario SIEMPRE obtener la fecha de fin de la liga independientemente si el usuario esta o no en la liga. Es para q e cliente pueda
	// mostrar en el mapa de liga la fecha de fin de la liga.
	public long getEndOfLeagueInSeconds() {
		return calculateSeasonEndDateInSeconds();
	}

	public Optional<PlayerLeague> addOrUpdatePlayerSeasons(Player player, LeagueConfiguration leagueConfiguration) {

		try {
			if (!leagueConfiguration.isLeagueEnabled()) {
				return Optional.empty();
			}

			Optional<PlayerWinRate> playerWinRateOptional = playerWinRateRepository.find(player.getUserId());

			// ************** CODIGO VIEJO => NO CUBRE TODOS LOS ESCENARIOS **************

			//		return playerWinRateOptional.map(playerWinRate -> {
			//			PlayerLeague playerLeague = playerLeagueRepository.findBy(player.getUserId()).orElse(new PlayerLeague());
			//			int currentPlayerMmr = playerWinRate.getMmr();
			//
			//			return playerLeague.getCurrent().map(currentPlayerSeason ->
			//				 updatePlayerLeague(player, leagueConfiguration, playerWinRate, playerLeague, currentPlayerMmr, currentPlayerSeason)//
			//			).orElseGet(() -> createNewPlayerLeague(leagueConfiguration, player));
			//		});

			// ************** CODIGO NUEVO **************
			if (playerWinRateOptional.isPresent()) {

				Optional<PlayerLeague> playerLeagueOptional = playerLeagueRepository.findBy(player.getUserId());
				PlayerWinRate playerWinRate = playerWinRateOptional.get();
				int currentPlayerMmr = playerWinRate.getMmr();

				if (playerLeagueOptional.isPresent()) {
					PlayerLeague playerLeague = playerLeagueOptional.orElse(new PlayerLeague());
					if (existACurrentSeason(playerLeague)) {
						return updatePlayerLeagueIfRequired(player, leagueConfiguration, playerLeague, playerWinRate, currentPlayerMmr);
					}
				}

				return createPlayerLeagueIfRequired(player, leagueConfiguration, currentPlayerMmr);
			}
		} catch (Exception e) {
			logger.error("Unexpected error when try to create or update a player league ", e);
		}

		return Optional.empty();
	}

	private boolean existACurrentSeason(PlayerLeague playerLeague) {
		return playerLeague.getCurrent().isPresent();
	}

	private Optional<PlayerLeague> updatePlayerLeagueIfRequired(Player player, LeagueConfiguration leagueConfiguration, PlayerLeague playerLeague,
			PlayerWinRate playerWinRate, int currentPlayerMmr) {
		PlayerSeason currentPlayerSeason = playerLeague.getCurrent().get();
		PlayerLeague playerLeagueUpdated = updatePlayerLeague(player, leagueConfiguration, playerWinRate, playerLeague, currentPlayerMmr,
				currentPlayerSeason);
		logger.info("==================> a player league was updated " + playerLeagueUpdated);
		return Optional.of(playerLeagueUpdated);
	}

	private PlayerLeague updatePlayerLeague(Player player, LeagueConfiguration leagueConfiguration, PlayerWinRate playerWinRate,
			PlayerLeague playerLeague, int currentPlayerMmr, PlayerSeason currentPlayerSeason) {
		DateTime currentSeasonStartDate = calculateCurrentSeasonStartDate();

		if (currentPlayerSeasonIsExpired(currentPlayerSeason, currentSeasonStartDate)) {
			return updateExpiredPlayerLeague(player, leagueConfiguration, playerWinRate, currentPlayerMmr, currentPlayerSeason,
					currentSeasonStartDate);
		} else {
			return updateCurrentPlayerLeague(player, leagueConfiguration, playerLeague, currentPlayerMmr, currentPlayerSeason,
					currentSeasonStartDate);
		}
	}

	private PlayerLeague updateCurrentPlayerLeague(Player player, LeagueConfiguration leagueConfiguration, PlayerLeague playerLeague,
			int currentPlayerMmr, PlayerSeason currentPlayerSeason, DateTime currentSeasonStartDate) {
		if (currentPlayerMmrIsEnoughForPlayLeague(leagueConfiguration, currentPlayerMmr)) {
			if (needUpdateCurrentPlayerSeasonMmr(currentPlayerMmr, currentPlayerSeason)) {
				return updateCurrentPlayerSeasons(playerLeague, currentPlayerMmr, currentSeasonStartDate, player);
			} else {
				return playerLeague;
			}
		} else {
			return playerLeague;
		}
	}

	private PlayerLeague updateExpiredPlayerLeague(Player player, LeagueConfiguration leagueConfiguration, PlayerWinRate playerWinRate,
			int currentPlayerMmr, PlayerSeason currentPlayerSeason, DateTime currentSeasonStartDate) {
		if (currentPlayerMmrIsEnoughForPlayLeague(leagueConfiguration, currentPlayerMmr)) {
			restartPlayerMmrToCappedMmr(leagueConfiguration, playerWinRate);
			return updatePlayerSeasonsWithCurrentAndPreviousPlayerSeason(leagueConfiguration, currentPlayerSeason, currentSeasonStartDate, player);
		} else {
			currentPlayerSeason.rewardIsPending();
			PlayerLeague playerLeague = new PlayerLeague(null, currentPlayerSeason);
			playerLeagueRepository.put(player.getUserId(), playerLeague);
			return playerLeague;
		}
	}

	private Optional<PlayerLeague> createPlayerLeagueIfRequired(Player player, LeagueConfiguration leagueConfiguration, int currentPlayerMmr) {
		if (currentPlayerMmrIsEnoughForPlayLeague(leagueConfiguration, currentPlayerMmr)) {
			PlayerLeague newPlayerSeasons = createNewPlayerLeague(player, currentPlayerMmr);
			logger.info("==================> new player league was created " + newPlayerSeasons);
			return Optional.of(newPlayerSeasons);
		} else {
			return Optional.empty();
		}
	}

	private PlayerLeague createNewPlayerLeague(Player player, int currentPlayerMmr) {
		DateTime currentSeasonStartDate = calculateCurrentSeasonStartDate();
		PlayerLeague playerLeagueUpdated = new PlayerLeague(new PlayerSeason(currentSeasonStartDate, currentPlayerMmr));
		playerLeagueRepository.put(player.getUserId(), playerLeagueUpdated);
		return playerLeagueUpdated;
	}

	private PlayerLeague updatePlayerSeasonsWithCurrentAndPreviousPlayerSeason(LeagueConfiguration leagueConfiguration,
			PlayerSeason currentPlayerSeason, DateTime currentSeasonStartDate, Player player) {
		PlayerLeague playerLeagueUpdated;
		int initialMmr = leagueConfiguration.getInitialMmr();
		currentPlayerSeason.rewardIsPending();
		playerLeagueUpdated = new PlayerLeague(new PlayerSeason(currentSeasonStartDate, initialMmr), currentPlayerSeason);
		playerLeagueRepository.put(player.getUserId(), playerLeagueUpdated);
		return playerLeagueUpdated;
	}

	private void restartPlayerMmrToCappedMmr(LeagueConfiguration leagueConfiguration, PlayerWinRate playerWinRate) {
		playerWinRate.updateMmr(leagueConfiguration.getInitialMmr());
		playerWinRateRepository.update(playerWinRate);
	}

	private PlayerLeague updateCurrentPlayerSeasons(PlayerLeague playerLeague, int currentPlayerMmr, DateTime currentSeasonStartDate, Player player) {
		PlayerLeague playerLeagueUpdated = playerLeague.getPrevious()
				.map(previousPlayerSeason -> new PlayerLeague(new PlayerSeason(currentSeasonStartDate, currentPlayerMmr), previousPlayerSeason))
				.orElse(new PlayerLeague(new PlayerSeason(currentSeasonStartDate, currentPlayerMmr)));
		playerLeagueRepository.put(player.getUserId(), playerLeagueUpdated);
		return playerLeagueUpdated;
	}

	private boolean needUpdateCurrentPlayerSeasonMmr(int currentPlayerMmr, PlayerSeason currentPlayerSeason) {
		return currentPlayerMmr > currentPlayerSeason.getMmr();
	}

	private boolean currentPlayerMmrIsEnoughForPlayLeague(LeagueConfiguration leagueConfiguration, int currentPlayerMmr) {
		return currentPlayerMmr >= leagueConfiguration.getInitialMmr();
	}

	private boolean currentPlayerSeasonIsExpired(PlayerSeason currentPlayerSeason, DateTime currentSeasonStartDate) {
		return !currentPlayerSeason.getInitialDateTime().isEqual(currentSeasonStartDate);
	}

	private DateTime calculateCurrentSeasonStartDate() {
		return ServerTime.roundToStartOfDay(serverTimeProvider.getDateTime().dayOfMonth().withMinimumValue());
	}

	private long calculateSeasonEndDateInSeconds() {
		DateTime endTime = serverTimeProvider.getDateTime().withZone(DateTimeZone.UTC).dayOfMonth().withMaximumValue();
		long endTimeInSeconds = ServerTime.fromDate(endTime);
		long endOfDayInSeconds = ServerTime.roundToEndOfDay(endTimeInSeconds);
		logger.info("==================> end of time of league  " + ServerTime.toDateTime(endOfDayInSeconds));
		return endOfDayInSeconds;
	}

	public Optional<PlayerLeague> findPlayerLeague(Player player) {
		return playerLeagueRepository.findBy(player.getUserId());
	}
}
