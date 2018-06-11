package com.etermax.spacehorse.core.login.action;

import com.etermax.spacehorse.core.login.model.GooglePlayValidator;
import com.etermax.spacehorse.core.login.repository.AuthRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.mock.MockUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LinkGooglePlayActionTest {

	private PlayerRepository playerRepository;

	private AuthRepository authRepository;

	private GooglePlayValidator googlePlayValidator;

	private LinkGooglePlayAction linkAction;

	private Integer gemsAmount;

	@Before
	public void setUp() {
		this.playerRepository = mock(PlayerRepository.class);
		this.authRepository = mock(AuthRepository.class);
		this.googlePlayValidator = mock(GooglePlayValidator.class);
		this.linkAction = new LinkGooglePlayAction(playerRepository, authRepository, googlePlayValidator);
		this.gemsAmount = 500;
	}

	@Test
	public void testLinkWithPlayServices() {
		String token = "token";
		String loginId = "loginId";
		String googlePlayId = "googlePlayId";
		when(googlePlayValidator.getGPGSIdWhenTokenIsVerified(token)).thenReturn(googlePlayId);
		when(authRepository.link(Platform.ANDROID,loginId, googlePlayId)).thenReturn(loginId);

		assertEquals(loginId, this.linkAction.linkWithPlayServices(token, loginId));
	}

	@Test
	public void testGetPlayerFromPlayToken() {
		String token = "token";
		String loginId = "loginId";
		String googlePlayId = "googlePlayId";
		Player player = MockUtils.mockPlayerWithGems(loginId, gemsAmount, playerRepository);
		player.setUserId(loginId);
		when(googlePlayValidator.getGPGSIdWhenTokenIsVerified(token)).thenReturn(googlePlayId);
		when(authRepository.findById(Platform.ANDROID, googlePlayId)).thenReturn(loginId);
		when(playerRepository.find(loginId)).thenReturn(player);

		Player retrieved = this.linkAction.getPlayerFromPlayToken(token);
		assertNotNull(retrieved);
		assertEquals(player.getUserId(), retrieved.getUserId());
	}

	@Test
	public void testUnableToGetPlayerFromPlayToken() {
		String token = "token";
		String loginId = "loginId";
		String googlePlayId = "googlePlayId";
		Player player = MockUtils.mockPlayerWithGems(loginId, gemsAmount, playerRepository);
		player.setUserId(loginId);
		when(googlePlayValidator.getGPGSIdWhenTokenIsVerified(token)).thenReturn(googlePlayId);
		when(authRepository.findById(Platform.ANDROID, googlePlayId)).thenReturn(null);
		when(playerRepository.find(loginId)).thenReturn(player);

		assertNull(this.linkAction.getPlayerFromPlayToken(token));
	}

}
