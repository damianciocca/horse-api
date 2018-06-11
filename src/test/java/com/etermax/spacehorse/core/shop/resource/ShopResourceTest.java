package com.etermax.spacehorse.core.shop.resource;

import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.shop.action.ShopBuyCardAction;
import com.etermax.spacehorse.core.shop.action.ShopBuyInAppItemAction;
import com.etermax.spacehorse.core.shop.action.ShopBuyItemAction;
import com.etermax.spacehorse.core.shop.action.ShopRefreshCardsAction;
import com.etermax.spacehorse.core.shop.resource.request.*;
import com.etermax.spacehorse.core.shop.resource.response.*;
import com.etermax.spacehorse.core.user.action.UserAction;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.mock.MockUtils;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

public class ShopResourceTest {

    private final String playerId = "playerId";

    private final String userId = "userId";

    private final String productId = "android.gems500";

    private final Boolean productValid = true;

    private final Platform platform = Platform.ANDROID;

    private final MarketType market = MarketType.ANDROID;

    private final String shopBuyItem = "Gold100";

    private final String shopBuyInAppItem = "Gems500";

    private ShopResource shopResource;

    public ShopResourceTest() {
        Catalog catalog = MockUtils.mockCatalog();
        CatalogAction catalogAction = MockUtils.mockCatalogAction(catalog);
        PlayerAction playerAction = MockUtils.mockPlayerAction(playerId, userId);
        UserAction userAction = MockUtils.mockUserAction(userId, platform);
        ShopBuyItemAction shopBuyItemAction = MockUtils.mockShopBuyItemAction(catalog, shopBuyItem);
        ShopBuyCardAction shopBuyCardAction = MockUtils.mockShopBuyCardAction();
        ShopRefreshCardsAction shopRefreshCardsAction = MockUtils.mockShopRefreshCardsAction();
        ShopBuyInAppItemAction shopBuyInAppItemAction = MockUtils.mockShopBuyInAppItemAction(catalog, shopBuyInAppItem, productId, market);
        this.shopResource = new ShopResource(shopBuyItemAction, shopBuyCardAction,
                shopRefreshCardsAction, playerAction, catalogAction, userAction, shopBuyInAppItemAction);
    }

    @Test
    public void testBuyItem() {
        HttpServletRequest request = MockUtils.mockHttpServletRequest(playerId);
        ShopBuyItemRequest shopBuyItemRequest = new ShopBuyItemRequest(shopBuyItem);

        Response response = this.shopResource.buyItem(request, shopBuyItemRequest);
        ShopBuyItemResponse shopBuyItemResponse = (ShopBuyItemResponse) response.getEntity();

        assertEquals(Response.Status.OK, response.getStatusInfo());
        assertNotNull(shopBuyItemResponse);
        assertNotNull(shopBuyItemResponse.getRewards());
        //TODO: agregar non empty.
    }

    @Test
    public void testBuyInAppItem() {
        HttpServletRequest request = MockUtils.mockHttpServletRequest(playerId);
        ShopBuyInAppItemRequest shopBuyInAppItemRequest =
                new ShopBuyInAppItemRequest(shopBuyInAppItem, "");

        Response response = shopResource.buyInAppItem(request, shopBuyInAppItemRequest);
        ShopBuyItemResponse shopBuyItemResponse = (ShopBuyItemResponse) response.getEntity();

        assertEquals(Response.Status.OK, response.getStatusInfo());
        assertNotNull(shopBuyItemResponse);
        assertNotNull(shopBuyItemResponse.getRewards());
        assertFalse(shopBuyItemResponse.getRewards().isEmpty());
    }

    @Test
    public void testApplyPending() {
        HttpServletRequest request = MockUtils.mockHttpServletRequest(playerId);
        ApplyPendingReceiptsRequest applyPendingReceiptsRequest = new ApplyPendingReceiptsRequest("");

        Response response = this.shopResource.applyPending(request, applyPendingReceiptsRequest);
        ShopBuyInAppItemResponse shopBuyInAppItemResponse = (ShopBuyInAppItemResponse) response.getEntity();

        assertEquals(Response.Status.OK, response.getStatusInfo());
        assertNotNull(shopBuyInAppItemResponse);
        assertNotNull(shopBuyInAppItemResponse.getInAppItems());
        assertFalse(shopBuyInAppItemResponse.getInAppItems().isEmpty());
    }

    @Test
    public void testBuyCard() {
        HttpServletRequest request = MockUtils.mockHttpServletRequest(playerId);
        ShopBuyCardRequest shopBuyCardRequest = new ShopBuyCardRequest(shopBuyItem);

        Response response = this.shopResource.buyCard(request, shopBuyCardRequest);
        ShopBuyCardResponse shopBuyCardResponse = (ShopBuyCardResponse) response.getEntity();

        assertEquals(Response.Status.OK, response.getStatusInfo());
        assertNotNull(shopBuyCardResponse);
        assertNotNull(shopBuyCardResponse.getUnlockedCard());
        assertNotNull(shopBuyCardResponse.getUnlockedCard().getId());
    }

    @Test
    public void testRefreshCards() {
        HttpServletRequest request = MockUtils.mockHttpServletRequest(playerId);
        ShopRefreshCardsRequest shopRefreshCardsRequest = new ShopRefreshCardsRequest();

        Response response = this.shopResource.refreshCards(request, shopRefreshCardsRequest);
        ShopRefreshCardsResponse shopRefreshCardsResponse = (ShopRefreshCardsResponse) response.getEntity();

        assertEquals(Response.Status.OK, response.getStatusInfo());
        assertNotNull(shopRefreshCardsResponse);
        assertNotNull(shopRefreshCardsResponse.getShopCards());
        assertNotNull(shopRefreshCardsResponse.getShopCards().getCards());
        assertFalse(shopRefreshCardsResponse.getShopCards().getCards().isEmpty());
        assertNotNull(shopRefreshCardsResponse.getShopCards().getExpirationServerTime());
    }

}
