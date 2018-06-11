package com.etermax.spacehorse.core.login.resource;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.authenticator.model.PasswordGenerator;
import com.etermax.spacehorse.core.login.action.LinkGameCenterAction;
import com.etermax.spacehorse.core.login.resource.request.GameCenterAuthRequestIncome;
import com.etermax.spacehorse.core.login.resource.request.GetPlayerFromGameCenterRequest;
import com.etermax.spacehorse.core.login.resource.response.PlayerResponseFactory;
import com.etermax.spacehorse.core.login.resource.response.PlayerSmallResponse;
import com.etermax.spacehorse.core.login.resource.response.ResponseFromLoginApi;
import com.etermax.spacehorse.core.login.resource.response.ResponseGetPlayerSmall;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.user.action.UserAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/v1/")
@Api(value = "/v1/gamecenter", tags = "Login")
public class GameCenterLoginResource {

	private static final Logger logger = LoggerFactory.getLogger(GameCenterLoginResource.class);

	private LinkGameCenterAction linkAction;
	private UserAction userAction;
	private PlayerResponseFactory playerResponseFactory;

	public GameCenterLoginResource(LinkGameCenterAction linkAction, UserAction userAction, PlayerResponseFactory playerResponseFactory) {
		this.linkAction = linkAction;
		this.userAction = userAction;
		this.playerResponseFactory = playerResponseFactory;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/link_game_center")
	@ApiOperation("Returns login information with Game Center.")
	public Response linkGameCenter(@Context HttpServletRequest request, GameCenterAuthRequestIncome loginRequest) {
		String loginId = request.getHeader("Login-Id");
		String gameCenterId = loginRequest.getGameCenterId();
		String linkedLoginId = linkAction.linkWithGameCenter(gameCenterId, loginId);
		String newPassword = "";
		if (!linkedLoginId.equals(loginId)) {
			newPassword = PasswordGenerator.generate();
			userAction.saveNewPassForUser(linkedLoginId, newPassword);
		}
		ResponseFromLoginApi responseFromLoginApi = new ResponseFromLoginApi(linkedLoginId, newPassword);
		return Response.ok(responseFromLoginApi).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/query_game_center2")
	@ApiOperation("Returns a Player when a GameCenterId is provided")
	public Response queryGameCenter(GetPlayerFromGameCenterRequest queryRequest) {

		String gameCenterId = queryRequest.getGameCenterId();

		Player player = linkAction.getPlayerFromPlayGameCenterId(gameCenterId);
		Boolean found = player != null;
		String userId = player != null ? player.getUserId() : null;
		ResponseGetPlayerSmall responseGetPlayer = new ResponseGetPlayerSmall(buildPlayerResponseSmall(player), found, userId);
		return Response.ok(responseGetPlayer).build();
	}

	private PlayerSmallResponse buildPlayerResponseSmall(Player player) {
		if (player == null)
			return null;
		return playerResponseFactory.createSmallFrom(player);
	}
}
