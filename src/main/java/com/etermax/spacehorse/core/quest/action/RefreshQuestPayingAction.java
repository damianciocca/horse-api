package com.etermax.spacehorse.core.quest.action;

import static com.google.common.base.Preconditions.checkArgument;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.PaymentMethodUtil;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestBoardUpdater;
import com.etermax.spacehorse.core.quest.model.QuestFactory;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.model.RefreshPayingQuestCostCalculator;
import com.etermax.spacehorse.core.quest.model.QuestWithPrice;

public class RefreshQuestPayingAction {

	private final QuestBoardRepository questBoardRepository;
	private final PlayerRepository playerRepository;
	private final QuestBoardUpdater questBoardUpdater;
	private final RefreshPayingQuestCostCalculator refreshPayingQuestCostCalculator;

	public RefreshQuestPayingAction(QuestBoardRepository questBoardRepository, QuestFactory questFactory, PlayerRepository playerRepository,
			RefreshPayingQuestCostCalculator refreshPayingQuestCostCalculator) {
		this.questBoardRepository = questBoardRepository;
		this.playerRepository = playerRepository;
		this.refreshPayingQuestCostCalculator = refreshPayingQuestCostCalculator;
		this.questBoardUpdater = new QuestBoardUpdater(questFactory, questBoardRepository);
	}

	public QuestWithPrice refresh(Player player, Catalog catalog, String slotId) {
		validateInputParameters(player, catalog);
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		int gemsCost = calculateGemsCost(slotId, questBoard);
		PaymentMethodUtil.payWithGems(player, gemsCost);
		Quest quest = questBoardUpdater.addOrUpdateNextQuestPaying(player, catalog, slotId, questBoard);
		playerRepository.update(player);
		return new QuestWithPrice(quest, gemsCost);
	}

	private int calculateGemsCost(String slotId, QuestBoard questBoard) {
		QuestSlot questSlot = questBoard.getSlot(slotId);
		return refreshPayingQuestCostCalculator.calculate(questSlot, questBoard.getQuestBoardConfiguration());
	}

	private void validateInputParameters(Player player, Catalog catalog) {
		checkArgument(player != null, "The player should not be null");
		checkArgument(catalog != null, "The catalog should not be null");
	}
}
