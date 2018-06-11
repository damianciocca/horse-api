package com.etermax.spacehorse.core.achievements.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.action.FindAchievementAction;
import com.etermax.spacehorse.core.achievements.model.observers.BattlePlaysReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.CaptainSkinsReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.CardsUnlockedReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.PlayerLevelReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.PlayerMapReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsInLoginObserver;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.player.model.Player;

public class AchievementsInLoginExecutor {

	private static final Logger logger = LoggerFactory.getLogger(AchievementsInLoginExecutor.class);

	private final FindAchievementAction findAchievementAction;
	private final CompleteAchievementAction completeAchievementAction;
	private final AchievementCollectionRepository achievementCollectionRepository;
	private final CaptainCollectionRepository captainCollectionRepository;

	public AchievementsInLoginExecutor(FindAchievementAction findAchievementAction, CompleteAchievementAction completeAchievementAction,
			AchievementCollectionRepository achievementCollectionRepository, CaptainCollectionRepository captainCollectionRepository) {
		this.findAchievementAction = findAchievementAction;
		this.completeAchievementAction = completeAchievementAction;
		this.achievementCollectionRepository = achievementCollectionRepository;
		this.captainCollectionRepository = captainCollectionRepository;
	}

	// just for achievements what are needed to check in login => use interface "AchievementInLoginObserver"
	public void execute(Player player, Catalog catalog) {
		try {
			AchievementCollection achievementCollection = achievementCollectionRepository.findOrDefaultBy(player);

			List<AchievementDefinition> achievementDefinitions = catalog.getAchievementsDefinitionsCollection().getEntries();

			updateAchievementIfBattlePlaysWereReached(player, achievementCollection, achievementDefinitions);
			updateAchievementIfCardsUnlockedWereReached(player, achievementCollection, achievementDefinitions);
			updateAchievementIfPlayerLevelWasReached(player, achievementCollection, achievementDefinitions);
			updateAchievementIfPlayerMapWasReached(player, achievementCollection, achievementDefinitions);
			updateAchievementIfCaptainSkinsPurchasedWasReached(player, achievementCollection, achievementDefinitions);
		} catch (Exception e) {
			logger.error("====> Unexpected error when trying to check if there are any achievement ready to claim", e);
		}
	}

	private void updateAchievementIfBattlePlaysWereReached(Player player, AchievementCollection achievementCollection,
			List<AchievementDefinition> achievementDefinitions) {
		BattlePlaysReachedAchievementObserver achievementObserver = createBattlePlaysReachedAchievementObserver();
		List<AchievementDefinition> definitionsByType = achievementObserver.filterDefinitionsByType(achievementDefinitions);

		update(player, achievementCollection, achievementObserver, definitionsByType);
	}

	private void updateAchievementIfCardsUnlockedWereReached(Player player, AchievementCollection achievementCollection,
			List<AchievementDefinition> achievementDefinitions) {
		CardsUnlockedReachedAchievementObserver achievementObserver = createCardsUnlockedReachedAchievementObserver();
		List<AchievementDefinition> definitionsByType = achievementObserver.filterDefinitionsByType(achievementDefinitions);

		update(player, achievementCollection, achievementObserver, definitionsByType);
	}

	private void updateAchievementIfPlayerLevelWasReached(Player player, AchievementCollection achievementCollection,
			List<AchievementDefinition> achievementDefinitions) {
		PlayerLevelReachedAchievementObserver achievementObserver = createPlayerLevelReachedAchievementObserver();
		List<AchievementDefinition> definitionsByType = achievementObserver.filterDefinitionsByType(achievementDefinitions);

		update(player, achievementCollection, achievementObserver, definitionsByType);
	}

	private void updateAchievementIfPlayerMapWasReached(Player player, AchievementCollection achievementCollection,
			List<AchievementDefinition> achievementDefinitions) {
		PlayerMapReachedAchievementObserver achievementObserver = createPlayerMapReachedAchievementObserver();
		List<AchievementDefinition> definitionsByType = achievementObserver.filterDefinitionsByType(achievementDefinitions);

		update(player, achievementCollection, achievementObserver, definitionsByType);
	}

	private void updateAchievementIfCaptainSkinsPurchasedWasReached(Player player, AchievementCollection achievementCollection,
			List<AchievementDefinition> achievementDefinitions) {
		CaptainsCollection captainsCollection = captainCollectionRepository.findOrDefaultBy(player);
		CaptainSkinsReachedAchievementObserver achievementObserver = createCaptainSkinsReachedAchievementObserver(captainsCollection);
		List<AchievementDefinition> definitionsByType = achievementObserver.filterDefinitionsByType(achievementDefinitions);

		update(player, achievementCollection, achievementObserver, definitionsByType);
	}

	private void update(Player player, AchievementCollection achievementCollection, AchievementsInLoginObserver achievementsInLoginObserver,
			List<AchievementDefinition> definitionsByType) {
		definitionsByType.forEach(achievementDefinition -> {
			Achievement achievement = achievementCollection.getAchievementById(achievementDefinition.getId());
			achievementsInLoginObserver.update(player, achievement);
		});
	}

	private PlayerLevelReachedAchievementObserver createPlayerLevelReachedAchievementObserver() {
		return new PlayerLevelReachedAchievementObserver(findAchievementAction, completeAchievementAction);
	}

	private PlayerMapReachedAchievementObserver createPlayerMapReachedAchievementObserver() {
		return new PlayerMapReachedAchievementObserver(findAchievementAction, completeAchievementAction);
	}

	private BattlePlaysReachedAchievementObserver createBattlePlaysReachedAchievementObserver() {
		return new BattlePlaysReachedAchievementObserver(findAchievementAction, completeAchievementAction);
	}

	private CardsUnlockedReachedAchievementObserver createCardsUnlockedReachedAchievementObserver() {
		return new CardsUnlockedReachedAchievementObserver(findAchievementAction, completeAchievementAction);
	}

	private CaptainSkinsReachedAchievementObserver createCaptainSkinsReachedAchievementObserver(CaptainsCollection captainsCollection) {
		return new CaptainSkinsReachedAchievementObserver(findAchievementAction, completeAchievementAction, captainsCollection);
	}
}
