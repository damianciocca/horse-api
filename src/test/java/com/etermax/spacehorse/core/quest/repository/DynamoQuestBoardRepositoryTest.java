package com.etermax.spacehorse.core.quest.repository;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.HARD;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.iterable.Extractor;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.etermax.spacehorse.mock.QuestScenarioBuilder;
import com.google.common.collect.Lists;

public class DynamoQuestBoardRepositoryTest {

	private static final String USER_ID = "10";
	private static final int CLAIM_COOL_DOWN_TIME_IN_SECONDS = 100;
	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();
	private DynamoQuestBoardRepository repository;
	private FixedServerTimeProvider timeProvider;
	private Player player;

	@Before
	public void setUp() {
		RULE.createSimpleTable(DynamoQuestBoard.class);
		DynamoDao dao = new DynamoDao(RULE.getAmazonDynamoDB());
		timeProvider = new FixedServerTimeProvider();
		CatalogRepository catalogRepository = aCatalogRepository();
		player = new PlayerScenarioBuilder(USER_ID).build();
		repository = new DynamoQuestBoardRepository(dao, timeProvider, catalogRepository);
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	@Test
	public void givenAPlayerWithoutQuestBoardWhenFindThenTheQuestBoardShouldFound() {

		QuestBoard defaultQuestBoard = repository.findOrDefaultBy(player);

		assertThat(defaultQuestBoard).isNotNull();
		thenAssertThatQuestBoardHasThreeQuestInInitialState(defaultQuestBoard);
	}

	@Test
	public void givenAQuestBoardWithInitialQuestWhenSaveThenTheQuestBoardShouldBePersistedSuccessfully() {

		QuestBoard questBoard = givenAQuestBoardWIthInitialQuest();

		whenSaveQuestBoard(questBoard);

		thenAssertThatQuestBoardWithInitialQuestShouldBePersisted();
	}

	@Test
	public void givenAQuestBoardWithStartedQuestWhenSaveThenTheQuestBoardShouldBePersistedSuccessfully() {

		QuestBoard questBoard = givenAQuestBoardWithOneQuestStarted(timeProvider);

		whenSaveQuestBoard(questBoard);

		thenAssertThatQuestBoardWithStartedQuestShouldBePersisted();
	}

	@Test
	public void givenAQuestBoardWithOneQuestStartedAndOtherClaimedWhenSaveThenTheQuestBoardShouldBePersistedSuccessfully() {

		QuestBoard questBoard = givenAQuestBoardWithOneQuestStartedAndOtherClaimed();

		whenSaveQuestBoard(questBoard);

		thenAssertThatQuestBoardWithStartedAndClaimedQuestsShouldBePersisted();
	}

	@Test
	public void givenAQuestBoardWithSkippedQuestWhenSaveThenTheQuestBoardShouldBePersistedSuccessfully() {

		QuestBoard questBoard = givenAQuestBoardWithOneQuestSkipped();

		whenSaveQuestBoard(questBoard);

		thenAssertThatQuestBoardWithSkippedQuestShouldBePersisted();
	}

	private void thenAssertThatQuestBoardWithInitialQuestShouldBePersisted() {
		QuestBoard questBoard = repository.findOrDefaultBy(player);
		assertThat(questBoard.getSlots()).hasSize(3);
		thenAssertThatQuestBoardHasThreeQuestInInitialState(questBoard);
	}

	private void thenAssertThatQuestBoardHasThreeQuestInInitialState(QuestBoard questBoard) {
		assertThat(questBoard.getSlots().values()).extracting((Extractor<QuestSlot, Object>) quests -> quests.getActiveQuest().getQuestId())
				.containsExactly("", "", "");
		assertThat(questBoard.getSkipTimeInSeconds()).isEqualTo(0L);
	}

	private void thenAssertThatQuestBoardWithStartedQuestShouldBePersisted() {
		QuestBoard questBoard = repository.findOrDefaultBy(player);
		assertThat(questBoard.getSlots()).hasSize(3);
		assertThat(questBoard.getSlots().values()).extracting((Extractor<QuestSlot, Object>) quests -> quests.getActiveQuest().getQuestId())
				.containsExactly("quest_ShipsDestroyedQuest", "", "");
		assertThat(questBoard.getSkipTimeInSeconds()).isEqualTo(0L);
	}

	private void thenAssertThatQuestBoardWithStartedAndClaimedQuestsShouldBePersisted() {
		QuestBoard questBoard = repository.findOrDefaultBy(player);
		assertThat(questBoard.getSlots()).hasSize(3);
		assertThat(questBoard.getSlots().values()).extracting((Extractor<QuestSlot, Object>) quests -> quests.getActiveQuest().getQuestId())
				.containsExactly("quest_ShipsDestroyedQuest", "", "quest_ShipsDestroyedQuest");
		assertThat(questBoard.getSkipTimeInSeconds()).isEqualTo(0L);
	}

	private void thenAssertThatQuestBoardWithSkippedQuestShouldBePersisted() {
		QuestBoard questBoard = repository.findOrDefaultBy(player);
		assertThat(questBoard.getSlots()).hasSize(3);
		assertThat(questBoard.getSlots().values()).extracting((Extractor<QuestSlot, Object>) quests -> quests.getActiveQuest().getQuestId())
				.containsExactly("quest_ShipsDestroyedQuest", "", "");

		long expectedSkipTime = timeProvider.getTimeNowAsSeconds() + 100;
		assertThat(questBoard.getSkipTimeInSeconds()).isEqualTo(expectedSkipTime);
	}

	private void whenSaveQuestBoard(QuestBoard questBoard) {
		repository.addOrUpdate(USER_ID, questBoard);
	}

	private QuestBoard givenAQuestBoardWIthInitialQuest() {
		return getQuestBoard(timeProvider);
	}

	private QuestBoard givenAQuestBoardWithOneQuestStartedAndOtherClaimed() {
		QuestBoard questBoard = givenAQuestBoardWithOneQuestStarted(timeProvider);
		Quest claimedQuest = new QuestScenarioBuilder().buildDefaultQuest();
		questBoard.putFreeQuest(HARD.toString(), claimedQuest);
		questBoard.getSlot(HARD.toString()).getActiveQuest().setCurrentProgress(10L);
		questBoard.claimQuest(HARD.toString(), CLAIM_COOL_DOWN_TIME_IN_SECONDS);

		return questBoard;
	}

	private QuestBoard givenAQuestBoardWithOneQuestStarted(FixedServerTimeProvider timeProvider) {
		QuestBoard questBoard = getQuestBoard(timeProvider);
		Quest startedQuest = new QuestScenarioBuilder().buildDefaultQuest();
		questBoard.putFreeQuest(EASY.toString(), startedQuest);
		return questBoard;
	}

	private QuestBoard getQuestBoard(FixedServerTimeProvider timeProvider) {
		Quest dailyQuest = new QuestScenarioBuilder().buildDefaultQuest();
		return new QuestBoard(timeProvider, getQuestBoardConfiguration(), dailyQuest);
	}

	private QuestBoard givenAQuestBoardWithOneQuestSkipped() {
		QuestBoard questBoard = givenAQuestBoardWithOneQuestStarted(timeProvider);
		questBoard.freeSkip(EASY.toString());
		return questBoard;
	}

	private CatalogRepository aCatalogRepository() {
		CatalogRepository catalogRepository = mock(CatalogRepository.class);
		Catalog catalog = mock(Catalog.class);
		String dailyQuestId = "daily_quest_definition_1";
		QuestDefinition questDefinition = new QuestDefinition(dailyQuestId, "ShipsDestroyedQuest", "courier_Quest", 5, 0, "EASY");
		GameConstants gameConstants = mock(GameConstants.class);
		when(gameConstants.getCurrentDailyQuestId()).thenReturn(dailyQuestId);
		when(gameConstants.getSkipTimeForQuestBoard()).thenReturn(100);

		when(catalog.getQuestCollection()).thenReturn(new CatalogEntriesCollection<>(newArrayList()));
		when(catalog.getGameConstants()).thenReturn(gameConstants);
		when(catalog.getDailyQuestCollection()).thenReturn(new CatalogEntriesCollection<>(Lists.newArrayList(questDefinition)));
		when(catalogRepository.getActiveCatalogWithTag(any())).thenReturn(catalog);
		return catalogRepository;
	}

	private QuestBoardConfiguration getQuestBoardConfiguration() {
		return new QuestBoardConfiguration(100, 10, 720, 1);
	}
}
