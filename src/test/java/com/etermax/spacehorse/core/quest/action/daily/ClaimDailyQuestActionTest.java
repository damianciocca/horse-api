package com.etermax.spacehorse.core.quest.action.daily;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.exception.QuestAlreadyClaimedException;
import com.etermax.spacehorse.core.quest.exception.QuestIncompleteException;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestRewardAssigner;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.quest.resource.response.QuestClaimResponse;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.NewPlayerConfigurationBuilder;

public class ClaimDailyQuestActionTest {

	private Player player;
	private Catalog catalog;
	private ClaimDailyQuestAction claimDailyQuestAction;
	private InMemoryQuestBoardRepository questBoardRepository;
	private FixedServerTimeProvider timeProvider;
	private QuestClaimResponse claimResponse;

	@Before
	public void setUp() {
		timeProvider = new FixedServerTimeProvider();

		PlayerRepository playerRepository = mock(PlayerRepository.class);

		GetRewardsDomainService getRewardsDomainService = new GetRewardsDomainService();
		ApplyRewardDomainService applyRewardDomainService = new ApplyRewardDomainService();

		questBoardRepository = new InMemoryQuestBoardRepository(timeProvider);

		QuestRewardAssigner questRewardAssigner = new QuestRewardAssigner(playerRepository, applyRewardDomainService, getRewardsDomainService,
				timeProvider, mock(AchievementsFactory.class));

		claimDailyQuestAction = new ClaimDailyQuestAction(questRewardAssigner, questBoardRepository);
		catalog = MockUtils.mockCatalog();

		NewPlayerConfiguration newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
		player = Player.buildNewPlayer("10", ABTag.emptyABTag(), timeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
	}

	@Test
	public void whenClaimACompleteDailyQuestThenTheDailyQuestRewardIsReturned() {
		givenACompleteDailyQuest();

		whenClaimDailyQuest();

		thenDailyQuestRewardIsReturnedAndQuestIsClaimed(claimResponse);
	}

	@Test
	public void whenClaimAnIncompleteDailyQuestThenExceptionIsThrown() {

		Throwable exception = catchThrowable(this::whenClaimDailyQuest);

		Assertions.assertThat(exception).isInstanceOf(QuestIncompleteException.class);
	}

	@Test
	public void whenClaimAnAlreadyClaimedDailyQuestThenExceptionIsThrown() {
		givenAAlreadyClaimedDailyQuest();

		Throwable exception = catchThrowable(this::whenClaimDailyQuest);

		Assertions.assertThat(exception).isInstanceOf(QuestAlreadyClaimedException.class);
	}

	private void givenAAlreadyClaimedDailyQuest() {
		givenACompleteDailyQuest();
		claimDailyQuestAction.claim(player, catalog);
	}

	private void givenACompleteDailyQuest() {
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		Quest dailyQuest = questBoard.getDailyQuest();
		dailyQuest.incrementProgress();
		questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
	}

	private void thenDailyQuestRewardIsReturnedAndQuestIsClaimed(QuestClaimResponse claimResponse) {
		assertThat(claimResponse.getRewardResponses()).isNotEmpty();
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		assertThat(questBoard.getDailyQuest().isClaimed()).isTrue();
	}

	private void whenClaimDailyQuest() {
		claimResponse = claimDailyQuestAction.claim(player, catalog);
	}

}
