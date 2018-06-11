package com.etermax.spacehorse.core.achievements.model;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.achievements.exception.AchievementInvalidStateException;
import com.etermax.spacehorse.core.achievements.exception.AchievementUnknownException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class AchievementCollection {

	private static final Logger logger = LoggerFactory.getLogger(AchievementCollection.class);

	private final String playerId;
	private Map<String, Achievement> achievementsByIds = Maps.newHashMap();

	public AchievementCollection(String playerId, Map<String, Achievement> achievementsByIds) {
		this.playerId = playerId;
		this.achievementsByIds = achievementsByIds;
	}

	public static AchievementCollection restore(String userId, List<Achievement> achievements) {
		Map<String, Achievement> achievementsByTypes = Maps.newHashMap();
		achievements.forEach(achievement -> achievementsByTypes.put(achievement.getAchievementId(), achievement));
		return new AchievementCollection(userId, achievementsByTypes);
	}

	public void achievementAccomplished(String achievementId) {
		validateAchievementId(achievementId);
		Achievement achievement = achievementsByIds.get(achievementId);
		if (achievementIsNotInInitialState(achievement)) {
			logger.error("invalid state to accomplish achievement. Actual [ " + achievement.getStateAsTxt() + " ]");
			throw new AchievementInvalidStateException(achievement.getStateAsTxt());
		}
		achievement.accomplished();
	}

	public void claimAchievementRewards(String achievementId) {
		validateAchievementId(achievementId);
		Achievement achievement = achievementsByIds.get(achievementId);
		if (achievementIsNotReadyToClaim(achievement)) {
			logger.error("invalid state to claim achievement. Actual [ " + achievement.getStateAsTxt() + " ]");
			throw new AchievementInvalidStateException(achievement.getStateAsTxt());
		}
		achievement.claimRewards();
	}

	public Achievement getAchievementById(String achievementId) {
		validateAchievementId(achievementId);
		return achievementsByIds.get(achievementId);
	}

	public List<Achievement> getAchievements() {
		return ImmutableList.copyOf(achievementsByIds.values());
	}

	public String getUserId() {
		return this.playerId;
	}

	private String getErrorMsg(String achievementId) {
		return "Unknown achievement ID [ " + achievementId + " ]";
	}

	private boolean achievementIsNotReadyToClaim(Achievement achievement) {
		return !achievement.getStateAsTxt().equals(Achievement.AchievementState.READY_TO_CLAIM.getState());
	}

	private boolean achievementIsNotInInitialState(Achievement achievement) {
		return !achievement.getStateAsTxt().equals(Achievement.AchievementState.INITIAL.getState());
	}

	private void validateAchievementId(String achievementId) {
		if (isNotFound(achievementId)) {
			logger.error(getErrorMsg(achievementId));
			throw new AchievementUnknownException(getErrorMsg(achievementId));
		}
	}

	private boolean isNotFound(String achievementId) {
		return !achievementsByIds.containsKey(achievementId);
	}

}
