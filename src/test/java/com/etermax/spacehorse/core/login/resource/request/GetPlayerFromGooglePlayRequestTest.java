package com.etermax.spacehorse.core.login.resource.request;

import static com.etermax.spacehorse.TestUtils.mapJsonNodeToObject;
import static com.etermax.spacehorse.TestUtils.readJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class GetPlayerFromGooglePlayRequestTest {

	@Test
	public void whenMapToObjAJsonGetGooglePlayTokenThenShouldBeCreatedProperly() throws Exception {
		// Given
		JsonNode expectedJson = readJson("com/etermax/spacehorse/login/resource/request/get-google-play-service-request.json");
		// When
		GetPlayerFromGooglePlayRequest request = mapJsonNodeToObject(expectedJson, GetPlayerFromGooglePlayRequest.class);
		// Then
		assertThat(request.getToken()).isEqualTo("token");
	}
}
