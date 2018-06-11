package com.etermax.spacehorse.core.player.resource;

import java.util.List;

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

import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalog;
import com.etermax.spacehorse.core.player.action.deck.GetDeckCardsAction;
import com.etermax.spacehorse.core.player.action.deck.GetSelectedCardsFromDeckAction;
import com.etermax.spacehorse.core.player.action.deck.SelectDeckCardsAction;
import com.etermax.spacehorse.core.player.action.deck.UpgradeCardAction;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.resource.request.SelectedCardsRequest;
import com.etermax.spacehorse.core.player.resource.request.UpgradeCardRequest;
import com.etermax.spacehorse.core.player.resource.response.SelectedCardsResponse;
import com.etermax.spacehorse.core.player.resource.response.UpgradeCardResponse;
import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;

import io.swagger.annotations.Api;

@Path("/v1/player/deck")
@Api(value = "/deck", tags = "Deck")
public class PlayerDeckResource {

	private final SelectDeckCardsAction selectDeckCardsAction;
	private final GetSelectedCardsFromDeckAction getSelectedCardsFromDeckAction;
	private final GetDeckCardsAction getDeckCardsAction;
	private final UpgradeCardAction upgradeCardAction;

	public PlayerDeckResource(SelectDeckCardsAction selectDeckCardsAction, GetSelectedCardsFromDeckAction getSelectedCardsFromDeckAction,
			GetDeckCardsAction getDeckCardsAction, UpgradeCardAction upgradeCardAction) {
		this.selectDeckCardsAction = selectDeckCardsAction;
		this.getSelectedCardsFromDeckAction = getSelectedCardsFromDeckAction;
		this.getDeckCardsAction = getDeckCardsAction;
		this.upgradeCardAction = upgradeCardAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("cards/selected")
	public Response postDeckSelectedCards(@Context HttpServletRequest request, SelectedCardsRequest selectedCardsRequest) {
		selectDeckCardsAction
				.selectDeckCards(request.getHeader("Login-Id"), selectedCardsRequest.getStockId(), selectedCardsRequest.getSelectedCardsIds());
		return Response.ok(new SelectedCardsResponse()).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("cards/upgrade")
	public Response postDeckUpgradeCard(@Context HttpServletRequest request, UpgradeCardRequest updateCardRequest) {
		Card card = upgradeCardAction.upgradeCard(request.getHeader("Login-Id"), updateCardRequest.getCardId());
		return Response.ok(new UpgradeCardResponse(new CardResponse(card))).build();
	}

	@GET
	@RolesAllowed({ "PLAYER" })
	@Produces(MediaType.APPLICATION_JSON)
	@Path("cards/selected")
	public Response getDeckCardsSelected(@Context HttpServletRequest request) {
		List<Long> selectedCards = getSelectedCardsFromDeckAction.getSelectedCards(request.getHeader("Login-Id"));
		return Response.ok(selectedCards).build();
	}

	@GET
	@RolesAllowed({ "PLAYER" })
	@Produces(MediaType.APPLICATION_JSON)
	@Path("cards")
	public Response getDeckCards(@Context HttpServletRequest request) {
		List<Card> cards = getDeckCardsAction.getDeckCards(request.getHeader("Login-Id"));
		return Response.ok(cards).build();
	}

}
