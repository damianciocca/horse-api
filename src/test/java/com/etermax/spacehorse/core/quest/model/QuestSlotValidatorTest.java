package com.etermax.spacehorse.core.quest.model;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.HARD;
import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.MEDIUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Test;

import com.etermax.spacehorse.core.quest.exception.QuestDifficultyUnknownException;

public class QuestSlotValidatorTest {

	@Test
	public void whenCreateQuestDifficultyEasyThenShouldBeCreatedSuccessfully() throws Exception {

		QuestSlotValidator questSlotValidator = new QuestSlotValidator();

		questSlotValidator.validate(EASY.toString());

		// if pass is ok
	}

	@Test
	public void whenCreateQuestDifficultyMediumThenShouldBeCreatedSuccessfully() throws Exception {

		QuestSlotValidator questSlotValidator = new QuestSlotValidator();

		questSlotValidator.validate(MEDIUM.toString());

		// if pas is ok
	}

	@Test
	public void whenCreateQuestDifficultyHardThenShouldBeCreatedSuccessfully() throws Exception {

		QuestSlotValidator questSlotValidator = new QuestSlotValidator();

		questSlotValidator.validate(HARD.toString());

		// if pass is ok
	}

	@Test
	public void whenCreateQuestDifficultyHardWithWhitespaceThenShouldBeCreatedSuccessfully() throws Exception {

		QuestSlotValidator questSlotValidator = new QuestSlotValidator();

		assertThatThrownBy(() -> questSlotValidator.validate("")).isInstanceOf(QuestDifficultyUnknownException.class)
				.hasMessageContaining("The Difficulty should not be blank");

	}

	@Test
	public void whenCreateQuestDifficultyWithUnknownDifficultyThenShouldThrowAnException() throws Exception {

		QuestSlotValidator questSlotValidator = new QuestSlotValidator();

		assertThatThrownBy(() -> questSlotValidator.validate("UNKNOWN")).isInstanceOf(QuestDifficultyUnknownException.class)
				.hasMessageContaining("Unexpected error when try to validate slotId");
	}

	@Test
	public void whenCreateQuestDifficultyWithNoDifficultyThenShouldThrowAnException() throws Exception {

		QuestSlotValidator questSlotValidator = new QuestSlotValidator();

		assertThatThrownBy(() -> questSlotValidator.validate(" ")).isInstanceOf(QuestDifficultyUnknownException.class)
				.hasMessageContaining("The Difficulty should not be blank");
	}
}
