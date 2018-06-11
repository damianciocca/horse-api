package com.etermax.spacehorse.core.shop.resource;

import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.filter.RequiresLatestCatalog;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.inapps.resource.response.InAppsItemResponse;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.shop.action.ShopBuyCardAction;
import com.etermax.spacehorse.core.shop.action.ShopBuyInAppItemAction;
import com.etermax.spacehorse.core.shop.action.ShopBuyItemAction;
import com.etermax.spacehorse.core.shop.action.ShopRefreshCardsAction;
import com.etermax.spacehorse.core.shop.model.ShopCards;
import com.etermax.spacehorse.core.shop.resource.request.*;
import com.etermax.spacehorse.core.shop.resource.response.*;
import com.etermax.spacehorse.core.user.action.UserAction;
import com.etermax.spacehorse.core.user.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.Validate;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v1")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/shop", tags = "Shop")
public class ShopResource {

	private ShopBuyItemAction shopBuyItemAction;
	private ShopBuyInAppItemAction shopBuyInAppItemAction;
	private ShopBuyCardAction shopBuyCardAction;
	private ShopRefreshCardsAction shopRefreshCardsAction;
	private PlayerAction playerAction;
	private CatalogAction catalogAction;
	private UserAction userAction;

	public ShopResource(ShopBuyItemAction shopBuyItemAction, ShopBuyCardAction shopBuyCardAction, ShopRefreshCardsAction shopRefreshCardsAction,
			PlayerAction playerAction, CatalogAction catalogAction, UserAction userAction, ShopBuyInAppItemAction shopBuyInAppItemAction) {
		this.shopBuyCardAction = shopBuyCardAction;
		this.shopRefreshCardsAction = shopRefreshCardsAction;
		this.playerAction = playerAction;
		this.catalogAction = catalogAction;
		this.shopBuyItemAction = shopBuyItemAction;
		this.userAction = userAction;
		this.shopBuyInAppItemAction = shopBuyInAppItemAction;
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/shop/buyItem")
	@ApiOperation("Buys the item from the shop and gives its contents to the player")
	public Response buyItem(@Context HttpServletRequest request, ShopBuyItemRequest shopBuyItemRequest) {
		Player player = playerAction.findByLoginId(request.getHeader("Login-Id")).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		List<RewardResponse> rewards = this.shopBuyItemAction.buy(player, catalog, shopBuyItemRequest.getShopItemId()
				, false);
		return Response.ok(new ShopBuyItemResponse(rewards)).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/shop/inApp/buyItem")
	@ApiOperation("Buys the in-app item from the shop and gives its contents to the player")
	public Response buyInAppItem(@Context HttpServletRequest request, ShopBuyInAppItemRequest shopBuyInAppItemRequest) {
		Player player = playerAction.findByLoginId(request.getHeader("Login-Id")).get();
		User user = userAction.findByUserId(player.getUserId());
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		String shopItemId = shopBuyInAppItemRequest.getShopItemId();
		Object receipt = shopBuyInAppItemRequest.getReceipt();

		Validate.notNull(player);
		Validate.notNull(user);
		Validate.notNull(catalog);
		Validate.notNull(shopItemId);
		Validate.notNull(receipt);

		List<RewardResponse> rewards = shopBuyInAppItemAction.buyInApp(player, user, catalog, shopItemId, receipt);
		return Response.ok(new ShopBuyItemResponse(rewards)).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/shop/inApp/applyPending")
	@ApiOperation("Validates the in-app pending receipts and applies them if valid and not previously applied")
	public Response applyPending(@Context HttpServletRequest request, ApplyPendingReceiptsRequest applyPendingReceiptsRequest) {
		Player player = playerAction.findByLoginId(request.getHeader("Login-Id")).get();
		User user = userAction.findByUserId(player.getUserId());
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		List<InAppsItemResponse> inAppsItemResponses = shopBuyInAppItemAction
				.applyPendingReceipts(player, user, catalog, applyPendingReceiptsRequest.getReceipts());
		return Response.ok(new ShopBuyInAppItemResponse(inAppsItemResponses)).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/shop/buyCard")
	@ApiOperation("Buys the card from the shop and gives the card parts to the player")
	public Response buyCard(@Context HttpServletRequest request, ShopBuyCardRequest shopBuyCardRequest) {
		Player player = playerAction.findByLoginId(request.getHeader("Login-Id")).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		Card unlockedCard = this.shopBuyCardAction.buy(player, shopBuyCardRequest.getShopCardId(), getAchievementDefinitions(catalog));
		return Response.ok(new ShopBuyCardResponse(unlockedCard != null ? new CardResponse(unlockedCard) : null)).build();
	}

	@POST
	@RolesAllowed({ "PLAYER" })
	@RequiresLatestCatalog
	@Path("/shop/refreshCards")
	@ApiOperation("Refresh the list of shop cards after it has expired")
	public Response refreshCards(@Context HttpServletRequest request, ShopRefreshCardsRequest shopRefreshCardsRequest) {
		Player player = playerAction.findByLoginId(request.getHeader("Login-Id")).get();
		Catalog catalog = catalogAction.getCatalogForUser(player.getAbTag());
		ShopCards shopCards = this.shopRefreshCardsAction.refresh(player, catalog.getCardDefinitionsCollection(),
				catalog.getShopCardsCollection(), catalog.getMapsCollection());
		return Response.ok(new ShopRefreshCardsResponse(new ShopCardsResponse(shopCards))).build();
	}

	private List<AchievementDefinition> getAchievementDefinitions(Catalog catalog) {
		return catalog.getAchievementsDefinitionsCollection().getEntries();
	}
}
