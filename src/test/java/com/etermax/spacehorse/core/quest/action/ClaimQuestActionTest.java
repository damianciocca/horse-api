package com.etermax.spacehorse.core.quest.action;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.model.QuestRewardAssigner;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.quest.resource.response.QuestClaimResponse;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.GetRewardsDomainService;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.NewPlayerConfigurationBuilder;

public class ClaimQuestActionTest {

	private Player player;
	private Catalog catalog;
	private ClaimQuestAction claimQuestAction;
	private RefreshQuestAction refreshQuestAction;
	private InMemoryQuestBoardRepository questBoardRepository;
	private FixedServerTimeProvider timeProvider;

	@Before
	public void setUp() {
		timeProvider = new FixedServerTimeProvider();

		PlayerRepository playerRepository = mock(PlayerRepository.class);

		PlayerWinRateRepository playerWinRateRepository = mock(PlayerWinRateRepository.class);
		when(playerWinRateRepository.findOrCrateDefault(anyString())).thenReturn(new PlayerWinRate(""));

		GetRewardsDomainService getRewardsDomainService = new GetRewardsDomainService();
		ApplyRewardDomainService applyRewardDomainService = new ApplyRewardDomainService();

		questBoardRepository = new InMemoryQuestBoardRepository(timeProvider);

		QuestRewardAssigner questRewardAssigner = new QuestRewardAssigner(playerRepository, applyRewardDomainService, getRewardsDomainService,
				timeProvider, mock(AchievementsFactory.class));

		claimQuestAction = new ClaimQuestAction(questRewardAssigner, questBoardRepository, timeProvider);

		catalog = MockUtils.mockCatalog();

		NewPlayerConfiguration newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
		player = Player.buildNewPlayer("10", ABTag.emptyABTag(), timeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);

		//Make the first quest available
		refreshQuestAction = new RefreshQuestAction(questBoardRepository, new QuestFactory(timeProvider));

	}

	@Test
	public void givenAQuestBoardWithOneQuestReadyToBeClaimedWhenClaimThenTheQuestShouldBeClaimed() {
		givenAQuestReadyToClaim();
		aTimeIsIncreasedInSeconds(50);

		QuestClaimResponse claimResponse = whenClaimSlot(EASY.toString());

		thenAssertThatQuestInSlotIsClaimed(claimResponse, EASY.toString());
	}

	private void thenAssertThatQuestInSlotIsClaimed(QuestClaimResponse claimResponse, String slotId) {
		assertThat(claimResponse.getRewardResponses()).isNotEmpty();
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		assertThat(questBoard.getSlots()).hasSize(3);
		assertThat(questBoard.getSlot(slotId).hasClaimedQuest()).isTrue();
		assertThat(questBoard.getSlot(slotId).getRefreshTimeInSeconds()).isEqualTo(timeProvider.getTimeNowAsSeconds() + 100);
	}

	private void givenAQuestReadyToClaim() {
		refreshQuestAction.refresh(player, catalog, EASY.toString());
		questBoardRepository.findOrDefaultBy(player).getSlot(EASY.toString()).getActiveQuest().setCurrentProgress(10L);
	}

	private void aTimeIsIncreasedInSeconds(int seconds) {
		DateTime increase = timeProvider.getDateTime().plusSeconds(seconds);
		timeProvider.changeTime(increase);
	}

	private QuestClaimResponse whenClaimSlot(String slotId) {
		return claimQuestAction.claim(player, catalog, slotId);
	}

}
