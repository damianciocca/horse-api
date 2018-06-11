package com.etermax.spacehorse.core.matchmaking.model.match;

import java.util.Collection;
import java.util.List;

import com.etermax.spacehorse.core.catalog.model.BotDefinition;
import com.etermax.spacehorse.core.catalog.model.MapDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;
import com.etermax.spacehorse.core.matchmaking.service.MatchmakingQueueEntry;

public interface MatchFactory {

	Match buildMatch(MatchmakingQueueEntry entry1, MatchmakingQueueEntry entry2, String extraData,
			Collection<MapDefinition> mapsCollection, MatchType matchType);

	Match buildBotMatch(MatchmakingQueueEntry entry1, BotDefinition botDefinition, String botName, String extraData,
			Collection<MapDefinition> mapsCollection, MatchType matchType, List<CaptainDefinition> captainDefinitions,
			List<CaptainSkinDefinition> captainSkinDefinitions);

}
