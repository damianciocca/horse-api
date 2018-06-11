package com.etermax.spacehorse.core.player.resource;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.mock.MockUtils;

public class PlayerResourceTest {

	@Test
	public void testGetPlayer() {
		PlayerAction playerAction = mock(PlayerAction.class);
		Player player = mock(Player.class);
		when(playerAction.findByLoginId(anyString())).thenReturn(Optional.of(player));
		PlayerResource playerResource = new PlayerResource(playerAction);
		String loginId = "loginId";
		HttpServletRequest transport = MockUtils.mockHttpServletRequest(loginId);

		try {
			Response response = playerResource.getPlayer(transport);

			assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetPlayerWhenPlayerIsEmpty() {
		PlayerAction playerAction = mock(PlayerAction.class);
		when(playerAction.findByLoginId(anyString())).thenReturn(Optional.empty());
		PlayerResource playerResource = new PlayerResource(playerAction);
		String loginId = "loginId";
		HttpServletRequest transport = MockUtils.mockHttpServletRequest(loginId);

		try {
			Response response = playerResource.getPlayer(transport);

			assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
