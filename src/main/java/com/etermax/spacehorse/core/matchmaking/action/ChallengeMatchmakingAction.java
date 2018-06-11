package com.etermax.spacehorse.core.matchmaking.action;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.container.AsyncResponse;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.catalog.model.BotDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.tutorialprogress.TutorialProgressEntry;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.matchmaking.model.BotOpponentVerifier;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.matchmaking.service.MatchmakingQueueEntry;
import com.etermax.spacehorse.core.matchmaking.service.challenge.ChallengeMatchmakingService;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchFactory;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class ChallengeMatchmakingAction {

	final private ChallengeMatchmakingService matchmakingService;
	final private PlayerWinRateRepository playerWinRateRepository;

	final private boolean allowExtraData;
	final private CatalogRepository catalogRepository;
	final private MatchFactory matchFactory;
	final private PlayerRepository playerRepository;
	private final CaptainCollectionRepository captainCollectionRepository;
	private final BotOpponentVerifier botOpponentVerifier;

	public ChallengeMatchmakingAction(MatchFactory matchFactory, CatalogRepository catalogRepository, PlayerWinRateRepository playerWinRateRepository,
			Boolean allowExtraData, PlayerRepository playerRepository, CaptainCollectionRepository captainCollectionRepository,
			BotOpponentVerifier botOpponentVerifier, ChallengeMatchmakingService matchmakingService) {
		this.matchFactory = matchFactory;
		this.allowExtraData = allowExtraData;
		this.catalogRepository = catalogRepository;
		this.playerWinRateRepository = playerWinRateRepository;
		this.playerRepository = playerRepository;
		this.botOpponentVerifier = botOpponentVerifier;
		this.matchmakingService = matchmakingService;
		this.captainCollectionRepository = captainCollectionRepository;
	}

	public void match(Player player, AsyncResponse asyncResponse, String extraData) {

		PlayerWinRate playerWinRate = playerWinRateRepository.findOrCrateDefault(player.getUserId());
		CaptainsCollection captainCollection = captainCollectionRepository.findOrDefaultBy(player);
		BattlePlayer battlePlayer = BattlePlayerFactory.buildBattlePlayer(player, playerWinRate, captainCollection);

		Catalog catalog = catalogRepository.getActiveCatalogWithTag(player.getAbTag());

		if (player.getTutorialProgress().hasActiveTutorial()) {
			startTutorialMatch(asyncResponse, extraData, playerWinRate, player, battlePlayer, catalog);
		} else {
			startMatch(asyncResponse, extraData, playerWinRate, battlePlayer, catalog);
		}
	}

	private void startMatch(AsyncResponse asyncResponse, String extraData, PlayerWinRate playerWinRate, BattlePlayer battlePlayer, Catalog catalog) {
		if (botOpponentVerifier.isForcedToPlayWithBot(battlePlayer.getMmr(), catalog.getBotsChancesDefinitionsCollection().getEntries())) {
			matchmakingService.forceStartBotMatch(catalog, newMatchQueueEntry(asyncResponse, extraData, playerWinRate, battlePlayer));
		} else {
			enqueueRequest(asyncResponse, extraData, playerWinRate, battlePlayer);
		}
	}

	private void enqueueRequest(AsyncResponse asyncResponse, String extraData, PlayerWinRate playerWinRate, BattlePlayer battlePlayer) {
		if (!allowExtraData) {
			extraData = "";
		}
		matchmakingService.enqueueRequest(battlePlayer, asyncResponse, extraData, playerWinRate);
	}

	private void startTutorialMatch(AsyncResponse asyncResponse, String extraData, PlayerWinRate playerWinRate, Player player,
			BattlePlayer battlePlayer, Catalog catalog) {

		MatchmakingQueueEntry queueEntry = newMatchQueueEntry(asyncResponse, extraData, playerWinRate, battlePlayer);

		BotDefinition botDefinition = buildBotDefinitionFromTutorialStep(
				catalog.getTutorialProgressCollection().findByIdOrFail(player.getTutorialProgress().getActiveTutorial()));

		if (botDefinition.getCards().size() == 0) {
			throw new ApiException("Invalid number of cards in bot defined by tutorial step " + player.getTutorialProgress().getActiveTutorial());
		}

		Battle battle = matchFactory
				.buildBotMatch(queueEntry, botDefinition, botDefinition.getName(), extraData, catalog.getMapsCollection().getEntries(),
						MatchType.TUTORIAL, catalog.getCaptainDefinitionsCollection().getEntries(),
						catalog.getCaptainSkinDefinitionsCollection().getEntries()).start();

		player.setLastBattleId(battle.getBattleId());
		playerRepository.update(player);
	}

	private MatchmakingQueueEntry newMatchQueueEntry(AsyncResponse asyncResponse, String extraData, PlayerWinRate playerWinRate,
			BattlePlayer battlePlayer) {
		return new MatchmakingQueueEntry(battlePlayer, asyncResponse, extraData, playerWinRate, System.currentTimeMillis(), battlePlayer.getAbTag(),
				false);
	}

	private BotDefinition buildBotDefinitionFromTutorialStep(TutorialProgressEntry tutorialStep) {
		List<Card> cards = tutorialStep.getBotCards().stream().collect(Collectors.toList());
		return new BotDefinition("tutorial_bot", "TutorialBot", 0, 0, 10, cards);
	}

}