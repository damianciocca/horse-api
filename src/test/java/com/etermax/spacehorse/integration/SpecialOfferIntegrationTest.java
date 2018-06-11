package com.etermax.spacehorse.integration;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.shop.resource.request.ApplyPendingReceiptsRequest;
import com.etermax.spacehorse.core.specialoffer.resource.request.SpecialOfferRequest;
import com.etermax.spacehorse.core.specialoffer.resource.request.inapp.SpecialOfferInAppSpecialOfferRequest;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;

public class SpecialOfferIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(SpecialOfferIntegrationTest.class);

	private static final String SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME = "2017-11-30";
	private static final String LEGENDARY_SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME = "2017-12-10";

	private static final String URL_SPECIAL_OFFER_REFRESH = "/v1/shop/refreshSpecialOffer";
	private static final String URL_SPECIAL_OFFER_BUY = "/v1/shop/buySpecialOffer";
	private static final String URL_SPECIAL_OFFER_APPLY_PENDING = "/v1/shop/inApp/specialOfferApplyPending";
	private static final String URL_SPECIAL_OFFER_IN_APP_BUY = "/v1/shop/inApp/buySpecialOffer";

	private static final String GLORIOUS_CHEST_OFFER_ID = "glorious_chest";
	private static final String EPIC_CHEST_0_OFFER_ID = "epic_chest_0";
	private static final String LEGENDARY_CHEST_OFFER_ID = "legendary_chest";
	private static final String STARTER_PACK1_OFFER_ID = "starter_pack1";

	private String sessionToken;
	private Player player;

	@Before
	public void setUp() throws Exception {
		String responseAsJson = aNewEditorPlayerLogged();
		String playerId = extractPlayerIdFrom(responseAsJson);
		player = getPlayerRepository().find(playerId);
		sessionToken = extractSessionTokenFrom(responseAsJson);
	}

	@Test
	public void whenRefreshTheSameDayOfSpecialOfferActivationThenTheSpecialOffersShouldBeCreatedInTheBoard() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		getServerTimeProvider().changeTime(now);
		// when
		Response response = whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		// then
		assertJsonResponseForEpicGloriousAndStarterPackSpecialOffers(response, "2017-12-01");
	}

	@Test
	public void whenRefreshTheSameDayOfSpecialOfferFrequencyThenTheSpecialOffersShouldBeCreatedInTheBoard() {
		// given
		DateTime now = ServerTime.roundToStartOfDay(SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		increaseDaysFrom(now, 2);
		// when
		Response response = whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		// then
		assertJsonResponseForEpicGloriousAndStarterPackSpecialOffers(response, "2017-12-03");
	}

	@Test
	public void whenRefreshTheSameDayOfSpecialOfferFrequencyThenSpecialOffersShouldBeCreatedInTheBoard() {
		// given
		DateTime now = ServerTime.roundToStartOfDay(SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		increaseDaysFrom(now, 3);
		// when
		Response response = whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		//then
		assertJsonResponseForGloriousAndStarterPackSpecialOffers(response, "2017-12-04");
	}

	@Test
	public void whenRefreshAndBuyThenRewardsShouldBeAppliedToPlayer() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		getServerTimeProvider().changeTime(now);
		whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		// when
		Response response = whenBuySpecialOffer(player.getUserId(), sessionToken, "epic_chest_0");
		//then
		String responseAsJson = assertThatResponseWasOk(response);
		Integer rewards = JsonPath.parse(responseAsJson).read("$.rewards.size()", Integer.class);
		assertThat(rewards).isGreaterThanOrEqualTo(1);
	}

	@Test
	public void whenRefreshAndBuyAndRefreshAgainThenSpecialOfferShouldBeConsumed() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		increaseDaysFrom(now, 3);
		whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		whenBuySpecialOffer(player.getUserId(), sessionToken, "glorious_chest");
		// when
		Response response = whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		//then
		assertJsonResponseForStarterPack1SpecialOffer(response, "2017-12-04");
	}

	@Test
	public void whenRefreshThenALegendaryAndStarterPack1ShouldBeCreatedInTheBoard() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(LEGENDARY_SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		getServerTimeProvider().changeTime(now);
		// when
		Response response = whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		//then
		assertThatJsonResponseForLegendaryAndStarterPack1SpecialOffers(response, "2017-12-11");
	}

	@Test
	public void whenRefreshTwiceInDifferentDaysThenLegendaryAndStarterPack1SpecialOffersContinuesInTheBoard() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(LEGENDARY_SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		getServerTimeProvider().changeTime(now);

		whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		increaseDaysFrom(now, 1);
		increaseSecondsFrom(getServerTimeProvider().getDateTime(), 10); // 1 days + 10 seconds => Legendary should be continues appears!
		// when
		Response response = whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		//then
		assertThatJsonResponseForLegendaryAndStarterPack1SpecialOffers(response, "2017-12-12", now);
	}

	@Test
	public void whenRefreshThreeTimesInDifferentDaysThenLegendarySpecialOfferAndStarterPack1ShouldBeRemoved() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(LEGENDARY_SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		getServerTimeProvider().changeTime(now);

		whenRefreshSpecialOffer(player.getUserId(), sessionToken);

		increaseDaysFrom(getServerTimeProvider().getDateTime(), 1);
		whenRefreshSpecialOffer(player.getUserId(), sessionToken);

		increaseDaysFrom(getServerTimeProvider().getDateTime(), 1);
		increaseSecondsFrom(getServerTimeProvider().getDateTime(), 10); // 2 days + 10 seconds => Legendary should be expired!
		// when
		Response response = whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		//then
		assertJsonResponseForEpicAndGloriousSpecialOffers(response, "2017-12-13");
	}

	@Test
	public void givenInAppSpecialOfferWhenApplyPendingRewardsThenTheRewardsShouldBeApplied() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		getServerTimeProvider().changeTime(now);
		whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		String inAppProductId = "dummy.starterPack1";
		// when
		Response response = whenApplyPendingRewardsSpecialOffer(player.getUserId(), sessionToken, inAppProductId);
		// then
		assertThatRewardsOfStarterPack1WereApplied(response);
	}

	@Test
	public void whenApplyPendingRewardsForUnknownProductIdThenTheRewardsShouldBeEmptyAndErrorCodeShouldBe200() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		getServerTimeProvider().changeTime(now);
		whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		String inAppProductId = "unknown";
		// when
		Response response = whenApplyPendingRewardsSpecialOffer(player.getUserId(), sessionToken, inAppProductId);
		// then
		assertThatErrorCodeIs200AndRewardsWereEmpty(response);
	}

	@Test
	public void whenBuyAnExistentInAppProductThenTheRewardsShouldBeApplied() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		getServerTimeProvider().changeTime(now);
		whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		String inAppProductId = "dummy.starterPack1";
		String specialOfferId = "starter_pack1";
		// when
		Response response = whenBuyInAppSpecialOffer(player.getUserId(), sessionToken, inAppProductId, specialOfferId);
		// then
		assertThatStarterPack1RewardsWereApplied(response);
	}

	@Test
	public void whenBuyANonExistentInAppProductThenAnErrorCode400ShouldBeThrown() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		getServerTimeProvider().changeTime(now);
		whenRefreshSpecialOffer(player.getUserId(), sessionToken);
		String inAppProductId = "unknown";
		String specialOfferId = "starter_pack1";
		// when
		Response response = whenBuyInAppSpecialOffer(player.getUserId(), sessionToken, inAppProductId, specialOfferId);
		// then
		assertThatAnExceptionWasThrownWithMsg(response, "No valid receipt could be created from the received receipt");
	}

	@Test
	public void whenBuyAnExistentInAppProductButSpecialOfferIsNoInTheBoardThenAnErrorCode400ShouldBeThrown() {
		// Given
		DateTime now = ServerTime.roundToStartOfDay(SPECIAL_OFFER_DEFINITION_ACTIVATION_TIME);
		getServerTimeProvider().changeTime(now);
		String inAppProductId = "dummy.starterPack1";
		String specialOfferId = "starter_pack1";
		// when
		Response response = whenBuyInAppSpecialOffer(player.getUserId(), sessionToken, inAppProductId, specialOfferId);
		// then
		assertThatAnExceptionWasThrownWithMsg(response, "Special Offer ID [ starter_pack1 ] not found in the board");
	}

	private Response whenRefreshSpecialOffer(String userId, String sessionToken) {
		return client.target(getSpecialOfferRefreshEndpoint()).request().header(LOGIN_ID_HEADER, userId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity("", APPLICATION_JSON));
	}

	private Response whenBuySpecialOffer(String userId, String sessionToken, String specialOfferId) {
		return client.target(getSpecialOfferBuyEndpoint()).request().header(LOGIN_ID_HEADER, userId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(new SpecialOfferRequest(specialOfferId), APPLICATION_JSON));
	}

	private Response whenApplyPendingRewardsSpecialOffer(String userId, String sessionToken, String productId) {
		return client.target(getSpecialOfferApplyPendingEndpoint()).request().header(LOGIN_ID_HEADER, userId)
				.header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(new ApplyPendingReceiptsRequest(Lists.newArrayList(productId)), APPLICATION_JSON));
	}

	private Response whenBuyInAppSpecialOffer(String userId, String sessionToken, String productId, String specialOfferId) {
		return client.target(getSpecialOfferInAppBuyEndpoint()).request().header(LOGIN_ID_HEADER, userId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(new SpecialOfferInAppSpecialOfferRequest(specialOfferId, productId), APPLICATION_JSON));
	}

	private void assertThatJsonResponseForLegendaryAndStarterPack1SpecialOffers(Response response, String refreshTimeAsText) {
		String responseAsJson = assertThatResponseWasOk(response);

		Integer specialOffersSize = JsonPath.parse(responseAsJson).read("$.specialOffers.size()", Integer.class);
		assertThat(specialOffersSize).isGreaterThanOrEqualTo(2);

		assertRefreshTime(refreshTimeAsText, responseAsJson);

		assertSpecialOffer(responseAsJson, 0, 172800, STARTER_PACK1_OFFER_ID);
		assertSpecialOffer(responseAsJson, 1, 172800, LEGENDARY_CHEST_OFFER_ID);
	}

	private void assertThatJsonResponseForLegendaryAndStarterPack1SpecialOffers(Response response, String refreshTimeAsText,
			DateTime specialOfferCreationTime) {
		String responseAsJson = assertThatResponseWasOk(response);

		Integer specialOffersSize = JsonPath.parse(responseAsJson).read("$.specialOffers.size()", Integer.class);
		assertThat(specialOffersSize).isGreaterThanOrEqualTo(2);

		assertRefreshTime(refreshTimeAsText, responseAsJson);

		assertSpecialOffer(responseAsJson, 0, 172800, STARTER_PACK1_OFFER_ID, specialOfferCreationTime);
		assertSpecialOffer(responseAsJson, 1, 172800, LEGENDARY_CHEST_OFFER_ID, specialOfferCreationTime);
	}

	private void assertJsonResponseForEpicGloriousAndStarterPackSpecialOffers(Response response, String refreshTimeAsText) {
		String responseAsJson = assertThatResponseWasOk(response);

		Integer specialOffersSize = JsonPath.parse(responseAsJson).read("$.specialOffers.size()", Integer.class);
		assertThat(specialOffersSize).isEqualTo(3);

		assertRefreshTime(refreshTimeAsText, responseAsJson);

		assertSpecialOffer(responseAsJson, 0, 172800, STARTER_PACK1_OFFER_ID);
		assertSpecialOffer(responseAsJson, 1, 3600, EPIC_CHEST_0_OFFER_ID);
		assertSpecialOffer(responseAsJson, 2, 1800, GLORIOUS_CHEST_OFFER_ID);
	}

	private void assertJsonResponseForEpicAndGloriousSpecialOffers(Response response, String refreshTimeAsText) {
		String responseAsJson = assertThatResponseWasOk(response);

		Integer specialOffersSize = JsonPath.parse(responseAsJson).read("$.specialOffers.size()", Integer.class);
		assertThat(specialOffersSize).isEqualTo(2);

		assertRefreshTime(refreshTimeAsText, responseAsJson);

		assertSpecialOffer(responseAsJson, 0, 3600, EPIC_CHEST_0_OFFER_ID);
		assertSpecialOffer(responseAsJson, 1, 1800, GLORIOUS_CHEST_OFFER_ID);
	}

	private void assertJsonResponseForGloriousAndStarterPackSpecialOffers(Response response, String refreshTimeAsText) {
		String responseAsJson = assertThatResponseWasOk(response);

		Integer specialOffersSize = JsonPath.parse(responseAsJson).read("$.specialOffers.size()", Integer.class);
		assertThat(specialOffersSize).isEqualTo(2);

		assertRefreshTime(refreshTimeAsText, responseAsJson);

		assertSpecialOffer(responseAsJson, 0, 172800, STARTER_PACK1_OFFER_ID);
		assertSpecialOffer(responseAsJson, 1, 1800, GLORIOUS_CHEST_OFFER_ID);
	}

	private void assertJsonResponseForStarterPack1SpecialOffer(Response response, String refreshTimeAsText) {
		String responseAsJson = assertThatResponseWasOk(response);

		Integer specialOffersSize = JsonPath.parse(responseAsJson).read("$.specialOffers.size()", Integer.class);
		assertThat(specialOffersSize).isEqualTo(1);

		assertSpecialOffer(responseAsJson, 0, 172800, STARTER_PACK1_OFFER_ID);

		assertRefreshTime(refreshTimeAsText, responseAsJson);
	}

	private void assertRefreshTime(String nextRefreshServerTime, String responseAsJson) {
		long nextRefreshTimeInSeconds = JsonPath.parse(responseAsJson).read("$.refreshServerTimeInSeconds", Long.class);
		DateTime nextRefreshTime = ServerTime.roundToStartOfDay(nextRefreshServerTime);
		long expectedNextRefreshTime = ServerTime.fromDate(nextRefreshTime);
		assertThat(nextRefreshTimeInSeconds).isEqualTo(expectedNextRefreshTime);
	}

	private String assertThatResponseWasOk(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		return responseAsJson;
	}

	private void assertSpecialOffer(String responseAsJson, int index, int specialOfferDurationInSeconds, String expectedSpecialOfferId) {
		String specialOfferId = JsonPath.parse(responseAsJson).read("$.specialOffers[" + index + "].id", String.class);
		assertThat(specialOfferId).isEqualTo(expectedSpecialOfferId);

		long expirationTimeInSeconds = JsonPath.parse(responseAsJson).read("$.specialOffers[" + index + "].expirationTimeInSeconds", Long.class);
		long expectedExpirationTime = getServerTimeProvider().getTimeNowAsSeconds() + specialOfferDurationInSeconds;

		assertThat(expirationTimeInSeconds).isEqualTo(expectedExpirationTime);
	}

	private void assertSpecialOffer(String responseAsJson, int index, int specialOfferDurationInSeconds, String expectedSpecialOfferId,
			DateTime specialOfferCreationTime) {
		String specialOfferId = JsonPath.parse(responseAsJson).read("$.specialOffers[" + index + "].id", String.class);
		assertThat(specialOfferId).isEqualTo(expectedSpecialOfferId);

		long expirationTimeInSeconds = JsonPath.parse(responseAsJson).read("$.specialOffers[" + index + "].expirationTimeInSeconds", Long.class);
		long expectedExpirationTime = ServerTime.fromDate(specialOfferCreationTime.plusSeconds(specialOfferDurationInSeconds));

		assertThat(expirationTimeInSeconds).isEqualTo(expectedExpirationTime);
	}

	private void assertThatRewardsOfStarterPack1WereApplied(Response response) {
		String responseAsJson = assertThatResponseWasOk(response);
		String productId = JsonPath.parse(responseAsJson).read("$.inAppItems[0].validation.productId", String.class);
		assertThat(productId).isEqualTo("dummy.starterPack1");
		boolean isValid = JsonPath.parse(responseAsJson).read("$.inAppItems[0].validation.valid", Boolean.class);
		assertThat(isValid).isTrue();
		String rewardType = JsonPath.parse(responseAsJson).read("$.inAppItems[0].rewards[0].rewardType", String.class);
		assertThat(rewardType).isEqualTo(RewardType.GOLD.toString());
		Integer amount = JsonPath.parse(responseAsJson).read("$.inAppItems[0].rewards[0].amount", Integer.class);
		assertThat(amount).isEqualTo(200);
	}

	private void assertThatErrorCodeIs200AndRewardsWereEmpty(Response response) {
		String responseAsJson = assertThatResponseWasOk(response);
		int responsesSize = JsonPath.parse(responseAsJson).read("$.inAppItems.size()", Integer.class);
		assertThat(responsesSize).isEqualTo(0);
	}

	private void assertThatStarterPack1RewardsWereApplied(Response response) {
		String responseAsJson = assertThatResponseWasOk(response);
		String rewardType = JsonPath.parse(responseAsJson).read("$.rewards[0].rewardType", String.class);
		assertThat(rewardType).isEqualTo(RewardType.GOLD.toString());
		Integer amount = JsonPath.parse(responseAsJson).read("$.rewards[0].amount", Integer.class);
		assertThat(amount).isEqualTo(200);
	}

	private void assertThatAnExceptionWasThrownWithMsg(Response response, String errorMessage) {
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		String errorDescription = JsonPath.parse(responseAsJson).read("$.error", String.class);
		assertThat(errorDescription).isEqualTo(errorMessage);
	}

	private String getSpecialOfferRefreshEndpoint() {
		return getBaseUrl().concat(URL_SPECIAL_OFFER_REFRESH);
	}

	private String getSpecialOfferBuyEndpoint() {
		return getBaseUrl().concat(URL_SPECIAL_OFFER_BUY);
	}

	private String getSpecialOfferApplyPendingEndpoint() {
		return getBaseUrl().concat(URL_SPECIAL_OFFER_APPLY_PENDING);
	}

	private String getSpecialOfferInAppBuyEndpoint() {
		return getBaseUrl().concat(URL_SPECIAL_OFFER_IN_APP_BUY);
	}

	private void increaseDaysFrom(DateTime activationTime, int days) {
		DateTime nextActivationTime = activationTime.plusDays(days);
		getServerTimeProvider().changeTime(nextActivationTime);
	}

	private void increaseSecondsFrom(DateTime now, int seconds) {
		DateTime nextActivationTime = now.plusSeconds(seconds);
		getServerTimeProvider().changeTime(nextActivationTime);
	}

}
