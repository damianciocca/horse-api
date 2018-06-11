package com.etermax.spacehorse.core.matchmaking.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.bot.BotsChancesByMmrDefinition;
import com.google.common.collect.Lists;

public class BotOpponentVerifierTest {

	private List<BotsChancesByMmrDefinition> definitions;
	private BotOpponentVerifier botOpponentVerifier;

	@Before
	public void setUp() throws Exception {
		definitions = Lists.newArrayList( //
				new BotsChancesByMmrDefinition("id-0", 0, 150, 100), //
				new BotsChancesByMmrDefinition("id-151", 151, 250, 80), //
				new BotsChancesByMmrDefinition("id-251", 251, 350, 30), //
				new BotsChancesByMmrDefinition("id-351", 351, 500, 10));
		botOpponentVerifier = new BotOpponentVerifier();
	}

	@Test
	public void givenA140OfMmr_ThenTheChancesOfPlayingWithABotShouldBeAlwaysTrue() {
		// given
		int mmr = 140;
		// when
		boolean result = botOpponentVerifier.isForcedToPlayWithBot(mmr, definitions);
		// then
		assertThat(result).isTrue();
	}

	@Test
	public void givenAMmrLessThanMinConfiguredMmr_ThenTheChancesOfPlayingWithABotShouldBeAlwaysTrue() {
		// given
		List<BotsChancesByMmrDefinition> anotherDefinitions = anotherDefinitions();
		int mmrLessThanMinMmr = 45;
		// when
		boolean result = botOpponentVerifier.isForcedToPlayWithBot(mmrLessThanMinMmr, anotherDefinitions);
		// then
		assertThat(result).isTrue();
	}

	@Test
	public void givenAMmrToMuchGreaterThanMaxConfiguredMmr_ThenTheChancesOfPlayingWithABotShouldBeAlwaysFalse() {
		// given
		int mmrGreaterThanMaxMmr = 99999;
		// when
		boolean result = botOpponentVerifier.isForcedToPlayWithBot(mmrGreaterThanMaxMmr, definitions);
		// then
		assertThat(result).isFalse();
	}

	@Test
	public void givenFrom100To260OfMmr_ThenTheChancesOfPlayingWithBootsShouldBeGreater() {
		// given
		int fromMmr = 100;
		int toMmr = 260;
		// when
		WhenBotPlayVerifierInvoked result = new WhenBotPlayVerifierInvoked(fromMmr, toMmr).invoke();
		// then
		assertThat(result.getChancesOfPlayingWithBot()).isGreaterThan(result.getChancesOfNotToPlayingWithBot());
	}

	@Test
	public void givenFrom300To350OfMmr_ThenTheChancesOfPlayingWithBootsShouldBeLess() {
		// given
		int fromMmr = 300;
		int toMmr = 350;
		// when
		WhenBotPlayVerifierInvoked result = new WhenBotPlayVerifierInvoked(fromMmr, toMmr).invoke();
		// then
		assertThat(result.getChancesOfPlayingWithBot()).isLessThan(result.getChancesOfNotToPlayingWithBot());
	}

	@Test
	public void givenFrom299To505OfMmr_ThenTheChancesOfPlayingWithBootsShouldBeLess() {
		// given
		int fromMmr = 299;
		int toMmr = 505;
		// when
		WhenBotPlayVerifierInvoked result = new WhenBotPlayVerifierInvoked(fromMmr, toMmr).invoke();
		// then
		assertThat(result.getChancesOfPlayingWithBot()).isLessThan(result.getChancesOfNotToPlayingWithBot());
	}

	private List<BotsChancesByMmrDefinition> anotherDefinitions() {
		return Lists.newArrayList( //
				new BotsChancesByMmrDefinition("id-100", 100, 200, 60), //
				new BotsChancesByMmrDefinition("id-200", 201, 300, 30));
	}

	private class WhenBotPlayVerifierInvoked {
		private int minMmr;
		private int maxMmr;
		private int chancesOfPlayingWithBot;
		private int chancesOfNotToPlayingWithBot;

		public WhenBotPlayVerifierInvoked(int minMmr, int maxMmr) {
			this.minMmr = minMmr;
			this.maxMmr = maxMmr;
			this.chancesOfPlayingWithBot = 0;
			this.chancesOfNotToPlayingWithBot = 0;
		}

		public int getChancesOfPlayingWithBot() {
			return chancesOfPlayingWithBot;
		}

		public int getChancesOfNotToPlayingWithBot() {
			return chancesOfNotToPlayingWithBot;
		}

		public WhenBotPlayVerifierInvoked invoke() {
			for (int currentMmr = minMmr; currentMmr < maxMmr; currentMmr++) {
				if (botOpponentVerifier.isForcedToPlayWithBot(currentMmr, definitions)) {
					chancesOfPlayingWithBot++;
				} else {
					chancesOfNotToPlayingWithBot++;
				}
			}
			return this;
		}
	}
}
