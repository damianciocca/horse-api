package com.etermax.spacehorse.core.battle.resource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.abtest.model.CatalogABTag;
import com.etermax.spacehorse.core.authenticator.model.realtime.RealtimeServerRequestValidator;
import com.etermax.spacehorse.core.battle.action.FindBattleAction;
import com.etermax.spacehorse.core.battle.action.FinishPlayerBattleAction;
import com.etermax.spacehorse.core.battle.action.FinishRealtimeBattleAction;
import com.etermax.spacehorse.core.battle.action.StartBattleAction;
import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.TeamStats;
import com.etermax.spacehorse.core.battle.model.UsedCardInfo;
import com.etermax.spacehorse.core.battle.resource.request.FinishBattlePlayerRequest;
import com.etermax.spacehorse.core.battle.resource.request.FinishBattleRequest;
import com.etermax.spacehorse.core.battle.resource.request.GetBattleInfoRequest;
import com.etermax.spacehorse.core.battle.resource.request.StartBattleRequest;
import com.etermax.spacehorse.core.battle.resource.request.TeamStatsRequest;
import com.etermax.spacehorse.core.battle.resource.response.BattleParametersResponse;
import com.etermax.spacehorse.core.battle.resource.response.BattleResponse;
import com.etermax.spacehorse.core.battle.resource.response.CardInfoResponse;
import com.etermax.spacehorse.core.battle.resource.response.FinishBattlePlayerResponse;
import com.etermax.spacehorse.core.battle.resource.response.FinishBattleResponse;
import com.etermax.spacehorse.core.battle.resource.response.GetBattleInfoResponse;
import com.etermax.spacehorse.core.battle.resource.response.StartBattleResponse;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.common.resource.response.ErrorResponse;

import io.swagger.annotations.Api;

@Path("/v1/battle")
@Api(value = "/battle", tags = "Battle")
public class BattleResource {

	private static final String LOGIN_ID_HEADER = "Login-Id";

	private final StartBattleAction startBattleAction;
	private final FinishRealtimeBattleAction finishRealtimeBattleAction;
	private final FinishPlayerBattleAction finishPlayerBattleAction;
	private final FindBattleAction findBattleAction;

	private RealtimeServerRequestValidator realtimeServerRequestValidator;
	private CatalogAction catalogAction;

	public BattleResource(RealtimeServerRequestValidator realtimeServerRequestValidator, CatalogAction catalogAction,
			StartBattleAction startBattleAction, FinishRealtimeBattleAction finishRealtimeBattleAction,
			FinishPlayerBattleAction finishPlayerBattleAction, FindBattleAction findBattleAction) {
		this.realtimeServerRequestValidator = realtimeServerRequestValidator;
		this.catalogAction = catalogAction;
		this.startBattleAction = startBattleAction;
		this.finishRealtimeBattleAction = finishRealtimeBattleAction;
		this.finishPlayerBattleAction = finishPlayerBattleAction;
		this.findBattleAction = findBattleAction;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/start")
	public Response startBattle(@Context HttpServletRequest request, StartBattleRequest startBattleRequest) {
		if (!realtimeServerRequestValidator.validateRequest(request)) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		if (startBattleAction.startBattle(startBattleRequest.getBattleId())) {
			return Response.ok(new StartBattleResponse()).build();
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse.build("Invalid finish battle")).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/finish")
	public Response finishBattle(@Context HttpServletRequest httpServletRequest, FinishBattleRequest request) {
		if (!realtimeServerRequestValidator.validateRequest(httpServletRequest)) {
			return Response.status(Response.Status.FORBIDDEN).build();
		}

		if (finishRealtimeBattleAction.finishBattle(request.getBattleId(), request.getWinnerLoginId(), request.getWinFullScore(),
				buildBattlePlayerStats(request.getTeam1Stats()), buildBattlePlayerStats(request.getTeam2Stats()))) {

			return Response.ok(new FinishBattleResponse()).build();
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse.build("Invalid finish battle")).build();
	}

	private TeamStats buildBattlePlayerStats(TeamStatsRequest teamStatsRequest) {
		if (teamStatsRequest == null) {
			return new TeamStats();
		}

		return new TeamStats(teamStatsRequest.getScore(), teamStatsRequest.isMotherShipDamaged(),
				teamStatsRequest.getUsedCards().stream().map(x -> new UsedCardInfo(x.getCardType(), x.getUseAmount())).collect(Collectors.toList()));
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getInfo")
	public Response getBattleInfo(@Context HttpServletRequest request, GetBattleInfoRequest getBattleInfoRequest) {

		if (!realtimeServerRequestValidator.validateRequest(request)) {
			return Response.status(Response.Status.FORBIDDEN.getStatusCode()).build();
		}

		Optional<Battle> battle = findBattleAction.findBattle(getBattleInfoRequest.getBattleId());
		if (battle.isPresent()) {
			String catalogIdWithTagFromPlayer = getABTagFromFirstHumanPlayer(battle);
			ABTag abTag = CatalogABTag.buildFromCatalogWithTag(catalogIdWithTagFromPlayer).getAbTag();
			Catalog catalog = catalogAction.getCatalogForUser(abTag);

			return Response.ok(new GetBattleInfoResponse( //
					new BattleResponse(battle.get()), //
					new BattleParametersResponse(catalog), //
					buildCardInfos(catalog)) //
			).build();
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(ErrorResponse.build("Invalid battle id")).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/applyRewards")
	public Response finishPlayer(@Context HttpServletRequest request, FinishBattlePlayerRequest finishBattlePlayerRequest) {
		FinishBattlePlayerResponse response = finishPlayerBattleAction
				.finishPlayerBattle(request.getHeader(LOGIN_ID_HEADER), finishBattlePlayerRequest.getBattleId());
		return Response.ok(response).build();
	}

	private String getABTagFromFirstHumanPlayer(Optional<Battle> battle) {
		return battle.get().getPlayers().stream().filter(battlePlayer -> !battlePlayer.getBot()).findFirst().get().getCatalogId();
	}

	private List<CardInfoResponse> buildCardInfos(Catalog catalog) {
		return catalog.getCardDefinitionsCollection().getEntries().stream().filter(x -> x.getEnabled())
				.map(x -> new CardInfoResponse(x.getId(), x.getEnergyCost())).collect(Collectors.toList());
	}

}
