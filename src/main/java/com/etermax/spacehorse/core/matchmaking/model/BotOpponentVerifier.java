package com.etermax.spacehorse.core.matchmaking.model;

import static com.etermax.spacehorse.core.catalog.model.bot.BotsChancesByMmrDefinition.NO_CHANCE;
import static com.etermax.spacehorse.core.catalog.model.bot.BotsChancesByMmrDefinition.TOP_CHANCE;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import com.etermax.spacehorse.core.catalog.model.bot.BotsChancesByMmrDefinition;

public class BotOpponentVerifier {

	private final Random random;

	public BotOpponentVerifier() {
		random = ThreadLocalRandom.current();
	}

	public boolean isForcedToPlayWithBot(int playerMmr, List<BotsChancesByMmrDefinition> definitions) {
		BotsChancesByMmrDefinition definition = findDefinitionFor(playerMmr, definitions);
		int randomNumber = random.nextInt(TOP_CHANCE);
		return randomNumber != 0 && randomNumber <= definition.getChance();
	}

	private BotsChancesByMmrDefinition findDefinitionFor(int playerMmr, List<BotsChancesByMmrDefinition> definitions) {
		BotsChancesByMmrDefinition definition = definitions.stream().sorted(byMinMmrAsc()).findFirst().orElse(aDefaultDefinitionWith(NO_CHANCE));

		if (isCurrentMmrLessThanMinConfiguredMmr(playerMmr, definition)) {
			return aDefaultDefinitionWith(TOP_CHANCE);
		}

		return definitions.stream().//
				filter(byMmrBetween(playerMmr)). //
				findFirst(). //
				orElse(aDefaultDefinitionWith(NO_CHANCE));
	}

	private Comparator<BotsChancesByMmrDefinition> byMinMmrAsc() {
		return Comparator.comparingLong(BotsChancesByMmrDefinition::getMinMmr);
	}

	private boolean isCurrentMmrLessThanMinConfiguredMmr(int mmr, BotsChancesByMmrDefinition definition) {
		return mmr < definition.getMinMmr();
	}

	private Predicate<BotsChancesByMmrDefinition> byMmrBetween(int mmr) {
		return definition -> definition.isBetween(mmr);
	}

	private BotsChancesByMmrDefinition aDefaultDefinitionWith(int chance) {
		return new BotsChancesByMmrDefinition("id-0", 0, 0, chance);
	}
}
