package com.etermax.spacehorse.core.login.model;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionFactory;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.achievements.model.observers.factories.AchievementsFactory;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollectionFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.player.model.NewPlayerConfiguration;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardConfiguration;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.reward.model.ApplyRewardConfiguration;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardContext;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;

public class PlayerCreationDomainService {

	private static final Logger logger = LoggerFactory.getLogger(PlayerCreationDomainService.class);

	private final PlayerRepository playerRepository;
	private final ApplyRewardDomainService applyRewardDomainService;
	private final QuestBoardRepository questBoardRepository;
	private final ServerTimeProvider timeProvider;
	private final SpecialOfferBoardRepository specialOfferBoardRepository;
	private final CaptainCollectionRepository captainCollectionRepository;
	private final CaptainsCollectionFactory captainsCollectionFactory;
	private final AchievementCollectionRepository achievementCollectionRepository;
	private final AchievementCollectionFactory achievementCollectionFactory;
	private final AchievementsFactory achievementsFactory;

	public PlayerCreationDomainService(PlayerRepository playerRepository, ApplyRewardDomainService applyRewardDomainService,
			QuestBoardRepository questBoardRepository, ServerTimeProvider timeProvider, SpecialOfferBoardRepository specialOfferBoardRepository,
			CaptainCollectionRepository captainCollectionRepository, AchievementCollectionRepository achievementCollectionRepository,
			AchievementCollectionFactory achievementCollectionFactory, AchievementsFactory achievementsFactory) {
		this.playerRepository = playerRepository;
		this.applyRewardDomainService = applyRewardDomainService;
		this.questBoardRepository = questBoardRepository;
		this.timeProvider = timeProvider;
		this.specialOfferBoardRepository = specialOfferBoardRepository;
		this.captainCollectionRepository = captainCollectionRepository;
		this.achievementCollectionRepository = achievementCollectionRepository;
		this.achievementCollectionFactory = achievementCollectionFactory;
		this.achievementsFactory = achievementsFactory;
		this.captainsCollectionFactory = new CaptainsCollectionFactory();
	}

	public Player createPlayer(String id, ABTag abTag, Catalog catalog, long timeNowInSeconds) {
		Player player = Player.buildNewPlayer(id, abTag, timeNowInSeconds, NewPlayerConfiguration.createBy(catalog));
		addOrUpdateAchievementCollection(catalog, player);

		if (tutorialIsNotAvailable(catalog)) {
			finishTutorialsForPlayer(catalog, player);
		}

		player.checkAndFixIntegrity(catalog, timeNowInSeconds);
		questBoardRepository.addOrUpdate(id, new QuestBoard(timeProvider, createQuestBoardConfigurationFrom(catalog), new Quest()));
		specialOfferBoardRepository.addOrUpdate(player, new SpecialOfferBoard(timeProvider));
		captainCollectionRepository.addOrUpdate(captainsCollectionFactory.create(player.getUserId(), catalog));

		playerRepository.add(player);
		return player;
	}

	private QuestBoardConfiguration createQuestBoardConfigurationFrom(Catalog catalog) {
		GameConstants gameConstants = catalog.getGameConstants();
		return new QuestBoardConfiguration(gameConstants.getSkipTimeForQuestBoard(), gameConstants.getGemsCostToSkipQuest(),
				gameConstants.getNextQuestRemainingTimeDividerFactor(), gameConstants.getNextQuestRemainingTimeGemsCostFactor());
	}

	private boolean tutorialIsNotAvailable(Catalog catalog) {
		return !catalog.getGameConstants().getTutorialAvailable();
	}

	private void finishTutorialsForPlayer(Catalog catalog, Player player) {
		giveAllTutorialRewards(player, catalog);
		setAllTutorialStepsAsFinished(player, catalog);
		skipEnterPlayerNameOnClient(player);
	}

	private void skipEnterPlayerNameOnClient(Player player) {
		player.getPlayerStats().incrementNameChanges();
	}

	private void setAllTutorialStepsAsFinished(Player player, Catalog catalog) {
		catalog.getTutorialProgressCollection().getEntries()
				.forEach(tutorialProgressEntry -> player.addFinishedTutorial(tutorialProgressEntry.getId()));
	}

	private void giveAllTutorialRewards(Player player, Catalog catalog) {
		List<Reward> rewards = new ArrayList<>();
		catalog.getTutorialProgressCollection().getEntries().forEach(tutorialProgressEntry -> {
			rewards.add(new Reward(RewardType.CHEST, tutorialProgressEntry.getChestId()));
		});
		ApplyRewardConfiguration configuration = ApplyRewardConfiguration.createBy(catalog);

		List<AchievementsObserver> achievementsObservers = achievementsFactory.createForApplyRewards();
		applyRewardDomainService.applyRewards(player, rewards, RewardContext.empty(), configuration, achievementsObservers);
	}

	private void addOrUpdateAchievementCollection(Catalog catalog, Player player) {
		try {
			achievementCollectionRepository.addOrUpdate(player.getUserId(), achievementCollectionFactory.create(player.getUserId(), catalog));
		} catch (Exception e) {
			logger.error("====> Unexpected error when trying to create a default achievement collection", e);
		}
	}
}
