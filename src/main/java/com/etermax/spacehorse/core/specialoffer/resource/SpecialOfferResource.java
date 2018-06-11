package com.etermax.spacehorse.core.specialoffer.resource;

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
import com.etermax.spacehorse.core.inapps.resource.response.InAppsItemResponse;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.specialoffer.action.ApplySpecialOfferPendingRewardsAction;
import com.etermax.spacehorse.core.specialoffer.action.BuyInAppSpecialOfferAction;
import com.etermax.spacehorse.core.specialoffer.action.BuySpecialOfferAction;
import com.etermax.spacehorse.core.specialoffer.action.RefreshSpecialOfferBoardAction;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.resource.request.inapp.SpecialOfferInAppSpecialOfferRequest;
import com.etermax.spacehorse.core.specialoffer.resource.request.inapp.SecialOfferApplyPendingReceiptsRequest;
import com.etermax.spacehorse.core.specialoffer.resource.request.SpecialOfferRequest;
import com.etermax.spacehorse.core.specialoffer.resource.response.SpecialOfferBoardResponse;
import com.etermax.spacehorse.core.specialoffer.resource.response.inapp.SpecialOfferBuyInAppPendingRewardsResponse;
import com.etermax.spacehorse.core.specialoffer.resource.response.SpecialOfferBuyResponse;
import com.etermax.spacehorse.core.user.action.UserAction;
import com.etermax.spacehorse.core.user.model.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/shop", tags = "Shop")
public class SpecialOfferResource {

	private static final String LOGIN_ID = "Login-Id";

	private final BuySpecialOfferAction buySpecialOfferAction;
	private final BuyInAppSpecialOfferAction buyInAppSpecialOfferAction;
	private final RefreshSpecialOfferBoardAction refreshSpecialOfferBoardAction;
	private final ApplySpecialOfferPendingRewardsAction applyPendingReceipts;
	private final PlayerAction playerAction;
	private final CatalogAction catalogAction;
	private final UserAction userAction;

	public SpecialOfferResource(BuySpecialOfferAction buySpecialOfferAction, BuyInAppSpecialOfferAction buyInAppSpecialOfferAction,
			RefreshSpecialOfferBoardAction refreshSpecialOfferBoardAction, ApplySpecialOfferPendingRewardsAction applyPendingReceipts,
			PlayerAction playerAction, CatalogAction catalogAction, UserAction userAction) {
		this.buySpecialOfferAction = buySpecialOfferAction;
		this.buyInAppSpecialOfferAction = buyInAppSpecialOfferAction;
		this.refreshSpecialOfferBoardAction = refreshSpecialOfferBoardAction;
		this.applyPendingReceipts = applyPendingReceipts;
		this.playerAction = playerAction;
		this.catalogAction = catalogAction;
		this.userAction = userAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/shop/buySpecialOffer")
	public Response buySpecialOffer(@Context HttpServletRequest httpServletRequest, SpecialOfferRequest request) {
		Player player = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID)).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		List<RewardResponse> rewards = buySpecialOfferAction.buy(player, request.getSpecialOfferId(), catalog);
		return Response.ok(new SpecialOfferBuyResponse(rewards)).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/shop/inApp/buySpecialOffer")
	public Response buyInAppSpecialOffer(@Context HttpServletRequest httpServletRequest, SpecialOfferInAppSpecialOfferRequest request) {
		Player player = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID)).get();
		User user = userAction.findByUserId(player.getUserId());
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		String specialOfferId = request.getSpecialOfferId();
		Object receipt = request.getReceipt();
		List<RewardResponse> rewards = buyInAppSpecialOfferAction.buy(user, player, specialOfferId, catalog, receipt);
		return Response.ok(new SpecialOfferBuyResponse(rewards)).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/shop/refreshSpecialOffer")
	public Response refreshSpecialOffer(@Context HttpServletRequest request) {
		Player player = playerAction.findByLoginId(request.getHeader(LOGIN_ID)).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		SpecialOfferBoard specialOfferBoard = refreshSpecialOfferBoardAction.refresh(player, catalog);
		return Response.ok(new SpecialOfferBoardResponse(specialOfferBoard)).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/shop/inApp/specialOfferApplyPending")
	@ApiOperation("Validates the in-app pending receipts and applies them if valid and not previously applied")
	public Response applyPending(@Context HttpServletRequest httpServletRequest, SecialOfferApplyPendingReceiptsRequest request) {
		Player player = playerAction.findByLoginId(httpServletRequest.getHeader(LOGIN_ID)).get();
		User user = userAction.findByUserId(player.getUserId());
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		List<InAppsItemResponse> inAppsItemResponses = applyPendingReceipts.applyPendingReceipts(player, user, catalog, request.getReceipts());
		return Response.ok(new SpecialOfferBuyInAppPendingRewardsResponse(inAppsItemResponses)).build();
	}

}
