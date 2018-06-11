package com.etermax.spacehorse.core.login.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.achievements.repository.InMemoryAchievementCollectionRepository;
import com.etermax.spacehorse.core.captain.InMemoryCaptainCollectionRepository;
import com.etermax.spacehorse.core.error.InvalidTokenException;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.login.action.LinkGooglePlayAction;
import com.etermax.spacehorse.core.login.resource.request.GetPlayerFromGooglePlayRequest;
import com.etermax.spacehorse.core.login.resource.request.PlayServicesAuthRequestIncome;
import com.etermax.spacehorse.core.login.resource.response.PlayerResponseFactory;
import com.etermax.spacehorse.core.login.resource.response.PlayerSmallResponse;
import com.etermax.spacehorse.core.login.resource.response.ResponseGetPlayerSmall;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.InMemorySpecialOfferBoardRepository;
import com.etermax.spacehorse.core.user.action.UserAction;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.NewPlayerConfigurationBuilder;

public class PlayServicesLoginResourceTest {

	private FixedServerTimeProvider serverTimeProvider;
	private PlayerResponseFactory playerResponseFactory;

	@Before
	public void setUp() throws Exception {
		serverTimeProvider = new FixedServerTimeProvider();
		PlayerLeagueService playerLeagueService = getPlayerSeasonsService();
		playerResponseFactory = new PlayerResponseFactory(new InMemoryQuestBoardRepository(serverTimeProvider),
				new InMemorySpecialOfferBoardRepository(serverTimeProvider), new InMemoryCaptainCollectionRepository(),
				new InMemoryAchievementCollectionRepository(), playerLeagueService);
	}

	@Test
	public void testLinkGooglePlayServices() {
		String loginId = "loginId";
		String token = "token";
		LinkGooglePlayAction linkAction = mock(LinkGooglePlayAction.class);
		UserAction userAction = MockUtils.mockUserAction(loginId, Platform.ANDROID);
		when(linkAction.linkWithPlayServices(anyString(), anyString())).thenReturn(loginId);
		PlayServicesLoginResource playServicesLoginResource = new PlayServicesLoginResource(linkAction, userAction, playerResponseFactory);
		HttpServletRequest transport = MockUtils.mockHttpServletRequest(loginId);
		PlayServicesAuthRequestIncome playServicesAuthRequestIncome = new PlayServicesAuthRequestIncome(token);
		Response response = playServicesLoginResource.linkGooglePlayServices(transport, playServicesAuthRequestIncome);

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getEntity()).hasFieldOrPropertyWithValue("password", "");
	}

	@Test
	public void testLinkGooglePlayServicesWithNewLogin() {
		String loginId = "loginId";
		String anotherLogin = "anotherLogin";
		String token = "token";
		LinkGooglePlayAction linkAction = mock(LinkGooglePlayAction.class);
		UserAction userAction = MockUtils.mockUserAction(loginId, Platform.ANDROID);
		when(linkAction.linkWithPlayServices(anyString(), anyString())).thenReturn(anotherLogin);
		PlayServicesLoginResource playServicesLoginResource = new PlayServicesLoginResource(linkAction, userAction, playerResponseFactory);
		HttpServletRequest transport = MockUtils.mockHttpServletRequest(loginId);
		PlayServicesAuthRequestIncome playServicesAuthRequestIncome = new PlayServicesAuthRequestIncome(token);
		Response response = playServicesLoginResource.linkGooglePlayServices(transport, playServicesAuthRequestIncome);

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getEntity()).extracting("password").isNotEmpty();
	}

	@Test
	public void testWhenLinkFails() {
		String loginId = "loginId";
		String token = "token";
		LinkGooglePlayAction linkAction = mock(LinkGooglePlayAction.class);
		UserAction userAction = MockUtils.mockUserAction(loginId, Platform.ANDROID);
		when(linkAction.linkWithPlayServices(anyString(), anyString())).thenThrow(InvalidTokenException.class);
		PlayServicesLoginResource playServicesLoginResource = new PlayServicesLoginResource(linkAction, userAction, playerResponseFactory);
		HttpServletRequest transport = MockUtils.mockHttpServletRequest(loginId);
		PlayServicesAuthRequestIncome playServicesAuthRequestIncome = new PlayServicesAuthRequestIncome(token);
		Response response = playServicesLoginResource.linkGooglePlayServices(transport, playServicesAuthRequestIncome);

		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
	}

	@Test
	public void testQueryGooglePlayServices() {
		String token = "token";
		LinkGooglePlayAction linkAction = mock(LinkGooglePlayAction.class);
		String loginId = "loginId";
		NewPlayerConfiguration newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
		Player player = Player.buildNewPlayer(loginId, ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
		when(linkAction.getPlayerFromPlayToken(token)).thenReturn(player);
		UserAction userAction = mock(UserAction.class);
		PlayServicesLoginResource playServicesLoginResource = new PlayServicesLoginResource(linkAction, userAction, playerResponseFactory);

		GetPlayerFromGooglePlayRequest request = new GetPlayerFromGooglePlayRequest(token);
		Response response = playServicesLoginResource.queryGooglePlayServices(request);
		ResponseGetPlayerSmall hRResponse = (ResponseGetPlayerSmall) response.getEntity();
		Boolean found = hRResponse.getFound();
		String userId = hRResponse.getUserId();
		PlayerSmallResponse player1 = hRResponse.getPlayer();

		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		assertThat(found).isEqualTo(true);
		assertThat(userId).isEqualTo(loginId);
		assertThat(player1).isNotNull();
	}

	@Test
	public void testQueryGooglePlayServicesWithPlayerNotFound() {
		// Given
		String token = "token";
		LinkGooglePlayAction linkAction = mock(LinkGooglePlayAction.class);
		when(linkAction.getPlayerFromPlayToken(token)).thenReturn(null);
		UserAction userAction = mock(UserAction.class);
		PlayServicesLoginResource playServicesLoginResource = new PlayServicesLoginResource(linkAction, userAction, playerResponseFactory);
		GetPlayerFromGooglePlayRequest request = new GetPlayerFromGooglePlayRequest(token);

		// When
		Response response = playServicesLoginResource.queryGooglePlayServices(request);
		ResponseGetPlayerSmall hRResponse = (ResponseGetPlayerSmall) response.getEntity();
		Boolean found = hRResponse.getFound();
		String userId = hRResponse.getUserId();
		PlayerSmallResponse player1 = hRResponse.getPlayer();

		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		assertThat(found).isEqualTo(false);
		assertThat(userId).isNullOrEmpty();
		assertThat(player1).isNull();
	}

	@Test
	public void testQueryGooglePlayServicesWhenFails() {
		String token = "token";
		LinkGooglePlayAction linkAction = mock(LinkGooglePlayAction.class);
		when(linkAction.getPlayerFromPlayToken(token)).thenThrow(InvalidTokenException.class);
		UserAction userAction = mock(UserAction.class);
		PlayServicesLoginResource playServicesLoginResource = new PlayServicesLoginResource(linkAction, userAction, playerResponseFactory);

		GetPlayerFromGooglePlayRequest request = new GetPlayerFromGooglePlayRequest(token);
		//        Response response = playServicesLoginResource.queryGooglePlayServices(request);

		Throwable thrown = catchThrowable(() -> playServicesLoginResource.queryGooglePlayServices(request));

		assertThat(thrown).isInstanceOf(InvalidTokenException.class);

	}

	private PlayerLeagueService getPlayerSeasonsService() {
		PlayerLeagueService playerLeagueService = mock(PlayerLeagueService.class);
		when(playerLeagueService.addOrUpdatePlayerSeasons(any(), any())).thenReturn(Optional.empty());
		return playerLeagueService;
	}
}
