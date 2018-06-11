package com.etermax.spacehorse.integration;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.HARD;
import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.MEDIUM;
import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.resource.request.QuestApplyRewardsRequest;
import com.etermax.spacehorse.core.quest.resource.request.QuestRefreshRequest;
import com.etermax.spacehorse.core.quest.resource.request.QuestSkipRequest;
import com.jayway.jsonpath.JsonPath;

public class QuestIntegrationTest extends BaseIntegrationTest {

	private static final Logger logger = LoggerFactory.getLogger(QuestIntegrationTest.class);

	private static final String SHIPS_DESTROYED_QUEST_TYPE = "ShipsDestroyedQuest";
	private static final String QUEST_SHIPS_DESTROYED_QUEST_ID = "quest_ShipsDestroyedQuest_EASY";
	private static final String NEXT_QUEST_SIMPLE_VICTORIES_QUEST_ID = "quest_SimpleVictoriesQuest_EASY";
	private static final String NEXT_SIMPLE_VICTORIES_QUEST_TYPE = "SimpleVictoriesQuest";

	private static final String URL_QUEST_REFRESH = "/v1/quest/refresh";
	private static final String URL_QUEST_CLAIM = "/v1/quest/claim";
	private static final String URL_QUEST_SKIP = "/v1/quest/skip";
	private static final String URL_QUEST_SKIP_PAYING = "/v1/quest/skipPaying";
	private static final String URL_QUEST_REFRESH_PAYING = "/v1/quest/refreshPaying";

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
	public void whenRefreshAQuestThenANewQuestShouldBeCreated() {
		Response response = whenRefreshAnEasyQuest(player.getUserId(), sessionToken);
		thenANewQuestShouldBeCreatedForRefresh(response);
	}

	@Test
	public void whenClaimACompletedQuestThenRewardsShouldBeApplyToInventory() {

		givenAnEasyQuestStarted();

		Response response = whenClaimEasyQuest(player.getUserId(), sessionToken);

		thenTheRewardsWereApplied(response);
	}

	@Test
	public void whenSkipAQuestThenANewQuestShouldBeCreated() {
		givenAnEasyQuestStarted();

		Response response = whenSkipEasyQuest(player.getUserId(), sessionToken);

		thenANewQuestShouldBeCreatedForSkipped(response);
	}

	@Test
	public void whenSkipAQuestPaysThenANewQuestShouldBeCreated() {
		givenAnEasyQuestStarted();

		whenSkipQuestPaying(player.getUserId(), sessionToken);

		thenPlayerGemsWasDecreased(player.getUserId(), 90);
	}

	@Test
	public void whenRefreshAQuestPaysThenANewQuestShouldBeCreatedCase() {
		givenAnEasyQuestStarted();
		whenClaimEasyQuest(player.getUserId(), sessionToken);

		whenRefreshQuestPaying(player.getUserId(), sessionToken);

		thenPlayerGemsWasDecreased(player.getUserId(), 99);
	}

	private void thenPlayerGemsWasDecreased(String userId, int expectedGemsCost) {
		player = getPlayerRepository().find(userId);
		Inventory inventory = player.getInventory();
		assertThat(inventory.getGems().getAmount()).isEqualTo(expectedGemsCost);
	}

	private void thenANewQuestShouldBeCreatedForRefresh(Response response) {
		QuestBoard questBoard = getQuestBoardRepository().findOrDefaultBy(player);
		assertJsonResponseForRefreshedQuest(response);
		assertThatActiveQuestWasCreatedInRepository(questBoard);
	}

	private void thenTheRewardsWereApplied(Response response) {
		player = getPlayerRepository().find(player.getUserId());
		QuestBoard questBoard = getQuestBoardRepository().findOrDefaultBy(player);
		assertJsonResponseForCompletedQuest(response);
		assertThatActiveQuestWasClaimedInRepository(questBoard);
		assertThatInventoryWasUpdatedInRepository(player);
	}

	private void thenANewQuestShouldBeCreatedForSkipped(Response response) {
		QuestBoard questBoard = getQuestBoardRepository().findOrDefaultBy(player);
		assertJsonResponseForSkippedQuest(response);
		assertThatActiveQuestWasSkippedInRepository(questBoard);
	}

	private void assertThatInventoryWasUpdatedInRepository(Player player) {
		Inventory inventory = player.getInventory();
		assertThat(inventory.getGems().getAmount()).isEqualTo(100);
		assertThat(inventory.getGold().getAmount()).isGreaterThan(100);
		assertThat(inventory.getCardParts().getAmounts().size()).isGreaterThanOrEqualTo(1);
	}

	private void givenAnEasyQuestStarted() {
		whenRefreshAnEasyQuest(player.getUserId(), sessionToken);
		PlayerRepository playerRepository = getPlayerRepository();
		player = playerRepository.find(player.getUserId());
		increaseQuestProgressToTen(player);
		playerRepository.update(player);
	}

	private void increaseQuestProgressToTen(Player player) {
		QuestBoard questBoard = getQuestBoardRepository().findOrDefaultBy(player);
		questBoard.getSlot(EASY.toString()).getActiveQuest().setCurrentProgress(10L);
		getQuestBoardRepository().addOrUpdate(player.getUserId(), questBoard);
	}

	private void assertThatActiveQuestWasCreatedInRepository(QuestBoard questBoard) {
		QuestSlot slot = questBoard.getSlot(EASY.toString());
		assertThat(slot.getActiveQuest().getGoalAmount()).isEqualTo(10L);
		assertThat(slot.getActiveQuest().getQuestType()).isEqualTo(SHIPS_DESTROYED_QUEST_TYPE);
		assertThat(slot.getActiveQuest().getQuestId()).isEqualTo(QUEST_SHIPS_DESTROYED_QUEST_ID);
		assertThat(slot.hasInitialQuest()).isFalse();
		assertThat(slot.hasStartedQuest()).isTrue();
		assertThat(slot.hasClaimedQuest()).isFalse();
		assertThat(slot.hasSkippedQuest()).isFalse();

		assertThatQuestInSlotIsInInitialState(questBoard, MEDIUM);
		assertThatQuestInSlotIsInInitialState(questBoard, HARD);
	}

	private void assertThatActiveQuestWasClaimedInRepository(QuestBoard questBoard) {
		QuestSlot slot = questBoard.getSlot(EASY.toString());
		assertThat(slot.getActiveQuest().getGoalAmount()).isEqualTo(10L);
		assertThat(slot.getActiveQuest().getQuestType()).isEqualTo(SHIPS_DESTROYED_QUEST_TYPE);
		assertThat(slot.getActiveQuest().getQuestId()).isEqualTo(QUEST_SHIPS_DESTROYED_QUEST_ID);
		assertThat(slot.hasInitialQuest()).isFalse();
		assertThat(slot.hasStartedQuest()).isFalse();
		assertThat(slot.hasClaimedQuest()).isTrue();
		assertThat(slot.hasSkippedQuest()).isFalse();

		assertThatQuestInSlotIsInInitialState(questBoard, MEDIUM);
		assertThatQuestInSlotIsInInitialState(questBoard, HARD);
	}

	private void assertThatActiveQuestWasSkippedInRepository(QuestBoard questBoard) {
		QuestSlot slot = questBoard.getSlot(EASY.toString());
		assertThat(slot.getActiveQuest().getGoalAmount()).isEqualTo(3);
		assertThat(slot.getActiveQuest().getQuestType()).isEqualTo(NEXT_SIMPLE_VICTORIES_QUEST_TYPE);
		assertThat(slot.getActiveQuest().getQuestId()).isEqualTo(NEXT_QUEST_SIMPLE_VICTORIES_QUEST_ID);
		assertThat(slot.hasInitialQuest()).isFalse();
		assertThat(slot.hasStartedQuest()).isTrue();
		assertThat(slot.hasClaimedQuest()).isFalse();
		assertThat(slot.hasSkippedQuest()).isFalse();

		assertThatQuestInSlotIsInInitialState(questBoard, MEDIUM);
		assertThatQuestInSlotIsInInitialState(questBoard, HARD);
	}

	private void assertThatQuestInSlotIsInInitialState(QuestBoard questBoard, QuestDifficultyType difficultyType) {
		QuestSlot slot = questBoard.getSlot(difficultyType.toString());
		assertThat(slot.getActiveQuest().getGoalAmount()).isEqualTo(0L);
		assertThat(slot.getActiveQuest().getQuestType()).isEqualTo("");
		assertThat(slot.getActiveQuest().getQuestId()).isEqualTo("");
		assertThat(slot.hasInitialQuest()).isTrue();
		assertThat(slot.hasStartedQuest()).isFalse();
		assertThat(slot.hasClaimedQuest()).isFalse();
		assertThat(slot.hasSkippedQuest()).isFalse();
	}

	private Response whenSkipQuestPaying(String playerId, String sessionToken) {
		QuestSkipRequest request = new QuestSkipRequest(EASY.toString());
		return client.target(getSkipQuestPayingEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private Response whenRefreshQuestPaying(String playerId, String sessionToken) {
		QuestRefreshRequest request = new QuestRefreshRequest(EASY.toString());
		return client.target(getRefreshQuestPayingEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private Response whenSkipEasyQuest(String playerId, String sessionToken) {
		QuestSkipRequest request = new QuestSkipRequest(EASY.toString());
		return client.target(getQuestSkipEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private Response whenRefreshAnEasyQuest(String playerId, String sessionToken) {
		QuestRefreshRequest request = new QuestRefreshRequest(EASY.toString());
		return client.target(getQuestRefreshEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private Response whenClaimEasyQuest(String playerId, String sessionToken) {
		QuestApplyRewardsRequest request = new QuestApplyRewardsRequest(EASY.toString());
		return client.target(getQuestClaimEndpoint()).request().header(LOGIN_ID_HEADER, playerId).header(SESSION_TOKEN_HEADER, sessionToken)
				.post(entity(request, APPLICATION_JSON));
	}

	private void assertJsonResponseForRefreshedQuest(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);

		String questId = JsonPath.parse(responseAsJson).read("$.newQuest.id", String.class);
		assertThat(questId).isEqualTo(QUEST_SHIPS_DESTROYED_QUEST_ID);

		Integer startProgress = JsonPath.parse(responseAsJson).read("$.newQuest.currentProgress", Integer.class);
		assertThat(startProgress).isEqualTo(0);

		String claimState = JsonPath.parse(responseAsJson).read("$.newQuest.state", String.class);
		assertThat(claimState).isEqualTo(Quest.STARTED_STATE);
	}

	private void assertJsonResponseForSkippedQuest(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);

		String questId = JsonPath.parse(responseAsJson).read("$.newQuest.id", String.class);
		assertThat(questId).isEqualTo(NEXT_QUEST_SIMPLE_VICTORIES_QUEST_ID);

		Integer startProgress = JsonPath.parse(responseAsJson).read("$.newQuest.currentProgress", Integer.class);
		assertThat(startProgress).isEqualTo(0);

		String claimState = JsonPath.parse(responseAsJson).read("$.newQuest.state", String.class);
		assertThat(claimState).isEqualTo(Quest.STARTED_STATE);

		long expectedSkipTime = getServerTimeProvider().getTimeNowAsSeconds() + 100;
		long skipTimeInSeconds = JsonPath.parse(responseAsJson).read("$.skipTimeInSeconds", Long.class);

		assertThat(skipTimeInSeconds).isEqualTo(expectedSkipTime);
	}

	private void assertJsonResponseForCompletedQuest(Response response) {
		assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
		String responseAsJson = mapToJsonStringFrom(response);
		logger.info("----------------- response [ {} ]", responseAsJson);

		Integer rewards = JsonPath.parse(responseAsJson).read("$.rewardResponses.size()", Integer.class);
		assertThat(rewards).isGreaterThanOrEqualTo(1);

		long expectedRefreshTime = getServerTimeProvider().getTimeNowAsSeconds() + 100;
		Long refreshTimeInSeconds = JsonPath.parse(responseAsJson).read("$.refreshTimeInSeconds", Long.class);
		assertThat(refreshTimeInSeconds).isEqualTo(expectedRefreshTime);
	}

	private String getQuestRefreshEndpoint() {
		return getBaseUrl().concat(URL_QUEST_REFRESH);
	}

	private String getQuestClaimEndpoint() {
		return getBaseUrl().concat(URL_QUEST_CLAIM);
	}

	private String getQuestSkipEndpoint() {
		return getBaseUrl().concat(URL_QUEST_SKIP);
	}

	private String getSkipQuestPayingEndpoint() {
		return getBaseUrl().concat(URL_QUEST_SKIP_PAYING);
	}

	private String getRefreshQuestPayingEndpoint() {
		return getBaseUrl().concat(URL_QUEST_REFRESH_PAYING);
	}

}
