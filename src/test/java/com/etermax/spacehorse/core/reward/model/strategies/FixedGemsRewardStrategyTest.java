package com.etermax.spacehorse.core.reward.model.strategies;

import static junit.framework.TestCase.assertEquals;

import java.util.List;

import org.junit.Test;

import com.etermax.spacehorse.core.reward.model.strategies.gems.FixedGemsRewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class FixedGemsRewardStrategyTest {

	@Test
	public void testNoRewardsForNegativeValue() {

		int fixedNegativeGems = -1;
		FixedGemsRewardStrategy strategy = new FixedGemsRewardStrategy(fixedNegativeGems);

		assertEquals(0, strategy.getRewards().size());
	}

	@Test
	public void testReceiveRewardsForPositiveValue() {
		final int fixedGems = 20;

		FixedGemsRewardStrategy strategy = new FixedGemsRewardStrategy(fixedGems);

		List<Reward> rewards = strategy.getRewards();
		assertEquals(1, rewards.size());
		assertEquals(RewardType.GEMS, rewards.stream().findFirst().get().getRewardType());
		assertEquals(fixedGems, rewards.stream().findFirst().get().getAmount());
	}

}
