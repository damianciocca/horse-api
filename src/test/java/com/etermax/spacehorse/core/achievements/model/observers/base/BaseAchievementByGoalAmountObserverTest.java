package com.etermax.spacehorse.core.achievements.model.observers.base;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.action.FindAchievementAction;
import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.mock.AchievementScenarioBuilder;
import com.etermax.spacehorse.mock.MockCatalog;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class BaseAchievementByGoalAmountObserverTest {

	private CompleteAchievementAction completeAchievementAction;
	private FindAchievementAction findAchievementAction;
	private Player player;
	private List<AchievementDefinition> achievementDefinitions;
	private Achievement achievement;

	@Before
	public void setUp() throws Exception {
		completeAchievementAction = Mockito.mock(CompleteAchievementAction.class);

		findAchievementAction = Mockito.mock(FindAchievementAction.class);
		achievement = new AchievementScenarioBuilder().buildDefaultAchievement();
		when(findAchievementAction.findBy(any(), anyString())).thenReturn(achievement);

		player = new PlayerScenarioBuilder("10").build();

		achievementDefinitions = MockCatalog.buildCatalog().getAchievementsDefinitionsCollection().getEntries();
	}

	@Test
	public void givenANotReachedAchievementWhenUpdateThenTheAchievementShouldNotBeCompletedCase1() throws Exception {
		// given
		BaseAchievementByGoalAmountObserver achievementObserver = aBaseAchievementByGoalAmountObserver(findAchievementAction,
				completeAchievementAction, false);
		// when
		achievementObserver.update(player, achievementDefinitions);
		// then
		verify(findAchievementAction, times(1)).findBy(any(), anyString());
		verify(completeAchievementAction, times(0)).complete(any(), anyString());
	}

	@Test
	public void givenANotReachedAchievementWhenUpdateThenTheAchievementShouldNotBeCompletedCase2() throws Exception {
		// given
		BaseAchievementByGoalAmountObserver achievementObserver = aBaseAchievementByGoalAmountObserver(findAchievementAction,
				completeAchievementAction, false);
		// when
		achievementObserver.update(player, achievement);
		// then
		verify(findAchievementAction, times(0)).findBy(any(), anyString());
		verify(completeAchievementAction, times(0)).complete(any(), anyString());
	}

	@Test
	public void givenAReachedAchievementInInitialStateWhenUpdateThenTheAchievementShouldBeCompletedCase1() throws Exception {
		// given
		BaseAchievementByGoalAmountObserver achievementByGoalAmountObserver = aBaseAchievementByGoalAmountObserver(findAchievementAction,
				completeAchievementAction, true);
		// when
		achievementByGoalAmountObserver.update(player, achievementDefinitions);
		// then
		verify(findAchievementAction, times(1)).findBy(any(), anyString());
		verify(completeAchievementAction, times(1)).complete(any(), anyString());
	}

	@Test
	public void givenAReachedAchievementInInitialStateWhenUpdateThenTheAchievementShouldBeCompletedCase2() throws Exception {
		// given
		BaseAchievementByGoalAmountObserver achievementByGoalAmountObserver = aBaseAchievementByGoalAmountObserver(findAchievementAction,
				completeAchievementAction, true);
		// when
		achievementByGoalAmountObserver.update(player, achievement);
		// then
		verify(findAchievementAction, times(0)).findBy(any(), anyString());
		verify(completeAchievementAction, times(1)).complete(any(), anyString());
	}

	@Test
	public void givenAReachedAchievementInStateReadyToClaimWhenUpdateThenTheAchievementShouldNotBeCompletedCase1() throws Exception {
		// given
		findAchievementAction = Mockito.mock(FindAchievementAction.class);
		Achievement achievement = new AchievementScenarioBuilder().buidReadyToClaimAchievement();
		when(findAchievementAction.findBy(any(), anyString())).thenReturn(achievement);

		BaseAchievementByGoalAmountObserver achievementByGoalAmountObserver = aBaseAchievementByGoalAmountObserver(findAchievementAction,
				completeAchievementAction, true);
		// when
		achievementByGoalAmountObserver.update(player, achievementDefinitions);
		// then
		verify(findAchievementAction, times(1)).findBy(any(), anyString());
		verify(completeAchievementAction, times(0)).complete(any(), anyString());
	}

	@Test
	public void givenAReachedAchievementInStateReadyToClaimWhenUpdateThenTheAchievementShouldNotBeCompletedCase2() throws Exception {
		// given
		findAchievementAction = Mockito.mock(FindAchievementAction.class);
		Achievement achievement = new AchievementScenarioBuilder().buidReadyToClaimAchievement();
		when(findAchievementAction.findBy(any(), anyString())).thenReturn(achievement);

		BaseAchievementByGoalAmountObserver achievementByGoalAmountObserver = aBaseAchievementByGoalAmountObserver(findAchievementAction,
				completeAchievementAction, true);
		// when
		achievementByGoalAmountObserver.update(player, achievement);
		// then
		verify(findAchievementAction, times(0)).findBy(any(), anyString());
		verify(completeAchievementAction, times(0)).complete(any(), anyString());
	}

	private BaseAchievementByGoalAmountObserver aBaseAchievementByGoalAmountObserver(FindAchievementAction findAchievementAction,
			CompleteAchievementAction completeAchievementAction, boolean isReached) {
		return new BaseAchievementByGoalAmountObserver(findAchievementAction, completeAchievementAction) {
			@Override
			public AchievementType getAchievementType() {
				return AchievementType.PLAY_BATTLE;
			}

			@Override
			public boolean achievementIsReached(Player player, Achievement achievement) {
				return isReached;
			}

			@Override
			public String getErrorMsg() {
				return "";
			}
		};
	}
}
