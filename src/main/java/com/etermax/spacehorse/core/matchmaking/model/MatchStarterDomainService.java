package com.etermax.spacehorse.core.matchmaking.model;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.catalog.model.BotDefinition;
import com.etermax.spacehorse.core.catalog.model.BotNameDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.matchmaking.service.challenge.ChallengeMatchmakingService;
import com.etermax.spacehorse.core.matchmaking.service.MatchmakingQueueEntry;
import com.etermax.spacehorse.core.matchmaking.model.match.Match;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchFactory;
import com.etermax.spacehorse.core.matchmaking.model.selectors.BotSelectionStrategy;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;

public class MatchStarterDomainService {

	private static final Logger logger = LoggerFactory.getLogger(ChallengeMatchmakingService.class);

	private final MatchFactory matchFactory;
	final private PlayerRepository playerRepository;

	public MatchStarterDomainService(MatchFactory matchFactory, PlayerRepository playerRepository) {
		this.matchFactory = matchFactory;
		this.playerRepository = playerRepository;
	}

	public Match startMatch(Catalog latestCatalog, MatchmakingQueueEntry queueEntry1, MatchmakingQueueEntry queueEntry2, MatchType matchType) {
		Match match = matchFactory
				.buildMatch(queueEntry1, queueEntry2, queueEntry1.getExtraData(), latestCatalog.getMapsCollection().getEntries(), matchType);
		Battle battle = match.start();
		updateLastBattleIds(battle);
		return match;
	}

	public Match startBotMatch(Catalog latestCatalog, MatchmakingQueueEntry queueEntry, ServerTimeProvider serverTimeProvider) {
		logger.info("Mmr to play with bot: " + queueEntry.getPlayer().getMmr());
		BotDefinition bot = getBestBot(queueEntry.getPlayer().getBotMmr(), latestCatalog.getBotsDefinitionsCollection().getEntries(),
				latestCatalog, serverTimeProvider);
		Match match = matchFactory
				.buildBotMatch(queueEntry, bot, getRandomBotName(latestCatalog.getBotsNamesDefinitionsCollection().getEntries(), bot),
						queueEntry.getExtraData(), latestCatalog.getMapsCollection().getEntries(), MatchType.CHALLENGE,
						latestCatalog.getCaptainDefinitionsCollection().getEntries(),
						latestCatalog.getCaptainSkinDefinitionsCollection().getEntries());
		Battle battle = match.start();
		updateLastBattleIds(battle);
		return match;
	}

	private BotDefinition getBestBot(int mmr, Collection<BotDefinition> botsCollection, Catalog catalog, ServerTimeProvider serverTimeProvider) {
		return new BotSelectionStrategy(catalog, serverTimeProvider).getBestBot(mmr, botsCollection);
	}

	private String getRandomBotName(List<BotNameDefinition> botsNamesCollection, BotDefinition botDefinition) {
		if (botsNamesCollection.size() == 0)
			return botDefinition.getName();

		int index = ThreadLocalRandom.current().nextInt(botsNamesCollection.size());
		return botsNamesCollection.get(index).getName();
	}

	private void updateLastBattleIds(Battle battle) {
		//This is a potential concurrent modification of the Player entities, if the player disconnects before
		//the matchmaking finishes.
		battle.getPlayers().stream().filter(x -> !x.getBot())
				.forEach(player -> playerRepository.updateLastBattleId(player.getUserId(), battle.getBattleId()));
	}

}
