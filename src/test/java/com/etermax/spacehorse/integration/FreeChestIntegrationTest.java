package com.etermax.spacehorse.integration;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.player.model.Player;
import com.jayway.jsonpath.JsonPath;

public class FreeChestIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(QuestIntegrationTest.class);

	private static final String URL_OPEN_FREE_CHEST = "/v1/player/chest/openFree";

	@Test
	public void whenOpenAFreeChestThenTheRewardsShouldBeApplyToInventory() {
		// given
		String responseAsJson = aNewPlayerLogged();
		String playerId = extractPlayerIdFrom(responseAsJson);
		String sessionToken = extractSessionTokenFrom(responseAsJson);
		// when
		Response response = openFreeChest(playerId, sessionToken);
		// Then
		assertThatTheInventoryWasUpdatedWithMoreRewards(response, playerId);
	}

	private Response openFreeChest(String playerId, String sessionToken) {
		return client.target(getOpenFreeChestEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity("", APPLICATION_JSON));
	}

	private void assertThatTheInventoryWasUpdatedWithMoreRewards(Response response, String playerId) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		assertResponseAsJson(responseAsJson);
		assertInventoryInRepository(playerId);
	}

	private void assertInventoryInRepository(String playerId) {
		Player player = getPlayerRepository().find(playerId);
		assertThat(player.getInventory().getGems().getAmount()).isGreaterThan(100);
		assertThat(player.getInventory().getGold().getAmount()).isGreaterThan(100);
		assertThat(player.getInventory().getCardParts().getAmounts().size()).isGreaterThanOrEqualTo(1);
	}

	private void assertResponseAsJson(String responseAsJson) {
		long nowInSeconds = getServerTimeProvider().getTimeNowAsSeconds();
		Long lastChestOpeningTimeInSeconds = JsonPath.parse(responseAsJson).read("$.newLastChestOpeningServerTime", Long.class);

		assertThat(nowInSeconds).isGreaterThan(lastChestOpeningTimeInSeconds);
		assertThat((Integer) JsonPath.read(responseAsJson, "$.rewards.length()")).isEqualTo(3);
	}

	private String getOpenFreeChestEndpoint() {
		return getBaseUrl().concat(URL_OPEN_FREE_CHEST);
	}
}
