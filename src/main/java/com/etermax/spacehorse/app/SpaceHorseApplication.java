package com.etermax.spacehorse.app;

import static com.google.common.collect.Lists.newArrayList;


import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.configuration.AuthenticationConfiguration;
import com.etermax.spacehorse.configuration.EnviromentType;
import com.etermax.spacehorse.configuration.SpaceHorseConfiguration;
import com.etermax.spacehorse.core.abtest.model.ABTesterService;
import com.etermax.spacehorse.core.achievements.action.ClaimAchievementRewardAction;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionFactory;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.AchievementsInLoginExecutor;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.achievements.model.observers.BattlePlaysReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.CardsUnlockedReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.DefaultCompletedAchievementsByTypeObserver;
import com.etermax.spacehorse.core.achievements.action.FindAchievementAction;
import com.etermax.spacehorse.core.achievements.model.observers.PlayerLevelReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.PlayerMapReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.resource.AchievementsResource;
import com.etermax.spacehorse.core.admin.resource.AdminResource;
import com.etermax.spacehorse.core.ads.videorewards.actions.ClaimBoostVideoRewardAction;
import com.etermax.spacehorse.core.ads.videorewards.actions.GetAvailableVideoRewardAction;
import com.etermax.spacehorse.core.ads.videorewards.actions.chest.ClaimSpeedupChestVideoRewardAction;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardRepository;
import com.etermax.spacehorse.core.ads.videorewards.resource.boost.FinishBattleVideoRewardResource;
import com.etermax.spacehorse.core.ads.videorewards.resource.chest.SpeedupChestVideoRewardResource;
import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.authenticator.model.realtime.DefaultRealtimeServerRequestValidator;
import com.etermax.spacehorse.core.authenticator.resource.SpaceHorseAuthFilter;
import com.etermax.spacehorse.core.authenticator.resource.UserAuthenticator;
import com.etermax.spacehorse.core.battle.action.FindBattleAction;
import com.etermax.spacehorse.core.battle.action.FinishPlayerBattleAction;
import com.etermax.spacehorse.core.battle.action.FinishRealtimeBattleAction;
import com.etermax.spacehorse.core.battle.action.StartBattleAction;
import com.etermax.spacehorse.core.battle.model.BattleRewardsStrategy;
import com.etermax.spacehorse.core.battle.model.DefaultBattleFactory;
import com.etermax.spacehorse.core.battle.model.FinishPlayerBattleDomainService;
import com.etermax.spacehorse.core.battle.model.MmrCalculatorDomainService;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.battle.resource.BattleResource;
import com.etermax.spacehorse.core.capitain.actions.BuyCaptainAction;
import com.etermax.spacehorse.core.capitain.actions.SelectCaptainAction;
import com.etermax.spacehorse.core.capitain.actions.skins.BuyCaptainSkinAction;
import com.etermax.spacehorse.core.capitain.actions.skins.UpdateCaptainSkinsAction;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.resource.CaptainResource;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalogFilter;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.catalog.repository.ServerSettingsRepository;
import com.etermax.spacehorse.core.catalog.resource.CatalogResource;
import com.etermax.spacehorse.core.cheat.action.CheatAction;
import com.etermax.spacehorse.core.cheat.resource.CheatResource;
import com.etermax.spacehorse.core.club.ClubReporterConfiguration;
import com.etermax.spacehorse.core.club.infrastructure.ClubClient;
import com.etermax.spacehorse.core.club.infrastructure.ClubReporter;
import com.etermax.spacehorse.core.club.infrastructure.ClubReporterFactory;
import com.etermax.spacehorse.core.club.resource.ClubResource;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.common.exception.handler.ApiExceptionHandler;
import com.etermax.spacehorse.core.dynamodb.DynamoDBRepositoriesFactory;
import com.etermax.spacehorse.core.freechest.action.OpenFreeChestAction;
import com.etermax.spacehorse.core.freechest.resource.FreeChestResource;
import com.etermax.spacehorse.core.inapps.domain.market.Markets;
import com.etermax.spacehorse.core.inapps.domain.market.MarketsFactory;
import com.etermax.spacehorse.core.inapps.domain.market.android.service.AndroidProperties;
import com.etermax.spacehorse.core.inapps.domain.market.ios.service.IosProperties;
import com.etermax.spacehorse.core.inapps.repository.MarketRepository;
import com.etermax.spacehorse.core.liga.action.ClaimLeagueRewardsAction;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueRepository;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.liga.resource.LeagueResource;
import com.etermax.spacehorse.core.login.action.LinkGameCenterAction;
import com.etermax.spacehorse.core.login.action.LinkGooglePlayAction;
import com.etermax.spacehorse.core.login.action.LoginAdminUserAction;
import com.etermax.spacehorse.core.login.action.LoginExistentUserAction;
import com.etermax.spacehorse.core.login.action.LoginNewUserAction;
import com.etermax.spacehorse.core.login.action.LoginSupportUserAction;
import com.etermax.spacehorse.core.login.error.handler.InvalidCredentialsExceptionHandler;
import com.etermax.spacehorse.core.login.model.ClientVersionValidator;
import com.etermax.spacehorse.core.login.model.PlayerCreationDomainService;
import com.etermax.spacehorse.core.login.repository.AuthRepository;
import com.etermax.spacehorse.core.login.resource.FeatureTogglesResource;
import com.etermax.spacehorse.core.login.resource.GameCenterLoginResource;
import com.etermax.spacehorse.core.login.resource.LoginResource;
import com.etermax.spacehorse.core.login.resource.PlayServicesLoginResource;
import com.etermax.spacehorse.core.login.resource.response.LoginResponseFactory;
import com.etermax.spacehorse.core.login.resource.response.PlayerResponseFactory;
import com.etermax.spacehorse.core.matchmaking.action.ChallengeMatchmakingAction;
import com.etermax.spacehorse.core.matchmaking.action.FriendlyMatchmakingAction;
import com.etermax.spacehorse.core.matchmaking.model.BotOpponentVerifier;
import com.etermax.spacehorse.core.matchmaking.model.MatchStarterDomainService;
import com.etermax.spacehorse.core.matchmaking.model.match.DefaultMatchFactory;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchFactory;
import com.etermax.spacehorse.core.matchmaking.resource.MatchmakingResource;
import com.etermax.spacehorse.core.matchmaking.service.challenge.ChallengeMatchmakingService;
import com.etermax.spacehorse.core.matchmaking.service.friendly.FriendlyMatchmakingService;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.action.chest.FinishOpeningChestAction;
import com.etermax.spacehorse.core.player.action.chest.SpeedupOpeningChestAction;
import com.etermax.spacehorse.core.player.action.chest.StartOpeningChestAction;
import com.etermax.spacehorse.core.player.action.deck.GetDeckCardsAction;
import com.etermax.spacehorse.core.player.action.deck.GetSelectedCardsFromDeckAction;
import com.etermax.spacehorse.core.player.action.deck.SelectDeckCardsAction;
import com.etermax.spacehorse.core.player.action.deck.UpgradeCardAction;
import com.etermax.spacehorse.core.player.integrityfixer.MmrIntegrityFixer;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.player.resource.PlayerChestResource;
import com.etermax.spacehorse.core.player.resource.PlayerDeckResource;
import com.etermax.spacehorse.core.player.resource.PlayerResource;
import com.etermax.spacehorse.core.quest.action.ClaimQuestAction;
import com.etermax.spacehorse.core.quest.action.RefreshQuestAction;
import com.etermax.spacehorse.core.quest.action.RefreshQuestPayingAction;
import com.etermax.spacehorse.core.quest.action.SkipQuestAction;
import com.etermax.spacehorse.core.quest.action.SkipQuestPayingAction;
import com.etermax.spacehorse.core.quest.action.daily.ClaimDailyQuestAction;
import com.etermax.spacehorse.core.quest.action.daily.RefreshDailyQuestAction;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.model.QuestProgressUpdater;
import com.etermax.spacehorse.core.quest.model.QuestRewardAssigner;
import com.etermax.spacehorse.core.quest.model.RefreshPayingQuestCostCalculator;
import com.etermax.spacehorse.core.quest.resource.QuestResource;
import com.etermax.spacehorse.core.reconnect.resource.ManageServerStatusAction;
import com.etermax.spacehorse.core.reconnect.resource.ServerUnderMaintenanceFilter;
import com.etermax.spacehorse.core.reconnect.resource.ServerUnderMaintenanceResource;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.reward.model.PurchasableRewardCollector;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.shop.action.ShopBuyCardAction;
import com.etermax.spacehorse.core.shop.action.ShopBuyInAppItemAction;
import com.etermax.spacehorse.core.shop.action.ShopBuyItemAction;
import com.etermax.spacehorse.core.shop.action.ShopRefreshCardsAction;
import com.etermax.spacehorse.core.shop.exception.handler.NotEnoughGemsHandler;
import com.etermax.spacehorse.core.shop.model.InAppReceiptCreationDomainService;
import com.etermax.spacehorse.core.shop.resource.ShopResource;
import com.etermax.spacehorse.core.socialplayer.action.GetSocialPlayerAction;
import com.etermax.spacehorse.core.socialplayer.resource.SocialPlayerResource;
import com.etermax.spacehorse.core.specialoffer.action.ApplySpecialOfferPendingRewardsAction;
import com.etermax.spacehorse.core.specialoffer.action.BuyInAppSpecialOfferAction;
import com.etermax.spacehorse.core.specialoffer.action.BuySpecialOfferAction;
import com.etermax.spacehorse.core.specialoffer.action.RefreshSpecialOfferBoardAction;
import com.etermax.spacehorse.core.specialoffer.model.BuySpecialOfferDomainService;
import com.etermax.spacehorse.core.specialoffer.model.RefreshSpecialOfferBoardDomainService;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOffersApplyPendingRewards;
import com.etermax.spacehorse.core.specialoffer.resource.SpecialOfferResource;
import com.etermax.spacehorse.core.status.StatusResource;
import com.etermax.spacehorse.core.support.action.DepositAction;
import com.etermax.spacehorse.core.support.resource.SupportResource;
import com.etermax.spacehorse.core.user.action.UserAction;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;
import com.etermax.spacehorse.healthcheck.SpaceHorseHealthCheck;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.bundles.assets.ConfiguredAssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class SpaceHorseApplication extends Application<SpaceHorseConfiguration> {

	public static final String HEALTHCHECK_APP = "space_horse_healthcheck";

	private static final Logger logger = LoggerFactory.getLogger(SpaceHorseApplication.class);
	protected HorseRaceRepositories horseRaceRepositories;
	private LoginExistentUserAction loginExistentUserAction;
	private ChallengeMatchmakingAction challengeMatchmakingAction;
	private FriendlyMatchmakingAction friendlyMatchmakingAction;
	private CatalogAction catalogAction;
	private UserAction userAction;
	private PlayerAction playerAction;
	private StartOpeningChestAction startOpeningChestAction;
	private FinishOpeningChestAction finishOpeningChestAction;
	private SpeedupOpeningChestAction speedupOpeningChestAction;
	private CheatAction cheatAction;
	private ShopBuyItemAction shopBuyItemAction;
	private ShopBuyCardAction shopBuyCardAction;
	private ShopRefreshCardsAction shopRefreshCardsAction;
	private ShopBuyInAppItemAction shopBuyInAppItemAction;
	private LinkGooglePlayAction linkGooglePlayAction;
	private LinkGameCenterAction linkGameCenterAction;
	private OpenFreeChestAction openFreeChestAction;
	private ManageServerStatusAction manageServerStatusAction;
	private MatchFactory matchFactory;
	private ServerTimeProvider serverTimeProvider;
	private SelectDeckCardsAction selectDeckCardsAction;
	private LoginNewUserAction loginNewUserAction;
	private GetSelectedCardsFromDeckAction getSelectedCardsFromDeckAction;
	private LoginAdminUserAction loginAdminUserAction;
	private GetDeckCardsAction getDeckCardsAction;
	private UpgradeCardAction upgradeCardAction;
	private StartBattleAction startBattleAction;
	private FinishRealtimeBattleAction finishRealtimeBattleAction;
	private FinishPlayerBattleAction finishPlayerBattleAction;
	private FindBattleAction findBattleAction;
	private RefreshQuestAction refreshQuestAction;
	private RefreshDailyQuestAction refreshDailyQuestAction;
	private RefreshQuestPayingAction refreshQuestPayingAction;
	private ClaimQuestAction claimQuestAction;
	private LoginResponseFactory loginResponseFactory;
	private PlayerResponseFactory playerResponseFactory;
	private SkipQuestAction skipQuestAction;
	private SkipQuestPayingAction skipQuestPayingAction;
	private BuySpecialOfferAction buySpecialOfferAction;
	private BuyInAppSpecialOfferAction buyInAppSpecialOfferAction;
	private RefreshSpecialOfferBoardAction refreshSpecialOfferBoardAction;
	private ApplySpecialOfferPendingRewardsAction applySpecialOfferPendingRewardsAction;
	private BuyCaptainAction buyCaptainAction;
	private SelectCaptainAction selectCaptainAction;
	private BuyCaptainSkinAction buyCaptainSkinAction;
	private UpdateCaptainSkinsAction updateCaptainSkinsAction;
	private GetSocialPlayerAction getSocialPlayerAction;
	private ClaimSpeedupChestVideoRewardAction claimSpeedupChestVideoRewardAction;
	private ClaimBoostVideoRewardAction claimBoostVideoRewardAction;
	private GetAvailableVideoRewardAction getAvailableVideoRewardAction;
	private ClaimDailyQuestAction claimDailyQuestAction;
	private DepositAction depositAction;
	private LoginSupportUserAction loginSupportUserAction;
	private CompleteAchievementAction completeAchievementAction;
	private FindAchievementAction findAchievementAction;
	private ClaimAchievementRewardAction claimAchievementRewardAction;
	private AchievementsFactory achievementsFactory;
	private PlayerLevelReachedAchievementObserver playerLevelReachedAchievementObserver;
	private BattlePlaysReachedAchievementObserver battlePlaysReachedAchievementObserver;
	private PlayerMapReachedAchievementObserver playerMapReachedAchievementObserver;
	private GetRewardsDomainService getRewardsDomainService;
	private ApplyRewardDomainService applyRewardDomainService;

	public static void main(String[] args) throws Exception {
		new SpaceHorseApplication().run(args);
	}

	@Override
	public void run(SpaceHorseConfiguration configuration, Environment environment) {

		serverTimeProvider = createServerTimeProvider();

		horseRaceRepositories = configurePersistence(configuration, serverTimeProvider);

		registerHealthChecks(environment);

		createActions(configuration, horseRaceRepositories);

		registerResources(configuration, environment);

		registerAuthenticator(configuration.getAuthenticationConfiguration(), environment);

		registerLatestCatalogFilter(environment, horseRaceRepositories);

		registerServerUnderMaintenanceFilter(environment);

		registerExceptionHandler(environment);

		createDefaultAdminUser(configuration.getAuthenticationConfiguration());

		createDefaultSupportUser(configuration.getAuthenticationConfiguration());

	}

	private void registerServerUnderMaintenanceFilter(Environment environment) {
		environment.jersey().register(new ServerUnderMaintenanceFilter(horseRaceRepositories.getServerSettingsRepository()));
	}

	private void registerLatestCatalogFilter(Environment environment, HorseRaceRepositories horseRaceRepositories) {
		environment.jersey().register(new RequiresLatestCatalogFilter(horseRaceRepositories.getCatalogRepository()));
	}

	private HorseRaceRepositories configurePersistence(SpaceHorseConfiguration configuration, ServerTimeProvider serverTimeProvider) {
		return DynamoDBRepositoriesFactory.createRepositories(configuration, serverTimeProvider);
	}

	private void createActions(SpaceHorseConfiguration configuration, HorseRaceRepositories horseRaceRepositories) {
		UserRepository userRepository = horseRaceRepositories.getUserRepository();
		CatalogRepository catalogRepository = horseRaceRepositories.getCatalogRepository();
		PlayerRepository playerRepository = horseRaceRepositories.getPlayerRepository();
		BattleRepository battleRepository = horseRaceRepositories.getBattleRepository();
		PlayerWinRateRepository playerWinRateRepository = horseRaceRepositories.getPlayerWinRateRepository();
		AuthRepository authRepository = horseRaceRepositories.getAuthRepository();
		MarketRepository marketRepository = horseRaceRepositories.getMarketRepository();
		ServerSettingsRepository serverSettingsRepository = horseRaceRepositories.getServerSettingsRepository();
		QuestBoardRepository questBoardRepository = horseRaceRepositories.getQuestBoardRepository();
		SpecialOfferBoardRepository specialOfferBoardRepository = horseRaceRepositories.getSpecialOfferBoardRepository();
		CaptainCollectionRepository captainCollectionRepository = horseRaceRepositories.getCaptainCollectionRepository();
		QuotaVideoRewardRepository quotaVideoRewardRepository = horseRaceRepositories.getQuotaVideoRewardRepository();
		AchievementCollectionRepository achievementCollectionRepository = horseRaceRepositories.getAchievementCollectionRepository();
		PlayerLeagueRepository playerLeagueRepository = horseRaceRepositories.getPlayerLeagueRepository();

		getRewardsDomainService = new GetRewardsDomainService();
		applyRewardDomainService = new ApplyRewardDomainService();

		completeAchievementAction = new CompleteAchievementAction(achievementCollectionRepository);
		findAchievementAction = new FindAchievementAction(achievementCollectionRepository);
		claimAchievementRewardAction = new ClaimAchievementRewardAction(achievementCollectionRepository, playerRepository);
		achievementsFactory = new AchievementsFactory(findAchievementAction, completeAchievementAction);
		playerLevelReachedAchievementObserver = new PlayerLevelReachedAchievementObserver(findAchievementAction, completeAchievementAction);
		battlePlaysReachedAchievementObserver = new BattlePlaysReachedAchievementObserver(findAchievementAction,completeAchievementAction);
		playerMapReachedAchievementObserver = new PlayerMapReachedAchievementObserver(findAchievementAction,completeAchievementAction);

		matchFactory = new DefaultMatchFactory(new DefaultBattleFactory(), configuration.getRealtimeServerConfiguration().getServer(),
				battleRepository);

		userAction = new UserAction(userRepository);
		catalogAction = new CatalogAction(catalogRepository, serverSettingsRepository);
		playerAction = new PlayerAction(playerRepository, catalogRepository);

		startOpeningChestAction = new StartOpeningChestAction(catalogRepository, playerRepository, serverTimeProvider);
		finishOpeningChestAction = new FinishOpeningChestAction(playerRepository, catalogRepository, serverTimeProvider, getRewardsDomainService,
				applyRewardDomainService, achievementsFactory);
		speedupOpeningChestAction = new SpeedupOpeningChestAction(playerRepository, catalogRepository, serverTimeProvider, getRewardsDomainService,
				applyRewardDomainService, achievementsFactory, completeAchievementAction);

		selectDeckCardsAction = new SelectDeckCardsAction(playerRepository, catalogRepository);
		getSelectedCardsFromDeckAction = new GetSelectedCardsFromDeckAction(playerRepository);
		getDeckCardsAction = new GetDeckCardsAction(playerRepository);
		upgradeCardAction = new UpgradeCardAction(playerRepository, catalogRepository, playerLevelReachedAchievementObserver);

		PurchasableRewardCollector rewardCollector = new PurchasableRewardCollector(getRewardsDomainService);

		linkGooglePlayAction = new LinkGooglePlayAction(playerRepository, authRepository);
		linkGameCenterAction = new LinkGameCenterAction(playerRepository, authRepository);

		shopBuyItemAction = new ShopBuyItemAction(playerRepository, applyRewardDomainService, rewardCollector, serverTimeProvider,
				achievementsFactory);

		CardsUnlockedReachedAchievementObserver cardsUnlockedReachedAchievementObserver = new CardsUnlockedReachedAchievementObserver(
				findAchievementAction, completeAchievementAction);
		DefaultCompletedAchievementsByTypeObserver purchaseOneCardAchievementObserver = new DefaultCompletedAchievementsByTypeObserver(completeAchievementAction,
				AchievementType.PURCHASE_CARD);
		shopBuyCardAction = new ShopBuyCardAction(playerRepository, serverTimeProvider,
				newArrayList(cardsUnlockedReachedAchievementObserver, purchaseOneCardAchievementObserver));

		shopRefreshCardsAction = new ShopRefreshCardsAction(playerRepository, serverTimeProvider);
		cheatAction = new CheatAction(playerRepository, catalogRepository, playerWinRateRepository, userRepository, serverTimeProvider,
				questBoardRepository, specialOfferBoardRepository, linkGooglePlayAction, linkGameCenterAction,
				cardsUnlockedReachedAchievementObserver, playerLevelReachedAchievementObserver,
				playerMapReachedAchievementObserver);

		BattleRewardsStrategy battleRewardsStrategy = new BattleRewardsStrategy(serverTimeProvider);
		MmrCalculatorDomainService mmrCalculatorDomainService = new MmrCalculatorDomainService(catalogRepository);

		PlayerLeagueService playerLeagueService = new PlayerLeagueService(playerLeagueRepository, serverTimeProvider, playerWinRateRepository);
		FinishPlayerBattleDomainService finishPlayerBattleDomainService = new FinishPlayerBattleDomainService(playerRepository,
				playerWinRateRepository, battleRewardsStrategy, applyRewardDomainService, mmrCalculatorDomainService,
				newArrayList(battlePlaysReachedAchievementObserver, playerMapReachedAchievementObserver),
				new QuestProgressUpdater(questBoardRepository,completeAchievementAction), achievementsFactory, playerLeagueService);

		startBattleAction = new StartBattleAction(battleRepository, serverTimeProvider);
		ClubReporterConfiguration clubReporterConfiguration = configuration.getClubReporterConfiguration();
		ClubClient clubClient = ClubReporterFactory.create(clubReporterConfiguration);
		ClubReporter clubReporter = new ClubReporter(clubClient, clubReporterConfiguration);
		finishRealtimeBattleAction = new FinishRealtimeBattleAction(battleRepository, playerRepository, catalogRepository, playerWinRateRepository,
				mmrCalculatorDomainService, clubReporter);
		finishPlayerBattleAction = new FinishPlayerBattleAction(battleRepository, playerRepository, catalogRepository,
				finishPlayerBattleDomainService);
		findBattleAction = new FindBattleAction(battleRepository);

		MatchStarterDomainService matchStarterDomainService = new MatchStarterDomainService(matchFactory, playerRepository);
		challengeMatchmakingAction = new ChallengeMatchmakingAction(matchFactory, catalogRepository, playerWinRateRepository,
				configuration.getEnvironment() == EnviromentType.DEVELOPMENT, playerRepository, captainCollectionRepository,
				new BotOpponentVerifier(), new ChallengeMatchmakingService(catalogRepository, matchStarterDomainService, serverTimeProvider));

		friendlyMatchmakingAction = new FriendlyMatchmakingAction(playerWinRateRepository,
				configuration.getEnvironment() == EnviromentType.DEVELOPMENT, captainCollectionRepository,
				new FriendlyMatchmakingService(catalogRepository, matchStarterDomainService));

		ABTesterService abTesterService = new ABTesterService(configuration.getAbTesterConfiguration());
		MmrIntegrityFixer mmrIntegrityFixer = new MmrIntegrityFixer();

		String androidPackageName = configuration.getInAppsAndroidConfiguration().getPackageName();
		String androidSignature = configuration.getInAppsAndroidConfiguration().getSignature();
		AndroidProperties androidProperties = new AndroidProperties(androidPackageName, androidSignature);

		String iosBundleId = configuration.getInAppsIosConfiguration().getBundleId();
		IosProperties iosProperties = new IosProperties(iosBundleId);

		Markets markets = MarketsFactory.buildMarkets(androidProperties, iosProperties);
		InAppReceiptCreationDomainService inAppReceiptCreationDomainService = new InAppReceiptCreationDomainService(markets, marketRepository,
				configuration.getEnvironment());

		RefreshSpecialOfferBoardDomainService refreshSpecialOfferBoardDomainService = new RefreshSpecialOfferBoardDomainService(
				specialOfferBoardRepository);
		refreshSpecialOfferBoardAction = new RefreshSpecialOfferBoardAction(refreshSpecialOfferBoardDomainService);

		BuySpecialOfferDomainService buySpecialOfferDomainService = new BuySpecialOfferDomainService(specialOfferBoardRepository, playerRepository,
				applyRewardDomainService, rewardCollector, achievementsFactory);
		buySpecialOfferAction = new BuySpecialOfferAction(buySpecialOfferDomainService, serverTimeProvider);

		buyInAppSpecialOfferAction = new BuyInAppSpecialOfferAction(playerRepository, specialOfferBoardRepository, applyRewardDomainService,
				rewardCollector, inAppReceiptCreationDomainService, serverTimeProvider, achievementsFactory);
		applySpecialOfferPendingRewardsAction = new ApplySpecialOfferPendingRewardsAction(
				new SpecialOffersApplyPendingRewards(inAppReceiptCreationDomainService, buySpecialOfferDomainService, serverTimeProvider));

		QuestFactory questFactory = new QuestFactory(serverTimeProvider);
		AchievementCollectionFactory achievementCollectionFactory = new AchievementCollectionFactory();
		PlayerCreationDomainService playerCreationDomainService = new PlayerCreationDomainService(playerRepository, applyRewardDomainService,
				questBoardRepository, serverTimeProvider, specialOfferBoardRepository, captainCollectionRepository,
				achievementCollectionRepository, achievementCollectionFactory, achievementsFactory);

		loginAdminUserAction = new LoginAdminUserAction(userRepository);
		loginExistentUserAction = new LoginExistentUserAction(userRepository, playerRepository, playerWinRateRepository, catalogAction,
				battleRepository, serverTimeProvider, abTesterService, playerCreationDomainService, mmrIntegrityFixer,
				finishPlayerBattleDomainService, clubReporter, captainCollectionRepository, questBoardRepository, linkGooglePlayAction,
				linkGameCenterAction, new AchievementsInLoginExecutor(findAchievementAction,completeAchievementAction, achievementCollectionRepository,
				captainCollectionRepository), playerLeagueService);
		loginNewUserAction = new LoginNewUserAction(userRepository, playerRepository, configuration.getEnvironmentRole(), catalogAction,
				playerWinRateRepository, serverTimeProvider, abTesterService, playerCreationDomainService, playerLeagueService);

		shopBuyInAppItemAction = new ShopBuyInAppItemAction(shopBuyItemAction, inAppReceiptCreationDomainService);
		openFreeChestAction = new OpenFreeChestAction(playerRepository, catalogRepository, getRewardsDomainService, applyRewardDomainService, serverTimeProvider,
				achievementsFactory);

		refreshQuestAction = new RefreshQuestAction(questBoardRepository, questFactory);
		RefreshPayingQuestCostCalculator refreshPayingQuestCostCalculator = new RefreshPayingQuestCostCalculator(serverTimeProvider);
		refreshQuestPayingAction = new RefreshQuestPayingAction(questBoardRepository, questFactory, playerRepository,
				refreshPayingQuestCostCalculator);
		refreshDailyQuestAction = new RefreshDailyQuestAction(questBoardRepository, questFactory);

		QuestRewardAssigner questRewardAssigner = new QuestRewardAssigner(playerRepository, applyRewardDomainService, getRewardsDomainService,
				serverTimeProvider, achievementsFactory);
		claimQuestAction = new ClaimQuestAction(questRewardAssigner, questBoardRepository, serverTimeProvider);
		skipQuestAction = new SkipQuestAction(questBoardRepository, questFactory);
		skipQuestPayingAction = new SkipQuestPayingAction(questBoardRepository, questFactory, playerRepository);

		manageServerStatusAction = new ManageServerStatusAction(serverSettingsRepository);

		playerResponseFactory = new PlayerResponseFactory(questBoardRepository, specialOfferBoardRepository, captainCollectionRepository,
				achievementCollectionRepository, playerLeagueService);
		loginResponseFactory = new LoginResponseFactory(configuration.getMatchmakingServerUrl(), serverTimeProvider, playerResponseFactory);

		buyCaptainAction = new BuyCaptainAction(captainCollectionRepository, playerRepository, new DefaultCompletedAchievementsByTypeObserver
				(completeAchievementAction, AchievementType.PURCHASE_CAPTAIN));
		selectCaptainAction = new SelectCaptainAction(captainCollectionRepository);
		buyCaptainSkinAction = new BuyCaptainSkinAction(captainCollectionRepository, playerRepository, findAchievementAction,
				completeAchievementAction);
		updateCaptainSkinsAction = new UpdateCaptainSkinsAction(captainCollectionRepository, playerRepository);

		getSocialPlayerAction = new GetSocialPlayerAction(playerRepository, playerWinRateRepository, captainCollectionRepository);

		claimSpeedupChestVideoRewardAction = new ClaimSpeedupChestVideoRewardAction(quotaVideoRewardRepository, serverTimeProvider, playerRepository);

		claimBoostVideoRewardAction = new ClaimBoostVideoRewardAction(quotaVideoRewardRepository, playerRepository);

		getAvailableVideoRewardAction = new GetAvailableVideoRewardAction(quotaVideoRewardRepository);

		claimDailyQuestAction = new ClaimDailyQuestAction(questRewardAssigner, questBoardRepository);

		loginSupportUserAction = new LoginSupportUserAction(userRepository);
		depositAction = new DepositAction(playerRepository);
	}

	private void registerResources(SpaceHorseConfiguration configuration, Environment environment) {
		Integer expectedClientVersion = configuration.getExpectedClientVersion();
		environment.jersey().register(RolesAllowedDynamicFeature.class);
		environment.jersey().register(new StatusResource());
		environment.jersey().register(new CatalogResource(catalogAction, loginAdminUserAction, isAutoActivateNewCatalog(configuration)));
		environment.jersey().register(new PlayerResource(playerAction));
		environment.jersey()
				.register(new PlayerDeckResource(selectDeckCardsAction, getSelectedCardsFromDeckAction, getDeckCardsAction, upgradeCardAction));
		environment.jersey().register(new PlayerChestResource(startOpeningChestAction, finishOpeningChestAction, speedupOpeningChestAction));
		environment.jersey().register(new FreeChestResource(openFreeChestAction));
		environment.jersey().register(
				new LoginResource(loginExistentUserAction, loginNewUserAction, new ClientVersionValidator(expectedClientVersion),
						manageServerStatusAction, loginResponseFactory));
		environment.jersey().register(new PlayServicesLoginResource(linkGooglePlayAction, userAction, playerResponseFactory));
		environment.jersey().register(new GameCenterLoginResource(linkGameCenterAction, userAction, playerResponseFactory));
		environment.jersey().register(new MatchmakingResource(challengeMatchmakingAction, friendlyMatchmakingAction, playerAction));
		environment.jersey().register(new AdminResource(userAction, loginAdminUserAction));
		environment.jersey().register(new SupportResource(loginSupportUserAction, playerAction, depositAction));
		environment.jersey().register(new CheatResource(cheatAction));
		environment.jersey().register(
				new BattleResource(new DefaultRealtimeServerRequestValidator(), catalogAction, startBattleAction, finishRealtimeBattleAction,
						finishPlayerBattleAction, findBattleAction));
		environment.jersey().register(
				new ShopResource(shopBuyItemAction, shopBuyCardAction, shopRefreshCardsAction, playerAction, catalogAction, userAction,
						shopBuyInAppItemAction));
		environment.jersey().register(
				new QuestResource(playerAction, catalogAction, refreshQuestAction, claimQuestAction, skipQuestAction, skipQuestPayingAction,
						refreshQuestPayingAction, refreshDailyQuestAction, claimDailyQuestAction));
		environment.jersey().register(new ServerUnderMaintenanceResource(manageServerStatusAction, loginAdminUserAction));

		environment.jersey().register(new SpecialOfferResource(buySpecialOfferAction, buyInAppSpecialOfferAction, refreshSpecialOfferBoardAction,
				applySpecialOfferPendingRewardsAction, playerAction, catalogAction, userAction));

		environment.jersey().register(new CaptainResource(playerAction, catalogAction, buyCaptainAction, selectCaptainAction, buyCaptainSkinAction,
				updateCaptainSkinsAction));

		environment.jersey().register(new SocialPlayerResource(getSocialPlayerAction));

		UserAuthenticator userAuthenticator = new UserAuthenticator(userAction);
		environment.jersey().register(new ClubResource(userAuthenticator, playerAction));

		environment.jersey().register(
				new SpeedupChestVideoRewardResource(playerAction, catalogAction, claimSpeedupChestVideoRewardAction, getAvailableVideoRewardAction));

		environment.jersey()
				.register(new FinishBattleVideoRewardResource(playerAction, catalogAction, claimBoostVideoRewardAction, getAvailableVideoRewardAction));

		environment.jersey().register(new FeatureTogglesResource());
		environment.jersey().register(new AchievementsResource(playerAction, catalogAction, claimAchievementRewardAction, completeAchievementAction));

		ClaimLeagueRewardsAction claimLeagueRewardsAction = new ClaimLeagueRewardsAction(getRewardsDomainService, applyRewardDomainService,
				serverTimeProvider, horseRaceRepositories.getPlayerRepository(), horseRaceRepositories.getPlayerLeagueRepository());
		environment.jersey().register(new LeagueResource(playerAction, catalogAction, claimLeagueRewardsAction));

	}

	private boolean isAutoActivateNewCatalog(SpaceHorseConfiguration configuration) {
		return configuration.getEnvironment() == EnviromentType.DEVELOPMENT;
	}

	private void registerHealthChecks(Environment environment) {
		environment.healthChecks().register(HEALTHCHECK_APP, new SpaceHorseHealthCheck());
	}

	private void registerAuthenticator(AuthenticationConfiguration configuration, Environment environment) {
		if (configuration.isEnabled()) {
			UserAuthenticator userAuthenticator = new UserAuthenticator(userAction);
			SpaceHorseAuthFilter authFilter = new SpaceHorseAuthFilter(userAuthenticator);
			environment.jersey().register(new AuthDynamicFeature(authFilter));
		}
	}

	private void registerExceptionHandler(Environment environment) {
		environment.jersey().register(new ApiExceptionHandler());
		environment.jersey().register(new NotEnoughGemsHandler());
		environment.jersey().register(new InvalidCredentialsExceptionHandler());
	}

	private void createDefaultAdminUser(AuthenticationConfiguration configuration) {
		createDefaultUser(configuration.getDefaultAdminLoginId(), configuration.getDefaultAdminPassword(), Role.ADMIN);
	}

	private void createDefaultSupportUser(AuthenticationConfiguration configuration) {
		createDefaultUser(configuration.getDefaultSupportLoginId(), configuration.getDefaultSupportPassword(), Role.SUPPORT);
	}

	private void createDefaultUser(String loginId, String password, Role role) {
		try {
			User user = userAction.findByUserId(loginId);
			if (user == null) {
				this.userAction.createUser(loginId, password, role, Platform.EDITOR);
			}
		} catch (Exception e) {
			logger.error("Unexpected error when trying to create a default user. " + e.getMessage(), e);
			throw new ApiException(e);
		}
	}

	@Override
	public void initialize(Bootstrap<SpaceHorseConfiguration> bootstrap) {
		bootstrap.addBundle(new SwaggerBundle<Configuration>() {
			@Override
			protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(Configuration configuration) {
				SwaggerBundleConfiguration swaggerConfig = new SwaggerBundleConfiguration();
				swaggerConfig.setResourcePackage("com.etermax.spacehorse.core");
				swaggerConfig.setTitle("Space-Horse Api");
				swaggerConfig.setDescription("Space-Horse Api Service");
				if (((SpaceHorseConfiguration) configuration).getEnvironment().equals(EnviromentType.PRODUCTION)) {
					swaggerConfig.setIsEnabled(false);
				}
				return swaggerConfig;
			}
		});

		bootstrap.addBundle(new ConfiguredAssetsBundle("/assets/", "/dashboard/"));

		bootstrap.setConfigurationSourceProvider(
				new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
		super.initialize(bootstrap);
	}

	protected ServerTimeProvider createServerTimeProvider() {
		return new ServerTime();
	}

	protected ServerTimeProvider getServerTimeProvider() {
		return serverTimeProvider;
	}
}
