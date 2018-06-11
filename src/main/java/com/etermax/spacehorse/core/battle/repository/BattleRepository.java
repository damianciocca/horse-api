package com.etermax.spacehorse.core.battle.repository;

import java.util.Optional;

import com.etermax.spacehorse.core.battle.model.Battle;

public interface BattleRepository {

    Battle add(Battle battle);
    Optional<Battle> find(String id);
    void update(Battle battle);

}
