package com.etermax.spacehorse.core.login.action;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.abtester.domain.ABTestingTag;
import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.abtest.model.ABTesterService;
import com.etermax.spacehorse.core.authenticator.model.PasswordGenerator;
import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.battle.model.DefaultPlayerMapNumberCalculator;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.liga.domain.LeagueConfiguration;
import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.login.model.PlayerCreationDomainService;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;

public class LoginNewUserAction {

	private static final Logger logger = LoggerFactory.getLogger(LoginNewUserAction.class);

	private final UserRepository userRepository;
	private final PlayerRepository playerRepository;
	private final PlayerWinRateRepository playerWinRateRepository;
	private final CatalogAction catalogAction;
	private final ServerTimeProvider serverTimeProvider;
	private final Role defaultRole;
	private final ABTesterService abTesterService;
	private final PlayerCreationDomainService playerCreationDomainService;
	private final PlayerLeagueService playerLeagueService;

	public LoginNewUserAction(UserRepository userRepository, PlayerRepository playerRepository, Role defaultRole, CatalogAction catalogAction,
			PlayerWinRateRepository playerWinRateRepository, ServerTimeProvider serverTimeProvider, ABTesterService abTesterService,
			PlayerCreationDomainService playerCreationDomainService, PlayerLeagueService playerLeagueService) {
		this.userRepository = userRepository;
		this.playerRepository = playerRepository;
		this.playerWinRateRepository = playerWinRateRepository;
		this.defaultRole = defaultRole;
		this.catalogAction = catalogAction;
		this.serverTimeProvider = serverTimeProvider;
		this.abTesterService = abTesterService;
		this.playerCreationDomainService = playerCreationDomainService;
		this.playerLeagueService = playerLeagueService;
	}

	public LoginInfo login(Platform platform) {
		String password = PasswordGenerator.generate();
		User user = createUser(password, platform);
		ABTag abTag = postulateUserToABSystem(user);
		Catalog catalog = catalogAction.getCatalogForUser(abTag);
		Player player = playerCreationDomainService.createPlayer(user.getUserId(), abTag, catalog, serverTimeProvider.getTimeNowAsSeconds());
		PlayerWinRate playerWinRate = this.playerWinRateRepository.findOrCrateDefault(player.getUserId());
		if (isTutorialDisabled(catalog)) {
			updatePlayerMmrAfterTutorial(playerWinRate, catalog);
		}
		updateMapNumber(catalog, playerWinRate, player);
		Integer mmr = playerWinRate.getMmr();
		Optional<PlayerLeague> playerSeasons = playerLeagueService.addOrUpdatePlayerSeasons(player, new LeagueConfiguration(catalog));

		return new LoginInfo(user, player, password, mmr, "", catalog, playerSeasons);
	}

	private void updateMapNumber(Catalog catalog, PlayerWinRate playerWinRate, Player player) {
		int mapNumber = new DefaultPlayerMapNumberCalculator(catalog.getMapsCollection().getEntries()).getMapNumber(playerWinRate.getMmr());

		if (mapNumber != player.getMapNumber()) {
			player.setMapNumber(mapNumber);
			playerRepository.update(player);
		}
	}

	private void updatePlayerMmrAfterTutorial(PlayerWinRate playerWinRate, Catalog catalog) {
		playerWinRate.updateMmr(catalog.getGameConstants().getMinimumMMRAfterTutorial(), getCappedMmrFrom(catalog),
				catalog.getGameConstants().isLeagueEnabled());
		playerWinRateRepository.update(playerWinRate);
	}

	private boolean isTutorialDisabled(Catalog catalog) {
		return !catalog.getGameConstants().getTutorialAvailable();
	}

	private ABTag postulateUserToABSystem(User user) {
		Optional<String> abTagOpt = abTesterService.postulateNewUser(user).map(ABTestingTag::getValue);
		return ABTag.buildFromPostulation(abTagOpt);
	}

	private User createUser(String password, Platform platform) {
		String autoGeneratedUserId = PasswordGenerator.generate();
		User user = new User(autoGeneratedUserId, password, defaultRole, platform);
		logger.info("User {} created", autoGeneratedUserId);
		userRepository.add(user);
		return user;
	}

	private int getCappedMmrFrom(Catalog catalog) {
		return catalog.getGameConstants().getCappedMmr();
	}
}