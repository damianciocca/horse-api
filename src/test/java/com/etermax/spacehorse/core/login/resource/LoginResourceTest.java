package com.etermax.spacehorse.core.login.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.achievements.repository.InMemoryAchievementCollectionRepository;
import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.captain.InMemoryCaptainCollectionRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.login.action.LoginExistentUserAction;
import com.etermax.spacehorse.core.login.action.LoginInfo;
import com.etermax.spacehorse.core.login.action.LoginNewUserAction;
import com.etermax.spacehorse.core.login.model.ClientVersionValidator;
import com.etermax.spacehorse.core.login.resource.request.LoginRequest;
import com.etermax.spacehorse.core.login.resource.response.LoginResponseFactory;
import com.etermax.spacehorse.core.login.resource.response.PlayerResponseFactory;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.PlayerBuilder;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.reconnect.resource.ManageServerStatusAction;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.InMemorySpecialOfferBoardRepository;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;
import com.etermax.spacehorse.mock.MockCatalog;

public class LoginResourceTest {

	private static final String LOGIN_ID = "loginId";
	private FixedServerTimeProvider timeProvider;
	private Catalog catalog;
	private int expectedClientVersion;

	@Before
	public void setUp() throws Exception {
		timeProvider = new FixedServerTimeProvider();
		catalog = MockCatalog.buildCatalog();
		expectedClientVersion = 2;
	}

	@Test
	public void whenReceivesARequestFromExistentUserReturnsAValidLoginResponse() {
		// given
		LoginNewUserAction loginNewUserAction = mock(LoginNewUserAction.class);
		LoginExistentUserAction loginExistentUserAction = aLoginExistingUserAction();
		LoginRequest loginRequest = new LoginRequest(LOGIN_ID, "password", expectedClientVersion, Platform.ANDROID);
		ManageServerStatusAction manageServerStatusAction = aManageServerStatusAction();
		LoginResource loginResource = aLoginResource(loginExistentUserAction, loginNewUserAction, expectedClientVersion, manageServerStatusAction);

		// When
		Response loginResponse = loginResource.login(loginRequest);
		// Given
		assertThat(loginResponse).isNotNull();
	}

	@Test
	public void whenReceivesARequestFromANewUserReturnsAValidLoginResponse() {
		// given
		LoginExistentUserAction loginExistentUserAction = mock(LoginExistentUserAction.class);
		LoginNewUserAction loginNewUserAction = aLoginNewUserAction();
		LoginRequest loginRequest = new LoginRequest(expectedClientVersion, Platform.ANDROID);
		ManageServerStatusAction manageServerStatusAction = aManageServerStatusAction();
		LoginResource loginResource = aLoginResource(loginExistentUserAction, loginNewUserAction, expectedClientVersion, manageServerStatusAction);
		// When
		Response loginResponse = loginResource.login(loginRequest);
		// Then
		assertThat(loginResponse).isNotNull();
	}

	private ManageServerStatusAction aManageServerStatusAction() {
		ManageServerStatusAction manageServerStatusAction = mock(ManageServerStatusAction.class);
		when(manageServerStatusAction.isServerUnderMaintenance()).thenReturn(false);
		return manageServerStatusAction;
	}

	private LoginNewUserAction aLoginNewUserAction() {
		LoginNewUserAction loginNewUserAction = mock(LoginNewUserAction.class);
		LoginInfo loginInfo = aLoginInfo();
		when(loginNewUserAction.login(Platform.ANDROID)).thenReturn(loginInfo);
		when(loginInfo.getPlayerSeasons()).thenReturn(Optional.empty());
		return loginNewUserAction;
	}

	private LoginExistentUserAction aLoginExistingUserAction() {
		LoginExistentUserAction loginExistentUserAction = mock(LoginExistentUserAction.class);
		LoginInfo loginInfo = aLoginInfo();
		when(loginExistentUserAction.login(anyString(), anyString(), any())).thenReturn(loginInfo);
		when(loginInfo.getPlayerSeasons()).thenReturn(Optional.empty());
		return loginExistentUserAction;
	}

	private LoginInfo aLoginInfo() {
		LoginInfo loginInfo = mock(LoginInfo.class);
		when(loginInfo.getUser()).thenReturn(givenAPlayerUser(LOGIN_ID));
		when(loginInfo.getLatestCatalogId()).thenReturn(catalog.getCatalogId());
		when(loginInfo.getCatalog()).thenReturn(catalog);
		when(loginInfo.getPlayer()).thenReturn(givenAPlayer(LOGIN_ID, catalog.getCatalogId()));
		return loginInfo;
	}

	private LoginResource aLoginResource(LoginExistentUserAction loginExistentUserAction, LoginNewUserAction loginNewUserAction,
			int expectedClientVersion, ManageServerStatusAction manageServerStatusAction) {
		PlayerLeagueService playerLeagueService = getPlayerSeasonsService();
		PlayerResponseFactory playerResponseFactory = new PlayerResponseFactory(new InMemoryQuestBoardRepository(timeProvider),
				new InMemorySpecialOfferBoardRepository(timeProvider), new InMemoryCaptainCollectionRepository(),
				new InMemoryAchievementCollectionRepository(), playerLeagueService);
		LoginResponseFactory loginResponseFactory = new LoginResponseFactory("mmServerURL", timeProvider, playerResponseFactory);
		return new LoginResource(loginExistentUserAction, loginNewUserAction, new ClientVersionValidator(expectedClientVersion),
				manageServerStatusAction, loginResponseFactory);
	}

	private PlayerLeagueService getPlayerSeasonsService() {
		PlayerLeagueService playerLeagueService = mock(PlayerLeagueService.class);
		when(playerLeagueService.addOrUpdatePlayerSeasons(any(), any())).thenReturn(Optional.empty());
		return playerLeagueService;
	}

	private Player givenAPlayer(String loginId, String catalogId) {
		return new PlayerBuilder().setUserId(loginId).setCatalogId(catalogId).setName("Name").createPlayer();
	}

	private User givenAPlayerUser(String loginId) {
		return new User(loginId, "password", Role.PLAYER, Platform.ANDROID);
	}

}