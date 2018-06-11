package com.etermax.spacehorse.core.reward.model.strategies.gold;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.etermax.spacehorse.core.reward.model.strategies.RewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class RandomGoldRewardStrategy implements RewardStrategy {

	public static final double CHANCES_BASE = 100.0;
	private final int goldAmount;
	private final int goldToleranceAmount;

	public RandomGoldRewardStrategy(int goldAmount, int goldToleranceAmount) {
		this.goldAmount = goldAmount;
		this.goldToleranceAmount = goldToleranceAmount;
	}

	@Override
	public List<Reward> getRewards() {
		if (goldAmount < 0 || goldToleranceAmount < 0) {
			return newArrayList();
		}
		int medianValue = goldAmount;
		double range = goldToleranceAmount / CHANCES_BASE / 2;
		int min = (int) (medianValue * (1 - range));
		int max = (int) (medianValue * (1 + range));
		int goldReward = ThreadLocalRandom.current().nextInt(min, max + 1);
		return newArrayList(new Reward(RewardType.GOLD, goldReward));
	}

}
