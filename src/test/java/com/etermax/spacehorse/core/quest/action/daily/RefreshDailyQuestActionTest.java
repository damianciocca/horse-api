package com.etermax.spacehorse.core.quest.action.daily;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.exception.QuestRefreshTimeNotReachedException;
import com.etermax.spacehorse.core.quest.exception.DailyQuestNotClaimedException;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class RefreshDailyQuestActionTest {

	private FixedServerTimeProvider timeProvider;
	private Catalog catalog;
	private QuestFactory questFactory;
	private Player player;
	private InMemoryQuestBoardRepository questBoardRepository;
	private RefreshDailyQuestAction refreshDailyQuestAction;
	private Quest dailyQuest;
	private Quest refreshDailyQuest;

	@Before
	public void setUp() {
		timeProvider = new FixedServerTimeProvider();
		catalog = MockUtils.mockCatalog();
		player = new PlayerScenarioBuilder("10").build();
		questBoardRepository = new InMemoryQuestBoardRepository(timeProvider);
		questFactory = new QuestFactory(timeProvider);
		PlayerWinRateRepository playerWinRateRepository = mock(PlayerWinRateRepository.class);
		when(playerWinRateRepository.findOrCrateDefault(any())).thenReturn(new PlayerWinRate("10"));
		refreshDailyQuestAction = new RefreshDailyQuestAction(questBoardRepository, questFactory);
	}

	@Test
	public void whenDailyQuestIsCompleteAndIsClaimedThenReturnAQuestWithRefreshTimeUpdated() {
		givenAClaimedQuest();
		aTimeIsIncreasedIn(1);

		whenRefreshDailyQuest();

		thenDailyQuestRefreshTimeIsUpdated();
	}

	@Test
	public void whenDailyQuestRefreshTimeIsNotRemainingThenExceptionIsThrown() {
		givenAClaimedQuest();

		Throwable exception = catchThrowable(this::whenRefreshDailyQuest);

		Assertions.assertThat(exception).isInstanceOf(QuestRefreshTimeNotReachedException.class);
	}

	@Test
	public void whenDailyQuestIsNotCompleteThenReturnAQuestWithRefreshTimeUpdated() {
		givenAIncompleteDailyQuest();
		aTimeIsIncreasedIn(1);

		whenRefreshDailyQuest();

		thenDailyQuestRefreshTimeIsUpdated();
	}

	private void thenDailyQuestRefreshTimeIsUpdated() {
		assertThat(refreshDailyQuest.getRefreshTimeInSeconds()).isNotEqualTo(dailyQuest.getRefreshTimeInSeconds());
	}

	private void whenRefreshDailyQuest() {
		refreshDailyQuest = refreshDailyQuestAction.refresh(player, catalog);
	}

	private void aTimeIsIncreasedIn(int days) {
		DateTime increase = timeProvider.getDateTime().plusDays(days);
		timeProvider.changeTime(increase);
	}

	private void givenAIncompleteDailyQuest() {
		dailyQuest = questBoardRepository.findOrDefaultBy(player).getDailyQuest();
	}

	private void givenAClaimedQuest() {
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		dailyQuest = questBoard.getDailyQuest();
		dailyQuest.incrementProgress();
		questBoard.claimDailyQuest();
		questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
	}

}
