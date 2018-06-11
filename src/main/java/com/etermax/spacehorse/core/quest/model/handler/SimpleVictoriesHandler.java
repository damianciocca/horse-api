package com.etermax.spacehorse.core.quest.model.handler;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattleUtils;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;

public class SimpleVictoriesHandler implements QuestProgressHandler {
	@Override
	public boolean handleBattleEnded(Quest quest, Battle battle, Player player) {
		if (BattleUtils.playerIsWinner(player, battle)) {
			quest.incrementProgress();
			return true;
		}
		return false;
	}
}
