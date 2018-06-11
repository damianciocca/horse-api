package com.etermax.spacehorse.core.socialplayer.resource;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalog;
import com.etermax.spacehorse.core.socialplayer.action.GetSocialPlayerAction;
import com.etermax.spacehorse.core.socialplayer.model.SocialPlayer;
import com.etermax.spacehorse.core.socialplayer.resource.request.GetSocialPlayerRequest;
import com.etermax.spacehorse.core.socialplayer.resource.response.GetSocialPlayerResponse;
import com.etermax.spacehorse.core.socialplayer.resource.response.SocialPlayerResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/socialPlayer", tags = "Social Player")
public class SocialPlayerResource {
	private GetSocialPlayerAction getSocialPlayerAction;

	public SocialPlayerResource(GetSocialPlayerAction getSocialPlayerAction) {
		this.getSocialPlayerAction = getSocialPlayerAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequiresLatestCatalog
	@Path("/player/socialPlayer")
	@ApiOperation("Returns a social player by player Id.")
	public Response getSocialPlayer(@Context HttpServletRequest httpServletRequest, GetSocialPlayerRequest request) {
		Optional<SocialPlayer> socialPlayerOpt = getSocialPlayerAction.getSocialPlayer(request.getUserId());
		if (!socialPlayerOpt.isPresent()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		return Response.ok(new GetSocialPlayerResponse(new SocialPlayerResponse(socialPlayerOpt.get()))).build();
	}
}
