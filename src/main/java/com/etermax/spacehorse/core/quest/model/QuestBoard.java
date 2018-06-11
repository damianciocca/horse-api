package com.etermax.spacehorse.core.quest.model;

import static com.google.common.collect.Maps.newConcurrentMap;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.PaymentMethodUtil;
import com.etermax.spacehorse.core.quest.exception.DailyQuestIsNotInitializedException;
import com.etermax.spacehorse.core.quest.exception.DailyQuestNotClaimedException;
import com.etermax.spacehorse.core.quest.exception.QuestAlreadyClaimedException;
import com.etermax.spacehorse.core.quest.exception.QuestAlreadyStartedException;
import com.etermax.spacehorse.core.quest.exception.QuestBoardAlreadyInitializedException;
import com.etermax.spacehorse.core.quest.exception.QuestBoardSkipTimeNotReachedException;
import com.etermax.spacehorse.core.quest.exception.QuestBoardSlotNotFoundException;
import com.etermax.spacehorse.core.quest.exception.QuestIncompleteException;
import com.etermax.spacehorse.core.quest.exception.QuestRefreshTimeNotReachedException;
import com.etermax.spacehorse.core.quest.model.handler.QuestProgressHandlerFactory;
import com.etermax.spacehorse.core.quest.model.unlock.strategies.QuestSlotsInspector;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.google.common.collect.ImmutableMap;

public class QuestBoard {

	private final ServerTimeProvider timeProvider;
	private final QuestBoardConfiguration configuration;
	private Map<String, QuestSlot> slots;
	private long skipTimeInSeconds;
	private Quest dailyQuest;

	public QuestBoard(ServerTimeProvider timeProvider, QuestBoardConfiguration configuration, Quest dailyQuest) {
		this.timeProvider = timeProvider;
		this.configuration = configuration;
		this.skipTimeInSeconds = 0L;
		this.slots = newConcurrentMap();
		this.dailyQuest = dailyQuest;
		initializeSlotsByDifficulty();
	}

	public static QuestBoard restore(ServerTimeProvider timeProvider, Map<String, QuestSlot> slots, Quest dailyQuest, long skipTimeInSeconds,
			QuestBoardConfiguration configuration) {
		return new QuestBoard(timeProvider, slots, dailyQuest, skipTimeInSeconds, configuration);
	}

	private QuestBoard(ServerTimeProvider timeProvider, Map<String, QuestSlot> slots, Quest dailyQuest, long skipTimeInSeconds,
			QuestBoardConfiguration configuration) {
		this.timeProvider = timeProvider;
		this.slots = slots;
		this.skipTimeInSeconds = skipTimeInSeconds;
		this.configuration = configuration;
		this.dailyQuest = dailyQuest;
	}

	public QuestSlot getSlot(String slotId) {
		validateSlotId(slotId);
		return slots.get(slotId);
	}

	public Quest claimQuest(String slotId, long refreshTimeInSeconds) {
		validateSlotId(slotId);
		QuestSlot slot = slots.get(slotId);
		if (isNotCompleted(slot)) {
			throw new QuestIncompleteException("Quest incomplete. Unable to apply rewards.");
		}
		if (isAlreadyClaimed(slot)) {
			throw new QuestAlreadyClaimedException("Quest already claimed. Unable to apply rewards.");
		}
		slot.claim(refreshTimeInSeconds);
		return slot.getActiveQuest();
	}

	public void claimDailyQuest() {
		if (!dailyQuest.isCompleted()) {
			throw new QuestIncompleteException("Daily quest incomplete. Unable to apply rewards.");
		}
		if (dailyQuest.isClaimed()) {
			throw new QuestAlreadyClaimedException("Daily quest already claimed. Unable to apply rewards.");
		}
		dailyQuest.claim();
	}

	public void putFreeQuest(String slotId, Quest quest) {
		validateSlotId(slotId);
		QuestSlot slot = slots.get(slotId);
		if (isAlreadyStarted(slot)) {
			throw new QuestAlreadyStartedException("Quest already started. Unable to refresh.");
		}
		if (isAlreadyClaimed(slot) && refreshTimeIsNotReached(slot)) {
			throw new QuestRefreshTimeNotReachedException();
		}
		putQuest(slotId, quest, slot);
	}

	public void putPayingQuest(String slotId, Quest quest) {
		validateSlotId(slotId);
		QuestSlot slot = slots.get(slotId);
		putQuest(slotId, quest, slot);
	}

	public void putDailyQuest(Quest newDailyQuest) {
		if (dailyQuest.isInitial()) {
			dailyQuest = newDailyQuest;
		} else {
			if (dailyQuestRefreshTimeIsNotReached()) {
				throw new QuestRefreshTimeNotReachedException();
			}
			if (dailyQuest.isCompleted() && isNotClaimed(dailyQuest)) {
				throw new DailyQuestNotClaimedException();
			}
			this.dailyQuest = newDailyQuest;
		}
	}

	public void freeSkip(String slotId) {
		validateSlotId(slotId);
		if (skipTimeIsNotReached()) {
			throw new QuestBoardSkipTimeNotReachedException();
		}
		skip(slotId);
	}

	public void skipPaying(String slotId, Player player) {
		validateSlotId(slotId);
		PaymentMethodUtil.payWithGems(player, configuration.getGemsCostToSkip());
		skip(slotId);
	}

	public boolean updateQuestsProgress(QuestProgressHandlerFactory questProgressHandlerFactory, Battle battle, Player player,
			QuestSlotsInspector questSlotsInspector) {
		boolean anyQuestWasUpdated = dailyQuest.handleBattleEnded(battle, player, questProgressHandlerFactory, timeProvider.getTimeNowAsSeconds());
		for (Map.Entry<String, QuestSlot> questSlotWithDifficulty : getSlots().entrySet()) {
			boolean updated = tryToUpdateQuestProgress(questProgressHandlerFactory, battle, player, questSlotsInspector,
					questSlotWithDifficulty.getKey(), questSlotWithDifficulty.getValue());
			if (updated) {
				anyQuestWasUpdated = true;
			}
		}
		return anyQuestWasUpdated;
	}

	public Map<String, QuestSlot> getSlots() {
		return ImmutableMap.copyOf(slots);
	}

	public int getQuestSequenceBy(String slotId) {
		return getSlot(slotId).getSequence();
	}

	public long getSkipTimeInSeconds() {
		return this.skipTimeInSeconds;
	}

	public void onQuestDefinitionChanged(CatalogEntriesCollection<QuestDefinition> questDefinitions,
			CatalogEntriesCollection<QuestDefinition> dailyQuestDefinitions) {
		getSlots().values().forEach(slot -> slot.getActiveQuest().onQuestDefinitionChanged(questDefinitions));
		if (dailyQuest != null)
			dailyQuest.onQuestDefinitionChanged(dailyQuestDefinitions);
	}

	public void cheatUpdateRemainingSkipTime(long skipTime) {
		this.skipTimeInSeconds = skipTime;
	}

	public QuestBoardConfiguration getQuestBoardConfiguration() {
		return configuration;
	}

	public Quest getDailyQuest() {
		return dailyQuest;
	}

	public boolean dailyQuestIsNotInitialized() {
		return dailyQuest == null;
	}

	public boolean isNotClaimed(Quest dailyQuest) {
		return !dailyQuest.isClaimed();
	}

	public void initializeDailyQuest(Quest dailyQuest) {
		if (!dailyQuestIsNotInitialized()) {
			throw new DailyQuestIsNotInitializedException();
		}
		this.dailyQuest = dailyQuest;
	}

	private void putQuest(String slotId, Quest newQuest, QuestSlot slot) {
		slot.addQuest(newQuest);
		slots.put(slotId, slot);
	}

	private boolean dailyQuestRefreshTimeIsNotReached() {
		return dailyQuest.refreshTimeIsNotExpired(timeProvider.getTimeNowAsSeconds());
	}

	private void skip(String slotId) {
		QuestSlot slot = slots.get(slotId);
		if (isInitialized(slot)) {
			throw new QuestBoardAlreadyInitializedException("Quest initialized. Unable to skip. Should be started");
		}
		slot.skip();
		updateQuestBoardSkipTime();
	}

	private boolean isInitialized(QuestSlot slot) {
		return slot.hasInitialQuest();
	}

	private void validateSlotId(String slotId) {
		if (slotIsUnknown(slotId)) {
			throw new QuestBoardSlotNotFoundException("Unexpected slot id {}");
		}
	}

	private boolean skipTimeIsNotReached() {
		return timeProvider.getTimeNowAsSeconds() <= skipTimeInSeconds;
	}

	private boolean isAlreadyStarted(QuestSlot slot) {
		return slot.hasStartedQuest();
	}

	private boolean isAlreadyClaimed(QuestSlot slot) {
		return slot.hasClaimedQuest();
	}

	private boolean isNotCompleted(QuestSlot slot) {
		return !slot.hasCompletedQuest();
	}

	private boolean slotIsUnknown(String slotId) {
		return !slots.containsKey(slotId);
	}

	private boolean refreshTimeIsNotReached(QuestSlot slot) {
		return slot.refreshTimeIsNotReached(timeProvider.getTimeNowAsSeconds());
	}

	private void updateQuestBoardSkipTime() {
		this.skipTimeInSeconds = timeProvider.getTimeNowAsSeconds() + configuration.getSkipTimeInSeconds();
	}

	private QuestDifficultyType questDifficultyTypeOf(String questSlotDifficulty) {
		return QuestDifficultyType.getTypeFromId(questSlotDifficulty);
	}

	private void initializeSlotsByDifficulty() {
		List<QuestDifficultyType> difficultyTypes = asList(QuestDifficultyType.values());
		for (QuestDifficultyType difficultyType : difficultyTypes) {
			this.slots.put(difficultyType.toString(), new QuestSlot());
		}
	}

	private boolean tryToUpdateQuestProgress(QuestProgressHandlerFactory questProgressHandlerFactory, Battle battle, Player player,
			QuestSlotsInspector questSlotsInspector, String questSlotDifficulty, QuestSlot questSlot) {
		boolean isSlotAvailable = questSlotsInspector.isAvailable(player.getProgress().getLevel(), questDifficultyTypeOf(questSlotDifficulty));
		if (isSlotAvailable) {
			Quest activeQuest = questSlot.getActiveQuest();
			activeQuest.handleBattleEnded(battle, player, questProgressHandlerFactory, timeProvider.getTimeNowAsSeconds());
			return true;
		}
		return false;
	}

}
