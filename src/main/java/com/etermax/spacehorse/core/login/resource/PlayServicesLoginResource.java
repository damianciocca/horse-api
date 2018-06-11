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
import com.etermax.spacehorse.core.error.InvalidTokenException;
import com.etermax.spacehorse.core.login.action.LinkGooglePlayAction;
import com.etermax.spacehorse.core.login.resource.request.GetPlayerFromGooglePlayRequest;
import com.etermax.spacehorse.core.login.resource.request.PlayServicesAuthRequestIncome;
import com.etermax.spacehorse.core.login.resource.response.PlayerResponseFactory;
import com.etermax.spacehorse.core.login.resource.response.PlayerSmallResponse;
import com.etermax.spacehorse.core.login.resource.response.ResponseFromLoginApi;
import com.etermax.spacehorse.core.login.resource.response.ResponseGetPlayerSmall;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.user.action.UserAction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/v1/")
@Api(value = "/v1/gpgs", tags = "Login")
public class PlayServicesLoginResource {

	private static final Logger logger = LoggerFactory.getLogger(PlayServicesLoginResource.class);

	private LinkGooglePlayAction linkAction;
	private UserAction userAction;
	private PlayerResponseFactory playerResponseFactory;

	public PlayServicesLoginResource(LinkGooglePlayAction linkAction, UserAction userAction, PlayerResponseFactory playerResponseFactory) {
		this.linkAction = linkAction;
		this.userAction = userAction;
		this.playerResponseFactory = playerResponseFactory;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/link_google_play_services")
	@ApiOperation("Returns login information with Google Play Services.")
	public Response linkGooglePlayServices(@Context HttpServletRequest request, PlayServicesAuthRequestIncome loginRequest) {
		String loginId = request.getHeader("Login-Id");
		String token = loginRequest.getToken();

		if (isValidToken(token)) {

			String linkedLoginId;
			try {
				linkedLoginId = linkAction.linkWithPlayServices(token, loginId);
			} catch (InvalidTokenException e) {
				logger.error("Error validating token: " + token);
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			String newPassword = "";
			if (!linkedLoginId.equals(loginId)) {
				newPassword = PasswordGenerator.generate();
				userAction.saveNewPassForUser(linkedLoginId, newPassword);
			}

			ResponseFromLoginApi responseFromLoginApi = new ResponseFromLoginApi(linkedLoginId, newPassword);
			return Response.ok(responseFromLoginApi).build();

		} else {
			logger.error("Received empty gpgs token, returning same user");
			ResponseFromLoginApi responseFromLoginApi = new ResponseFromLoginApi(loginId, "");
			return Response.ok(responseFromLoginApi).build();
		}
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/query_google_play_services2")
	@ApiOperation("Returns a Player when a GPGS token is provided")
	public Response queryGooglePlayServices(GetPlayerFromGooglePlayRequest tokenRequest) {

		String token = tokenRequest.getToken();

		Player player = null;

		if (isValidToken(token)) {

			try {
				player = linkAction.getPlayerFromPlayToken(token);
			} catch (InvalidTokenException e) {
				logger.error("Error validating token: " + token);
				throw new InvalidTokenException("invalid token");
			}

		} else {
			logger.error("Received empty gpgs token, ignoring request and returning user not found");
		}

		boolean found = player != null;
		String userId = player != null ? player.getUserId() : null;
		ResponseGetPlayerSmall responseGetPlayer = new ResponseGetPlayerSmall(buildPlayerResponseSmall(player), found, userId);
		return Response.ok(responseGetPlayer).build();
	}

	private boolean isValidToken(String token) {
		return token != null && !token.isEmpty();
	}

	private PlayerSmallResponse buildPlayerResponseSmall(Player player) {
		if (player == null)
			return null;
		return playerResponseFactory.createSmallFrom(player);
	}
}
