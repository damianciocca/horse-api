package com.etermax.spacehorse.core.quest.model.handler;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattleUtils;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;

public class ShipsDestroyedHandler implements QuestProgressHandler {
	@Override
	public boolean handleBattleEnded(Quest quest, Battle battle, Player player) {

		int opponentShipsDestroyed = BattleUtils.getOpponentShipsDestroyed(player, battle);

		if (opponentShipsDestroyed > 0) {
			for (int i = 0; i < opponentShipsDestroyed; i++)
				quest.incrementProgress();
			return true;
		}

		return false;
	}
}
