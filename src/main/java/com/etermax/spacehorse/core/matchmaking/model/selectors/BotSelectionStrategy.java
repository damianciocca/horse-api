package com.etermax.spacehorse.core.matchmaking.model.selectors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import com.etermax.spacehorse.core.catalog.model.BotDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class BotSelectionStrategy {

	private final Catalog catalog;
	private final ServerTimeProvider serverTimeProvider;
	private Random random;

	public BotSelectionStrategy(Catalog catalog, ServerTimeProvider serverTimeProvider) {
		this.catalog = catalog;
		this.serverTimeProvider = serverTimeProvider;
		this.random = ThreadLocalRandom.current();
	}

	public BotDefinition getBestBot(int mmr, Collection<BotDefinition> bots) {

		Validate.notNull(bots);
		Validate.notEmpty(bots);

		List<BotDefinition> filteredBots = bots.stream().filter(botDefinition -> botMmrIsEqualsOrGreaterThanMinMmr(mmr, botDefinition)).collect(Collectors.toList());

		if (filteredBots.isEmpty()) {
			filteredBots = new ArrayList<>(bots);
		}

		filteredBots = filteredBots.stream().filter(this::botHasActiveCards).collect(Collectors.toList());

		return filteredBots.get(random.nextInt(filteredBots.size()));
	}

	private boolean botMmrIsEqualsOrGreaterThanMinMmr(int mmr, BotDefinition x) {
		return mmr >= x.getMinMMR() && mmr < x.getMaxMMR();
	}

	private boolean botHasActiveCards(BotDefinition botDefinition) {
		return botDefinition.getCards().stream().anyMatch(card -> card.getDefinition(catalog).isActiveFor(serverTimeProvider.getDateTime()));
	}
}
