package com.etermax.spacehorse.core.quest.model;

import com.etermax.spacehorse.core.player.model.Player;

public interface QuestBoardRepository {

	QuestBoard findOrDefaultBy(Player userId);

	void addOrUpdate(String userId, QuestBoard questBoard);
}
