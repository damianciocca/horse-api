package com.etermax.spacehorse.core.player.repository;

import java.util.List;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.PlayerInfo;

public interface PlayerRepository {

    void update(Player player);

	void add(Player player);

	Player find(String id);

	List<Player> find(List<String> playerIds);

	void updateLastBattleId(String userId, String lastBattleId);

}
