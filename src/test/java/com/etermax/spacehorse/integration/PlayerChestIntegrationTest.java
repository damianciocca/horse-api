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

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.player.model.inventory.chest.ChestState;
import com.etermax.spacehorse.core.player.model.inventory.currency.Currency;
import com.etermax.spacehorse.core.player.resource.request.FinishOpeningChestRequest;
import com.etermax.spacehorse.core.player.resource.request.SpeedupOpeningChestRequest;
import com.etermax.spacehorse.core.player.resource.request.StartOpeningChestRequest;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.jayway.jsonpath.JsonPath;

public class PlayerChestIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(QuestIntegrationTest.class);

	private static final String URL_START_OPENING_CHEST = "/v1/player/chest/startOpening";
	private static final String URL_FINISH_OPENING_CHEST = "/v1/player/chest/finishOpening";
	private static final String URL_SPEEDUP_OPENING_CHEST = "/v1/player/chest/speedupOpening";
	private static final long CHEST_ID = 1L;

	private String playerId;
	private String sessionToken;

	@Before
	public void setUp() throws Exception {
		String responseAsJson = aNewPlayerLogged();
		playerId = extractPlayerIdFrom(responseAsJson);
		sessionToken = extractSessionTokenFrom(responseAsJson);
	}

	@Test
	public void whenStartAChestThenTheChestShouldBeUpdatedProperly() {

		givenAClosedChestFor(playerId);

		Response response = startOpeningChest(playerId, sessionToken, new StartOpeningChestRequest(CHEST_ID));

		thenTheChestWasUpdatedProperly(response);
	}

	@Test
	public void whenFinishAChestThenTheRewardsShouldBeAppliedAndChestShouldBeRemovedFromInventory() {

		givenAChestOpened();

		Response response = finishOpeninigChest(playerId, sessionToken, new FinishOpeningChestRequest(CHEST_ID));

		thenTheChestWasRemovedProperly(response);
	}

	@Test
	public void whenSpeedupAClosedChestThenTheRewardsShouldBeAppliedAndChestShouldBeRemovedAndGemsWereDecreased() {

		givenAClosedChestFor(playerId);
		Currency gemsBeforeSpeedup = givenCurrentGems();

		Response response = speedupOpeningChest(playerId, sessionToken, new SpeedupOpeningChestRequest(CHEST_ID));

		thenTheRewardsWereAppliedAndGemsWereDecreased(gemsBeforeSpeedup, response);
	}

	@Test
	public void whenSpeedupAnOpeningChestThenTheTheRewardsShouldBeAppliedAndChestShouldBeRemovedAndGemsWereDecreased() {

		givenAChestOpening();
		Currency gemsBeforeSpeedup = givenCurrentGems();

		Response response = speedupOpeningChest(playerId, sessionToken, new SpeedupOpeningChestRequest(CHEST_ID));

		thenTheRewardsWereAppliedAndGemsWereDecreased(gemsBeforeSpeedup, response);
	}

	private void givenAClosedChestFor(String playerId) {
		Player player = getPlayerRepository().find(playerId);
		RewardContext rewardContext = RewardContext.fromMapNumber(1);
		Reward chest = new Reward(RewardType.CHEST, "courier_Silver", 1);
		ApplyRewardDomainService applyRewardDomainService = new ApplyRewardDomainService();
		ApplyRewardConfiguration configuration = ApplyRewardConfiguration.createBy(getActiveCatalog());
		applyRewardDomainService.applyRewards(player, newArrayList(chest), rewardContext, configuration, newArrayList());
		getPlayerRepository().update(player);
	}

	private void givenAChestOpening() {
		givenAClosedChestFor(playerId);
		startOpeningChest(playerId, sessionToken, new StartOpeningChestRequest(CHEST_ID));
	}

	private void givenAChestOpened() {
		givenAChestOpening();
		increaseTimeInSeconds(10801);
	}

	private Currency givenCurrentGems() {
		return getPlayerRepository().find(playerId).getInventory().getGems();
	}

	private void thenTheRewardsWereAppliedAndGemsWereDecreased(Currency gemsBeforeSpeedup, Response response) {
		assertThatRewardsWereApplied(response);
		assertThatChestWasRemoved();
		assertThatGemsWereDecreased(gemsBeforeSpeedup);
	}

	private void thenTheChestWasRemovedProperly(Response response) {
		assertThatRewardsWereApplied(response);
		assertThatChestWasRemoved();
	}

	private void thenTheChestWasUpdatedProperly(Response response) {
		assertThatTheStartAndEndTimesWereUpdated(response);
		assertThatChestIsInOpeningState();
	}

	private void increaseTimeInSeconds(int seconds) {
		DateTime timeIncreased = getServerTimeProvider().getDateTime().plusSeconds(seconds);
		getServerTimeProvider().changeTime(timeIncreased);
	}

	private void assertThatChestIsInOpeningState() {
		Chest chest = getPlayerRepository().find(playerId).getInventory().getChests().findChestById(1L).get();
		ChestState chestState = chest.getChestState(getServerTimeProvider().getDate());
		assertThat(chestState).isEqualTo(ChestState.OPENING);
	}

	private void assertThatChestWasRemoved() {
		Inventory inventory = getPlayerRepository().find(playerId).getInventory();
		assertThat(inventory.getChests().findChestById(1L)).isEmpty();
	}

	private void assertThatGemsWereDecreased(Currency gemsBeforeSeedup) {
		Inventory inventory = getPlayerRepository().find(playerId).getInventory();
		assertThat(inventory.getGems().getAmount()).isLessThan(gemsBeforeSeedup.getAmount());
	}

	private Response startOpeningChest(String playerId, String sessionToken, StartOpeningChestRequest request) {
		return client.target(getStartOpeningChestEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private Response finishOpeninigChest(String playerId, String sessionToken, FinishOpeningChestRequest request) {
		return client.target(getFinishOpeningChestEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private Response speedupOpeningChest(String playerId, String sessionToken, SpeedupOpeningChestRequest request) {
		return client.target(getSpeedupOpeningChestEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private void assertThatTheStartAndEndTimesWereUpdated(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		assertResponseAsJson(responseAsJson);
	}

	private void assertResponseAsJson(String responseAsJson) {
		long timeNowAsSeconds = getServerTimeProvider().getTimeNowAsSeconds();

		Long startServerTimeInSeconds = JsonPath.parse(responseAsJson).read("$.chest.chestOpeningStartServerTime", Long.class);
		assertThat(timeNowAsSeconds).isEqualTo(startServerTimeInSeconds);

		Long endServerTimeInSeconds = JsonPath.parse(responseAsJson).read("$.chest.chestOpeningEndServerTime", Long.class);
		assertThat(timeNowAsSeconds).isLessThan(endServerTimeInSeconds);

		assertThat((Integer) JsonPath.read(responseAsJson, "$.chest.id")).isEqualTo(1);
	}

	private void assertThatRewardsWereApplied(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		assertThat((Integer) JsonPath.read(responseAsJson, "$.rewards.length()")).isGreaterThanOrEqualTo(1);
	}

	private String getStartOpeningChestEndpoint() {
		return getBaseUrl().concat(URL_START_OPENING_CHEST);
	}

	private String getFinishOpeningChestEndpoint() {
		return getBaseUrl().concat(URL_FINISH_OPENING_CHEST);
	}

	private String getSpeedupOpeningChestEndpoint() {
		return getBaseUrl().concat(URL_SPEEDUP_OPENING_CHEST);
	}
}
