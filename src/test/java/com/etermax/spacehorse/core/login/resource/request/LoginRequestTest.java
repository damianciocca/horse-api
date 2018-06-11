package com.etermax.spacehorse.core.login.resource.request;

import static com.etermax.spacehorse.TestUtils.mapJsonNodeToObject;
import static com.etermax.spacehorse.TestUtils.readJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.etermax.spacehorse.core.user.model.Platform;
import com.fasterxml.jackson.databind.JsonNode;

public class LoginRequestTest {

	@Test
	public void whenMapToObjAJsonLoginRequestForExistentUserThenTheLoginRequestShouldBeCreatedProperly() throws Exception {
		// Given
		JsonNode expectedJson = readJson("com/etermax/spacehorse/login/resource/request/login-existent-user-request.json");
		// When
		LoginRequest loginRequest = mapJsonNodeToObject(expectedJson, LoginRequest.class);
		// Then
		assertThat(loginRequest.getPlatform()).isEqualTo(Platform.ANDROID);
		assertThat(loginRequest.getClientVersion()).isEqualTo(13);
	}

	@Test
	public void whenMapToObjAJsonLoginRequestForNewUserThenTheLoginRequestShoulBeCreatedProperly() throws Exception {
		// Given
		JsonNode expectedJson = readJson("com/etermax/spacehorse/login/resource/request/login-new-user-request.json");
		// When
		LoginRequest loginRequest = mapJsonNodeToObject(expectedJson, LoginRequest.class);
		// Then
		assertThat(loginRequest.getPlatform()).isEqualTo(Platform.ANDROID);
		assertThat(loginRequest.getClientVersion()).isEqualTo(13);
		assertThat(loginRequest.getLoginId()).isEqualTo("loginId");
		assertThat(loginRequest.getPassword()).isEqualTo("pass");
	}
}
