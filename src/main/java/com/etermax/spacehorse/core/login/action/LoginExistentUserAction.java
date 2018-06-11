package com.etermax.spacehorse.core.login.action;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.abtest.model.ABTesterService;
import com.etermax.spacehorse.core.achievements.model.AchievementsInLoginExecutor;
import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.DefaultPlayerMapNumberCalculator;
import com.etermax.spacehorse.core.battle.model.FinishPlayerBattleDomainService;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainFactory;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.capitain.model.slots.CaptainSlot;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;
import com.etermax.spacehorse.core.club.infrastructure.ClubReporter;
import com.etermax.spacehorse.core.liga.domain.LeagueConfiguration;
import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.login.error.InvalidCredentialsException;
import com.etermax.spacehorse.core.login.model.PlayerCreationDomainService;
import com.etermax.spacehorse.core.login.resource.LoginResource;
import com.etermax.spacehorse.core.player.integrityfixer.MmrIntegrityFixer;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.stats.PlayerStats;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;

public class LoginExistentUserAction {

	private static final Logger logger = LoggerFactory.getLogger(LoginResource.class);

	private final UserRepository userRepository;
	private final PlayerRepository playerRepository;
	private final PlayerWinRateRepository playerWinRateRepository;
	private final CatalogAction catalogAction;
	private final BattleRepository battleRepository;
	private final ServerTimeProvider serverTimeProvider;
	private final ABTesterService abTesterService;
	private final PlayerCreationDomainService playerCreationDomainService;
	private final MmrIntegrityFixer mmrIntegrityFixer;
	private final FinishPlayerBattleDomainService finishPlayerBattleDomainService;
	private final ClubReporter clubReporter;
	private final CaptainCollectionRepository captainCollectionRepository;
	private final QuestBoardRepository questBoardRepository;
	private final LinkGooglePlayAction linkGooglePlayAction;
	private final LinkGameCenterAction linkGameCenterAction;
	private final AchievementsInLoginExecutor achievementsExecutor;
	private final PlayerLeagueService playerLeagueService;

	public LoginExistentUserAction(UserRepository userRepository, PlayerRepository playerRepository, PlayerWinRateRepository playerWinRateRepository,
			CatalogAction catalogAction, BattleRepository battleRepository, ServerTimeProvider serverTimeProvider, ABTesterService abTesterService,
			PlayerCreationDomainService playerCreationDomainService, MmrIntegrityFixer mmrIntegrityFixer,
			FinishPlayerBattleDomainService finishPlayerBattleDomainService, ClubReporter clubReporter,
			CaptainCollectionRepository captainCollectionRepository, QuestBoardRepository questBoardRepository,
			LinkGooglePlayAction linkGooglePlayAction, LinkGameCenterAction linkGameCenterAction, AchievementsInLoginExecutor achievementsExecutor,
			PlayerLeagueService playerLeagueService) {
		this.userRepository = userRepository;
		this.playerRepository = playerRepository;
		this.playerWinRateRepository = playerWinRateRepository;
		this.catalogAction = catalogAction;
		this.battleRepository = battleRepository;
		this.serverTimeProvider = serverTimeProvider;
		this.abTesterService = abTesterService;
		this.playerCreationDomainService = playerCreationDomainService;
		this.mmrIntegrityFixer = mmrIntegrityFixer;
		this.finishPlayerBattleDomainService = finishPlayerBattleDomainService;
		this.clubReporter = clubReporter;
		this.captainCollectionRepository = captainCollectionRepository;
		this.questBoardRepository = questBoardRepository;
		this.linkGooglePlayAction = linkGooglePlayAction;
		this.linkGameCenterAction = linkGameCenterAction;
		this.achievementsExecutor = achievementsExecutor;
		this.playerLeagueService = playerLeagueService;
	}

	public LoginInfo login(String loginId, String password, Platform platform) {
		User user = userRepository.find(loginId);
		if (userNotExists(user)) {
			logger.error("Unknown loginId => ", loginId);
			throw new InvalidCredentialsException("Unknown loginId");
		}

		if (user.isSupport() || user.isAdmin()) {
			logger.error("Admin or support roles are not allowed to play => ", loginId);
			throw new InvalidCredentialsException("Admin or support roles are not allowed to play");
		}

		validatePassword(user, loginId, password);

		updateSessionTokenAndPlatform(user, platform);
		Player player = playerRepository.find(loginId);

		Catalog catalog;

		if (shouldCreatePlayer(user, player)) {
			ABTag abTag = getABTagFromABTesterService(user);
			catalog = getCatalogForUser(abTag);
			player = createPlayer(user, catalog, abTag);
		} else {
			catalog = getCatalogForUser(player.getAbTag());
		}

		int mmr = 0;
		String linkedWithSocialAccountId = "";

		finishUnfinishedButStartedPlayerBattles(player, catalog);
		Optional<PlayerLeague> playerSeasons = addOrUpdateLeagueIfRequired(player, catalog); // should be invoked before get mmr from playerWinRate
		PlayerWinRate playerWinRate = getPlayerWinRate(player);
		mmr = playerWinRate.getMmr();
		updatePlayerProperties(player, playerWinRate, catalog);
		updatePlayerWinRate(catalog, player, playerWinRate);
		clubReporter.updateUserScore(player.getUserId(), mmr);
		updateDefaultCaptainSkinsForOldUsers(player, catalog);
		updateQuestBoard(player);
		linkedWithSocialAccountId = getLinkedWithSocialAccountId(player.getUserId(), platform);
		achievementsExecutor.execute(player, catalog);

		return new LoginInfo(user, player, null, mmr, linkedWithSocialAccountId, catalog, playerSeasons);
	}

	private Optional<PlayerLeague> addOrUpdateLeagueIfRequired(Player player, Catalog catalog) {
		return playerLeagueService.addOrUpdatePlayerSeasons(player, new LeagueConfiguration(catalog));
	}

	private void updatePlayerProperties(Player player, PlayerWinRate playerWinRate, Catalog catalog) {
		boolean playerNeedsUpdate = false;

		if (updatePlayerCatalogId(player, catalog)) {
			playerNeedsUpdate = true;
		}

		if (updateMapNumber(catalog, playerWinRate, player)) {
			playerNeedsUpdate = true;
		}

		if (updateBotMmr(player, playerWinRate)) {
			playerNeedsUpdate = true;
		}

		if (updateTutorialProgress(player)) {
			playerNeedsUpdate = true;
		}

		if (updateDaysSinceJoin(player)) {
			playerNeedsUpdate = true;
		}

		if (updateCumulativeDays(player)) {
			playerNeedsUpdate = true;
		}

		if (playerNeedsUpdate) {
			playerRepository.update(player);
		}
	}

	private String getLinkedWithSocialAccountId(String userId, Platform platform) {
		switch (platform) {
			case ANDROID:
				return linkGooglePlayAction.findGooglePlayIdLinkedWithLoginId(userId).orElse("");
			case IOS:
				return linkGameCenterAction.findGameCenterIdLinkedWithLoginId(userId).orElse("");
			default:
				return "";
		}
	}

	private boolean updateDaysSinceJoin(Player player) {
		int aDayInSeconds = 86400;
		long currentDaysSinceJoin = (serverTimeProvider.getTimeNowAsSeconds() - ServerTime.fromDate(player.getCreatedDate())) / aDayInSeconds;
		PlayerStats playerStats = player.getPlayerStats();
		if (playerStats.getDaysSinceJoin() < currentDaysSinceJoin) {
			playerStats.setDaysSinceJoin(currentDaysSinceJoin);
			return true;
		}
		return false;
	}

	private boolean updateCumulativeDays(Player player) {
		DateTime lastUpdatedUtcDate = ServerTime.toDateTime(ServerTime.fromDate(player.getLastUpdatedDate()));
		int dateComparison = DateTimeComparator.getDateOnlyInstance().compare(serverTimeProvider.getDateTime(), lastUpdatedUtcDate);
		if (dateComparison > 0) {
			player.getPlayerStats().incrementCumulativeDays();
			return true;
		}
		return false;
	}

	private void updateQuestBoard(Player player) {
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		if (questBoard.dailyQuestIsNotInitialized()) {
			Quest dailyQuest = new Quest();
			questBoard.initializeDailyQuest(dailyQuest);
			questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
		}
	}

	private Player createPlayer(User user, Catalog catalog, ABTag abTag) {
		return playerCreationDomainService.createPlayer(user.getUserId(), abTag, catalog, serverTimeProvider.getTimeNowAsSeconds());
	}

	private ABTag getABTagFromABTesterService(User user) {
		Optional<String> userTagOpt = abTesterService.findUserTag(user.getUserId());
		return ABTag.buildFromPostulation(userTagOpt);
	}

	private boolean updateBotMmr(Player player, PlayerWinRate playerWinRate) {
		if (isBotMmrNotInitialized(player)) {
			player.setBotMmr(playerWinRate.getMmr());
			return true;
		}
		return false;
	}

	private boolean isBotMmrNotInitialized(Player player) {
		return player.getBotMmr() == Player.NOT_INITIALIZED_BOT_MMR;
	}

	private void updatePlayerWinRate(Catalog catalog, Player player, PlayerWinRate playerWinRate) {
		int cappedMmr = getCappedMmrFrom(catalog);
		boolean isCappedEnabled = isCappedEnabledCheckingLeagueActivation(catalog);
		int newMmr = mmrIntegrityFixer.getFixedMmrIfIsLessThanMinimumRequired(player, playerWinRate.getMmr(), getMinimumMmrequired(catalog));
		boolean fixedMmrMinimum = tryToFixMmrWithMinimumRequired(playerWinRate, cappedMmr, newMmr, isCappedEnabled);
		boolean fixedMmrCapped = tryToFixMmrWithCapped(playerWinRate, cappedMmr, isCappedEnabled);
		if (fixedMmrMinimum || fixedMmrCapped) {
			playerWinRateRepository.update(playerWinRate);
		}
	}

	private boolean isCappedEnabledCheckingLeagueActivation(Catalog catalog) {
		// se activa el cappeo cuando la liga esta desactivada. En caso contrario, el cappeo tiene q estar desactivado
		return !catalog.getGameConstants().isLeagueEnabled();
	}

	private boolean tryToFixMmrWithMinimumRequired(PlayerWinRate playerWinRate, int cappedMmr, int newMmr, boolean isCappedEnabled) {
		if (newMmr != playerWinRate.getMmr()) {
			playerWinRate.updateMmr(newMmr, cappedMmr, isCappedEnabled);
			return true;
		}
		return false;
	}

	private boolean tryToFixMmrWithCapped(PlayerWinRate playerWinRate, int cappedMmr, boolean isCappedEnabled) {
		return playerWinRate.tryToFixMmrWith(cappedMmr, isCappedEnabled);
	}

	private boolean updateMapNumber(Catalog catalog, PlayerWinRate playerWinRate, Player player) {
		int mapNumber = new DefaultPlayerMapNumberCalculator(catalog.getMapsCollection().getEntries()).getMapNumber(playerWinRate.getMmr());
		if (mapNumber != player.getMapNumber()) {
			player.setMapNumber(mapNumber);
			return true;
		}
		return false;
	}

	private boolean userNotExists(User user) {
		return user == null;
	}

	private boolean shouldCreatePlayer(User user, Player player) {
		return player == null;
	}

	private Catalog getCatalogForUser(ABTag abTag) {
		return catalogAction.getCatalogForUser(abTag);
	}

	private PlayerWinRate getPlayerWinRate(Player player) {
		return playerWinRateRepository.findOrCrateDefault(player.getUserId());
	}

	private void updateSessionTokenAndPlatform(User user, Platform platform) {
		user.setSessionToken(UUID.randomUUID().toString());
		user.setPlatform(platform);
		userRepository.update(user);
	}

	private void validatePassword(User user, String loginId, String password) {
		if (!user.validatePassword(password)) {
			logger.info("Invalid credentials {}", loginId);
			throw new InvalidCredentialsException("Invalid Credentials Exception");
		}
	}

	private void finishUnfinishedButStartedPlayerBattles(Player player, Catalog catalog) {
		if (hasUnfinishedBattle(player)) {
			finishStartedBattle(player, catalog, player.getLastBattleId());
			resetUnfinishedBattle(player);
		}
	}

	private void resetUnfinishedBattle(Player player) {
		if (hasUnfinishedBattle(player)) {
			player.setLastBattleId("");
			playerRepository.update(player);
		}
	}

	private boolean hasUnfinishedBattle(Player player) {
		return player.getLastBattleId() != null && player.getLastBattleId().length() > 0;
	}

	private void finishStartedBattle(Player player, Catalog catalog, String lastBattleId) {
		battleRepository.find(lastBattleId).filter(Battle::getStarted)
				.ifPresent(battle -> finishPlayerBattleDomainService.finishBattlePlayer(player, catalog, battle));
	}

	private boolean updatePlayerCatalogId(Player player, Catalog catalog) {
		if (!catalog.getCatalogId().equals(player.getCatalogId())) {
			player.setCatalogId(catalog.getCatalogId());
			return true;
		}
		return false;
	}

	private void updateDefaultCaptainSkinsForOldUsers(Player player, Catalog catalog) {
		CaptainsCollection captainsCollection = captainCollectionRepository.findOrDefaultBy(player);
		List<Captain> captains = captainsCollection.getCaptains();
		boolean updateIsNeeded = false;
		for (Captain captain : captains) {
			List<CaptainSkinDefinition> captainSkinDefinitions = catalog.getCaptainSkinDefinitionsCollection().getEntries();
			if (captain.getCaptainSlots() == null || captain.getCaptainSlots().isEmpty()) {
				Map<Integer, CaptainSlot> captainSlots = new CaptainFactory()
						.createDefaultCaptainSlots(captain.getCaptainId(), captainSkinDefinitions);
				captain.forceUpdateCaptainSlotsForMigrationTask(captainSlots);
				updateIsNeeded = true;
			}
			if (captain.getOwnedSkins() == null || captain.getOwnedSkins().isEmpty()) {
				Map<String, CaptainSkin> ownedSkins = new CaptainFactory().createDefaultSkins(captain.getCaptainId(), captainSkinDefinitions);
				captain.forceUpdateOwnedCaptainsForMigrationTask(ownedSkins);
				updateIsNeeded = true;
			}
		}
		if (updateIsNeeded) {
			captainCollectionRepository.addOrUpdate(captainsCollection);
		}
	}

	private boolean updateTutorialProgress(Player player) {
		if (isOldPlayerTutorialFinished(player) || isTutorialUserProfileActive(player)) {
			player.finishActiveTutorial();
			player.addFinishedTutorial("UserProfile");
			return true;
		}
		return false;
	}

	private boolean isTutorialUserProfileActive(Player player) {
		return player.isTutorialIdActive("UserProfile");
	}

	private boolean isOldPlayerTutorialFinished(Player player) {
		return !player.isTutorialIdFinished("UserProfile") && player.isTutorialIdFinished("ThirdBattle") && !Objects
				.equals(player.getName(), Player.DEFAULT_PLAYER_NAME);
	}

	private int getCappedMmrFrom(Catalog catalog) {
		return catalog.getGameConstants().getCappedMmr();
	}

	private int getMinimumMmrequired(Catalog catalog) {
		return catalog.getGameConstants().getMinimumMMRAfterTutorial();
	}

}
