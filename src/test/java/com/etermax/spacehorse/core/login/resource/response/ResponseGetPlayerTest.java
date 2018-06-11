package com.etermax.spacehorse.core.login.resource.response;

import static com.etermax.spacehorse.TestUtils.mapToJson;
import static com.etermax.spacehorse.TestUtils.readJson;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.resource.response.player.PlayerResponse;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ResponseGetPlayerTest {

	private static final int MMR = 234;
	private static final String PLAYER_ID = "1";
	private FixedServerTimeProvider serverTimeProvider;

	@Before
	public void setUp() throws Exception {
		DateTime anyDateTime = new DateTime(Date.from(Instant.EPOCH)).plusDays(1).withZone(DateTimeZone.UTC);
		serverTimeProvider = new FixedServerTimeProvider(anyDateTime);
	}

	@Test
	public void whenCreateAGetGooglePlayServiceResponseThenTheJsonShouldBeCreatedProperly() throws Exception {
		// Given
		PlayerResponse playerResponse = aPlayerResponse();
		// when
		ResponseGetPlayer response = new ResponseGetPlayer(playerResponse, true, "userId");
		// Then
		JsonNode actualJson = mapToJson(response);
		JsonNode expectedJson = readJson("com/etermax/spacehorse/login/resource/response/get-googla-play-service-response.json");
		Assertions.assertThat(actualJson).isEqualTo(expectedJson);
	}

	private PlayerResponse aPlayerResponse() {
		Quest dailyQuest = mock(Quest.class);
		QuestBoard questBoard = new QuestBoard(serverTimeProvider, getQuestBoardConfiguration(), dailyQuest);
		SpecialOfferBoard specialOfferBoard = new SpecialOfferBoard(serverTimeProvider);
		CaptainsCollection captainsCollection = new CaptainsCollection(aPlayer().getUserId(), anEmptyUnlockedCaptains(), "");
		AchievementCollection achievementCollection = new AchievementCollection(aPlayer().getUserId(), Maps.newHashMap());
		return new PlayerResponse(new PlayerScenarioBuilder(PLAYER_ID, serverTimeProvider).build(), MMR, questBoard, specialOfferBoard,
				captainsCollection, achievementCollection, Optional.empty(), 0);
	}

	private QuestBoardConfiguration getQuestBoardConfiguration() {
		return new QuestBoardConfiguration(100, 10, 720, 1);
	}

	private List<Captain> anEmptyUnlockedCaptains() {
		return Lists.newArrayList();
	}

	private Player aPlayer() {
		return new PlayerScenarioBuilder("10").build();
	}
}
