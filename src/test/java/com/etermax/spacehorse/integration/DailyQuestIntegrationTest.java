package com.etermax.spacehorse.integration;

import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.jayway.jsonpath.JsonPath;

public class DailyQuestIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(DailyQuestIntegrationTest.class);
	private static final String SHIPS_DESTROYED_QUEST_TYPE = "ShipsDestroyedQuest";
	private static final String DAILY_QUEST_ID = "daily_quest_definition_1";
	private static final String URL_DAILY_QUEST_REFRESH = "/v1/quest/daily/refresh";
	private static final String URL_DAILY_QUEST_CLAIM = "/v1/quest/daily/claim";
	private String sessionToken;
	private Player player;

	@Before
	public void setUp() throws Exception {
		String responseAsJson = aNewPlayerLogged();
		String playerId = extractPlayerIdFrom(responseAsJson);
		player = getPlayerRepository().find(playerId);
		sessionToken = extractSessionTokenFrom(responseAsJson);
	}

	@Test
	public void whenRefreshADailyQuestThenANewDailyQuestShouldBeCreated() {
		increaseADay();
		claimADailyQuest();

		Response response = whenRefreshADailyQuest(player.getUserId(), sessionToken);

		thenANewQuestShouldBeCreatedForRefresh(response);
	}

	@Test
	public void whenClaimACompletedQuestThenRewardsShouldBeApplyToInventory() {
		givenACompletedDailyQuest();

		Response response = whenClaimDailyQuest(player.getUserId(), sessionToken);

		thenTheRewardsWereApplied(response);
	}

	private void claimADailyQuest() {
		QuestBoard questBoard = getQuestBoardRepository().findOrDefaultBy(player);
		questBoard.getDailyQuest().claim();
		getQuestBoardRepository().addOrUpdate(player.getUserId(), questBoard);
	}

	private void increaseADay() {
		DateTime nextActivationTime = getServerTimeProvider().getDateTime().plusDays(1);
		getServerTimeProvider().changeTime(nextActivationTime);
	}

	private void thenTheRewardsWereApplied(Response response) {
		player = getPlayerRepository().find(player.getUserId());
		QuestBoard questBoard = getQuestBoardRepository().findOrDefaultBy(player);
		assertJsonResponseForCompletedQuest(response);
		assertThatActiveQuestWasClaimedInRepository(questBoard);
		assertThatInventoryWasUpdatedInRepository(player);
	}

	private void assertThatActiveQuestWasClaimedInRepository(QuestBoard questBoard) {
		Quest quest = questBoard.getDailyQuest();
		assertThat(quest.getGoalAmount()).isEqualTo(5L);
		assertThat(quest.getQuestType()).isEqualTo(SHIPS_DESTROYED_QUEST_TYPE);
		assertThat(quest.getQuestId()).isEqualTo(DAILY_QUEST_ID);
		assertThat(quest.isClaimed()).isTrue();
	}

	private void thenANewQuestShouldBeCreatedForRefresh(Response response) {
		QuestBoard questBoard = getQuestBoardRepository().findOrDefaultBy(player);
		assertJsonResponseForRefreshedQuest(response);
		assertThatActiveQuestWasCreatedInRepository(questBoard);
	}

	private void assertThatActiveQuestWasCreatedInRepository(QuestBoard questBoard) {
		Quest quest = questBoard.getDailyQuest();
		assertThat(quest.getGoalAmount()).isEqualTo(5L);
		assertThat(quest.getQuestType()).isEqualTo(SHIPS_DESTROYED_QUEST_TYPE);
		assertThat(quest.getQuestId()).isEqualTo(DAILY_QUEST_ID);
		assertThat(quest.isStarted()).isTrue();

	}

	private void assertThatInventoryWasUpdatedInRepository(Player player) {
		Inventory inventory = player.getInventory();
		assertThat(inventory.getGems().getAmount()).isEqualTo(100);
		assertThat(inventory.getGold().getAmount()).isGreaterThan(100);
		assertThat(inventory.getCardParts().getAmounts().size()).isGreaterThanOrEqualTo(1);
	}

	private void givenACompletedDailyQuest() {
		increaseADay();
		claimADailyQuest();
		whenRefreshADailyQuest(player.getUserId(), sessionToken);
		PlayerRepository playerRepository = getPlayerRepository();
		player = playerRepository.find(player.getUserId());
		increaseQuestProgressToTen(player);
		playerRepository.update(player);
	}

	private void increaseQuestProgressToTen(Player player) {
		QuestBoard questBoard = getQuestBoardRepository().findOrDefaultBy(player);
		questBoard.getDailyQuest().setCurrentProgress(5L);
		getQuestBoardRepository().addOrUpdate(player.getUserId(), questBoard);
	}

	private Response whenRefreshADailyQuest(String playerId, String sessionToken) {
		return client.target(getRefreshDailyQuestEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(null);
	}

	private Response whenClaimDailyQuest(String playerId, String sessionToken) {
		return client.target(getClaimDailyQuestEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(null);
	}

	private void assertJsonResponseForRefreshedQuest(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);

		String questId = JsonPath.parse(responseAsJson).read("$.newQuest.id", String.class);
		assertThat(questId).isEqualTo(DAILY_QUEST_ID);

		Integer startProgress = JsonPath.parse(responseAsJson).read("$.newQuest.currentProgress", Integer.class);
		assertThat(startProgress).isEqualTo(0);

		String claimState = JsonPath.parse(responseAsJson).read("$.newQuest.state", String.class);
		assertThat(claimState).isEqualTo(Quest.STARTED_STATE);
	}

	private void assertJsonResponseForCompletedQuest(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);

		Integer rewards = JsonPath.parse(responseAsJson).read("$.rewardResponses.size()", Integer.class);
		assertThat(rewards).isGreaterThanOrEqualTo(1);
	}

	private String getRefreshDailyQuestEndpoint() {
		return getBaseUrl().concat(URL_DAILY_QUEST_REFRESH);
	}

	private String getClaimDailyQuestEndpoint() {
		return getBaseUrl().concat(URL_DAILY_QUEST_CLAIM);
	}

}
