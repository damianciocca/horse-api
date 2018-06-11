package com.etermax.spacehorse.core.achievements.model.observers.base;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.mock.MockCatalog;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class BaseAchievementObserverTest {

	private CompleteAchievementAction completeAchievementAction;
	private Player player;
	private List<AchievementDefinition> achievementDefinitions;

	@Before
	public void setUp() throws Exception {
		completeAchievementAction = Mockito.mock(CompleteAchievementAction.class);

		player = new PlayerScenarioBuilder("10").build();

		achievementDefinitions = MockCatalog.buildCatalog().getAchievementsDefinitionsCollection().getEntries();
	}

	@Test
	public void givenANotReachedAchievementWhenUpdateThenTheAchievementShouldNotBeCompleted() throws Exception {
		// given
		BaseAchievementObserver achievementObserver = aBaseAchievementObserver(completeAchievementAction, false);
		// when
		achievementObserver.update(player, achievementDefinitions);
		// then
		verify(completeAchievementAction, times(0)).complete(any(), anyString());
	}

	@Test
	public void givenAReachedAchievementWhenUpdateThenTheAchievementShouldBeCompleted() throws Exception {
		// given
		BaseAchievementObserver achievementObserver = aBaseAchievementObserver(completeAchievementAction, true);
		// when
		achievementObserver.update(player, achievementDefinitions);
		// then
		verify(completeAchievementAction, times(1)).complete(any(), anyString());
	}

	private BaseAchievementObserver aBaseAchievementObserver(CompleteAchievementAction completeAchievementAction, boolean isReached) {
		return new BaseAchievementObserver(completeAchievementAction) {
			@Override
			public AchievementType getAchievementType() {
				return AchievementType.PLAY_BATTLE;
			}

			@Override
			public boolean achievementIsReached(Player player) {
				return isReached;
			}

			@Override
			public String getErrorMsg() {
				return "";
			}
		};
	}
}
