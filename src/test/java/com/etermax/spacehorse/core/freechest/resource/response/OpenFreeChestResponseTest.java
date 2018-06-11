package com.etermax.spacehorse.core.freechest.resource.response;

import static com.etermax.spacehorse.TestUtils.mapToJson;
import static com.etermax.spacehorse.TestUtils.readJson;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.fasterxml.jackson.databind.JsonNode;

public class OpenFreeChestResponseTest {

	@Test
	public void whenCreateAnOpenFreeChestResponseThenTheJsonShouldBeCreatedProperly() throws Exception {
		// Given
		List<RewardResponse> rewards = newArrayList(aRewardResponseWith(10L), aRewardResponseWith(100L, "cardType"));
		int newLastChestOpeningServerTime = 1333;
		// When
		OpenFreeChestResponse response = new OpenFreeChestResponse(rewards, newLastChestOpeningServerTime);
		// Then
		JsonNode actualJson = mapToJson(response);
		JsonNode expectedJson = readJson("com/etermax/spacehorse/freechest/resource/response/opening-free-chest-response.json");
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	private RewardResponse aRewardResponseWith(long chestId) {
		return new RewardResponse(new ChestResponse(aChestWith(chestId)));
	}

	private RewardResponse aRewardResponseWith(long cardId, String cardType) {
		CardResponse cardResponse = new CardResponse(cardId, cardType, 2);
		return new RewardResponse(RewardType.TUTORIAL_CHEST, "rewardId", 10, cardResponse);
	}

	private Chest aChestWith(long id) {
		return new Chest(id, "chestType", 2, 3);
	}
}
