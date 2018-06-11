package com.etermax.spacehorse.core.quest.model;

import java.util.List;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.achievements.model.observers.DailyQuestCompletedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.HardQuestCompletedAchievementObserver;
import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.matchmaking.model.match.MatchType;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.handler.DefaultQuestProgressHandlerFactory;
import com.etermax.spacehorse.core.quest.model.handler.QuestProgressHandlerFactory;
import com.etermax.spacehorse.core.quest.model.unlock.QuestSlotDifficultiesConfiguration;
import com.etermax.spacehorse.core.quest.model.unlock.strategies.QuestSlotsInspector;
import com.etermax.spacehorse.core.quest.model.unlock.strategies.QuestSlotsInspectorFactory;
import com.google.common.collect.Lists;

public class QuestProgressUpdater {

	private final QuestBoardRepository questBoardRepository;
	private final CompleteAchievementAction completeAchievementAction;

	public QuestProgressUpdater(QuestBoardRepository questBoardRepository, CompleteAchievementAction completeAchievementAction) {
		this.questBoardRepository = questBoardRepository;
		this.completeAchievementAction = completeAchievementAction;
	}

	public void updateQuestsProgress(Player player, Battle battle, Catalog catalog) {

		if (isFriendlyMatch(battle))
			return;

		QuestProgressHandlerFactory questProgressHandlerFactory = new DefaultQuestProgressHandlerFactory(
				catalog.getCardDefinitionsCollection().getEntries(), catalog.getQuestCollection().getEntries());

		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		QuestSlotsInspector questSlotsInspector = newQuestSlotsInspector(catalog);

		boolean updated = questBoard.updateQuestsProgress(questProgressHandlerFactory, battle, player, questSlotsInspector);

		if (updated)
			questBoardRepository.addOrUpdate(player.getUserId(), questBoard);

		getAchievementObservers(questBoard).forEach(achievementObserver -> achievementObserver.update(player, catalog
				.getAchievementsDefinitionsCollection().getEntries()));
	}

	private List<AchievementsObserver> getAchievementObservers(QuestBoard questBoard) {
		List<AchievementsObserver> observers = Lists.newArrayList();
		observers.add(new HardQuestCompletedAchievementObserver(completeAchievementAction, questBoard));
		observers.add(new DailyQuestCompletedAchievementObserver(completeAchievementAction, questBoard));
		return observers;
	}

	private QuestSlotsInspector newQuestSlotsInspector(Catalog catalog) {
		QuestSlotDifficultiesConfiguration questSlotDifficultiesConfiguration = QuestSlotDifficultiesConfiguration
				.create(catalog.getFeatureByPlayerLvlDefinitionCollection().getEntries());
		return new QuestSlotsInspectorFactory().create(catalog.getGameConstants().isUseFeaturesByPlayerLvl(), questSlotDifficultiesConfiguration);
	}

	private boolean isFriendlyMatch(Battle battle) {
		return battle.getMatchType().equals(MatchType.FRIENDLY);
	}

}
