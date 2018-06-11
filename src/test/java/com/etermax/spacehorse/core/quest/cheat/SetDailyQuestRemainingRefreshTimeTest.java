package com.etermax.spacehorse.core.quest.cheat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.NewPlayerConfigurationBuilder;

public class SetDailyQuestRemainingRefreshTimeTest {

	private Catalog catalog;
	private FixedServerTimeProvider serverTimeProvider;
	private NewPlayerConfiguration newPlayerConfiguration;
	private QuestBoardRepository questBoardRepository;
	private SetDailyQuestRemainingRefreshTime setDailyQuestRemainingRefreshTime;

	@Before
	public void setUp() {
		serverTimeProvider = new FixedServerTimeProvider();
		catalog = MockUtils.mockCatalog();
		newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
		questBoardRepository = new InMemoryQuestBoardRepository(serverTimeProvider);
		setDailyQuestRemainingRefreshTime = new SetDailyQuestRemainingRefreshTime(questBoardRepository);
	}

	@Test
	public void testApplyCheatSuccessful() {
		Player player = Player.buildNewPlayer("id", ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
		Quest dailyQuest = getQuestBoard(player).getDailyQuest();
		long coolDownTime = dailyQuest.getRefreshTimeInSeconds();
		long expirationTime = 1;
		String[] parameters = new String[1];
		parameters[0] = String.valueOf(expirationTime);
		setDailyQuestRemainingRefreshTime.apply(player, parameters, catalog);

		Assert.assertEquals(expirationTime, dailyQuest.getRefreshTimeInSeconds());
		Assert.assertNotEquals(coolDownTime, dailyQuest.getRefreshTimeInSeconds());
	}

	@Test
	public void testApplyCheatFails() {
		Player player = Player.buildNewPlayer("id", ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
		Quest dailyQuest = getQuestBoard(player).getDailyQuest();
		long claimCoolDownTime = dailyQuest.getRefreshTimeInSeconds();
		long expirationTime = -100;
		String[] parameters = new String[1];
		parameters[0] = String.valueOf(expirationTime);
		setDailyQuestRemainingRefreshTime.apply(player, parameters, catalog);

		Assert.assertEquals(claimCoolDownTime, dailyQuest.getRefreshTimeInSeconds());
		Assert.assertNotEquals(expirationTime, dailyQuest.getRefreshTimeInSeconds());
	}

	private QuestBoard getQuestBoard(Player player) {
		return questBoardRepository.findOrDefaultBy(player);
	}

}
