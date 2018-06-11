package com.etermax.spacehorse.core.achievements.model;

import java.util.Map;

import com.etermax.spacehorse.core.achievements.exception.AchievementStateUnknownException;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.google.common.collect.Maps;

public class Achievement {

	private final String achievementId;
	private final AchievementDefinition definition;
	private AchievementState state;

	public Achievement(String achievementId, AchievementDefinition definition) {
		this.achievementId = achievementId;
		this.state = AchievementState.INITIAL;
		this.definition = definition;
	}

	public static Achievement restore(AchievementDefinition definition, String achievementId, AchievementState state) {
		return new Achievement(achievementId, definition, state);
	}

	private Achievement(String achievementId, AchievementDefinition definition, AchievementState state) {
		this.achievementId = achievementId;
		this.state = state;
		this.definition = definition;
	}

	public void accomplished() {
		this.state = AchievementState.READY_TO_CLAIM;
	}

	public void claimRewards() {
		this.state = AchievementState.CLAIMED;
	}

	public int getGoalAmount() {
		return definition.getGoalAmount();
	}

	public String getAchievementId() {
		return achievementId;
	}

	public String getStateAsTxt() {
		return state.getState();
	}

	public boolean isNotAccomplished() {
		return this.state == AchievementState.INITIAL;
	}

	public enum AchievementState {

		INITIAL("Initial"), //
		READY_TO_CLAIM("ReadyToClaim"), //
		CLAIMED("Claimed");

		private String state;

		AchievementState(String state) {
			this.state = state;
		}

		public static AchievementState typeOf(String state) {
			Map<String, AchievementState> achievementsStates = Maps.newHashMap();
			achievementsStates.put(INITIAL.getState(), INITIAL);
			achievementsStates.put(READY_TO_CLAIM.getState(), READY_TO_CLAIM);
			achievementsStates.put(CLAIMED.getState(), CLAIMED);
			if (isNotFound(state, achievementsStates)) {
				throw new AchievementStateUnknownException(state);
			}
			return achievementsStates.get(state);
		}

		private static boolean isNotFound(String state, Map<String, AchievementState> achievementsStates) {
			return !achievementsStates.containsKey(state);
		}

		public String getState() {
			return state;
		}
	}
}
