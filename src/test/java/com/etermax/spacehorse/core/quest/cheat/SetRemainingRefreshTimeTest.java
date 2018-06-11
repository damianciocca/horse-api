package com.etermax.spacehorse.core.quest.cheat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.NewPlayerConfigurationBuilder;

public class SetRemainingRefreshTimeTest {

	private Catalog catalog;
	private FixedServerTimeProvider serverTimeProvider;
	private NewPlayerConfiguration newPlayerConfiguration;
	private QuestBoardRepository questBoardRepository;

	@Before
	public void setUp() {
		serverTimeProvider = new FixedServerTimeProvider();
		catalog = MockUtils.mockCatalog();
		newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
		questBoardRepository = new InMemoryQuestBoardRepository(serverTimeProvider);
	}

	@Test
	public void testApplyCheatSuccessful() {
		SetRemainingRefreshTime setRemainingRefreshTime = new SetRemainingRefreshTime(questBoardRepository);
		Player player = Player.buildNewPlayer("id", ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
		long coolDownTime = getSlot(player).getActiveQuest().getRefreshTimeInSeconds();
		long expirationTime = 1;
		String[] parameters = new String[2];
		parameters[0] = QuestDifficultyType.EASY.toString();
		parameters[1] = String.valueOf(expirationTime);
		setRemainingRefreshTime.apply(player, parameters, catalog);

		QuestSlot slot = getSlot(player);
		Assert.assertEquals(expirationTime, slot.getActiveQuest().getRefreshTimeInSeconds());
		Assert.assertNotEquals(coolDownTime, slot.getActiveQuest().getRefreshTimeInSeconds());
	}

	@Test
	public void testApplyCheatFails() {
		SetRemainingRefreshTime setRemainingRefreshTime = new SetRemainingRefreshTime(questBoardRepository);
		Player player = Player.buildNewPlayer("id", ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
		long claimCoolDownTime = getSlot(player).getActiveQuest().getRefreshTimeInSeconds();
		long expirationTime = -100;
		String[] parameters = new String[2];
		parameters[0] = QuestDifficultyType.EASY.toString();
		parameters[1] = String.valueOf(expirationTime);
		setRemainingRefreshTime.apply(player, parameters, catalog);

		QuestSlot slot = getSlot(player);
		Assert.assertEquals(claimCoolDownTime, slot.getActiveQuest().getRefreshTimeInSeconds());
		Assert.assertNotEquals(expirationTime, slot.getActiveQuest().getRefreshTimeInSeconds());
	}

	private QuestSlot getSlot(Player player) {
		return questBoardRepository.findOrDefaultBy(player).getSlot(QuestDifficultyType.EASY.toString());
	}

}
