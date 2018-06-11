package com.etermax.spacehorse.core.quest.model;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public enum QuestDifficultyType {
	EASY, //
	MEDIUM,//
	HARD;

	public static QuestDifficultyType getTypeFromId(String difficultyType) {
		Map<String, QuestDifficultyType> difficultyTypes = Maps.newHashMap();
		difficultyTypes.put(EASY.toString(), EASY);
		difficultyTypes.put(MEDIUM.toString(), MEDIUM);
		difficultyTypes.put(HARD.toString(), HARD);
		return difficultyTypes.get(difficultyType);
	}

	public static List<QuestDifficultyType> getTypesUpTo(String difficultyType) {
		Map<String, List<QuestDifficultyType>> maxSlotDifficultyWithPrevious = Maps.newHashMap();
		maxSlotDifficultyWithPrevious.put(EASY.toString(), newArrayList(EASY));
		maxSlotDifficultyWithPrevious.put(MEDIUM.toString(), newArrayList(EASY, MEDIUM));
		maxSlotDifficultyWithPrevious.put(HARD.toString(), newArrayList(EASY, MEDIUM, HARD));
		return maxSlotDifficultyWithPrevious.get(difficultyType);
	}

}
