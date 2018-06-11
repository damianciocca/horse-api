package com.etermax.spacehorse.core.login.resource.request;

import static com.etermax.spacehorse.TestUtils.mapJsonNodeToObject;
import static com.etermax.spacehorse.TestUtils.readJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class PlayServicesAuthRequestIncomeTest {

	@Test
	public void whenMapToObjAJsonLinkWithGooglePlayThenShouldBeCreatedProperly() throws Exception {
		// Given
		JsonNode expectedJson = readJson("com/etermax/spacehorse/login/resource/request/link-google-play-service-request.json");
		// When
		PlayServicesAuthRequestIncome request = mapJsonNodeToObject(expectedJson, PlayServicesAuthRequestIncome.class);
		// Then
		assertThat(request.getToken()).isEqualTo("token");
	}
}
