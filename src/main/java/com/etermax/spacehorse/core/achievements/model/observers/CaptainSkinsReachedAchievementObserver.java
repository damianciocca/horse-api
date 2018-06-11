package com.etermax.spacehorse.core.achievements.model.observers;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.action.FindAchievementAction;
import com.etermax.spacehorse.core.achievements.model.Achievement;
import com.etermax.spacehorse.core.achievements.model.AchievementType;
import com.etermax.spacehorse.core.achievements.model.observers.base.BaseAchievementByGoalAmountObserver;
import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.player.model.Player;
import com.google.common.collect.Maps;

public class CaptainSkinsReachedAchievementObserver extends BaseAchievementByGoalAmountObserver {

	private final CaptainsCollection captainsCollection;

	public CaptainSkinsReachedAchievementObserver(FindAchievementAction findAchievementAction, CompleteAchievementAction completeAchievementAction,
			CaptainsCollection captainsCollection) {
		super(findAchievementAction, completeAchievementAction);
		this.captainsCollection = captainsCollection;
	}

	@Override
	public AchievementType getAchievementType() {
		return AchievementType.PURCHASE_SKIN;
	}

	@Override
	public boolean achievementIsReached(Player player, Achievement achievement) {
		Map<String, Integer> quantityGroupedBySkinIds = Maps.newHashMap();
		Captain captain = captainsCollection.getSelectedCaptain();

		captain.getOwnedSkinsById().forEach((id, captainSkin) -> {
			if (isNotDefault(captainSkin)) {
				incrementCounter(quantityGroupedBySkinIds, captainSkin);
			}
		});

		Optional<Integer> counterHigher = quantityGroupedBySkinIds.values().stream().filter(byQuantitiesGreaterThanGoalAmountOf(achievement))
				.findFirst();
		return counterHigher.isPresent();
	}

	@Override
	public String getErrorMsg() {
		return "====> Unexpected error when trying to update achievements for captain skin reached achievement type";
	}

	private Predicate<Integer> byQuantitiesGreaterThanGoalAmountOf(Achievement achievement) {
		return counter -> counter >= achievement.getGoalAmount();
	}

	private void incrementCounter(Map<String, Integer> quantityGroupedBySkinIds, CaptainSkin captainSkin) {
		String skindId = captainSkin.getSkindId();
		if (existSkinId(quantityGroupedBySkinIds, skindId)) {
			Integer counter = quantityGroupedBySkinIds.get(skindId);
			quantityGroupedBySkinIds.put(skindId, ++counter);
		} else {
			quantityGroupedBySkinIds.put(skindId, 1);
		}
	}

	private boolean existSkinId(Map<String, Integer> countersBySkinIds, String skindId) {
		return countersBySkinIds.containsKey(skindId);
	}

	private boolean isNotDefault(CaptainSkin captainSkin) {
		return !captainSkin.isDefault();
	}
}
