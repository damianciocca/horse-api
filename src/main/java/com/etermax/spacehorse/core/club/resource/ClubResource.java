package com.etermax.spacehorse.core.club.resource;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.authenticator.model.Credentials;
import com.etermax.spacehorse.core.authenticator.resource.UserAuthenticator;
import com.etermax.spacehorse.core.club.response.CheckSessionResponse;
import com.etermax.spacehorse.core.club.response.ClubInfoResponse;
import com.etermax.spacehorse.core.club.request.CheckSessionRequest;
import com.etermax.spacehorse.core.club.response.PlayerInfoResponse;
import com.etermax.spacehorse.core.club.response.UsersInfoBulkResponse;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.PlayerInfo;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;

@Path("/v1")
@Api(value = "/userInfo", tags = "Club")
public class ClubResource {

	private UserAuthenticator userAuthenticator;
	private PlayerAction playerAction;

	public ClubResource(UserAuthenticator userAuthenticator, PlayerAction playerAction) {
		this.userAuthenticator = userAuthenticator;
		this.playerAction = playerAction;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/users/query")
	public Response getUserInfo(@QueryParam("ids") String userIdsParam) {
		boolean success = true;

		String[] userIdsSplitted = userIdsParam.split(",");
		List<String> userIds = Lists.newArrayList(userIdsSplitted);

		List<PlayerInfoResponse> playerInfoResponses = playerAction.findPlayersInfo(userIds).stream().map(toPlayerInfoResponse())
				.collect(Collectors.toList());

		if (playerInfoResponses.isEmpty()) {
			playerInfoResponses = Collections.emptyList();
		}

		UsersInfoBulkResponse usersInfoBulkResponse = new UsersInfoBulkResponse(playerInfoResponses);
		return Response.ok(new ClubInfoResponse(success, usersInfoBulkResponse)).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/check-session")
	public Response validateUserSession(CheckSessionRequest checkSessionRequest) {
		String userId = checkSessionRequest.getUserId();
		String cookie = checkSessionRequest.getCookie();
		boolean success = true;
		boolean content;

		Credentials credentials = new Credentials(userId, cookie);
		try {
			userAuthenticator.authenticate(credentials);
			content = true;
		} catch (Exception e) {
			 content = false;
		}

		CheckSessionResponse checkSessionResponse = new CheckSessionResponse(content);
		return Response.ok(new ClubInfoResponse(success, checkSessionResponse)).build();
	}

	private Function<PlayerInfo, PlayerInfoResponse> toPlayerInfoResponse() {
		return userInfo -> new PlayerInfoResponse(userInfo
				.getUserId(), userInfo.getUserName());
	}
}
