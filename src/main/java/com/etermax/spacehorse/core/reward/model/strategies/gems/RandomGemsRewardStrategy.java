package com.etermax.spacehorse.core.reward.model.strategies.gems;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.etermax.spacehorse.core.reward.model.strategies.RewardStrategy;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class RandomGemsRewardStrategy implements RewardStrategy {

	public static final double CHANCES_BASE = 100.0;
	private final int gemsAmount;
	private final int gemsToleranceAmount;

	public RandomGemsRewardStrategy(int gemsAmount, int gemsToleranceAmount) {
		this.gemsAmount = gemsAmount;
		this.gemsToleranceAmount = gemsToleranceAmount;
	}

	@Override
	public List<Reward> getRewards() {
		if (gemsAmount < 0 || gemsToleranceAmount < 0) {
			return Collections.emptyList();
		}
		int medianValue = gemsAmount;
		double range = gemsToleranceAmount / CHANCES_BASE / 2;
		int min = (int) (medianValue * (1 - range));
		int max = (int) (medianValue * (1 + range));
		int gemsReward = ThreadLocalRandom.current().nextInt(min, max + 1);
		return Arrays.asList(new Reward(RewardType.GEMS, gemsReward));
	}

}
