package com.etermax.spacehorse.core.login.resource.response;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueService;
import com.etermax.spacehorse.core.liga.domain.LeagueConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.resource.response.player.PlayerResponse;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;
import com.google.common.collect.Maps;

public class PlayerResponseFactory {

	private static final Logger logger = LoggerFactory.getLogger(PlayerResponseFactory.class);

	private final QuestBoardRepository questBoardRepository;
	private final SpecialOfferBoardRepository specialOfferBoardRepository;
	private final CaptainCollectionRepository captainCollectionRepository;
	private final AchievementCollectionRepository achievementCollectionRepository;
	private final PlayerLeagueService playerLeagueService;

	public PlayerResponseFactory(QuestBoardRepository questBoardRepository, SpecialOfferBoardRepository specialOfferBoardRepository,
			CaptainCollectionRepository captainCollectionRepository, AchievementCollectionRepository achievementCollectionRepository,
			PlayerLeagueService playerLeagueService) {
		this.questBoardRepository = questBoardRepository;
		this.specialOfferBoardRepository = specialOfferBoardRepository;
		this.captainCollectionRepository = captainCollectionRepository;
		this.achievementCollectionRepository = achievementCollectionRepository;
		this.playerLeagueService = playerLeagueService;
	}

	public PlayerResponse createFrom(Player player, Integer mmr, Optional<PlayerLeague> playerSeasons) {
		if (player == null) { // TODO: 11/3/17 URGENTE revisar el constructor del PlayerResponse
			return new PlayerResponse(null, mmr, null, null, null, null, null, 0);
		}
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		SpecialOfferBoard specialOfferBoard = specialOfferBoardRepository.findOrDefaultBy(player);
		CaptainsCollection captainsCollection = captainCollectionRepository.findOrDefaultBy(player);
		AchievementCollection achievementCollection = findOrDefaultAchievementCollection(player);
		long endOfLeagueInSeconds = playerLeagueService.getEndOfLeagueInSeconds();
		return new PlayerResponse(player, mmr, questBoard, specialOfferBoard, captainsCollection, achievementCollection, playerSeasons,
				endOfLeagueInSeconds);
	}

	private AchievementCollection findOrDefaultAchievementCollection(Player player) {
		try {
			return achievementCollectionRepository.findOrDefaultBy(player);
		} catch (Exception e) {
			logger.error("====> Unexpected error when trying to create a default achievement collection", e);
			return new AchievementCollection(player.getUserId(), Maps.newHashMap());
		}
	}

	public PlayerSmallResponse createSmallFrom(Player player) {
		return new PlayerSmallResponse(player.getUserId(), player.getName(), player.getProgress().getLevel());
	}
}
