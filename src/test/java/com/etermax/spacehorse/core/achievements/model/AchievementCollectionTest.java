package com.etermax.spacehorse.core.achievements.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.etermax.spacehorse.core.achievements.exception.AchievementInvalidStateException;
import com.etermax.spacehorse.mock.AchievementScenarioBuilder;
import com.google.common.collect.Maps;

public class AchievementCollectionTest {

	private static final String ACHIEVEMENT_PURCHASE_CAPTAIN_ID = "achievement_PurchaseCaptain";
	private static final String PLAYER_ID = "10";

	@Test
	public void testGetAchievementById() throws Exception {
		// given
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		AchievementCollection achievementCollection = new AchievementCollection(PLAYER_ID, achievements);
		// when
		Achievement achievement = achievementCollection.getAchievementById(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		// then
		assertAchievement(achievement, Achievement.AchievementState.INITIAL);
	}

	@Test
	public void testGetAchievements() throws Exception {
		// given
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		AchievementCollection achievementCollection = new AchievementCollection(PLAYER_ID, achievements);
		// when
		List<Achievement> achievementsList = achievementCollection.getAchievements();
		// then
		assertThat(achievementsList.size()).isEqualTo(1);
		assertAchievement(achievementsList.get(0), Achievement.AchievementState.INITIAL);
	}

	@Test
	public void testAchievementAccomplishedCase1() throws Exception {
		// given
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		AchievementCollection achievementCollection = new AchievementCollection(PLAYER_ID, achievements);
		// when
		achievementCollection.achievementAccomplished(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		// then
		Achievement achievementUpdated = achievementCollection.getAchievementById(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		assertAchievement(achievementUpdated, Achievement.AchievementState.READY_TO_CLAIM);
	}

	@Test
	public void testAchievementAccomplishedCase2() throws Exception {
		// given
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		AchievementCollection achievementCollection = new AchievementCollection(PLAYER_ID, achievements);
		achievementCollection.achievementAccomplished(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		// when - then
		assertThatThrownBy(() -> achievementCollection.achievementAccomplished(ACHIEVEMENT_PURCHASE_CAPTAIN_ID))
				.isInstanceOf(AchievementInvalidStateException.class);
	}

	@Test
	public void testAchievementAccomplishedCase3() throws Exception {
		// given
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		AchievementCollection achievementCollection = new AchievementCollection(PLAYER_ID, achievements);
		achievementCollection.achievementAccomplished(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		achievementCollection.claimAchievementRewards(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		// when - then
		assertThatThrownBy(() -> achievementCollection.achievementAccomplished(ACHIEVEMENT_PURCHASE_CAPTAIN_ID))
				.isInstanceOf(AchievementInvalidStateException.class);
	}

	@Test
	public void testClaimAchievementRewardsCase1() throws Exception {
		// given
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		AchievementCollection achievementCollection = new AchievementCollection(PLAYER_ID, achievements);
		achievementCollection.achievementAccomplished(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		// when
		achievementCollection.claimAchievementRewards(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		// then
		Achievement achievementUpdated = achievementCollection.getAchievementById(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		assertAchievement(achievementUpdated, Achievement.AchievementState.CLAIMED);
	}

	@Test
	public void testClaimAchievementRewardsCase2() throws Exception {
		// given
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		AchievementCollection achievementCollection = new AchievementCollection(PLAYER_ID, achievements);
		// when - then
		assertThatThrownBy(() -> achievementCollection.claimAchievementRewards(ACHIEVEMENT_PURCHASE_CAPTAIN_ID))
				.isInstanceOf(AchievementInvalidStateException.class);
	}

	@Test
	public void testClaimAchievementRewardsCase3() throws Exception {
		// given
		Map<String, Achievement> achievements = givenOneAchievementInStateInitial();
		AchievementCollection achievementCollection = new AchievementCollection(PLAYER_ID, achievements);
		achievementCollection.achievementAccomplished(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		achievementCollection.claimAchievementRewards(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		// when - then
		assertThatThrownBy(() -> achievementCollection.claimAchievementRewards(ACHIEVEMENT_PURCHASE_CAPTAIN_ID))
				.isInstanceOf(AchievementInvalidStateException.class);
	}

	private void assertAchievement(Achievement achievement, Achievement.AchievementState state) {
		assertThat(achievement.getAchievementId()).isEqualTo(ACHIEVEMENT_PURCHASE_CAPTAIN_ID);
		assertThat(achievement.getStateAsTxt()).isEqualTo(state.getState());
		assertThat(achievement.getGoalAmount()).isEqualTo(50);
	}

	private Map<String, Achievement> givenOneAchievementInStateInitial() {
		Map<String, Achievement> achievements = Maps.newHashMap();
		Achievement achievement = new AchievementScenarioBuilder().buildDefaultAchievement();
		achievements.put(ACHIEVEMENT_PURCHASE_CAPTAIN_ID, achievement);
		return achievements;
	}
}
