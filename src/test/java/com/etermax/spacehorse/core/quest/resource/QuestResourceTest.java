package com.etermax.spacehorse.core.quest.resource;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import com.etermax.spacehorse.core.quest.action.daily.ClaimDailyQuestAction;
import com.etermax.spacehorse.core.quest.action.daily.RefreshDailyQuestAction;
import com.etermax.spacehorse.core.quest.action.RefreshQuestPayingAction;
import com.etermax.spacehorse.core.quest.action.SkipQuestPayingAction;
import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.action.ClaimQuestAction;
import com.etermax.spacehorse.core.quest.action.RefreshQuestAction;
import com.etermax.spacehorse.core.quest.action.SkipQuestAction;
import com.etermax.spacehorse.core.quest.exception.QuestAlreadyClaimedException;
import com.etermax.spacehorse.core.quest.exception.QuestNotExpiredException;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;
import com.etermax.spacehorse.core.quest.model.QuestType;
import com.etermax.spacehorse.core.quest.resource.request.QuestApplyRewardsRequest;
import com.etermax.spacehorse.core.quest.resource.request.QuestRefreshRequest;
import com.etermax.spacehorse.core.quest.resource.response.QuestClaimResponse;
import com.etermax.spacehorse.core.quest.resource.response.QuestRefreshResponse;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.mock.MockUtils;
import com.etermax.spacehorse.mock.NewPlayerConfigurationBuilder;
import com.google.common.collect.Lists;

public class QuestResourceTest {

	private static final String PLAYER_1_ID = "player1";
	private static final String PLAYER_2_ID = "player2";

	private QuestResource questResource;

	@Before
	public void setUp() {
		Catalog catalog = MockUtils.mockCatalog();
		FixedServerTimeProvider serverTimeProvider = new FixedServerTimeProvider();
		CatalogAction catalogAction = MockUtils.mockCatalogAction(catalog);

		PlayerAction playerAction = mock(PlayerAction.class);

		NewPlayerConfiguration newPlayerConfiguration = new NewPlayerConfigurationBuilder().build();
		Player player1 = Player.buildNewPlayer(PLAYER_1_ID, ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
		when(playerAction.findByLoginId(PLAYER_1_ID)).thenReturn(Optional.ofNullable(player1));
		Player player2 = Player.buildNewPlayer(PLAYER_2_ID, ABTag.emptyABTag(), serverTimeProvider.getTimeNowAsSeconds(), newPlayerConfiguration);
		when(playerAction.findByLoginId(PLAYER_2_ID)).thenReturn(Optional.ofNullable(player2));

		RefreshQuestAction refreshQuestAction = mock(RefreshQuestAction.class);
		ClaimQuestAction claimQuestAction = mock(ClaimQuestAction.class);

		String questType = QuestType.QUEST_SIMPLE_VICTORIES;
		QuestDefinition questDefinition = new QuestDefinition("questId", questType, "chestId", 3, QuestDifficultyType.EASY.toString());
		Quest quest = new Quest(questDefinition);
		when(refreshQuestAction.refresh(player1, catalog, EASY.toString())).thenReturn(quest);
		when(refreshQuestAction.refresh(player2, catalog, EASY.toString()))
				.thenThrow(new QuestNotExpiredException("Active Quest not expired yet, unable to " + "refresh"));

		when(claimQuestAction.claim(player2, catalog, EASY.toString())).thenReturn(new QuestClaimResponse(Lists.newArrayList(), 100));
		when(claimQuestAction.claim(player1, catalog, EASY.toString()))
				.thenThrow(new QuestAlreadyClaimedException("Quest already " + "claimed. Unable to apply rewards."));

		SkipQuestAction skipQuestAction = mock(SkipQuestAction.class);
		SkipQuestPayingAction skipQuestPayingAction = mock(SkipQuestPayingAction.class);
		RefreshQuestPayingAction refreshQuestPayingAction = mock(RefreshQuestPayingAction.class);
		RefreshDailyQuestAction refreshDailyQuestAction = mock(RefreshDailyQuestAction.class);
		ClaimDailyQuestAction claimDailyQuestAction = mock(ClaimDailyQuestAction.class);
		questResource = new QuestResource(playerAction, catalogAction, refreshQuestAction, claimQuestAction, skipQuestAction, skipQuestPayingAction,
				refreshQuestPayingAction, refreshDailyQuestAction, claimDailyQuestAction);
	}

	@Test
	public void testRefreshSuccessful() {
		QuestRefreshRequest questRefreshRequest = new QuestRefreshRequest(EASY.toString());
		HttpServletRequest httpServletRequest = MockUtils.mockHttpServletRequest(PLAYER_1_ID);

		Response response = questResource.refresh(httpServletRequest, questRefreshRequest);
		QuestRefreshResponse questRefreshResponse = (QuestRefreshResponse) response.getEntity();

		assertEquals(Response.Status.OK, response.getStatusInfo());
		assertNotNull(questRefreshResponse);
		assertNotNull(questRefreshResponse.getQuestResponse());
		assertNotNull(questRefreshResponse.getQuestResponse().getId());
	}

	@Test(expected = QuestNotExpiredException.class)
	public void testRefreshFailed() {
		QuestRefreshRequest questRefreshRequest = new QuestRefreshRequest(EASY.toString());
		HttpServletRequest httpServletRequest = MockUtils.mockHttpServletRequest(PLAYER_2_ID);

		questResource.refresh(httpServletRequest, questRefreshRequest);
	}

	@Test
	public void testClaimSuccessfulFirstTime() {
		QuestApplyRewardsRequest questApplyRewardsRequest = new QuestApplyRewardsRequest(EASY.toString());
		HttpServletRequest httpServletRequest = MockUtils.mockHttpServletRequest(PLAYER_2_ID);

		Response response = questResource.claim(httpServletRequest, questApplyRewardsRequest);
		assertEquals(Response.Status.OK, response.getStatusInfo());
	}

	@Test(expected = QuestAlreadyClaimedException.class)
	public void testClaimSuccessfulFirstTimeAndFailsAfterAlreadyClaimed() {
		QuestApplyRewardsRequest questApplyRewardsRequest = new QuestApplyRewardsRequest(EASY.toString());
		HttpServletRequest httpServletRequest = MockUtils.mockHttpServletRequest(PLAYER_1_ID);

		Response response = questResource.claim(httpServletRequest, questApplyRewardsRequest);
		assertEquals(Response.Status.OK, response.getStatusInfo());

		questResource.claim(httpServletRequest, questApplyRewardsRequest);
	}

}
