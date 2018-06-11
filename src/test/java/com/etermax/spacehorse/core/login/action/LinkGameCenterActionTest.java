package com.etermax.spacehorse.core.login.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.login.repository.AuthRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.mock.MockUtils;

public class LinkGameCenterActionTest {

	private PlayerRepository playerRepository;

	private AuthRepository authRepository;

	private LinkGameCenterAction linkAction;

	private Integer gemsAmount;

	@Before
	public void setUp() {
		this.playerRepository = mock(PlayerRepository.class);
		this.authRepository = mock(AuthRepository.class);
		this.linkAction = new LinkGameCenterAction(playerRepository, authRepository);
		this.gemsAmount = 500;
	}

	@Test
	public void testLinkWithGameCenter() {
		String loginId = "loginId";
		String gameCenterId = "gameCenterId";
		when(authRepository.link(Platform.IOS,loginId, gameCenterId)).thenReturn(loginId);

		assertEquals(loginId, this.linkAction.linkWithGameCenter(gameCenterId, loginId));
	}

	@Test
	public void testGetPlayerFromGameCenterId() {
		String loginId = "loginId";
		String gameCenterId = "gameCenterId";
		Player player = MockUtils.mockPlayerWithGems(loginId, gemsAmount, playerRepository);
		player.setUserId(loginId);
		when(authRepository.findById(Platform.IOS, gameCenterId)).thenReturn(loginId);
		when(playerRepository.find(loginId)).thenReturn(player);

		Player retrieved = this.linkAction.getPlayerFromPlayGameCenterId(gameCenterId);
		assertNotNull(retrieved);
		assertEquals(player.getUserId(), retrieved.getUserId());
	}

	@Test
	public void testUnableToGetPlayerFromGameCenterId() {
		String loginId = "loginId";
		String gameCenterId = "gameCenterId";
		Player player = MockUtils.mockPlayerWithGems(loginId, gemsAmount, playerRepository);
		player.setUserId(loginId);
		when(authRepository.findById(Platform.IOS, gameCenterId)).thenReturn(null);
		when(playerRepository.find(loginId)).thenReturn(player);

		assertNull(this.linkAction.getPlayerFromPlayGameCenterId(gameCenterId));
	}

}
