package com.etermax.spacehorse.core.reward.model.strategies;

import static junit.framework.TestCase.assertEquals;

import java.util.List;

import org.junit.Test;

import com.etermax.spacehorse.core.reward.model.strategies.gold.FixedGoldRewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class FixedGoldRewardStrategyTest {

	@Test
	public void testNoRewardsForNegativeValue() {

		int fixedNegativeGold = -1;
		FixedGoldRewardStrategy strategy = new FixedGoldRewardStrategy(fixedNegativeGold);

		assertEquals(0, strategy.getRewards().size());
	}

	@Test
	public void testReceiveRewardsForPositiveValue() {
		int fixedGolds = 20;
		FixedGoldRewardStrategy strategy = new FixedGoldRewardStrategy(fixedGolds);

		List<Reward> rewards = strategy.getRewards();

		assertEquals(1, rewards.size());
		assertEquals(RewardType.GOLD, rewards.stream().findFirst().get().getRewardType());
		assertEquals(fixedGolds, rewards.stream().findFirst().get().getAmount());
	}

}
