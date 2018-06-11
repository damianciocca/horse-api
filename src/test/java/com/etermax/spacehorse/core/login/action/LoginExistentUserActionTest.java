package com.etermax.spacehorse.core.login.action;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.abtest.model.ABTesterService;
import com.etermax.spacehorse.core.achievements.model.AchievementsInLoginExecutor;
import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.battle.model.FinishPlayerBattleDomainService;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.captain.InMemoryCaptainCollectionRepository;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.club.infrastructure.ClubReporter;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.login.error.InvalidCredentialsException;
import com.etermax.spacehorse.core.login.model.PlayerCreationDomainService;
import com.etermax.spacehorse.core.player.integrityfixer.MmrIntegrityFixer;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.InMemorySpecialOfferBoardRepository;
import com.etermax.spacehorse.core.specialoffer.model.RefreshSpecialOfferBoardDomainService;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.core.user.repository.UserRepository;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class LoginExistentUserActionTest {

	private static final String CAMPAIGN_NAME_SEGMENT_TAG = "campaignName-segmentTag";
	private static final String USER_ID = "123456";
	private static final String LOGIN_ID = "10";

	private LoginExistentUserAction action;
	private String catalogId;
	private int MMR = 160;
	private UserRepository userRepository;
	private PlayerRepository playerRepository;
	private Player player;

	@Before
	public void setUp() throws Exception {
		FixedServerTimeProvider timeProvider = new FixedServerTimeProvider();
		Catalog catalog = MockUtils.mockCatalog();
		catalogId = catalog.getCatalogId();
		playerRepository = aPlayerRepository();
		player = new PlayerScenarioBuilder(LOGIN_ID, new ABTag(CAMPAIGN_NAME_SEGMENT_TAG)).build();
		userRepository = anEmptyUserRepository();
		QuestBoardRepository questBoardRepository = new InMemoryQuestBoardRepository(timeProvider);
		PlayerWinRateRepository playerWinRateRepository = aPlayerWinRateRepository();
		CatalogAction catalogAction = aCatalogAction(catalog);
		BattleRepository battleRepository = mock(BattleRepository.class);
		FinishPlayerBattleDomainService finishPlayerBattleDomainService = mock(FinishPlayerBattleDomainService.class);
		PlayerCreationDomainService playerCreationDomainService = aPlayerCreationDomainService(catalog, timeProvider);
		LinkGooglePlayAction linkGooglePlayAction = mock(LinkGooglePlayAction.class);
		when(linkGooglePlayAction.findGooglePlayIdLinkedWithLoginId(any())).thenReturn(Optional.empty());
		LinkGameCenterAction linkGameCenterAction = mock(LinkGameCenterAction.class);
		when(linkGameCenterAction.findGameCenterIdLinkedWithLoginId(any())).thenReturn(Optional.empty());

		ClubReporter clubReporter = mock(ClubReporter.class);
		doNothing().when(clubReporter).updateUserScore(anyString(), anyInt());
		action = new LoginExistentUserAction(userRepository, playerRepository, playerWinRateRepository, catalogAction, battleRepository, timeProvider,
				anAbTesterService(), playerCreationDomainService, new MmrIntegrityFixer(), finishPlayerBattleDomainService,
				clubReporter, new InMemoryCaptainCollectionRepository(), questBoardRepository, linkGooglePlayAction, linkGameCenterAction,
				mock(AchievementsInLoginExecutor.class), mock(PlayerLeagueService.class));
	}

	@Test
	public void whenLoginANonExistentUserThenAnExceptionShouldBeThrown() {

		assertThatThrownBy(() -> action.login(LOGIN_ID, "password", Platform.ANDROID)).isInstanceOf(InvalidCredentialsException.class)
				.hasMessageContaining("Unknown loginId");
	}

	@Test
	public void whenLoginAnExistentUserWithInvalidCredentialThenAnExceptionShouldBeThrown() {
		// Given
		String invalidCredential = "otherPassword";
		givenAUserRepositoryWithUserWithPassword(invalidCredential);
		// When
		assertThatThrownBy(() -> action.login(LOGIN_ID, "password", Platform.ANDROID)).isInstanceOf(InvalidCredentialsException.class)
				.hasMessageContaining("Invalid Credentials Exception");
	}

	@Test
	public void whenLoginAnExistentUserAndNonExistentPlayerWithValidCredentialThenTheUserShouldBeLogged() {
		// given
		String validCredential = "otherPassword";
		givenAUserRepositoryWithUserWithPassword(validCredential);
		// when
		LoginInfo loginInfo = action.login(LOGIN_ID, validCredential, Platform.ANDROID);
		// then
		thenAssertThatNewUserWasLogged(loginInfo);
	}

	@Test
	public void whenLoginAnExistentUserAndExistentPlayerWithValidCredentialThenTheUserShouldBeLogged() {
		// given
		String validCredential = "otherPassword";
		givenAUserRepositoryWithUserWithPassword(validCredential);
		when(playerRepository.find(LOGIN_ID)).thenReturn(player);
		// when
		LoginInfo loginInfo = action.login(LOGIN_ID, validCredential, Platform.ANDROID);
		// then
		thenAssertThatNewUserWasLogged(loginInfo);
	}

	private void givenAUserRepositoryWithUserWithPassword(String otherPassword) {
		when(userRepository.find(LOGIN_ID)).thenReturn(new User(USER_ID, otherPassword, Role.PLAYER, Platform.ANDROID));
	}

	private void thenAssertThatNewUserWasLogged(LoginInfo loginInfo) {
		assertThat(loginInfo.getUser().getUserId()).isNotNull();
		assertThat(loginInfo.getLatestCatalogId()).isEqualTo(catalogId.concat("-").concat(CAMPAIGN_NAME_SEGMENT_TAG));
		assertThat(loginInfo.getMmr()).isEqualTo(MMR);
		assertThat(loginInfo.getPassword()).isNull();
		assertThat(loginInfo.getPlayer().getUserId()).isNotNull();
		assertThat(loginInfo.getPlayer().getCatalogId()).isEqualTo(catalogId);
	}

	private PlayerCreationDomainService aPlayerCreationDomainService(Catalog catalog, FixedServerTimeProvider timeProvider) {
		PlayerCreationDomainService playerCreationDomainService = mock(PlayerCreationDomainService.class);
		when(playerCreationDomainService.createPlayer(USER_ID, new ABTag(CAMPAIGN_NAME_SEGMENT_TAG), catalog, timeProvider.getTimeNowAsSeconds()))
				.thenReturn(player);
		return playerCreationDomainService;
	}

	private ABTesterService anAbTesterService() {
		ABTesterService abTesterService = mock(ABTesterService.class);
		when(abTesterService.findUserTag(USER_ID)).thenReturn(Optional.of("gameId-".concat(CAMPAIGN_NAME_SEGMENT_TAG)));
		return abTesterService;
	}

	private PlayerWinRateRepository aPlayerWinRateRepository() {
		PlayerWinRateRepository repository = mock(PlayerWinRateRepository.class);
		when(repository.findOrCrateDefault(anyString())).thenReturn(new PlayerWinRate(LOGIN_ID, 2, 4, 1, MMR));
		return repository;
	}

	private CatalogAction aCatalogAction(Catalog catalog) {
		CatalogAction catalogAction = mock(CatalogAction.class);
		when(catalogAction.getCatalogForUser(any(ABTag.class))).thenReturn(catalog);
		return catalogAction;
	}

	private UserRepository anEmptyUserRepository() {
		return mock(UserRepository.class);
	}

	private PlayerRepository aPlayerRepository() {
		return mock(PlayerRepository.class);
	}

	private RefreshSpecialOfferBoardDomainService aRefreshSpecialOfferBoardDomainService(FixedServerTimeProvider timeProvider) {
		return new RefreshSpecialOfferBoardDomainService(new InMemorySpecialOfferBoardRepository(timeProvider));
	}

}
