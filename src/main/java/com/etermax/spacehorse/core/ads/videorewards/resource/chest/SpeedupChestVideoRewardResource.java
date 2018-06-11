package com.etermax.spacehorse.core.ads.videorewards.resource.chest;

import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.ads.videorewards.actions.GetAvailableVideoRewardAction;
import com.etermax.spacehorse.core.ads.videorewards.actions.chest.ClaimSpeedupChestVideoRewardAction;
import com.etermax.spacehorse.core.ads.videorewards.resource.request.ChestVideoRewardRequest;
import com.etermax.spacehorse.core.ads.videorewards.resource.response.GetAvailableVideoRewardResponse;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalog;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/videoreward", tags = "VideoRewards")
public class SpeedupChestVideoRewardResource {

	private static final String LOGIN_ID_HEADER = "Login-Id";
	private final PlayerAction playerAction;
	private final CatalogAction catalogAction;

	private final ClaimSpeedupChestVideoRewardAction claimSpeedupChestVideoRewardAction;
	private final GetAvailableVideoRewardAction getAvailableVideoRewardAction;

	public SpeedupChestVideoRewardResource(PlayerAction playerAction, CatalogAction catalogAction, ClaimSpeedupChestVideoRewardAction claimSpeedupChestVideoRewardAction,
			GetAvailableVideoRewardAction getAvailableVideoRewardAction) {
		this.playerAction = playerAction;
		this.catalogAction = catalogAction;
		this.claimSpeedupChestVideoRewardAction = claimSpeedupChestVideoRewardAction;
		this.getAvailableVideoRewardAction = getAvailableVideoRewardAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/videoreward/speedupChest/claim")
	@ApiOperation("Speedup the opening end time of the chest through a video reward")
	public Response claim(@Context HttpServletRequest httpServletRequest, ChestVideoRewardRequest request) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		return playerOpt.map(player -> {
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			Chest chest = claimSpeedupChestVideoRewardAction.claim(player, VideoRewardDefinition.SPEEDUP_COURIER_ID, request.getChestId(), catalog);
			return Response.ok(new ChestResponse(chest)).build();
		}).orElseGet(() -> Response.status(Response.Status.BAD_REQUEST).build());
	}

	@GET
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/videoreward/speedupChest/hasAvailable")
	@ApiOperation("Check if the chest has more video rewards to claim")
	public Response hasAvailable(@Context HttpServletRequest httpServletRequest) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		return playerOpt.map(player -> {
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			boolean hasAvailableVideoRewards = getAvailableVideoRewardAction.hasAvailable(player, VideoRewardDefinition.SPEEDUP_COURIER_ID, catalog);
			return Response.ok(new GetAvailableVideoRewardResponse(hasAvailableVideoRewards)).build();
		}).orElseGet(() -> Response.status(Response.Status.BAD_REQUEST).build());
	}

}
