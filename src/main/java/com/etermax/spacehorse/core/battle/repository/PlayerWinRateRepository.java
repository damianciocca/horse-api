package com.etermax.spacehorse.core.battle.repository;

import java.util.Optional;

import com.etermax.spacehorse.core.battle.model.BattleResult;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.model.PlayerWinRateConfiguration;

public interface PlayerWinRateRepository {

	void updateMmr(String userId, int deltaMmr, PlayerWinRateConfiguration configuration);

	boolean updateMmrOnlyIfOldValueIs(String userId, int oldMmr, int deltaMmr, PlayerWinRateConfiguration configuration);

	boolean updateMmrAndScoreOnlyIfOldValueIs(String userId, int oldMmr, int deltaMmr, BattleResult result, PlayerWinRateConfiguration configuration);

	void add(PlayerWinRate playerWinRate);

	void update(PlayerWinRate playerWinRate);

	Optional<PlayerWinRate> find(String userId);

	PlayerWinRate findOrCrateDefault(String userId);

	void updateScore(String userId, BattleResult result);

}
