package com.etermax.spacehorse.core.player.resource;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.player.action.chest.FinishOpeningChestAction;
import com.etermax.spacehorse.core.player.action.chest.SpeedupOpeningChestAction;
import com.etermax.spacehorse.core.player.action.chest.StartOpeningChestAction;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.resource.request.FinishOpeningChestRequest;
import com.etermax.spacehorse.core.player.resource.request.SpeedupOpeningChestRequest;
import com.etermax.spacehorse.core.player.resource.request.StartOpeningChestRequest;
import com.etermax.spacehorse.core.player.resource.response.FinishOpeningChestResponse;
import com.etermax.spacehorse.core.player.resource.response.SpeedupOpeningChestResponse;
import com.etermax.spacehorse.core.player.resource.response.StartOpeningChestResponse;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;

import io.swagger.annotations.Api;

@Path("/v1/player/chest")
@Api(value = "/chest", tags = "Chest")
public class PlayerChestResource {

	public static final String LOGIN_ID_HEADER = "Login-Id";
	private final StartOpeningChestAction startOpeningChestAction;
	private final FinishOpeningChestAction finishOpeningChestAction;
	private final SpeedupOpeningChestAction speedupOpeningChest;

	public PlayerChestResource(StartOpeningChestAction startOpeningChestAction, FinishOpeningChestAction finishOpeningChestAction,
			SpeedupOpeningChestAction speedupOpeningChest) {
		this.startOpeningChestAction = startOpeningChestAction;
		this.finishOpeningChestAction = finishOpeningChestAction;
		this.speedupOpeningChest = speedupOpeningChest;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("startOpening")
	public Response postChestStartOpening(@Context HttpServletRequest request, StartOpeningChestRequest startOpeningChestRequest) {
		String playerId = request.getHeader(LOGIN_ID_HEADER);
		Chest chest = startOpeningChestAction.start(playerId, startOpeningChestRequest.getChestId());
		return Response.ok(new StartOpeningChestResponse(new ChestResponse(chest))).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("finishOpening")
	public Response postChestFinishOpening(@Context HttpServletRequest request, FinishOpeningChestRequest finishOpeningChestRequest) {
		String playerId = request.getHeader(LOGIN_ID_HEADER);
		List<RewardResponse> rewards = finishOpeningChestAction.finish(playerId, finishOpeningChestRequest.getChestId());
		return Response.ok(new FinishOpeningChestResponse(rewards)).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("speedupOpening")
	public Response postChestSpeedupOpening(@Context HttpServletRequest request, SpeedupOpeningChestRequest speedupOpeningChestRequest) {
		String playerId = request.getHeader(LOGIN_ID_HEADER);
		SpeedupOpeningChestResponse response = speedupOpeningChest.speedup(playerId, speedupOpeningChestRequest.getChestId());
		return Response.ok(response).build();
	}

}
