package com.etermax.spacehorse.core.login.resource.response;

import static com.etermax.spacehorse.TestUtils.mapToJson;
import static com.etermax.spacehorse.TestUtils.readJson;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class ResponseFromLoginApiTest {

	@Test
	public void whenCreateALinkGooglePlayServiceResponseThenTheJsonShouldBeCreatedProperly() throws Exception {
		// Given
		String userId = "userId";
		String password = "password";
		// when
		ResponseFromLoginApi response = new ResponseFromLoginApi(userId, password);
		// Then
		JsonNode actualJson = mapToJson(response);
		JsonNode expectedJson = readJson("com/etermax/spacehorse/login/resource/response/link-googla-play-service-response.json");
		Assertions.assertThat(actualJson).isEqualTo(expectedJson);
	}
}
