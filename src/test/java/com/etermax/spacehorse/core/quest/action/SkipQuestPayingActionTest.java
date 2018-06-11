package com.etermax.spacehorse.core.quest.action;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.quest.resource.response.QuestSkipResponse;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.shop.exception.NotEnoughGemsException;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class SkipQuestPayingActionTest {

	private static final int GEMS_COST_TO_SKIP = 10;
	private static final int PLAYER_INVENTORY_GEMS = 2000;
	private Player player;
	private Catalog catalog;
	private FixedServerTimeProvider timeProvider;
	private QuestBoardRepository questBoardRepository;
	private RefreshQuestAction refreshQuestAction;
	private SkipQuestPayingAction action;

	@Before
	public void setUp() {
		timeProvider = new FixedServerTimeProvider();
		catalog = MockUtils.mockCatalog();
		questBoardRepository = new InMemoryQuestBoardRepository(timeProvider);
		QuestFactory questFactory = new QuestFactory(timeProvider);
		action = new SkipQuestPayingAction(questBoardRepository, questFactory, mock(PlayerRepository.class));
		refreshQuestAction = new RefreshQuestAction(questBoardRepository, questFactory);
	}

	@Test
	public void givenOneQuestReadyStartedWhenSkipThenNewQuestShouldBeStarted() {
		// given
		givenAPlayerWithEnoughGemsToPaySkipQuest();
		givenAStartedQuest(EASY);
		// when
		QuestSkipResponse response = action.skip(player, catalog, EASY.toString());
		// Then
		thenAssertThatOneQuestWasSkipped(EASY.toString(), response.getQuestResponse().getId());
	}

	@Test
	public void givenOneQuestRecentlySkippedWhenTryToSkipAgainWithoutEnoughGemsThenAnExceptionShouldBeThrown() {
		// given
		givenAPlayerWithoutEnoughGemsToSkip();
		givenAStartedQuest(EASY);

		// when
		Throwable exception = catchThrowable(() -> action.skip(player, catalog, EASY.toString()));

		//then
		assertThat(exception).isInstanceOf(NotEnoughGemsException.class).hasMessageContaining("Insufficient gems");
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(2);
	}

	@Test
	public void givenOneQuestRecentlySkippedWhenTryToSkipAgainThenPlayerGemsDecreasedTen() {
		// given
		givenAPlayerWithEnoughGemsToPaySkipQuest();
		givenAStartedQuest(EASY);
		action.skip(player, catalog, EASY.toString()); // decrease 10 gems

		// when
		action.skip(player, catalog, EASY.toString()); // decrease 10 gems again

		//then
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(PLAYER_INVENTORY_GEMS - 20);
	}

	private void givenAPlayerWithEnoughGemsToPaySkipQuest() {
		player = new PlayerScenarioBuilder("10").withGems(PLAYER_INVENTORY_GEMS).build();
	}

	private void givenAPlayerWithoutEnoughGemsToSkip() {
		player = new PlayerScenarioBuilder("10").withGems(2).build();
	}

	private void givenAStartedQuest(QuestDifficultyType difficultyType) {
		Quest dailyQuest = mock(Quest.class);
		questBoardRepository.addOrUpdate(player.getUserId(), new QuestBoard(timeProvider, getQuestBoardConfiguration(), dailyQuest));
		refreshQuestAction.refresh(player, catalog, difficultyType.toString());
	}

	private QuestBoardConfiguration getQuestBoardConfiguration() {
		return new QuestBoardConfiguration(100, GEMS_COST_TO_SKIP,
				720, 1);
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

		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(PLAYER_INVENTORY_GEMS - 10);
	}

}

