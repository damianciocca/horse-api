package com.etermax.spacehorse.integration;

import static com.google.common.collect.Lists.newArrayList;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.ads.videorewards.resource.request.ChestVideoRewardRequest;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.resource.request.StartOpeningChestRequest;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;

public class ChestVideoRewardIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(ChestVideoRewardIntegrationTest.class);

	private static final int SPEEDUP_TIME_IN_SECONDS = 1200;
	private static final long CHEST_ID = 1L;
	private static final String URL_START_OPENING_CHEST = "/v1/player/chest/startOpening";
	private static final String URL_CLAIM_VIDEO_REWARD = "/v1/videoreward/speedupChest/claim";
	private static final String URL_HAS_AVAILABLE_VIDEO_REWARD = "/v1/videoreward/speedupChest/hasAvailable";

	private String playerId;
	private String sessionToken;

	@Before
	public void setUp() throws Exception {
		String responseAsJson = aNewPlayerLogged();
		playerId = extractPlayerIdFrom(responseAsJson);
		sessionToken = extractSessionTokenFrom(responseAsJson);
	}

	@Test
	public void givenAnOpenedChestWhenAVideoRewardWasClaimedThenAnExceptionWasThrown() {
		// given
		givenAnOpenedChest();
		// when
		Response response = claimVideoReward(playerId, sessionToken, new ChestVideoRewardRequest(CHEST_ID));
		// then
		assertThatErrorCode400WasThrown(response);
	}

	@Test
	public void givenAClosedChestWhenAVideoRewardWasClaimedThenTheOpeningEndTimeShouldNotBeUpdated() {
		// given
		givenAClosedChestFor(playerId);
		Chest chestBeforeClaimVideoReward = findChestBy(CHEST_ID);
		// when
		Response response = claimVideoReward(playerId, sessionToken, new ChestVideoRewardRequest(CHEST_ID));
		// then
		Chest chestAfterVideoRewardClaimed = findChestBy(CHEST_ID);
		assertThat(chestAfterVideoRewardClaimed.getChestOpeningEndDate()).isEqualTo(chestBeforeClaimVideoReward.getChestOpeningEndDate());
		int expectedSpeedupTimeInSeconds = 0;
		assertThatResponseHasANewChestOpeningEndTime(chestBeforeClaimVideoReward, response, expectedSpeedupTimeInSeconds);
	}

	@Test
	public void givenAnOpeningChestWhenAVideoRewardWasClaimedThenTheOpeningEndTimeShouldBeDecreased() {
		// given
		givenAnOpeningChest();
		Chest chestBeforeClaimVideoReward = findChestBy(CHEST_ID);
		// when
		Response response = claimVideoReward(playerId, sessionToken, new ChestVideoRewardRequest(CHEST_ID));
		// then
		Chest chestAfterVideoRewardClaimed = findChestBy(CHEST_ID);
		assertThat(chestAfterVideoRewardClaimed.getChestOpeningEndDate()).isBefore(chestBeforeClaimVideoReward.getChestOpeningEndDate());
		assertThatResponseHasANewChestOpeningEndTime(chestBeforeClaimVideoReward, response, SPEEDUP_TIME_IN_SECONDS);
	}

	@Test
	public void givenAnOpeningChestWhenQuotaWasExpiredThenAndExceptionWasThrown() {
		// given
		givenAnOpeningChest();
		whenClaimVideoReward();
		whenClaimVideoReward();
		whenClaimVideoReward();
		// when
		Response response = claimVideoReward(playerId, sessionToken, new ChestVideoRewardRequest(CHEST_ID));
		// then
		assertThatErrorCode400WasThrown(response);
	}

	@Test
	public void givenAnOpeningChestWhenQuotaWasExpiredAndResetThenTheOpeningEndTimeShouldBeDecreased() {
		// given
		givenAnOpeningChest();
		Chest chestBeforeClaimVideoReward = findChestBy(CHEST_ID);
		whenClaimVideoReward();
		whenClaimVideoReward();
		whenClaimVideoReward();
		increaseTimeInSeconds(601); // The TimeFrameInSeconds of a quota is 600 seconds. Then this time, the quota expired and the counter reset
		// when
		Response response = claimVideoReward(playerId, sessionToken, new ChestVideoRewardRequest(CHEST_ID));
		// then
		assertThatResponseHasANewChestOpeningEndTime(chestBeforeClaimVideoReward, response, SPEEDUP_TIME_IN_SECONDS * 4);
	}

	@Test
	public void givenAnOpeningChestWhenQuotaWasExpiredTwiceThenTheQuotaOfVideoRewardWasReachedAndExceptionWasThrown() {
		// given
		givenAnOpeningChest();
		whenClaimVideoReward();
		whenClaimVideoReward();
		whenClaimVideoReward();
		increaseTimeInSeconds(601); // The TimeFrameInSeconds of a quota is 600 seconds. Then this time, the quota expired and the counter reset
		whenClaimVideoReward();
		whenClaimVideoReward();
		whenClaimVideoReward();
		// when
		Response response = claimVideoReward(playerId, sessionToken, new ChestVideoRewardRequest(CHEST_ID));
		// then
		assertThatErrorCode400WasThrown(response);
	}

	@Test
	public void givenAnOpeningChestWhenCheckIfHasMoreVideoRewardsAvailableThenTheResponseIsTrueCase1() {
		// given
		givenAnOpeningChest();
		whenClaimVideoReward();
		// when
		Response response = hasAvailableVideoReward(playerId, sessionToken);
		// then
		assertThatHasMoreVideoRewardsAvailable(response);
	}

	@Test
	public void givenAnOpeningChestWhenCheckIfHasMoreVideoRewardsAvailableThenTheResponseIsTrueCase2() {
		// given
		givenAnOpeningChest();
		whenClaimVideoReward();
		whenClaimVideoReward();
		whenClaimVideoReward();
		increaseTimeInSeconds(700);
		// when
		Response response = hasAvailableVideoReward(playerId, sessionToken);
		// then
		assertThatHasMoreVideoRewardsAvailable(response);
	}

	@Test
	public void givenAnOpeningChestWhenCheckIfHasMoreVideoRewardsAvailableThenTheResponseIsFalseCase1() {
		// given
		givenAnOpeningChest();
		whenClaimVideoReward();
		whenClaimVideoReward();
		whenClaimVideoReward();
		// when
		Response response = hasAvailableVideoReward(playerId, sessionToken);
		// then
		assertThatHasNotAvailabeVideoRewards(response);
	}

	@Test
	public void givenAnOpeningChestWhenCheckIfHasMoreVideoRewardsAvailableThenTheResponseIsFalseCase2() {
		// given
		givenAnOpeningChest();
		whenClaimVideoReward();
		whenClaimVideoReward();
		whenClaimVideoReward();
		increaseTimeInSeconds(601); // The TimeFrameInSeconds of a quota is 600 seconds. Then this time, the quota expired and the counter reset
		whenClaimVideoReward();
		whenClaimVideoReward();
		whenClaimVideoReward();
		// when
		Response response = hasAvailableVideoReward(playerId, sessionToken);
		// then
		assertThatHasNotAvailabeVideoRewards(response);
	}

	private Chest findChestBy(long chestId) {
		return getPlayerRepository().find(playerId).getInventory().getChests().findChestById(chestId).get();
	}

	private void givenAClosedChestFor(String playerId) {
		Player player = getPlayerRepository().find(playerId);
		RewardContext rewardContext = RewardContext.fromMapNumber(1);
		Reward chest = new Reward(RewardType.CHEST, "courier_Silver", 1);
		ApplyRewardDomainService applyRewardDomainService = new ApplyRewardDomainService();
		ApplyRewardConfiguration configuration = ApplyRewardConfiguration.createBy(getActiveCatalog());
		applyRewardDomainService.applyRewards(player, Lists.newArrayList(chest), rewardContext, configuration, newArrayList());
		getPlayerRepository().update(player);
	}

	private void givenAnOpeningChest() {
		givenAClosedChestFor(playerId);
		startOpeningChest(playerId, sessionToken, new StartOpeningChestRequest(CHEST_ID));
	}

	private void givenAnOpenedChest() {
		givenAnOpeningChest();
		increaseTimeInSeconds(10801);
	}

	private void whenClaimVideoReward() {
		Chest chestBeforeClaimVideoReward = findChestBy(CHEST_ID);
		Response response = claimVideoReward(playerId, sessionToken, new ChestVideoRewardRequest(CHEST_ID));
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		Chest chestAfterVideoRewardClaimed = findChestBy(CHEST_ID);
		assertThat(chestAfterVideoRewardClaimed.getChestOpeningEndDate()).isBefore(chestBeforeClaimVideoReward.getChestOpeningEndDate());
	}

	private void assertThatErrorCode400WasThrown(Response response) {
		assertThat(response.getStatus()).isNotEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
	}

	private String assertHttpCode200(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		return responseAsJson;
	}

	private void assertThatHasMoreVideoRewardsAvailable(Response response) {
		String responseAsJson = assertHttpCode200(response);
		boolean hasAvailable = JsonPath.parse(responseAsJson).read("$.hasAvailable", Boolean.class);
		assertThat(hasAvailable).isTrue();
	}

	private void assertThatHasNotAvailabeVideoRewards(Response response) {
		String responseAsJson = assertHttpCode200(response);
		boolean hasAvailable = JsonPath.parse(responseAsJson).read("$.hasAvailable", Boolean.class);
		assertThat(hasAvailable).isFalse();
	}

	private void assertThatResponseHasANewChestOpeningEndTime(Chest chestBeforeClaimVideoReward, Response response, int speedupTimeInSeconds) {
		String responseAsJson = assertHttpCode200(response);
		Long openingEndTimeInSeconds = JsonPath.parse(responseAsJson).read("$.chestOpeningEndServerTime", Long.class);
		// "Note: mockCatalog.json => "SpeedupTimeInSeconds : 1200"
		long expected = ServerTime.fromDate(chestBeforeClaimVideoReward.getChestOpeningEndDate()) - speedupTimeInSeconds;
		assertThat(openingEndTimeInSeconds).isEqualTo(expected);
	}

	private void increaseTimeInSeconds(int seconds) {
		DateTime timeIncreased = getServerTimeProvider().getDateTime().plusSeconds(seconds);
		getServerTimeProvider().changeTime(timeIncreased);
	}

	private Response startOpeningChest(String playerId, String sessionToken, StartOpeningChestRequest request) {
		return client.target(getStartOpeningChestEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private Response claimVideoReward(String playerId, String sessionToken, ChestVideoRewardRequest request) {
		return client.target(getClaimVideoRewardEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private Response hasAvailableVideoReward(String playerId, String sessionToken) {
		return client.target(getHasAvailableVideoRewardEndpoint()).request().header(LOGIN_ID_HEADER, playerId)
				.header(SESSION_TOKEN_HEADER, sessionToken).get();
	}

	private String getStartOpeningChestEndpoint() {
		return getBaseUrl().concat(URL_START_OPENING_CHEST);
	}

	private String getClaimVideoRewardEndpoint() {
		return getBaseUrl().concat(URL_CLAIM_VIDEO_REWARD);
	}

	public String getHasAvailableVideoRewardEndpoint() {
		return getBaseUrl().concat(URL_HAS_AVAILABLE_VIDEO_REWARD);
	}
}
