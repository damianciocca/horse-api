package com.etermax.spacehorse.core.quest.action;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
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
import com.etermax.spacehorse.core.quest.exception.QuestBoardSkipTimeNotReachedException;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.exception.QuestBoardAlreadyInitializedException;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.quest.resource.response.QuestSkipResponse;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class SkipQuestActionTest {

	private Player player;
	private Catalog catalog;
	private FixedServerTimeProvider timeProvider;
	private QuestBoardRepository questBoardRepository;
	private RefreshQuestAction refreshQuestAction;
	private SkipQuestAction skipQuestAction;

	@Before
	public void setUp() {
		timeProvider = new FixedServerTimeProvider();
		catalog = MockUtils.mockCatalog();
		player = new PlayerScenarioBuilder("10").build();
		questBoardRepository = new InMemoryQuestBoardRepository(timeProvider);
		PlayerWinRateRepository playerWinRateRepository = mock(PlayerWinRateRepository.class);
		when(playerWinRateRepository.findOrCrateDefault(any())).thenReturn(new PlayerWinRate("10"));
		QuestFactory questFactory = new QuestFactory(timeProvider);
		skipQuestAction = new SkipQuestAction(questBoardRepository, questFactory);
		refreshQuestAction = new RefreshQuestAction(questBoardRepository, questFactory);
	}

	@Test
	public void givenOneQuestReadyStartedWhenSkipThenNewQuestShouldBeStarted() {
		// given
		givenAStartedQuest(EASY);
		// when
		QuestSkipResponse response = skipQuestAction.skip(player, catalog, EASY.toString());
		// Then
		thenAssertThatOneQuestWasSkipped(EASY.toString(), response.getQuestResponse().getId());
	}

	@Test
	public void givenOneQuestRecentlySkippedWhenTryToSkipAgainThenAnExceptionShouldBeThrown() {
		// given
		givenAStartedQuest(EASY);
		skipQuestAction.skip(player, catalog, EASY.toString());
		// when - then
		assertThatThrownBy(() -> skipQuestAction.skip(player, catalog, EASY.toString())).isInstanceOf(QuestBoardSkipTimeNotReachedException.class)
				.hasMessageContaining("Quest board skip time not reached. Unable to skip");
	}

	@Test
	public void givenOneQuestSkippedWhenPassTimeAndSkipAgainThenANewQuestShouldBeStarted() {
		// given
		givenAStartedQuest(EASY);
		skipQuestAction.skip(player, catalog, EASY.toString());
		aTimeIsIncreasedInHours(24);
		// when
		QuestSkipResponse response = skipQuestAction.skip(player, catalog, EASY.toString());
		// Then
		thenAssertThatOneQuestWasSkipped(EASY.toString(), response.getQuestResponse().getId());
	}

	@Test
	public void givenOneQuestStartedForMediumDifficultyWhenTryToSkipForEasyDifficultyThenAnExceptionShouldBeThrown() throws Exception {
		// given
		givenAStartedQuest(MEDIUM);
		// when - then
		assertThatThrownBy(() -> skipQuestAction.skip(player, catalog, EASY.toString())).isInstanceOf(QuestBoardAlreadyInitializedException.class);
	}

	private void givenAStartedQuest(QuestDifficultyType difficultyType) {
		Quest dailyQuest = mock(Quest.class);
		questBoardRepository.addOrUpdate(player.getUserId(), new QuestBoard(timeProvider, getQuestBoardConfiguration(), dailyQuest));
		refreshQuestAction.refresh(player, catalog, difficultyType.toString());
	}

	private QuestBoardConfiguration getQuestBoardConfiguration() {
		return new QuestBoardConfiguration(100, 10, 720, 1);
	}

	private void aTimeIsIncreasedInHours(int hours) {
		DateTime increase = timeProvider.getDateTime().plusHours(hours);
		timeProvider.changeTime(increase);
	}

	private void thenAssertThatOneQuestWasSkipped(String slotId, String questId) {
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		QuestSlot slot = questBoard.getSlot(slotId);

		assertThat(questBoard.getSkipTimeInSeconds()).isGreaterThan(0);

		assertThat(slot.getActiveQuest()).isNotNull();
		assertThat(slot.getActiveQuest()).isNotNull();
		assertThat(slot.getActiveQuest().getQuestId()).isEqualTo(questId);
		assertThat(slot.getActiveQuest().getRefreshTimeInSeconds()).isEqualTo(0);

		assertThat(slot.hasStartedQuest()).isTrue();
		assertThat(slot.hasClaimedQuest()).isFalse();
		assertThat(slot.hasSkippedQuest()).isFalse();
		assertThat(slot.hasCompletedQuest()).isFalse();
	}

}
