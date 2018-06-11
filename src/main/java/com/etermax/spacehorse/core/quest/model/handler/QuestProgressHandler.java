package com.etermax.spacehorse.core.quest.model.handler;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.quest.model.Quest;

public interface QuestProgressHandler {
	boolean handleBattleEnded(Quest quest, Battle battle, Player player);
}
