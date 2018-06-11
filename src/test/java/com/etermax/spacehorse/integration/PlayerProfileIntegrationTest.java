package com.etermax.spacehorse.integration;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.player.model.Gender;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.resource.request.ChangeProfileRequest;

public class PlayerProfileIntegrationTest extends BaseIntegrationTest {

	private static final String PLAYER_NAME_RAUL = "raul";
	private static final Gender PLAYER_GENDER_MALE = Gender.MALE;
	private static final int PLAYER_AGE = 43;
	private static final Logger logger = LoggerFactory.getLogger(PlayerProfileIntegrationTest.class);
	private static final String URL_PROFILE_RESOURCE = "/v1/player/profile";
	private String sessionToken;
	private Player player;

	@Before
	public void setUp() throws Exception {
		String responseAsJson = aNewEditorPlayerLogged();
		String playerId = extractPlayerIdFrom(responseAsJson);
		player = getPlayerRepository().find(playerId);
		sessionToken = extractSessionTokenFrom(responseAsJson);
	}

	@Test
	public void whenUpdateFullProfileThenThePlayerNameAgeAndGenderShouldBeUpdated() {
		ChangeProfileRequest profileRequest = new ChangeProfileRequest(PLAYER_NAME_RAUL, PLAYER_GENDER_MALE, PLAYER_AGE);
		Response response = whenUpdatePlayerProfile(player.getUserId(), sessionToken, profileRequest);
		assertThatAgeGenderAndNameWereUpdated(response);
	}

	private Response whenUpdatePlayerProfile(String userId, String sessionToken, ChangeProfileRequest name) {
		return client.target(getUpdatePlayerProfileEndpoint()).request().header(LOGIN_ID_HEADER, userId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(name, APPLICATION_JSON));
	}

	private void assertThatAgeGenderAndNameWereUpdated(Response response) {
		assertThatResponseWasOk(response);

		Player player = getPlayerRepository().find(this.player.getUserId());

		assertThat(player.getName()).isEqualTo(PLAYER_NAME_RAUL);
		assertThat(player.getAge()).isEqualTo(PLAYER_AGE);
		assertThat(player.getGender()).isEqualTo(PLAYER_GENDER_MALE);
	}

	private String assertThatResponseWasOk(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		return responseAsJson;
	}

	private String getUpdatePlayerProfileEndpoint() {
		return getBaseUrl().concat(URL_PROFILE_RESOURCE);
	}

}
