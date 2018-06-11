package com.etermax.spacehorse.core.login.resource.request;

import static com.etermax.spacehorse.TestUtils.mapJsonNodeToObject;
import static com.etermax.spacehorse.TestUtils.readJson;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;

public class GameCenterAuthRequestIncomeTest {

	@Test
	public void whenMapToObjAJsonLinkWithGameCenterThenShouldBeCreatedProperly() throws Exception {
		// Given
		JsonNode expectedJson = readJson("com/etermax/spacehorse/login/resource/request/link-game-center-request.json");
		// When
		GameCenterAuthRequestIncome request = mapJsonNodeToObject(expectedJson, GameCenterAuthRequestIncome.class);
		// Then
		assertThat(request.getGameCenterId()).isEqualTo("gameCenterId");
	}
}
