package com.etermax.spacehorse.core.socialplayer.action;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.battle.repository.dynamo.PlayerWinRateDynamoRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.player.repository.dynamo.PlayerDynamoRepository;
import com.etermax.spacehorse.core.socialplayer.model.SocialPlayer;

public class GetSocialPlayerActionTest {

	public static final String LOGIN_ID = "loginId";
	public static final String NON_EXISTING_ID = "nonExistingId";
	private PlayerRepository playerRepositoryMock = mock(PlayerDynamoRepository.class);
	private PlayerWinRateRepository playerWinRateRepositoryMock = mock(PlayerWinRateDynamoRepository.class);
	private CaptainCollectionRepository captainCollectionRepositoryMock = mock(CaptainCollectionRepository.class);
	private Player playerMock = mock(Player.class);
	private PlayerWinRate playerWinRateMock = mock(PlayerWinRate.class);
	private CaptainsCollection captainsCollectionMock = mock(CaptainsCollection.class);
	private GetSocialPlayerAction getSocialPlayerAction;
	private Optional<SocialPlayer> socialPlayer;

	@Before
	public void setup() {
		when(playerMock.getUserId()).thenReturn(LOGIN_ID);
		when(playerWinRateRepositoryMock.findOrCrateDefault(anyString())).thenReturn(playerWinRateMock);
		when(captainCollectionRepositoryMock.findOrDefaultBy(playerMock)).thenReturn(captainsCollectionMock);
	}

	@Test
	public void gettingSocialPlayerWithAExistingIdShouldReturnASocialPlayer() {
		givenAGetSocialPlayerAction(playerMock);

		whenGetSocialPlayer(LOGIN_ID);

		thenSocialPlayerIsNotEmpty();
	}

	@Test
	public void gettingSocialPlayerWithANonExistingIdShouldNotReturnASocialPlayer() {
		givenAGetSocialPlayerAction(null);

		whenGetSocialPlayer(NON_EXISTING_ID);

		thenSocialPlayerIsEmpty();
	}

	private void givenAGetSocialPlayerAction(Player player) {
		when(playerRepositoryMock.find(anyString())).thenReturn(player);
		getSocialPlayerAction = new GetSocialPlayerAction(playerRepositoryMock, playerWinRateRepositoryMock, captainCollectionRepositoryMock);
	}

	private void whenGetSocialPlayer(String loginId) {
		socialPlayer = getSocialPlayerAction.getSocialPlayer(loginId);
	}

	private void thenSocialPlayerIsNotEmpty() {
		assertThat(socialPlayer).isNotEmpty();
	}

	private void thenSocialPlayerIsEmpty() {
		assertThat(socialPlayer).isEmpty();
	}
}