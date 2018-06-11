package com.etermax.spacehorse.core.quest.action;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.model.RefreshPayingQuestCostCalculator;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.shop.exception.NotEnoughGemsException;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class RefreshQuestPayingActionTest {

	private static final String QUEST_ID_QUEST_SHIPS_DESTROYED_QUEST = "quest_ShipsDestroyedQuest_EASY";
	private static final String QUEST_TYPE_SHIPS_DESTROYED_QUEST = "ShipsDestroyedQuest";
	private Player player;
	private Catalog catalog;
	private FixedServerTimeProvider timeProvider;
	private QuestBoardRepository questBoardRepository;
	private RefreshQuestPayingAction refreshQuestPayingAction;

	@Before
	public void setUp() {
		timeProvider = new FixedServerTimeProvider();
		catalog = MockUtils.mockCatalog();
		player = new PlayerScenarioBuilder("10").build();
		questBoardRepository = new InMemoryQuestBoardRepository(timeProvider);
		QuestFactory questFactory = new QuestFactory(timeProvider);
		PlayerWinRateRepository playerWinRateRepository = mock(PlayerWinRateRepository.class);
		when(playerWinRateRepository.findOrCrateDefault(any())).thenReturn(new PlayerWinRate("10"));
		PlayerRepository playerRepository = mock(PlayerRepository.class);
		RefreshPayingQuestCostCalculator refreshPayingQuestCostCalculator = mock(RefreshPayingQuestCostCalculator.class);
		when(refreshPayingQuestCostCalculator.calculate(any(), any())).thenReturn(10);
		refreshQuestPayingAction = new RefreshQuestPayingAction(questBoardRepository, questFactory, playerRepository,
				refreshPayingQuestCostCalculator);
	}

	@Test
	public void whenRefreshPayingQuestThenNewQuestShouldBeStarted() {
		// given
		givenAPlayerWithEnoughGemsToPaySkipCooldownQuest();
		givenAStoredQuestBoard();
		// when
		refreshQuestPayingAction.refresh(player, catalog, EASY.toString());
		// Then
		thenAssertThatOneQuestWasRefreshed(EASY.toString(), QUEST_ID_QUEST_SHIPS_DESTROYED_QUEST, QUEST_TYPE_SHIPS_DESTROYED_QUEST, 10);

	}

	@Test
	public void whenRefreshPayingQuestThenPlayerGemsWereDecreased() {
		// given
		givenAPlayerWithEnoughGemsToPaySkipCooldownQuest();
		givenAStoredQuestBoard();
		// when
		refreshQuestPayingAction.refresh(player, catalog, EASY.toString());
		// Then
		thenPlayerGemsWereDecreased();
	}

	@Test
	public void whenRefreshPayingQuestWithoutEnoughGemsThenExceptionIsThrown() {
		// given
		givenAPlayerWithoutEnoughGemsToPaySkipCooldownQuest();
		givenAStoredQuestBoard();
		// when
		Throwable exception = catchThrowable(() -> refreshQuestPayingAction.refresh(player, catalog, EASY.toString()));
		// Then
		assertThat(exception).isInstanceOf(NotEnoughGemsException.class).hasMessageContaining("Insufficient gems");
	}

	private void thenPlayerGemsWereDecreased() {
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(90);
	}

	private void givenAPlayerWithEnoughGemsToPaySkipCooldownQuest() {
		player = new PlayerScenarioBuilder("10").withGems(100).build();
	}

	private void givenAPlayerWithoutEnoughGemsToPaySkipCooldownQuest() {
		player = new PlayerScenarioBuilder("10").withGems(0).build();
	}

	private void givenAStoredQuestBoard() {
		Quest dailyQuest = mock(Quest.class);
		questBoardRepository.addOrUpdate(player.getUserId(), new QuestBoard(timeProvider, getQuestBoardConfiguration(), dailyQuest));
	}

	private QuestBoardConfiguration getQuestBoardConfiguration() {
		return new QuestBoardConfiguration(100, 10, 720, 1);
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
