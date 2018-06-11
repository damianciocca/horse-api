package com.etermax.spacehorse.integration;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.shop.resource.request.ShopBuyInAppItemRequest;
import com.jayway.jsonpath.JsonPath;

public class ShopIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(ShopIntegrationTest.class);

	private static final String SHOP_ITEM_ID = "Gems100";
	private static final String IN_APP_ID_RECEIPT = "dummy.gems100";
	private static final String URL_BUY_IN_APP = "/v1/shop/inApp/buyItem";
	private String playerId;
	private String sessionToken;

	@Before
	public void setUp() throws Exception {
		String responseAsJson = aNewEditorPlayerLogged();
		playerId = extractPlayerIdFrom(responseAsJson);
		sessionToken = extractSessionTokenFrom(responseAsJson);
	}

	@Test
	public void givenAnEditorPlayerWith100GemsWhenPurchase100GemsThenTheInventoryShouldBeIncreasedTo200() {

		ShopBuyInAppItemRequest request = givenAShopBuyInAppItemRequest();

		Response response = whenPurchaseGemsInApp(playerId, sessionToken, request);

		thenAssertThatThePurchaseWasSuccessful(response, playerId);
	}

	private ShopBuyInAppItemRequest givenAShopBuyInAppItemRequest() {
		return new ShopBuyInAppItemRequest(SHOP_ITEM_ID, IN_APP_ID_RECEIPT);
	}

	private Response whenPurchaseGemsInApp(String playerId, String sessionToken, ShopBuyInAppItemRequest request) {
		return client.target(getBuyInAppEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private void thenAssertThatThePurchaseWasSuccessful(Response response, String playerId) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		assertResponseAsJson(responseAsJson);
		assertInventoryInRepository(playerId);
	}

	private void assertInventoryInRepository(String playerId) {
		Player player = getPlayerRepository().find(playerId);
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(200);
	}

	private void assertResponseAsJson(String responseAsJson) {
		assertThat((Integer) JsonPath.read(responseAsJson, "$.rewards.length()")).isEqualTo(1);
	}

	private String getBuyInAppEndpoint() {
		return getBaseUrl().concat(URL_BUY_IN_APP);
	}
}
