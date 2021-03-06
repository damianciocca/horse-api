package com.etermax.spacehorse.core.reward.model.strategies;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.etermax.spacehorse.core.reward.model.strategies.gold.RandomGoldRewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class RandomGoldRewardStrategyTest {

	@Test
	public void testNoRewardsForNegativeValue() {
		// given
		RandomGoldRewardStrategy strategy = new RandomGoldRewardStrategy(-1, 30);
		// when
		List<Reward> rewards = strategy.getRewards();
		// then
		assertThat(rewards).hasSize(0);
	}

	@Test
	public void testReceiveRewardsForPositiveValue() {
		// given
		final int value = 100;
		final int chances = 10;
		final int diff = (int) (value * chances / RandomGoldRewardStrategy.CHANCES_BASE);
		final int max = value + diff;
		final int min = value - diff;
		RandomGoldRewardStrategy strategy = new RandomGoldRewardStrategy(value, chances);
		// when
		List<Reward> rewards = strategy.getRewards();
		// Then
		assertEquals(1, rewards.size());
		assertEquals(RewardType.GOLD, rewards.stream().findFirst().get().getRewardType());
		final int rewardValue = rewards.stream().findFirst().get().getAmount();
		assertTrue(min <= rewardValue && rewardValue <= max);
	}

}
