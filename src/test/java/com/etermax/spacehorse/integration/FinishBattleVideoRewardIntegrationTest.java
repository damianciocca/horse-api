package com.etermax.spacehorse.integration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.ads.videorewards.resource.response.FinishBattleVideoRewardResponse;
import com.jayway.jsonpath.JsonPath;
import com.newrelic.agent.deps.com.google.gson.Gson;

public class FinishBattleVideoRewardIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(FinishBattleVideoRewardIntegrationTest.class);
	private static final String URL_CLAIM_VIDEO_REWARD = "/v1/videoreward/finishBattle/claim";
	private static final String URL_HAS_AVAILABLE_VIDEO_REWARD = "/v1/videoreward/finishBattle/hasAvailable";
	private String playerId;
	private String sessionToken;
	private Response response;

	@Test
	public void whenClaimVideoRewardThenPlayerGoldIsIncremented() {
		givenAPlayer();
		// when
		whenClaimVideoReward(playerId, sessionToken);
		// then
		thenPlayerGoldIsIncremented();
	}

	@Test
	public void whenAskIfIfHasAvailableVideoRewardThenReturnTrue() {
		givenAPlayer();
		// when
		whenAskIfhasAvailableVideoReward(playerId, sessionToken);
		// then
		thenReturnTrue();
	}

	private void thenReturnTrue() {
		String responseAsJson = assertHttpCode200(response);
		Boolean hasAvailable = JsonPath.parse(responseAsJson).read("$.hasAvailable", Boolean.class);
		assertThat(hasAvailable);
	}

	private String assertHttpCode200(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		return responseAsJson;
	}

	private void thenPlayerGoldIsIncremented() {
		assertThat(getPlayerRepository().find(playerId).getInventory().getGold().getAmount()).isEqualTo(110);
		String responseAsJson = assertHttpCode200(response);
		FinishBattleVideoRewardResponse finishBattleVideoRewardResponse = new Gson().fromJson(responseAsJson, FinishBattleVideoRewardResponse.class);
		assertThat(finishBattleVideoRewardResponse.getRewards().get(0).getAmount()).isEqualTo(10);
	}

	private void givenAPlayer() {
		String responseAsJson = aNewPlayerLogged();
		playerId = extractPlayerIdFrom(responseAsJson);
		sessionToken = extractSessionTokenFrom(responseAsJson);
	}

	private void whenClaimVideoReward(String playerId, String sessionToken) {
		response = client.target(getClaimVideoRewardEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(null);
	}

	private void whenAskIfhasAvailableVideoReward(String playerId, String sessionToken) {
		response = client.target(getHasAvailableVideoRewardEndpoint()).request().header(LOGIN_ID_HEADER, playerId)
				.header(SESSION_TOKEN_HEADER, sessionToken).get();
	}

	private String getClaimVideoRewardEndpoint() {
		return getBaseUrl().concat(URL_CLAIM_VIDEO_REWARD);
	}

	public String getHasAvailableVideoRewardEndpoint() {
		return getBaseUrl().concat(URL_HAS_AVAILABLE_VIDEO_REWARD);
	}
}
