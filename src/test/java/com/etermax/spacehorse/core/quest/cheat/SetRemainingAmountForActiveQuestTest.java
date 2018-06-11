package com.etermax.spacehorse.core.quest.cheat;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.action.RefreshQuestAction;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.repository.InMemoryQuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.NewPlayerConfigurationBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SetRemainingAmountForActiveQuestTest {

    private Catalog catalog;
    private RefreshQuestAction refreshQuestAction;
    private FixedServerTimeProvider serverTimeProvider;
    private SetRemainingAmountForActiveQuest setRemainingAmountForActiveQuest;
    private NewPlayerConfiguration newPlayerConfiguration;
    private QuestBoardRepository questBoardRepository;

    @Before
    public void setUp() {
        catalog = MockUtils.mockCatalog();
        serverTimeProvider = new FixedServerTimeProvider();

        PlayerWinRateRepository playerWinRateRepository = mock(PlayerWinRateRepository.class);
        when(playerWinRateRepository.findOrCrateDefault(anyString())).thenReturn(new PlayerWinRate(""));

        questBoardRepository = new InMemoryQuestBoardRepository(serverTimeProvider);

        refreshQuestAction = new RefreshQuestAction(questBoardRepository, new QuestFactory(serverTimeProvider));
        setRemainingAmountForActiveQuest = new SetRemainingAmountForActiveQuest(questBoardRepository);

        newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
    }

    @Test
    public void testApplyCheatSuccessful() {
        // given
        Player player = Player.buildNewPlayer("id", ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
        refreshQuestAction.refresh(player, catalog, EASY.toString());
        long oldRemainingAmount = getSlot(player).getActiveQuest().getRemainingAmount();
        long newRemainingAmount = 1;
        String[] parameters = new String[2];
        parameters[0] = QuestDifficultyType.EASY.toString();
        parameters[1] = String.valueOf(newRemainingAmount);
        // when
        setRemainingAmountForActiveQuest.apply(player, parameters, catalog);
        // then
        Assert.assertEquals(newRemainingAmount, getSlot(player).getActiveQuest().getRemainingAmount());
        Assert.assertNotEquals(oldRemainingAmount, getSlot(player).getActiveQuest().getRemainingAmount());
    }

	@Test
    public void testApplyCheatFails() {
        // given
        Player player = Player.buildNewPlayer("id", ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
        refreshQuestAction.refresh(player, catalog, EASY.toString());
        long remainingAmount = getSlot(player).getActiveQuest().getRemainingAmount();
        long newRemainingAmount = 1000;
        String[] parameters = new String[2];
        parameters[0] = QuestDifficultyType.EASY.toString();
        parameters[1] = String.valueOf(newRemainingAmount);
        // when
        setRemainingAmountForActiveQuest.apply(player, parameters, catalog);
        // then
        Assert.assertNotEquals(newRemainingAmount, getSlot(player).getActiveQuest().getRemainingAmount());
        Assert.assertEquals(remainingAmount, getSlot(player).getActiveQuest().getRemainingAmount());
    }

	private QuestSlot getSlot(Player player) {
        return questBoardRepository.findOrDefaultBy(player).getSlot(EASY.toString());
	}

}
