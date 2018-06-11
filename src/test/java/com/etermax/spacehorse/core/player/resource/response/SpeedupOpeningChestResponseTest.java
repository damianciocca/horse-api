package com.etermax.spacehorse.core.player.resource.response;

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

public class SpeedupOpeningChestResponseTest {

	@Test
	public void whenCreateASpeedupOpeningChestResponseThenTheJsonShouldBeCreatedProperly() throws Exception {
		// Given
		List<RewardResponse> rewards = newArrayList(aRewardResponseWith(10L), aRewardResponseWith(100L, "cardType"));
		int costInGems = 1000;
		// When
		SpeedupOpeningChestResponse response = new SpeedupOpeningChestResponse(rewards, costInGems);
		// Then
		JsonNode actualJson = mapToJson(response);
		JsonNode expectedJson = readJson("com/etermax/spacehorse/player/resource/response/speedup-opening-chest-response.json");
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
