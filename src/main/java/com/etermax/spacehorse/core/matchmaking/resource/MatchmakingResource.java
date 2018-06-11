package com.etermax.spacehorse.core.matchmaking.resource;

import java.util.concurrent.TimeUnit;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.configuration.NewRelicIgnoreTransaction;
import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalog;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.matchmaking.action.FriendlyMatchmakingAction;
import com.etermax.spacehorse.core.matchmaking.action.ChallengeMatchmakingAction;
import com.etermax.spacehorse.core.matchmaking.resource.request.FriendlyMatchmakingRequest;
import com.etermax.spacehorse.core.matchmaking.resource.request.MatchmakingRequest;
import com.etermax.spacehorse.core.player.action.PlayerAction;

@Path("/v1")
public class MatchmakingResource {

	private final ChallengeMatchmakingAction challengeMatchmakingAction;
	private final FriendlyMatchmakingAction friendlyMatchmakingAction;
	private final PlayerAction playerAction;

	public MatchmakingResource(ChallengeMatchmakingAction challengeMatchmakingAction, FriendlyMatchmakingAction friendlyMatchmakingAction, PlayerAction playerAction) {
		this.challengeMatchmakingAction = challengeMatchmakingAction;
		this.friendlyMatchmakingAction = friendlyMatchmakingAction;
		this.playerAction = playerAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresLatestCatalog
	@NewRelicIgnoreTransaction
	@Path("/matchmaking")
	public void matchmaking(@Context HttpServletRequest request,
			@Suspended final AsyncResponse asyncResponse,
			MatchmakingRequest matchmakingRequest) {
		if (matchmakingRequest == null || !matchmakingRequest.isValid()) {
			throw new ApiException(("Invalid request"));
		}
		asyncResponse.setTimeoutHandler(
				asyncResponse1 -> asyncResponse1.resume(
						Response.status(Response.Status.SERVICE_UNAVAILABLE)
								.entity("Operation time out.").build()));
		asyncResponse.setTimeout(25, TimeUnit.SECONDS);

		challengeMatchmakingAction.match(
				playerAction.findByLoginIdOrFail(request.getHeader("Login-Id")),
				asyncResponse, matchmakingRequest.getExtraData());
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresLatestCatalog
	@NewRelicIgnoreTransaction
	@Path("/friendlymatch")
	public void friendlymatch(@Context HttpServletRequest request,
			@Suspended final AsyncResponse asyncResponse,
			FriendlyMatchmakingRequest friendlyMatchmakingRequest) {
		if (friendlyMatchmakingRequest == null || !friendlyMatchmakingRequest.isValid()) {
			throw new ApiException(("Invalid request"));
		}
		asyncResponse.setTimeoutHandler(
				asyncResponse1 -> asyncResponse1.resume(
						Response.status(Response.Status.SERVICE_UNAVAILABLE)
								.entity("Operation time out.").build()));
		asyncResponse.setTimeout(25, TimeUnit.SECONDS);

		friendlyMatchmakingAction.match(
				playerAction.findByLoginIdOrFail(request.getHeader("Login-Id")), friendlyMatchmakingRequest.getOpponentId(),
				asyncResponse, friendlyMatchmakingRequest.getExtraData());
	}
}