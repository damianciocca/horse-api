package com.etermax.spacehorse.core.liga.resource;

import static com.google.common.base.Preconditions.checkNotNull;

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

import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalog;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.liga.action.ClaimLeagueRewardsAction;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;

import io.swagger.annotations.Api;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/league", tags = "League")
public class LeagueResource {

	private static final String LOGIN_ID = "Login-Id";

	private final PlayerAction playerAction;
	private final CatalogAction catalogAction;
	private final ClaimLeagueRewardsAction claimLeagueRewardsAction;

	public LeagueResource(PlayerAction playerAction, CatalogAction catalogAction, ClaimLeagueRewardsAction claimLeagueRewardsAction) {
		this.playerAction = checkNotNull(playerAction);
		this.catalogAction = checkNotNull(catalogAction);
		this.claimLeagueRewardsAction = claimLeagueRewardsAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/league/claimRewards")
	public Response claim(@Context HttpServletRequest httpServletRequest) {
		Player player = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID)).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		List<RewardResponse> rewards = claimLeagueRewardsAction.claim(player, catalog);
		return Response.ok(new ClaimLeagueResponse(rewards)).build();
	}

}
