package com.etermax.spacehorse.integration;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.resource.AchievementRequest;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.capitain.resource.BuyCaptainRequest;
import com.etermax.spacehorse.core.player.model.Player;

public class AchievementIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(AchievementIntegrationTest.class);

	private static final String PURCHASE_CAPTAIN_ACHIEVEMENT_ID = "achievement_PurchaseCaptain";
	private static final String PLAY_BATTLE_PRACTICE_ACHIEVEMENT_ID = "achievement_PlayBattlePractice";

	private static final String URL_BUY_CAPTAIN = "/v1/captain/buyCaptain";
	private static final String URL_CLAIM_ACHIEVEMENT = "/v1/achievement/claim";
	private static final String URL_COMPLETE_ACHIEVEMENT = "/v1/achievement/complete";

	private static final String CAPTAIN_ID_REX = "captain-rex";
	private static final String CAPTAIN_ID_HELA = "captain-hela";

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
	public void whenClaimAchievementRewardsThenTheGemsWereIncreased() {
		// Given
		givenAPlayerInMapNumber2();
		String captainId = givenACaptainIdToBuy();
		whenBuyCaptain(player.getUserId(), sessionToken, captainId);
		int playerGemsBeforeClaimRewards = getPlayerRepository().find(player.getUserId()).getInventory().getGems().getAmount();
		// when
		Response response = whenClaimAchievementRewards(player.getUserId(), sessionToken);
		// then
		assertThatResponseWasOk(response);
		assertThatGemsWereDecreased(playerGemsBeforeClaimRewards);
	}

	@Test
	public void whenCompleteAnAchievementByClientThenTheAchievementShouldBeUpdatedToReadyToClaim() {
		// when
		Response response = whenCompleteAchievement(player.getUserId(), sessionToken, PLAY_BATTLE_PRACTICE_ACHIEVEMENT_ID);
		// then
		assertThatResponseWasOk(response);
		Achievement achievement = getAchievementCollectionRepository().findOrDefaultBy(player)
				.getAchievementById(PLAY_BATTLE_PRACTICE_ACHIEVEMENT_ID);
		assertThat(achievement.getStateAsTxt()).isEqualTo(Achievement.AchievementState.READY_TO_CLAIM.getState());
	}

	@Test
	public void whenTryToCompleteAnAchievementByClientThatIsNotAllowedThenAnExceptionShouldBeThrown() {
		// when
		Response response = whenCompleteAchievement(player.getUserId(), sessionToken, PURCHASE_CAPTAIN_ACHIEVEMENT_ID);
		// then
		assertThatResponseWasNotOk(response);
		Achievement achievement = getAchievementCollectionRepository().findOrDefaultBy(player)
				.getAchievementById(PURCHASE_CAPTAIN_ACHIEVEMENT_ID);
		assertThat(achievement.getStateAsTxt()).isEqualTo(Achievement.AchievementState.INITIAL.getState());
	}

	private void givenAPlayerInMapNumber2() {
		player.setMapNumber(2);
		getPlayerRepository().update(player);
	}

	private String givenACaptainIdToBuy() {
		CaptainsCollection captainsCollection = getCaptainCollectionRepository().findOrDefaultBy(player);
		return captainsCollection.getSelectedCaptainId().equals(CAPTAIN_ID_HELA) ? CAPTAIN_ID_REX : CAPTAIN_ID_HELA;
	}

	private Response whenBuyCaptain(String userId, String sessionToken, String captainId) {
		return client.target(getBuyCaptainEndpoint()).request().header(LOGIN_ID_HEADER, userId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(new BuyCaptainRequest(captainId), APPLICATION_JSON));
	}

	private Response whenClaimAchievementRewards(String userId, String sessionToken) {
		return client.target(getClaimAchievementRewardsEndpoint()).request().header(LOGIN_ID_HEADER, userId)
				.header(SESSION_TOKEN_HEADER, sessionToken).post(entity(new AchievementRequest(PURCHASE_CAPTAIN_ACHIEVEMENT_ID), APPLICATION_JSON));
	}

	private Response whenCompleteAchievement(String userId, String sessionToken, String achievementId) {
		return client.target(getCompleteAchievementEndpoint()).request().header(LOGIN_ID_HEADER, userId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(new AchievementRequest(achievementId), APPLICATION_JSON));
	}

	private String assertThatResponseWasOk(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		return responseAsJson;
	}

	private String assertThatResponseWasNotOk(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);
		return responseAsJson;
	}

	private void assertThatGemsWereDecreased(int playerGemsBeforeClaimRewards) {
		assertThat(getPlayerRepository().find(player.getUserId()).getInventory().getGems().getAmount()).isGreaterThan(playerGemsBeforeClaimRewards);
		int expectedGems = playerGemsBeforeClaimRewards + 80;
		assertThat(getPlayerRepository().find(player.getUserId()).getInventory().getGems().getAmount()).isEqualTo(expectedGems);
	}

	private String getBuyCaptainEndpoint() {
		return getBaseUrl().concat(URL_BUY_CAPTAIN);
	}

	private String getClaimAchievementRewardsEndpoint() {
		return getBaseUrl().concat(URL_CLAIM_ACHIEVEMENT);
	}

	private String getCompleteAchievementEndpoint() {
		return getBaseUrl().concat(URL_COMPLETE_ACHIEVEMENT);
	}
}
