package com.etermax.spacehorse.core.achievements.resource;

import static com.google.common.base.Preconditions.checkNotNull;

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

import com.etermax.spacehorse.core.achievements.action.ClaimAchievementRewardAction;
import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalog;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;

import io.swagger.annotations.Api;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/achievements", tags = "Achievements")
public class AchievementsResource {

	private static final Logger logger = LoggerFactory.getLogger(AchievementsResource.class);

	private static final String LOGIN_ID = "Login-Id";

	private final PlayerAction playerAction;
	private final CatalogAction catalogAction;

	private final ClaimAchievementRewardAction claimAchievementRewardAction;
	private final CompleteAchievementAction completeAchievementAction;

	public AchievementsResource(PlayerAction playerAction, CatalogAction catalogAction, ClaimAchievementRewardAction claimAchievementRewardAction,
			CompleteAchievementAction completeAchievementAction) {
		this.playerAction = checkNotNull(playerAction);
		this.catalogAction = checkNotNull(catalogAction);
		this.claimAchievementRewardAction = claimAchievementRewardAction;
		this.completeAchievementAction = completeAchievementAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/achievement/complete")
	public Response completeAchievement(@Context HttpServletRequest httpServletRequest, AchievementRequest request) {
		Player player = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID)).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		AchievementDefinition achievementDefinition = getAchievementByIdOrFail(request, catalog);
		if (achievementDefinition.isAllowedToCompleteByClient()) {
			completeAchievementAction.complete(player, request.getAchievementId());
			return Response.ok(new EmptyAchievementResponse()).build();
		}
		logger.error("====> Unexpected error when trying to complete an achievement that is not allowed to be executed by the client: " + request);
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/achievement/claim")
	public Response claimAchievement(@Context HttpServletRequest httpServletRequest, AchievementRequest request) {
		Player player = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID)).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		claimAchievementRewardAction.claimRewards(player, request.getAchievementId(), getAchievementByIdOrFail(request, catalog));
		return Response.ok(new EmptyAchievementResponse()).build();
	}

	private AchievementDefinition getAchievementByIdOrFail(AchievementRequest request, Catalog catalog) {
		return catalog.getAchievementsDefinitionsCollection().findByIdOrFail(request.getAchievementId());
	}

}


