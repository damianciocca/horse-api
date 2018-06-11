package com.etermax.spacehorse.core.ads.videorewards.resource.boost;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

import com.etermax.spacehorse.core.ads.videorewards.actions.ClaimBoostVideoRewardAction;
import com.etermax.spacehorse.core.ads.videorewards.actions.GetAvailableVideoRewardAction;
import com.etermax.spacehorse.core.ads.videorewards.resource.response.FinishBattleVideoRewardResponse;
import com.etermax.spacehorse.core.ads.videorewards.resource.response.GetAvailableVideoRewardResponse;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalog;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.google.common.collect.Lists;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/videoreward", tags = "VideoRewards")
public class FinishBattleVideoRewardResource {

	private static final String LOGIN_ID_HEADER = "Login-Id";
	private final PlayerAction playerAction;
	private final CatalogAction catalogAction;
	private final ClaimBoostVideoRewardAction claimBoostVideoRewardAction;
	private final GetAvailableVideoRewardAction getAvailableVideoRewardAction;

	public FinishBattleVideoRewardResource(PlayerAction playerAction, CatalogAction catalogAction,
			ClaimBoostVideoRewardAction claimBoostVideoRewardAction, GetAvailableVideoRewardAction getAvailableVideoRewardAction) {
		this.playerAction = playerAction;
		this.catalogAction = catalogAction;
		this.claimBoostVideoRewardAction = claimBoostVideoRewardAction;
		this.getAvailableVideoRewardAction = getAvailableVideoRewardAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/videoreward/finishBattle/claim")
	@ApiOperation("Claim reward coins")
	public Response claim(@Context HttpServletRequest httpServletRequest) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		return playerOpt.map(player -> {
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			String videoRewardDefinitionId = getVideoRewardDefinitionId(player, catalog);
			int coins = claimBoostVideoRewardAction.claim(player, videoRewardDefinitionId, catalog);
			return Response.ok(createFinishBattleVideoRewardResponse(coins)).build();
		}).orElseGet(() -> Response.status(Response.Status.BAD_REQUEST).build());
	}

	@GET
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/videoreward/finishBattle/hasAvailable")
	@ApiOperation("Check if user can boost more video rewards")
	public Response hasAvailable(@Context HttpServletRequest httpServletRequest) {
		Optional<Player> playerOpt = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID_HEADER));
		return playerOpt.map(player -> {
			Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
			String videoRewardDefinitionId = getVideoRewardDefinitionId(player, catalog);
			boolean hasAvailableVideoRewards = getAvailableVideoRewardAction.hasAvailable(player, videoRewardDefinitionId, catalog);
			return Response.ok(new GetAvailableVideoRewardResponse(hasAvailableVideoRewards)).build();
		}).orElseGet(() -> Response.status(Response.Status.BAD_REQUEST).build());
	}

	private String getVideoRewardDefinitionId(Player player, Catalog catalog) {
		List<VideoRewardDefinition> videoRewardDefinitionsForPlacement = getVideoRewardDefinitionsForBattleFinish(catalog);
		String videoRewardDefinitionId;
		if (videoRewardDefinitionsForPlacement.size() == 1) {
			videoRewardDefinitionId = videoRewardDefinitionsForPlacement.get(0).getId();
		} else {
			videoRewardDefinitionId = videoRewardDefinitionsForPlacement.stream().filter(getVideoRewardDefinitionWithPlayerMapNumber(player))
					.findFirst().get().getId();
		}
		return videoRewardDefinitionId;
	}

	private Predicate<VideoRewardDefinition> getVideoRewardDefinitionWithPlayerMapNumber(Player player) {
		return videoRewardDefinition -> videoRewardDefinition.getMapNumber() == player.getMapNumber();
	}

	private List<VideoRewardDefinition> getVideoRewardDefinitionsForBattleFinish(Catalog catalog) {
		return catalog.getVideoRewardDefinitionsCollection().getEntries().stream()
				.filter(videoRewardDefinition -> videoRewardDefinition.getPlaceName().equals(VideoRewardDefinition.BATTLE_FINISH_PLACE))
				.collect(Collectors.toList());
	}

	private FinishBattleVideoRewardResponse createFinishBattleVideoRewardResponse(int coins) {
		return new FinishBattleVideoRewardResponse(Lists.newArrayList(new RewardResponse(RewardType.GOLD, coins)));
	}

}