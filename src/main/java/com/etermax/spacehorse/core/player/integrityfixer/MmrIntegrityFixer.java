package com.etermax.spacehorse.core.player.integrityfixer;

import java.util.List;
import java.util.function.Predicate;

import com.etermax.spacehorse.core.player.model.Player;

public class MmrIntegrityFixer {
	private static final String SECOND_BATTLE_TUTORIAL_ID = "SecondBattle";

	public MmrIntegrityFixer() {
	}

	public int getFixedMmrIfIsLessThanMinimumRequired(Player player, int playerMmr, int minimumMmr) {
		List<String> playerFinishedTutorials = player.getTutorialProgress().getFinishedTutorials();
		boolean playerHasFinishedSecondTutorialBattle = playerFinishedTutorials.stream().anyMatch(withTutorialStep());
		if (playerHasFinishedSecondTutorialBattle && playerMmr < minimumMmr) {
			return minimumMmr;
		} else {
			return playerMmr;
		}
	}

	private Predicate<String> withTutorialStep() {
		return tutorialId -> tutorialId.equals(SECOND_BATTLE_TUTORIAL_ID);
	}
}
