package com.etermax.spacehorse.core.quest.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.QuestBoard;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;

public class SetRemainingSkipTime extends Cheat {

	private final QuestBoardRepository questBoardRepository;

	public SetRemainingSkipTime(QuestBoardRepository questBoardRepository) {
		this.questBoardRepository = questBoardRepository;
	}

	@Override
	public String getCheatId() {
		return "setRemainingSkipTime";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		int skipTime = getParameterInt(parameters, 1);
		if (skipTime >= 0) {
			QuestBoard questBoard = questBoardRepository.findOrDefaultBy(player);
			questBoard.cheatUpdateRemainingSkipTime(skipTime);
			questBoardRepository.addOrUpdate(player.getUserId(), questBoard);
			return new CheatResponse(skipTime);
		}
		return null;
	}

}
