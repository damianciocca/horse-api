package com.etermax.spacehorse.core.player.resource.request;

import static com.etermax.spacehorse.TestUtils.mapJsonNodeToObject;
import static com.etermax.spacehorse.TestUtils.readJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class StartOpeningChestRequestTest {

	@Test
	public void whenMapToObjAJsonStartOpeningChestRequestThenShouldBeCreatedProperly() throws Exception {
		// Given
		JsonNode expectedJson = readJson("com/etermax/spacehorse/player/resource/request/start-opening-chest-request.json");
		// When
		StartOpeningChestRequest request = mapJsonNodeToObject(expectedJson, StartOpeningChestRequest.class);
		// Then
		assertThat(request.getChestId()).isEqualTo(10L);
	}
}

