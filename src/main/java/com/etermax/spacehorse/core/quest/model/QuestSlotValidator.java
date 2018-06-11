package com.etermax.spacehorse.core.quest.model;

import static org.apache.logging.log4j.util.Strings.isBlank;

import java.util.Arrays;

import com.etermax.spacehorse.core.quest.exception.QuestDifficultyUnknownException;

public class QuestSlotValidator {

	public void validate(String slotId) {
		shouldNoBeBlank(slotId);
		validateIfSlotIdMatchWithAnyDifficultyType(slotId);
	}

	private void shouldNoBeBlank(String difficulty) {
		if (isBlank(difficulty)) {
			throw new QuestDifficultyUnknownException("The Difficulty should not be blank");
		}
	}

	private void validateIfSlotIdMatchWithAnyDifficultyType(String slotId) {
		Arrays.stream(QuestDifficultyType.values()).filter(difficulty -> difficulty.toString().equals(slotId)).findFirst()
				.orElseThrow(() -> new QuestDifficultyUnknownException("Unexpected error when try to validate slotId"));
	}
}
