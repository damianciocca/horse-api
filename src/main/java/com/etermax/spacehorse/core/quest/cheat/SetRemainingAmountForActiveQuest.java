package com.etermax.spacehorse.core.quest.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.quest.model.QuestSlot;

public class SetRemainingAmountForActiveQuest extends Cheat {

	private final QuestBoardRepository questBoardRepository;

	public SetRemainingAmountForActiveQuest(QuestBoardRepository questBoardRepository) {
		this.questBoardRepository = questBoardRepository;
	}

	@Override
	public String getCheatId() {
		return "setRemainingAmountForActiveQuest";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		String slotId = getParameterString(parameters, 0);
		long remainingAmount = getParameterLong(parameters, 1);
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		QuestSlot slot = questBoard.getSlot(slotId);
		if (slot.getActiveQuest().canSetRemainingAmount(remainingAmount)) {
			slot.getActiveQuest().cheatSetRemainingAmount(remainingAmount);
			questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
			return new CheatResponse((int) slot.getActiveQuest().getRemainingAmount());
		}
		return null;
	}

}
