package com.etermax.spacehorse.core.player.resource;

import com.etermax.spacehorse.core.common.resource.response.ErrorResponse;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Gender;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.resource.request.ChangeProfileRequest;
import com.etermax.spacehorse.core.player.resource.request.StartTutorialRequest;
import com.etermax.spacehorse.core.player.resource.response.ChangeProfileResponse;
import com.etermax.spacehorse.core.player.resource.response.FinishTutorialResponse;
import com.etermax.spacehorse.core.player.resource.response.StartTutorialResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URISyntaxException;
import java.util.Optional;

@Path("/v1")
@Api(value = "/player", tags = "Player")
public class PlayerResource {

	private PlayerAction playerAction;

	public PlayerResource(PlayerAction playerAction) {
		this.playerAction = playerAction;
	}

	@GET
	@RolesAllowed({ "PLAYER" })
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/player")
	@ApiOperation("Returns the player for the current login-id and session-token")
	public Response getPlayer(@Context HttpServletRequest request) throws URISyntaxException {
		Optional<Player> playerOpt = playerAction.findByLoginId(request.getHeader("Login-Id"));
		if (playerOpt.isPresent()) {
			return Response.ok(playerOpt.get()).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/player/profile")
	@ApiOperation("Sets the player profile for the current login-id and session-token")
	public Response putPlayerProfile(@Context HttpServletRequest request, ChangeProfileRequest changeProfileRequest) {
		Optional<Player> playerOpt = playerAction.findByLoginId(request.getHeader("Login-Id"));
		if (!playerOpt.isPresent()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}

		String name = changeProfileRequest.getName();
		Gender gender = changeProfileRequest.getGender();
		int age = changeProfileRequest.getAge();
		playerAction.updatePlayerProfile(playerOpt.get(), name, gender, age);

		return Response.ok(new ChangeProfileResponse()).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/player/tutorial/start")
	@ApiOperation("Start a given tutorial.")
	public Response startTutorial(@Context HttpServletRequest servletRequest, StartTutorialRequest startTutorialRequest) {
		Optional<Player> playerOpt = playerAction.findByLoginId(servletRequest.getHeader("Login-Id"));
		String tutorialId = startTutorialRequest.getTutorialId();
		if (playerOpt.isPresent()) {
			Player player = playerOpt.get();
			playerAction.startTutorial(player, tutorialId);
			return Response.ok(new StartTutorialResponse()).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/player/tutorial/end")
	@ApiOperation("Starts a specific tutorial, given an id.")
	public Response finishTutorial(@Context HttpServletRequest servletRequest) {
		Optional<Player> playerOpt = playerAction.findByLoginId(servletRequest.getHeader("Login-Id"));
		if (playerOpt.isPresent()) {
			Player player = playerOpt.get();
			playerAction.finishActiveTutorial(player);
			return Response.ok(new FinishTutorialResponse()).build();
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}

}
