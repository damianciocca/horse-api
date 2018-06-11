package com.etermax.spacehorse.integration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.login.resource.request.LoginRequest;
import com.etermax.spacehorse.core.player.model.Player;
import com.jayway.jsonpath.JsonPath;

public class LoginIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(QuestIntegrationTest.class);

	@Test
	public void whenNewPlayerTriesToLoginThenThePlayerShouldBeCreated() {
		// given
		LoginRequest loginRequest = new LoginRequest("", "", CLIENT_VERSION, ANDROID_PLATFORM);
		// when
		Response response = loginPlayer(loginRequest);
		// Then
		assertLoggedPlayer(response);
	}

	@Test
	public void whenAnExistentPlayerTriesToLoginThenThePlayerShouldBeUpdated() {
		// given
		String responseAsJson = aNewPlayerLogged();
		LoginRequest request = mapToLoginRequestFrom(responseAsJson);
		String previousSessionToken = extractSessionTokenFrom(responseAsJson);
		// when
		Response response = loginPlayer(request);
		// Then
		assertExistentLoggedPlayer(response, previousSessionToken);
	}

	private void assertExistentLoggedPlayer(Response response, String previousSessionToken) {
		String responseAsJson = assertLoggedPlayer(response);
		String actualSessionToken = extractSessionTokenFrom(responseAsJson);
		assertThat(previousSessionToken).isNotEqualTo(actualSessionToken);
	}

	private String assertLoggedPlayer(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		assertResponseAsJson(responseAsJson);
		assertPlayerInRepository(responseAsJson);
		return responseAsJson;
	}

	private void assertPlayerInRepository(String responseAsJson) {
		String playerId = extractPlayerIdFrom(responseAsJson);
		Player player = getPlayerRepository().find(playerId);
		assertThat(player).isNotNull();
		assertThat(player.getUserId()).isEqualTo(playerId);
	}

	private void assertResponseAsJson(String responseAsJson) {
		assertThat((String) JsonPath.read(responseAsJson, "$.catalogId")).containsPattern("1507298083884-");
		assertThat((Integer) JsonPath.read(responseAsJson, "$.mmr")).isEqualTo(0);
		assertThat((String) JsonPath.read(responseAsJson, "$.userRole")).isEqualTo("PLAYER");
		assertThat((String) JsonPath.read(responseAsJson, "$.player.name")).isEqualTo("DEFAULT_PLAYER_NAME");
		assertThat((Integer) JsonPath.read(responseAsJson, "$.player.inventory.gold.amount")).isEqualTo(100);
		assertThat((Integer) JsonPath.read(responseAsJson, "$.player.inventory.gems.amount")).isEqualTo(100);
		assertThat((Integer) JsonPath.read(responseAsJson, "$.player.progress.level")).isEqualTo(0);
		assertThat((Integer) JsonPath.read(responseAsJson, "$.player.progress.xp")).isEqualTo(0);
	}

	private LoginRequest mapToLoginRequestFrom(String responseAsJson) {
		String playerId = extractPlayerIdFrom(responseAsJson);
		String password = extractPasswordFrom(responseAsJson);
		return new LoginRequest(playerId, password, CLIENT_VERSION, ANDROID_PLATFORM);
	}

	private String extractPasswordFrom(String responseAsJson) {
		return JsonPath.read(responseAsJson, "$.password");
	}

}
