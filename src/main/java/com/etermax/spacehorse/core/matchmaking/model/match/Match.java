package com.etermax.spacehorse.core.matchmaking.model.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattleFactory;
import com.etermax.spacehorse.core.battle.model.BattlePlayer;
import com.etermax.spacehorse.core.battle.model.BattlePlayerBuilder;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.capitain.infrastructure.skins.DynamoCaptainSkin;
import com.etermax.spacehorse.core.capitain.infrastructure.slots.DynamoCaptainSlot;
import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.resource.skins.CaptainSlotResponse;
import com.etermax.spacehorse.core.catalog.model.BotDefinition;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;
import com.etermax.spacehorse.core.matchmaking.resource.response.MatchmakingResponse;
import com.etermax.spacehorse.core.matchmaking.resource.response.MatchmakingResponseOpponentData;
import com.etermax.spacehorse.core.matchmaking.service.MatchmakingQueueEntry;
import com.etermax.spacehorse.core.matchmaking.model.selectors.MapSelectorStrategy;
import com.etermax.spacehorse.core.matchmaking.model.selectors.captains.CaptainSelectorStrategy;
import com.etermax.spacehorse.core.matchmaking.model.selectors.captains.NullCaptainSelectorStrategy;
import com.etermax.spacehorse.core.matchmaking.model.selectors.captains.RandomCaptainSelectorStrategy;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;

public class Match {

	private final int MMR_BOT_RANGE = 60;

	final private MatchmakingQueueEntry entry1;
	final private Optional<MatchmakingQueueEntry> entry2;
	final private BattleFactory battleFactory;
	final private BattleRepository battleRepository;
	final private String realtimeServerUrl;
	final private String extraData;
	final private Random random;
	final private Collection<MapDefinition> mapsCollection;
	final private BotDefinition botDefinition;
	final private String botName;
	final private MatchType matchType;
	private final CaptainSelectorStrategy captainSelectorStrategy;

	public Match(MatchmakingQueueEntry entry1, MatchmakingQueueEntry entry2, BattleFactory battleFactory, BattleRepository battleRepository,
			String realtimeServerUrl, String extraData, Collection<MapDefinition> mapsCollection, MatchType matchType) {
		this.entry1 = entry1;
		this.entry2 = Optional.of(entry2);
		this.battleFactory = battleFactory;
		this.battleRepository = battleRepository;
		this.realtimeServerUrl = realtimeServerUrl;
		this.extraData = extraData;
		this.random = ThreadLocalRandom.current();
		this.mapsCollection = mapsCollection;
		this.botDefinition = null;
		this.botName = "";
		this.matchType = matchType;
		this.captainSelectorStrategy = new NullCaptainSelectorStrategy();
	}

	public Match(MatchmakingQueueEntry entry1, BotDefinition botDefinition, String botName, BattleFactory battleFactory,
			BattleRepository battleRepository, String realtimeServerUrl, String extraData, Collection<MapDefinition> mapsCollection,
			MatchType matchType, List<CaptainDefinition> captainDefinitions, List<CaptainSkinDefinition> captainSkinDefinitions) {
		this.entry1 = entry1;
		this.entry2 = Optional.empty();
		this.battleFactory = battleFactory;
		this.battleRepository = battleRepository;
		this.realtimeServerUrl = realtimeServerUrl;
		this.extraData = extraData;
		this.random = ThreadLocalRandom.current();
		this.mapsCollection = mapsCollection;
		this.botDefinition = botDefinition;
		this.botName = botName;
		this.matchType = matchType;
		this.captainSelectorStrategy = new RandomCaptainSelectorStrategy(mapsCollection, captainDefinitions, captainSkinDefinitions);
	}

	static private List<CardResponse> convertCardsToCardsResponse(List<Card> playerCards) {
		return playerCards.stream().map(CardResponse::new).collect(Collectors.toList());
	}

	public MatchmakingQueueEntry getEntry1() {
		return entry1;
	}

	public Optional<MatchmakingQueueEntry> getEntry2() {
		return entry2;
	}

	public Battle start() {

		BattlePlayer player1 = getPlayer1();
		BattlePlayer player2 = getPlayer2();

		int seed = getSeed();
		MapDefinition map = getBestMap(player1.getMmr(), player2.getMmr());
		String realtimeServerSessionKey = buildRealtimeServerSessionKey(player1, player2);

		Battle battle = buildAndPersistBattle(player1, player2, seed, map, matchType);

		sendPlayerResponse(entry1, player1, player2, seed, map, realtimeServerSessionKey, battle);

		entry2.ifPresent(entry2 -> sendPlayerResponse(entry2, player2, player1, seed, map, realtimeServerSessionKey, battle));

		return battle;
	}

	private BattlePlayer getPlayer1() {
		return entry1.getPlayer();
	}

	private BattlePlayer getPlayer2() {
		return entry2.map(x -> x.getPlayer()).orElseGet(() -> buildPlayerFromBot(botDefinition, botName));
	}

	private BattlePlayer buildPlayerFromBot(BotDefinition bot, String botName) {
		Integer randomBotMmr = getRandomBotMmr();
		Captain captain = captainSelectorStrategy.chooseRandomBotCaptain(randomBotMmr).get();
		return new BattlePlayerBuilder() //
				.setUserId("bot")//
				.setSelectedCards(shuffleCards(bot.getCards()))//
				.setIsBot(true)//
				.setMmr(randomBotMmr)//
				.setCatalogId(getPlayer1().getCatalogId())//
				.setAbTag(getPlayer1().getAbTag().toString())//
				.setName(botName).setLevel(bot.getLevel())//
				.setSelectedCaptainId(captain.getCaptainId())//
				.setCaptainSlots(mapToCaptainSlotsFrom(captain)) //
				.createBattlePlayer();
	}

	private List<DynamoCaptainSlot> mapToCaptainSlotsFrom(Captain captain) {
		return captain.getCaptainSlots().stream()
				.map(captainSlot -> new DynamoCaptainSlot(captainSlot.getSlotNumber(), new DynamoCaptainSkin(captainSlot.getCaptainSkinId())))
				.collect(Collectors.toList());
	}

	private List<Card> shuffleCards(List<Card> selectedCards) {
		Collections.shuffle(selectedCards, ThreadLocalRandom.current());
		return selectedCards;
	}

	private Integer getRandomBotMmr() {

		int playerMmr = getPlayer1().getMmr();

		int minMMR = Math.max(playerMmr - MMR_BOT_RANGE, 0);
		int maxMMR = Math.max(playerMmr + MMR_BOT_RANGE, 0);

		if (maxMMR > minMMR) {
			return minMMR + random.nextInt(maxMMR - minMMR);
		} else {
			return minMMR;
		}
	}

	private MapDefinition getBestMap(Integer mmr1, Integer mmr2) {
		return new MapSelectorStrategy().getBestMap(mmr1, mmr2, mapsCollection);
	}

	private void sendPlayerResponse(MatchmakingQueueEntry entry, BattlePlayer player, BattlePlayer opponentPlayer, int seed, MapDefinition map,
			String realtimeServerSessionKey, Battle battle) {
		MatchmakingResponse response = buildResponse(battle, player, opponentPlayer, seed, map, realtimeServerSessionKey);
		entry.found(response);
	}

	private MatchmakingResponse buildResponse(Battle battle, BattlePlayer player, BattlePlayer opponentPlayer, int seed, MapDefinition map,
			String realtimeServerSessionKey) {

		List<CardResponse> playerCards = convertCardsToCardsResponse(player.getSelectedCards());
		List<CardResponse> opponentCards = opponentPlayer.getBot() ?
				convertCardsToCardsResponse(opponentPlayer.getSelectedCards()) :
				new ArrayList<>();

		return new MatchmakingResponse(//
				battle.getBattleId(), //
				playerCards,//
				opponentCards,//
				new MatchmakingResponseOpponentData(opponentPlayer), //
				seed, //
				map.getId(), //
				realtimeServerSessionKey, //
				realtimeServerUrl, //
				extraData, //
				player.getSelectedCaptainId(), //
				player.getCaptainSlots().stream().map(toCaptainSlotResponse()).collect(Collectors.toList()), //
				battle.getMatchType().toString());
	}

	private Function<DynamoCaptainSlot, CaptainSlotResponse> toCaptainSlotResponse() {
		return captainSlot -> new CaptainSlotResponse(captainSlot.getSlotNumber(), captainSlot.getCaptainSkinId());
	}

	private Battle buildAndPersistBattle(BattlePlayer player1, BattlePlayer player2, int seed, MapDefinition map, MatchType matchType) {
		Battle battle = battleFactory
				.buildBattle(Arrays.asList(player1, player2), player1.getCatalogId(), seed, map, extraData, new Date(), matchType);
		battle = battleRepository.add(battle);
		return battle;
	}

	private String buildRealtimeServerSessionKey(BattlePlayer player1, BattlePlayer player2) {
		return player1.getUserId() + "-" + player2.getUserId() + "-" + random.nextInt(99999999);
	}

	private int getSeed() {
		return random.nextInt(99999);
	}
}

