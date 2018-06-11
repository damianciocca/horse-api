package com.etermax.spacehorse.integration;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.junit.Test;

import com.etermax.spacehorse.core.authenticator.model.PasswordGenerator;
import com.etermax.spacehorse.core.login.resource.request.GameCenterAuthRequestIncome;
import com.etermax.spacehorse.core.login.resource.request.LoginRequest;
import com.jayway.jsonpath.JsonPath;

public class LinkGameCenterIntegrationTest extends BaseIntegrationTest {

	private static final String URL_LINK_GAME_CENTER = "/v1/link_game_center";

	private String sessionToken;
	private String playerId;
	private String password;

	private String gameCenterId;

	@Test
	public void whenNewPlayerLinksHisAccountThenTheAccountShouldBeListedAsLinkedInANewLogin() {
		givenANewUserLogin();

		whenLinkingAccountWithGameCenter();

		thenANewLoginShouldHaveTheGameCenterId();
	}

	private void thenANewLoginShouldHaveTheGameCenterId() {

		LoginRequest loginRequest = new LoginRequest(playerId, password, CLIENT_VERSION, IOS_PLATFORM);

		Response loginResponse = loginPlayer(loginRequest);

		String loginResponseJson = mapToJsonStringFrom(loginResponse);

		String linkedWithSocialAccountId = JsonPath.read(loginResponseJson, "$.linkedWithSocialAccountId");

		assertThat(linkedWithSocialAccountId).isEqualTo(gameCenterId);
	}

	private void whenLinkingAccountWithGameCenter() {

		gameCenterId = "g" + PasswordGenerator.generate().replace("-", "");

		GameCenterAuthRequestIncome request = new GameCenterAuthRequestIncome(gameCenterId);

		client.target(getLinkGameCenterEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private String getLinkGameCenterEndpoint() {
		return getBaseUrl().concat(URL_LINK_GAME_CENTER);
	}

	private void givenANewUserLogin() {
		// given
		String loginResponse = aNewIosPlayerLogged();
		playerId = extractPlayerIdFrom(loginResponse);
		password = extractPasswordFrom(loginResponse);
		sessionToken = extractSessionTokenFrom(loginResponse);
	}

	private String extractPasswordFrom(String responseAsJson) {
		return JsonPath.read(responseAsJson, "$.password");
	}
}
