package com.etermax.spacehorse.core.quest.resource.response;

import static com.etermax.spacehorse.TestUtils.mapToJson;
import static com.etermax.spacehorse.TestUtils.readJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.etermax.spacehorse.core.player.resource.response.player.progress.QuestResponse;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.fasterxml.jackson.databind.JsonNode;

public class QuestRefreshResponseTest {

	@Test
	public void whenCreateAQuestRefreshResponseThenTheJsonShouldBeCreatedProperly() throws Exception {
		// Given
		Quest activeQuest = anActiveQuest();
		QuestResponse questResponse = new QuestResponse(activeQuest);
		// When
		QuestRefreshResponse response = new QuestRefreshResponse(questResponse);
		// Then
		JsonNode actualJson = mapToJson(response);
		JsonNode expectedJson = readJson("com/etermax/spacehorse/quest/resource/response/refresh-quest-response.json");
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	private Quest anActiveQuest() {
		Quest activeQuest = mock(Quest.class);
		when(activeQuest.getQuestId()).thenReturn("id");
		when(activeQuest.getCurrentProgress()).thenReturn(300L);
		when(activeQuest.isClaimed()).thenReturn(false);
		when(activeQuest.getState()).thenReturn(Quest.INITIAL_STATE);
		return activeQuest;
	}
}
