package com.etermax.spacehorse.integration;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.player.resource.request.StartOpeningChestRequest;

public class InvalidCredentialIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(QuestIntegrationTest.class);

	private static final String URL_START_OPENING_CHEST = "/v1/player/chest/startOpening";
	private static final long CHEST_ID = 1L;

	private String playerId;

	@Before
	public void setUp() throws Exception {
		String responseAsJson = aNewPlayerLogged();
		playerId = extractPlayerIdFrom(responseAsJson);
	}

	@Test
	public void whenTriesToStartAChestWithAnInvalidSessionTokenThenTheApiFilterShouldNotAllowToExecuteTheAction() {

		String anInvalidSessionToken = "invalid-session";

		Response response = startOpeningChest(playerId, anInvalidSessionToken, new StartOpeningChestRequest(CHEST_ID));

		thenTheResponseHasAForbiddenErrorCode(response);
	}

	private void thenTheResponseHasAForbiddenErrorCode(Response response) {
		logger.info(response.toString());
		assertThat(response.getStatus()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode());
	}

	private Response startOpeningChest(String playerId, String sessionToken, StartOpeningChestRequest request) {
		return client.target(getStartOpeningChestEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private String getStartOpeningChestEndpoint() {
		return getBaseUrl().concat(URL_START_OPENING_CHEST);
	}

}
