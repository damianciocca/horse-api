package com.etermax.spacehorse.core.quest.model.handler;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattleUtils;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;

public class VictoriesWithMainShipNotDamagedHandler implements QuestProgressHandler {
	@Override
	public boolean handleBattleEnded(Quest quest, Battle battle, Player player) {
		if (BattleUtils.playerIsWinner(player, battle) && !BattleUtils.getTeamStats(player, battle).map(x -> x.getMotherShipDamaged()).orElse(true)) {
			quest.incrementProgress();
			return true;
		}
		return false;
	}
}
