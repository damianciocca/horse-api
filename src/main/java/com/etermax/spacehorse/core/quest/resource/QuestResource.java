package com.etermax.spacehorse.core.quest.resource;

import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalog;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.resource.response.player.progress.QuestResponse;
import com.etermax.spacehorse.core.quest.action.daily.ClaimDailyQuestAction;
import com.etermax.spacehorse.core.quest.action.daily.RefreshDailyQuestAction;
import com.etermax.spacehorse.core.quest.resource.response.RefreshQuestPayingReponse;
import com.etermax.spacehorse.core.quest.action.ClaimQuestAction;
import com.etermax.spacehorse.core.quest.action.RefreshQuestAction;
import com.etermax.spacehorse.core.quest.action.RefreshQuestPayingAction;
import com.etermax.spacehorse.core.quest.action.SkipQuestAction;
import com.etermax.spacehorse.core.quest.action.SkipQuestPayingAction;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestSlotValidator;
import com.etermax.spacehorse.core.quest.model.QuestWithPrice;
import com.etermax.spacehorse.core.quest.resource.request.QuestApplyRewardsRequest;
import com.etermax.spacehorse.core.quest.resource.request.QuestRefreshRequest;
import com.etermax.spacehorse.core.quest.resource.request.QuestSkipRequest;
import com.etermax.spacehorse.core.quest.resource.response.QuestClaimResponse;
import com.etermax.spacehorse.core.quest.resource.response.QuestRefreshResponse;
import com.etermax.spacehorse.core.quest.resource.response.QuestSkipResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Optional;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/quest", tags = "Quest")
public class QuestResource {

	private static final String LOGIN_ID_HEADER = "Login-Id";
	private final PlayerAction playerAction;
	private final CatalogAction catalogAction;
	private final RefreshQuestAction refreshQuestAction;
	private final ClaimQuestAction claimQuestAction;
	private final SkipQuestAction skipQuestAction;
	private final SkipQuestPayingAction skipQuestPayingAction;
	private final QuestSlotValidator questSlotValidator;
	private final RefreshQuestPayingAction refreshQuestPayingAction;
	private final RefreshDailyQuestAction refreshDailyQuestAction;
	private final ClaimDailyQuestAction claimDailyQuestAction;

	public QuestResource(PlayerAction playerAction, CatalogAction catalogAction, RefreshQuestAction refreshQuestAction,
			ClaimQuestAction claimQuestAction, SkipQuestAction skipQuestAction, SkipQuestPayingAction skipQuestPayingAction,
			RefreshQuestPayingAction refreshQuestPayingAction, RefreshDailyQuestAction refreshDailyQuestAction,
			ClaimDailyQuestAction claimDailyQuestAction) {
		this.playerAction = playerAction;
		this.catalogAction = catalogAction;
		this.refreshQuestAction = refreshQuestAction;
		this.claimQuestAction = claimQuestAction;
		this.skipQuestAction = skipQuestAction;
		this.skipQuestPayingAction = skipQuestPayingAction;
		this.refreshQuestPayingAction = refreshQuestPayingAction;
		this.refreshDailyQuestAction = refreshDailyQuestAction;
		this.claimDailyQuestAction = claimDailyQuestAction;
		this.questSlotValidator = new QuestSlotValidator();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/quest/refresh")
	@ApiOperation("Refresh the active quest of the player after it has expired. Fails if quest should not be refreshed.")
	public Response refresh(@Context HttpServletRequest httpServletRequest, QuestRefreshRequest request) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		if (playerOpt.isPresent()) {
			Player player = playerOpt.get();
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			questSlotValidator.validate(request.getSlotId());
			Quest newQuest = refreshQuestAction.refresh(player, catalog, request.getSlotId());
			return Response.ok(new QuestRefreshResponse(new QuestResponse(newQuest))).build();
		} else {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/quest/daily/refresh")
	@ApiOperation("Refresh the daily quest after it expires. Fails if refresh time is not reached")
	public Response refreshDailyQuest(@Context HttpServletRequest httpServletRequest) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		if (playerOpt.isPresent()) {
			Player player = playerOpt.get();
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			Quest newQuest = refreshDailyQuestAction.refresh(player, catalog);
			return Response.ok(new QuestRefreshResponse(new QuestResponse(newQuest))).build();
		} else {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/quest/daily/claim")
	@ApiOperation("Claims rewards to player after completing a daily quest. Fails if active quest is already claimed or not completed.")
	public Response claimDailyQuest(@Context HttpServletRequest httpServletRequest) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		if (playerOpt.isPresent()) {
			Player player = playerOpt.get();
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			QuestClaimResponse questClaimResponse = claimDailyQuestAction.claim(player, catalog);
			return Response.ok(questClaimResponse).build();
		} else {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/quest/refreshPaying")
	@ApiOperation("Get a new Quest. Fails if player not has enough gems")
	public Response refreshPaying(@Context HttpServletRequest httpServletRequest, QuestRefreshRequest request) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		if (playerOpt.isPresent()) {
			Player player = playerOpt.get();
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			questSlotValidator.validate(request.getSlotId());
			QuestWithPrice questWithPrice = refreshQuestPayingAction.refresh(player, catalog, request.getSlotId());
			return Response.ok(new RefreshQuestPayingReponse(questWithPrice)).build();
		} else {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/quest/claim")
	@ApiOperation("Claims rewards to player after completing a quest. Fails if active quest is already claimed or not completed.")
	public Response claim(@Context HttpServletRequest httpServletRequest, QuestApplyRewardsRequest request) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		if (playerOpt.isPresent()) {
			Player player = playerOpt.get();
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			questSlotValidator.validate(request.getSlotId());
			QuestClaimResponse questClaimResponse = claimQuestAction.claim(player, catalog, request.getSlotId());
			return Response.ok(questClaimResponse).build();
		} else {
			return Response.status(Response.Status.PRECONDITION_FAILED).build();
		}
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/quest/skip")
	@ApiOperation("Skip one quest. Fails if cool down is not reached.")
	public Response freeSkip(@Context HttpServletRequest httpServletRequest, QuestSkipRequest request) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		return playerOpt.map(player -> {
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			questSlotValidator.validate(request.getSlotId());
			QuestSkipResponse questSkipResponse = skipQuestAction.skip(player, catalog, request.getSlotId());
			return Response.ok(questSkipResponse).build();
		}).orElseGet(() -> Response.status(Response.Status.PRECONDITION_FAILED).build());
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/quest/skipPaying")
	@ApiOperation("Skip one quest discounting gems. Time does not matter")
	public Response skipPaying(@Context HttpServletRequest httpServletRequest, QuestSkipRequest request) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		return playerOpt.map(player -> {
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			questSlotValidator.validate(request.getSlotId());
			QuestSkipResponse questSkipResponse = skipQuestPayingAction.skip(player, catalog, request.getSlotId());
			return Response.ok(questSkipResponse).build();
		}).orElseGet(() -> Response.status(Response.Status.PRECONDITION_FAILED).build());
	}

}
