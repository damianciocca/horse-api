package com.etermax.spacehorse.core.quest.action;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.HARD;
import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.exception.QuestAlreadyStartedException;
import com.etermax.spacehorse.core.quest.exception.QuestBoardSlotNotFoundException;
import com.etermax.spacehorse.core.quest.exception.QuestRefreshTimeNotReachedException;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.etermax.spacehorse.mock.QuestScenarioBuilder;

public class RefreshQuestActionTest {

	private static final String UNKNOWN_SLOT_ID = "unknown_slot";
	private static final String QUEST_ID_QUEST_SIMPLE_VICTORIES_QUEST = "quest_SimpleVictoriesQuest_EASY";
	private static final String QUEST_TYPE_SIMPLE_VICTORIES_QUEST = "SimpleVictoriesQuest";
	private static final String QUEST_ID_QUEST_SHIPS_DESTROYED_QUEST = "quest_ShipsDestroyedQuest_EASY";
	private static final String QUEST_TYPE_SHIPS_DESTROYED_QUEST = "ShipsDestroyedQuest";
	private static final String QUEST_ID_QUEST_SHIPS_DESTROYED_QUEST_HARD = "quest_ShipsDestroyedQuest_HARD";

	private Player player;
	private Catalog catalog;
	private FixedServerTimeProvider timeProvider;
	private QuestBoardRepository questBoardRepository;
	private RefreshQuestAction refreshAction;
	private QuestBoard questBoard;

	@Before
	public void setUp() {
		timeProvider = new FixedServerTimeProvider();
		catalog = MockUtils.mockCatalog();
		player = new PlayerScenarioBuilder("10").build();
		questBoardRepository = new InMemoryQuestBoardRepository(timeProvider);
		QuestFactory questFactory = new QuestFactory(timeProvider);
		PlayerWinRateRepository playerWinRateRepository = mock(PlayerWinRateRepository.class);
		when(playerWinRateRepository.findOrCrateDefault(any())).thenReturn(new PlayerWinRate("10"));
		refreshAction = new RefreshQuestAction(questBoardRepository, questFactory);
		questBoard = getQuestBoard();
	}

	@Test
	public void givenAnEmptyQuestBoardWhenRefreshFirstSlotThenTheANewQuestShouldBeFound() {
		// given
		givenAStoredQuestBoard();
		// when
		refreshAction.refresh(player, catalog, EASY.toString());
		// Then
		thenAssertThatOneQuestWasRefreshed(EASY.toString(), QUEST_ID_QUEST_SHIPS_DESTROYED_QUEST, QUEST_TYPE_SHIPS_DESTROYED_QUEST, 10);
	}

	@Test
	public void givenAQuestBoardWithAnActiveQuestStartedWhenRefreshThenAnExceptionShouldBeThrown() {
		// given
		givenAStoredQuestBoard();
		refreshAction.refresh(player, catalog, EASY.toString());
		// when - then
		assertThatThrownBy(() -> refreshAction.refresh(player, catalog, EASY.toString())).isInstanceOf(QuestAlreadyStartedException.class);
	}

	@Test
	public void givenAnEmptyQuestBoardWhenRefreshThreeTimesThenTheQuestBoardShouldBeComplete() {
		// given
		givenAStoredQuestBoard();
		refreshAction.refresh(player, catalog, EASY.toString());
		refreshAction.refresh(player, catalog, MEDIUM.toString());
		// when
		refreshAction.refresh(player, catalog, HARD.toString());
		// Then
		thenAssertThatOneQuestWasRefreshed(HARD.toString(), QUEST_ID_QUEST_SHIPS_DESTROYED_QUEST_HARD, QUEST_TYPE_SHIPS_DESTROYED_QUEST, 10);
	}

	@Test
	public void givenAQuestBoardWhenTryToRefreshAnUnknownSlotThenAnExceptionShouldBeThrown() {
		// given
		givenAStoredQuestBoard();
		// when - then
		assertThatThrownBy(() -> refreshAction.refresh(player, catalog, UNKNOWN_SLOT_ID)).isInstanceOf(QuestBoardSlotNotFoundException.class);
	}

	@Test
	public void givenAQuestBoardWithOneQuestClaimedWhenTryToRefreshAndRefreshTimeIsNotReachedThenThenAnExceptionShouldBeThrown() {
		// given
		givenAQuestBoardWithOneQuest();
		theQuestIsCompleted();
		aTimeIsIncreasedIn(15);
		aQuestIsClaimed();
		aTimeIsIncreasedIn(15);
		// when - then
		assertThatThrownBy(() -> refreshAction.refresh(player, catalog, EASY.toString())).isInstanceOf(QuestRefreshTimeNotReachedException.class);
	}

	@Test
	public void givenAQuestBoardWithOneQuestClaimedWhenTryToRefreshAndRefreshTimeIsReachedThenANewQuestShouldBeFound() {
		// given
		givenAQuestBoardWithOneQuest();
		theQuestIsCompleted();
		aQuestIsClaimed();
		aTimeIsIncreasedIn(102);
		// when
		refreshAction.refresh(player, catalog, EASY.toString());
		// Then
		thenAssertThatOneQuestWasRefreshed(EASY.toString(), QUEST_ID_QUEST_SIMPLE_VICTORIES_QUEST, QUEST_TYPE_SIMPLE_VICTORIES_QUEST, 3);
	}

	private QuestBoard getQuestBoard() {
		Quest dailyQuest = mock(Quest.class);
		return new QuestBoard(timeProvider, getQuestBoardConfiguration(), dailyQuest);
	}

	private QuestBoardConfiguration getQuestBoardConfiguration() {
		return new QuestBoardConfiguration(100, 10, 720, 1);
	}

	private void givenAStoredQuestBoard() {
		questBoardRepository.addOrUpdate(player.getUserId(), getQuestBoard());
	}

	private void aTimeIsIncreasedIn(int seconds) {
		DateTime increase = timeProvider.getDateTime().plusSeconds(seconds);
		timeProvider.changeTime(increase);
	}

	private void aQuestIsClaimed() {
		long refreshTimeInSeconds = timeProvider.getTimeNowAsSeconds() + 100;
		questBoard.claimQuest(EASY.toString(), refreshTimeInSeconds);
		questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
	}

	private void theQuestIsCompleted() {
		questBoardRepository.findOrDefaultBy(player).getSlot(EASY.toString()).getActiveQuest().setCurrentProgress(10L);
	}

	private void givenAQuestBoardWithOneQuest() {
		Quest quest = new QuestScenarioBuilder().buildDefaultQuest();
		questBoard.putFreeQuest(EASY.toString(), quest);
		questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
	}

	private void thenAssertThatOneQuestWasRefreshed(String slotId, String questId, String questType, int goalAmount) {
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		QuestSlot slot = questBoard.getSlot(slotId);
		assertThat(slot.getActiveQuest()).isNotNull();
		assertThat(slot.getActiveQuest().getQuestId()).isEqualTo(questId);
		assertThat(slot.getActiveQuest().getQuestType()).isEqualTo(questType);
		assertThat(slot.getActiveQuest().getRefreshTimeInSeconds()).isEqualTo(0);
		assertThat(slot.getActiveQuest().getCurrentProgress()).isEqualTo(0);
		assertThat(slot.getActiveQuest().getGoalAmount()).isEqualTo(goalAmount);

		assertThat(slot.hasStartedQuest()).isTrue();
		assertThat(slot.hasClaimedQuest()).isFalse();
		assertThat(slot.hasCompletedQuest()).isFalse();
	}

}
