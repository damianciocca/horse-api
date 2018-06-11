package com.etermax.spacehorse.core.quest.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;

public class SetRemainingAmountForDailyQuest extends Cheat {

	private final QuestBoardRepository questBoardRepository;

	public SetRemainingAmountForDailyQuest(QuestBoardRepository questBoardRepository) {
		this.questBoardRepository = questBoardRepository;
	}

	@Override
	public String getCheatId() {
		return "setRemainingAmountForDailyQuest";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		long remainingAmount = getParameterLong(parameters, 0);
		QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
		Quest dailyQuest = questBoard.getDailyQuest();
		if (dailyQuest.canSetRemainingAmount(remainingAmount)) {
			dailyQuest.cheatSetRemainingAmount(remainingAmount);
			questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
			return new CheatResponse((int) dailyQuest.getRemainingAmount());
		}
		return null;
	}

}
