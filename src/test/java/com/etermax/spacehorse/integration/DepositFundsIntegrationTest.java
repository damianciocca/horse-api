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
import com.etermax.spacehorse.core.player.model.inventory.currency.Currency;
import com.etermax.spacehorse.core.support.resource.DepositFundsRequest;
import com.jayway.jsonpath.JsonPath;

public class DepositFundsIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(DepositFundsIntegrationTest.class);

	private static final String SUPPORT_LOGIN_ID = "support";
	private static final String SUPPORT_PASSWORD = "654321";
	private static final String URL_SUPPORT = "/v1/support/depositFunds";
	private Player player;
	private Currency gemsBeforeDeposit;
	private Currency goldBeforeDeposit;

	@Before
	public void setUp() throws Exception {
		String responseAsJson = aNewPlayerLogged();
		String playerId = extractPlayerIdFrom(responseAsJson);
		player = getPlayerRepository().find(playerId);
		gemsBeforeDeposit = getPlayerRepository().find(playerId).getInventory().getGems();
		goldBeforeDeposit = getPlayerRepository().find(playerId).getInventory().getGold();
	}

	@Test
	public void whenDepositGemsThenTheGemsShouldBeIncreased() {
		// given
		String playerId = player.getUserId();
		int gemsAmountToDeposit = 1000;
		int goldAmountToDeposit = 0;
		// when
		Response response = whenDepositTo(playerId, goldAmountToDeposit, gemsAmountToDeposit);
		// Then
		assertThatGemsAndGoldsWereIncreased(playerId, gemsAmountToDeposit, goldAmountToDeposit, response);
	}

	@Test
	public void whenDepositGoldThenTheGoldShouldBeIncreased() {
		// given
		String playerId = player.getUserId();
		int gemsAmountToDeposit = 0;
		int goldAmountToDeposit = 1000;
		// when
		Response response = whenDepositTo(playerId, goldAmountToDeposit, gemsAmountToDeposit);
		// Then
		assertThatGemsAndGoldsWereIncreased(playerId, gemsAmountToDeposit, goldAmountToDeposit, response);
	}

	@Test
	public void whenDepositGemsAndGoldThenTheGemsAndGoldShouldBeIncreased() {
		// given
		String playerId = player.getUserId();
		int gemsAmountToDeposit = 1000;
		int goldAmountToDeposit = 5000;
		// when
		Response response = whenDepositTo(playerId, goldAmountToDeposit, gemsAmountToDeposit);
		// Then
		assertThatResponseWasSuccessfullyCreated(playerId, response);
		assertThatGemsAndGoldsWereIncreased(playerId, gemsAmountToDeposit, goldAmountToDeposit, response);
	}

	private void assertThatResponseWasSuccessfullyCreated(String playerId, Response response) {
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		String playerIdAsResponse = JsonPath.parse(responseAsJson).read("$.playerid", String.class);
		int goldsAsResponse = JsonPath.parse(responseAsJson).read("$.newGolds", Integer.class);
		int gemsAsResponse = JsonPath.parse(responseAsJson).read("$.newGems", Integer.class);
		assertThat(playerIdAsResponse).isEqualTo(playerId);
		assertThat(goldsAsResponse).isEqualTo(5100);
		assertThat(gemsAsResponse).isEqualTo(1100);
	}

	private void assertThatGemsAndGoldsWereIncreased(String playerId, int gemsAmountToDeposit, int goldAmountToDeposit, Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		assertThat(getPlayerRepository().find(playerId).getInventory().getGems().getAmount()).isEqualTo(expectedGems(gemsAmountToDeposit));
		assertThat(getPlayerRepository().find(playerId).getInventory().getGold().getAmount()).isEqualTo(expectedGold(goldAmountToDeposit));
	}

	private int expectedGold(int goldAmountToDeposit) {
		return goldBeforeDeposit.getAmount() + goldAmountToDeposit;
	}

	private int expectedGems(int gemsAmountToDeposit) {
		return gemsBeforeDeposit.getAmount() + gemsAmountToDeposit;
	}

	private Response whenDepositTo(String playerId, int gold, int gems) {

		DepositFundsRequest request = new DepositFundsRequest(SUPPORT_LOGIN_ID, SUPPORT_PASSWORD, playerId, gold, gems);
		return client.target(getSupportEndpoint()).request().post(entity(request, APPLICATION_JSON));
	}

	private String getSupportEndpoint() {
		return getBaseUrl().concat(URL_SUPPORT);
	}

}
