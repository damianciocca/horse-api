package com.etermax.spacehorse.core.player.resource.request;

import static com.etermax.spacehorse.TestUtils.mapJsonNodeToObject;
import static com.etermax.spacehorse.TestUtils.readJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class FinishOpeningChestRequestTest {

	@Test
	public void whenMapToObjAJsonFinishOpeningChestRequestThenShouldBeCreatedProperly() throws Exception {
		// Given
		JsonNode expectedJson = readJson("com/etermax/spacehorse/player/resource/request/finish-opening-chest-request.json");
		// When
		FinishOpeningChestRequest request = mapJsonNodeToObject(expectedJson, FinishOpeningChestRequest.class);
		// Then
		assertThat(request.getChestId()).isEqualTo(10L);
	}
}

