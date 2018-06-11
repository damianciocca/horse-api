package com.etermax.spacehorse.core.login.resource;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.login.action.LinkGameCenterAction;
import com.etermax.spacehorse.core.login.resource.request.GameCenterAuthRequestIncome;
import com.etermax.spacehorse.core.login.resource.request.GetPlayerFromGameCenterRequest;
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

public class GameCenterLoginResourceTest {

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
	public void testLinkGameCenter() {
		String loginId = "loginId";
		String gameCenterID = "gameCenterID";
		LinkGameCenterAction linkAction = mock(LinkGameCenterAction.class);
		UserAction userAction = MockUtils.mockUserAction(loginId, Platform.IOS);
		when(linkAction.linkWithGameCenter(anyString(), anyString())).thenReturn(loginId);
		GameCenterLoginResource playServicesLoginResource = new GameCenterLoginResource(linkAction, userAction, playerResponseFactory);
		HttpServletRequest transport = MockUtils.mockHttpServletRequest(loginId);
		GameCenterAuthRequestIncome gameCenterAuthRequestIncome = new GameCenterAuthRequestIncome(gameCenterID);
		Response response = playServicesLoginResource.linkGameCenter(transport, gameCenterAuthRequestIncome);

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getEntity()).hasFieldOrPropertyWithValue("password", "");
	}

	@Test
	public void testLinkGameCenterWithNewLogin() {
		String loginId = "loginId";
		String anotherLogin = "anotherLogin";
		String gameCenterId = "gameCenterId";
		LinkGameCenterAction linkAction = mock(LinkGameCenterAction.class);
		UserAction userAction = MockUtils.mockUserAction(loginId, Platform.IOS);
		when(linkAction.linkWithGameCenter(anyString(), anyString())).thenReturn(anotherLogin);
		GameCenterLoginResource gameCenterLoginResource = new GameCenterLoginResource(linkAction, userAction, playerResponseFactory);
		HttpServletRequest transport = MockUtils.mockHttpServletRequest(loginId);
		GameCenterAuthRequestIncome gameCenterAuthRequestIncome = new GameCenterAuthRequestIncome(gameCenterId);
		Response response = gameCenterLoginResource.linkGameCenter(transport, gameCenterAuthRequestIncome);

		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(response.getEntity()).extracting("password").isNotEmpty();
	}

	@Test
	public void testQueryGameCenter() {
		String gameCenterId = "gameCenterId";
		LinkGameCenterAction linkAction = mock(LinkGameCenterAction.class);
		String loginId = "loginId";
		NewPlayerConfiguration newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
		Player player = Player.buildNewPlayer(loginId, ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
		when(linkAction.getPlayerFromPlayGameCenterId(gameCenterId)).thenReturn(player);
		UserAction userAction = mock(UserAction.class);
		GameCenterLoginResource gameCenterLoginResource = new GameCenterLoginResource(linkAction, userAction, playerResponseFactory);

		GetPlayerFromGameCenterRequest request = new GetPlayerFromGameCenterRequest(gameCenterId);
		Response response = gameCenterLoginResource.queryGameCenter(request);
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
	public void testQueryGameCenterWithPlayerNotFound() {
		// Given
		String gameCenterId = "gameCenterId";
		LinkGameCenterAction linkAction = mock(LinkGameCenterAction.class);
		when(linkAction.getPlayerFromPlayGameCenterId(gameCenterId)).thenReturn(null);
		UserAction userAction = mock(UserAction.class);
		GameCenterLoginResource gameCenterLoginResource = new GameCenterLoginResource(linkAction, userAction, playerResponseFactory);
		GetPlayerFromGameCenterRequest request = new GetPlayerFromGameCenterRequest(gameCenterId);

		// When
		Response response = gameCenterLoginResource.queryGameCenter(request);
		ResponseGetPlayerSmall hRResponse = (ResponseGetPlayerSmall) response.getEntity();
		Boolean found = hRResponse.getFound();
		String userId = hRResponse.getUserId();
		PlayerSmallResponse player1 = hRResponse.getPlayer();

		assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		assertThat(found).isEqualTo(false);
		assertThat(userId).isNullOrEmpty();
		assertThat(player1).isNull();
	}

	private PlayerLeagueService getPlayerSeasonsService() {
		PlayerLeagueService playerLeagueService = mock(PlayerLeagueService.class);
		when(playerLeagueService.addOrUpdatePlayerSeasons(any(), any())).thenReturn(Optional.empty());
		return playerLeagueService;
	}
}
