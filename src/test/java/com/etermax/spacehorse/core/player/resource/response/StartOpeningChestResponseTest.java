package com.etermax.spacehorse.core.player.resource.response;

import static com.etermax.spacehorse.TestUtils.mapToJson;
import static com.etermax.spacehorse.TestUtils.readJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.fasterxml.jackson.databind.JsonNode;

public class StartOpeningChestResponseTest {

	@Test
	public void whenCreateAStartOpeningChestResponseThenTheJsonShouldBeCreatedProperly() throws Exception {
		// Given
		Chest chest = new Chest(10L, "chestType", 2, 3);
		ChestResponse chestResponse = new ChestResponse(chest);
		// When
		StartOpeningChestResponse response = new StartOpeningChestResponse(chestResponse);
		// Then
		JsonNode actualJson = mapToJson(response);
		JsonNode expectedJson = readJson("com/etermax/spacehorse/player/resource/response/start-opening-chest-response.json");
		assertThat(actualJson).isEqualTo(expectedJson);
	}
}
