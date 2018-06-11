package com.etermax.spacehorse.core.quest.model;

import java.util.Map;
import java.util.function.Predicate;

import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.google.common.collect.Maps;

public class ClaimQuestConfiguration {

	private final Map<QuestDifficultyType, Integer> refreshTimeInSecondsByDifficulty = Maps.newConcurrentMap();

	public ClaimQuestConfiguration(GameConstants gameConstants) {
		refreshTimeInSecondsByDifficulty.put(QuestDifficultyType.EASY, gameConstants.getRefreshTimeForEasyQuestSlot());
		refreshTimeInSecondsByDifficulty.put(QuestDifficultyType.MEDIUM, gameConstants.getRefreshTimeForMediumQuestSlot());
		refreshTimeInSecondsByDifficulty.put(QuestDifficultyType.HARD, gameConstants.getRefreshTimeForHardQuestSlot());
	}

	public int getRefreshTimeInSecondsBy(String difficulty) {
		return refreshTimeInSecondsByDifficulty.entrySet().stream().filter(byDifficulty(difficulty)).findFirst().orElseThrow(this::throwException)
				.getValue();
	}

	private RuntimeException throwException() {
		return new RuntimeException("Unexpected error when trying to get refresh time in seconds");
	}

	private Predicate<Map.Entry<QuestDifficultyType, Integer>> byDifficulty(String slotId) {
		return entry -> entry.getKey().toString().equals(slotId);
	}

}
