package com.etermax.spacehorse.core.quest.model.unlock;

import java.util.List;

import com.etermax.spacehorse.core.quest.model.QuestDifficultyType;

public class AvailableQuestSlotDifficultiesByPlayerLevel {

	private final int playerLevel;
	private final List<QuestDifficultyType> availableSlotDifficulties;

	public AvailableQuestSlotDifficultiesByPlayerLevel(int playerLevel, List<QuestDifficultyType> availableSlotDifficulties) {
		this.playerLevel = playerLevel;
		this.availableSlotDifficulties = availableSlotDifficulties;
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public List<QuestDifficultyType> getAvailableSlotDifficulties() {
		return availableSlotDifficulties;
	}

	public boolean contains(QuestDifficultyType questDifficultyType) {
		return availableSlotDifficulties.contains(questDifficultyType);
	}
}
