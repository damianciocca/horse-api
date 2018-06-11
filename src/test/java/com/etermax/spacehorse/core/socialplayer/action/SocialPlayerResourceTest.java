package com.etermax.spacehorse.core.socialplayer.action;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.player.model.deck.Deck;
import com.etermax.spacehorse.core.player.model.progress.PlayerProgress;
import com.etermax.spacehorse.core.player.model.stats.MatchStats;
import com.etermax.spacehorse.core.player.model.stats.PlayerStats;
import com.etermax.spacehorse.core.player.resource.response.player.deck.DeckResponse;
import com.etermax.spacehorse.core.player.resource.response.player.progress.PlayerProgressResponse;
import com.etermax.spacehorse.core.player.resource.response.player.stats.PlayerStatsResponse;
import com.etermax.spacehorse.core.socialplayer.model.SocialPlayer;
import com.etermax.spacehorse.core.socialplayer.resource.SocialPlayerResource;
import com.etermax.spacehorse.core.socialplayer.resource.request.GetSocialPlayerRequest;
import com.etermax.spacehorse.core.socialplayer.resource.response.GetSocialPlayerResponse;
import com.etermax.spacehorse.core.socialplayer.resource.response.SocialPlayerResponse;
import com.etermax.spacehorse.mock.MockUtils;

public class SocialPlayerResourceTest {
	private static final String SOCIAL_PLAYER_ID = "socialPlayerId";
	private static final String PLAYER_ID = "playerId";
	private static final String SOCIAL_PLAYER_NAME = "srcres";
	private static final int SOCIAL_PLAYER_MMR = 100;
	private static final int SOCIAL_PLAYER_MAP_NUMBER = 13;
	private static final String SOCIAL_PLAYER_SELECTED_CAPTAIN_ID = "captain_rex";
	private final GetSocialPlayerAction getSocialPlayerActionMock = mock(GetSocialPlayerAction.class);
	private final SocialPlayer socialPlayerMock = mock(SocialPlayer.class);
	private Deck deckMock = mock(Deck.class);
	private PlayerProgress playerProgressMock = mock(PlayerProgress.class);
	private PlayerStats playerStatsMock = mock(PlayerStats.class);
	private MatchStats matchStatsMock = mock(MatchStats.class);
	private SocialPlayerResource socialPlayerResource;
	private GetSocialPlayerRequest getSocialPlayerRequest;
	private HttpServletRequest httpServletRequestMock;
	private Response response;
	private GetSocialPlayerResponse getSocialPlayerResponse;

	@Before
	public void setUp() {
		when(socialPlayerMock.getUserId()).thenReturn(SOCIAL_PLAYER_ID);
		when(socialPlayerMock.getName()).thenReturn(SOCIAL_PLAYER_NAME);
		when(socialPlayerMock.getDeck()).thenReturn(deckMock);
		when(socialPlayerMock.getProgress()).thenReturn(playerProgressMock);
		when(socialPlayerMock.getPlayerStats()).thenReturn(playerStatsMock);
		when(socialPlayerMock.getPlayerStats().getMatchStats()).thenReturn(matchStatsMock);
		when(socialPlayerMock.getMmr()).thenReturn(SOCIAL_PLAYER_MMR);
		when(socialPlayerMock.getMapNumber()).thenReturn(SOCIAL_PLAYER_MAP_NUMBER);
		when(socialPlayerMock.getSelectedCaptainId()).thenReturn(SOCIAL_PLAYER_SELECTED_CAPTAIN_ID);
	}

	@Test
	public void gettingSocialPlayerResponseWithAnExistingIdShouldResponseOk() {
		givenASocialPlayerResource(Optional.of(socialPlayerMock));
		givenASocialPlayerRequest();

		whenGetSocialPlayerResponse();

		thenSocialPlayerResponseWasOk();
	}

	@Test
	public void gettingSocialPlayerResponseWithANonExistingIdShouldResponseNotFound() {
		givenASocialPlayerResource(Optional.empty());
		givenASocialPlayerRequest();

		whenGetSocialPlayerResponse();

		thenSocialPlayerResponseWasPreconditionFailed();
	}

	private void givenASocialPlayerResource(Optional<SocialPlayer> socialPlayer) {
		when(getSocialPlayerActionMock.getSocialPlayer(SOCIAL_PLAYER_ID)).thenReturn(socialPlayer);
		socialPlayerResource = new SocialPlayerResource(getSocialPlayerActionMock);
	}

	private void givenASocialPlayerRequest() {
		getSocialPlayerRequest = new GetSocialPlayerRequest(SOCIAL_PLAYER_ID);
		httpServletRequestMock = MockUtils.mockHttpServletRequest(PLAYER_ID);
	}

	private void whenGetSocialPlayerResponse() {
		response = socialPlayerResource.getSocialPlayer(httpServletRequestMock, getSocialPlayerRequest);
		getSocialPlayerResponse = (GetSocialPlayerResponse) response.getEntity();
	}

	private void thenSocialPlayerResponseWasOk() {
		assertEquals(response.getStatusInfo(), Response.Status.OK);
		assertEquals(getSocialPlayerResponse.getSocialPlayer().getUserId(), socialPlayerMock.getUserId());
		assertEquals(getSocialPlayerResponse.getSocialPlayer().getName(), socialPlayerMock.getName());
		assertEquals(getSocialPlayerResponse.getSocialPlayer().getDeck(), new DeckResponse(socialPlayerMock.getDeck()));
		assertEquals(getSocialPlayerResponse.getSocialPlayer().getProgress(), new PlayerProgressResponse(socialPlayerMock.getProgress()));
		assertEquals(getSocialPlayerResponse.getSocialPlayer().getPlayerStats(), new PlayerStatsResponse(socialPlayerMock.getPlayerStats()));
		assertEquals(getSocialPlayerResponse.getSocialPlayer().getMmr(), socialPlayerMock.getMmr());
		assertEquals(getSocialPlayerResponse.getSocialPlayer().getMapNumber(), socialPlayerMock.getMapNumber());
		assertEquals(getSocialPlayerResponse.getSocialPlayer().getSelectedCaptainId(), socialPlayerMock.getSelectedCaptainId());
	}

	private void thenSocialPlayerResponseWasPreconditionFailed() {
		assertEquals(response.getStatusInfo(), Response.Status.NOT_FOUND);
	}
}