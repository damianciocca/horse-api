package com.etermax.spacehorse.core.player.integrityfixer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.progress.TutorialProgress;
import com.etermax.spacehorse.core.player.repository.dynamo.PlayerDynamoRepository;
import com.etermax.spacehorse.mock.MockUtils;

public class MMRIntegrityFixerTest {

	private final int MINIMUM_MMR_AFTER_TUTORIAL = 100;
	private MmrIntegrityFixer mmrIntegrityFixer;
	private Player player;

	@Before
	public void setUp() throws Exception {
		mmrIntegrityFixer = givenAMMRIntegrityFixer();
		player = givenAPlayer();
	}

	@Test
	public void whenPlayerHasMMRBelowMinimumFixMMRReturnsMinimum() {
		//Given
		int playerMmr = 99;
		int expectedMMR = 100;

		//When
		int fixedMMR = mmrIntegrityFixer.getFixedMmrIfIsLessThanMinimumRequired(player, playerMmr, MINIMUM_MMR_AFTER_TUTORIAL);

		//Then
		assertThat(fixedMMR).isEqualTo(expectedMMR);
	}

	@Test
	public void whenPlayerHasMinimumMMEFixMMRReturnsPlayersMMR() {
		//Given
		int playerMmr = 100;
		int expectedMMR = 100;

		//When
		int fixedMMR = mmrIntegrityFixer.getFixedMmrIfIsLessThanMinimumRequired(player, playerMmr, MINIMUM_MMR_AFTER_TUTORIAL);

		//Then
		assertThat(fixedMMR).isEqualTo(expectedMMR);
	}

	@Test
	public void whenPlayerHasMMRAboveMinimumFixMMRReturnsPlayerMMR() {
		//Given
		int playerMmr = 101;
		int expectedMMR = 101;

		//When
		int fixedMMR = mmrIntegrityFixer.getFixedMmrIfIsLessThanMinimumRequired(player, playerMmr, MINIMUM_MMR_AFTER_TUTORIAL);

		//Then
		assertThat(fixedMMR).isEqualTo(expectedMMR);
	}

	private Player givenAPlayer() {
		int gemsAmount = 100;
		Player player = MockUtils.mockPlayerWithGems("playerId", gemsAmount, mock(PlayerDynamoRepository.class));
		TutorialProgress tutorialProgress = mock(TutorialProgress.class);
		List<String> secondBattleTutorial = new ArrayList<>();
		secondBattleTutorial.add("SecondBattle");
		when(tutorialProgress.getFinishedTutorials()).thenReturn(secondBattleTutorial);
		when(player.getTutorialProgress()).thenReturn(tutorialProgress);
		return player;
	}

	private MmrIntegrityFixer givenAMMRIntegrityFixer() {
		return new MmrIntegrityFixer();
	}
}
