package com.etermax.spacehorse.core.quest.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;

public class SetRemainingRefreshTime extends Cheat {

	private final QuestBoardRepository questBoardRepository;

	public SetRemainingRefreshTime(QuestBoardRepository questBoardRepository) {
		this.questBoardRepository = questBoardRepository;
	}

	@Override
	public String getCheatId() {
		return "setRemainingRefreshTime";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		int serverTime = getParameterInt(parameters, 1);
		if (serverTime >= 0) {
			QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
			questBoard.getSlot(getParameterString(parameters, 0)).getActiveQuest().setRefreshTimeInSeconds(serverTime);
			questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
			return new CheatResponse(serverTime);
		}
		return null;
	}

}
