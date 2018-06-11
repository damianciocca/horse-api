package com.etermax.spacehorse.core.matchmaking.model.match;

import java.util.Collection;
import java.util.List;

import com.etermax.spacehorse.core.battle.model.BattleFactory;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.catalog.model.BotDefinition;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;
import com.etermax.spacehorse.core.matchmaking.service.MatchmakingQueueEntry;

public class DefaultMatchFactory implements MatchFactory {

	final private BattleFactory battleFactory;
	final private String realTimeServerUrl;
	final private BattleRepository battleRepository;

	public DefaultMatchFactory(BattleFactory battleFactory, String realTimeServerUrl, BattleRepository battleRepository) {
		this.battleFactory = battleFactory;
		this.realTimeServerUrl = realTimeServerUrl;
		this.battleRepository = battleRepository;
	}

	@Override
	public Match buildMatch(MatchmakingQueueEntry entry1, MatchmakingQueueEntry entry2, String extraData, Collection<MapDefinition> mapsCollection,
			MatchType matchType) {
		return new Match(entry1, entry2, battleFactory, battleRepository, realTimeServerUrl, extraData, mapsCollection, matchType);
	}

	@Override
	public Match buildBotMatch(MatchmakingQueueEntry entry1, BotDefinition botDefinition, String botName, String extraData,
			Collection<MapDefinition> mapsCollection, MatchType matchType, List<CaptainDefinition> captainDefinitions,
			List<CaptainSkinDefinition> captainSkinDefinitions) {
		return new Match(entry1, botDefinition, botName, battleFactory, battleRepository, realTimeServerUrl, extraData, mapsCollection, matchType,
				captainDefinitions, captainSkinDefinitions);
	}
}
