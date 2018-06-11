package com.etermax.spacehorse.core.battle.repository.dynamo;

import java.util.Optional;

import com.etermax.spacehorse.core.battle.model.Battle;
import com.etermax.spacehorse.core.battle.model.BattleFactory;
import com.etermax.spacehorse.core.battle.repository.BattleRepository;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class BattleDynamoRepository implements BattleRepository {
	private final DynamoDao dynamoDao;
	private final BattleFactory battleFactory;

	public BattleDynamoRepository(DynamoDao dao, BattleFactory battleFactory) {
		this.dynamoDao = dao;
		this.battleFactory = battleFactory;
	}

	public Battle add(Battle battle) {
		DynamoBattle dynamoBattle = DynamoBattle.createFromBattle(battle);
		dynamoDao.add(dynamoBattle);
		return DynamoBattle.mapToBattle(dynamoBattle, battleFactory);
	}

	public Optional<Battle> find(String id) {
		DynamoBattle searchParam = newDynamoBattleWithId(id);
		DynamoBattle dynamoBattle = (DynamoBattle) dynamoDao.find(searchParam);

		if (dynamoBattle != null) {
			return Optional.of(DynamoBattle.mapToBattle(dynamoBattle, battleFactory));
		} else {
			return Optional.empty();
		}
	}

	public void update(Battle battle) {
		DynamoBattle dynamoBattle = DynamoBattle.createFromBattle(battle);
		dynamoDao.update(dynamoBattle);
	}

	private DynamoBattle newDynamoBattleWithId(String id) {
		DynamoBattle dynamoBattle = new DynamoBattle();
		dynamoBattle.setId(id);
		return dynamoBattle;
	}

}
