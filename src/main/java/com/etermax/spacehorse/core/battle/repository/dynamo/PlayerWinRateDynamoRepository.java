package com.etermax.spacehorse.core.battle.repository.dynamo;

import java.util.Optional;

import com.etermax.spacehorse.core.battle.model.BattleResult;
import com.etermax.spacehorse.core.battle.model.PlayerWinRate;
import com.etermax.spacehorse.core.battle.model.PlayerWinRateConfiguration;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class PlayerWinRateDynamoRepository implements PlayerWinRateRepository {

	private DynamoDao dynamoDao;

	/**
	 * todo {Damian} REFACTORIZAR este repo:
	 * Armar Actions para interactuar con el repo... sino tenemos que estar pasandole la configuracion en cada metodo... El repo solo debe guardar y
	 * obtener el playerWinRate.... y la logica de negocio debe estar en el modelo y NO ACA!
	 */

	public PlayerWinRateDynamoRepository(DynamoDao dao) {
		this.dynamoDao = dao;
	}

	@Override
	public void updateMmr(String userId, int deltaMmr, PlayerWinRateConfiguration configuration) {
		PlayerWinRate playerWinRate = findOrCrateDefault(userId);
		addDeltaMmr(playerWinRate, deltaMmr, configuration);
		this.dynamoDao.update(DynamoPlayerWinRate.fromPlayerWinRate(playerWinRate));
	}

	@Override
	public boolean updateMmrOnlyIfOldValueIs(String userId, int oldMmr, int deltaMmr, PlayerWinRateConfiguration configuration) {
		PlayerWinRate playerWinRate = findOrCrateDefault(userId);
		if (tryToUpdateMmrOnlyIfOldValueIs(playerWinRate, oldMmr, deltaMmr, configuration)) {
			this.dynamoDao.update(DynamoPlayerWinRate.fromPlayerWinRate(playerWinRate));
			return true;
		}
		return false;
	}

	@Override
	public boolean updateMmrAndScoreOnlyIfOldValueIs(String userId, int oldMmr, int deltaMmr, BattleResult result,
			PlayerWinRateConfiguration configuration) {
		PlayerWinRate playerWinRate = findOrCrateDefault(userId);
		if (tryToUpdateMmrOnlyIfOldValueIs(playerWinRate, oldMmr, deltaMmr, configuration)) {
			updateScore(playerWinRate, result);
			this.dynamoDao.update(DynamoPlayerWinRate.fromPlayerWinRate(playerWinRate));
			return true;
		}
		return false;
	}

	@Override
	public void add(PlayerWinRate playerWinRate) {
		DynamoPlayerWinRate dynamoPlayerWinRate = DynamoPlayerWinRate.fromPlayerWinRate(playerWinRate);
		this.dynamoDao.add(dynamoPlayerWinRate);
	}

	@Override
	public void update(PlayerWinRate playerWinRate) {
		DynamoPlayerWinRate dynamoPlayerWinRate = DynamoPlayerWinRate.fromPlayerWinRate(playerWinRate);
		this.dynamoDao.add(dynamoPlayerWinRate);
	}

	@Override
	public Optional<PlayerWinRate> find(String userId) {
		DynamoPlayerWinRate dynamoPlayerWinRate = (DynamoPlayerWinRate) dynamoDao.find(new DynamoPlayerWinRate(userId));

		if (dynamoPlayerWinRate != null) {
			return Optional.of(DynamoPlayerWinRate.mapToPlayerWinRate(dynamoPlayerWinRate));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public PlayerWinRate findOrCrateDefault(String userId) {

		PlayerWinRate playerWinRate;

		Optional<PlayerWinRate> dynamoPlayerWinRate = find(userId);

		if (!dynamoPlayerWinRate.isPresent()) {
			playerWinRate = new PlayerWinRate(userId);
			add(playerWinRate);
		} else {
			playerWinRate = dynamoPlayerWinRate.get();
		}

		return playerWinRate;
	}

	@Override
	public void updateScore(String userId, BattleResult result) {
		PlayerWinRate playerWinRate = findOrCrateDefault(userId);
		updateScore(playerWinRate, result);
		this.dynamoDao.update(DynamoPlayerWinRate.fromPlayerWinRate(playerWinRate));
	}

	private boolean tryToUpdateMmrOnlyIfOldValueIs(PlayerWinRate playerWinRate, Integer oldMmr, Integer deltaMmr,
			PlayerWinRateConfiguration configuration) {
		if (playerWinRate.getMmr() == oldMmr) {
			addDeltaMmr(playerWinRate, deltaMmr, configuration);
			return true;
		}
		return false;
	}

	private void addDeltaMmr(PlayerWinRate playerWinRate, Integer deltaMmr, PlayerWinRateConfiguration configuration) {
		int cappedMmr = configuration.getCappedMmr();
		boolean cappedEnabled = configuration.isCappedEnabled();
		playerWinRate.updateMmr(playerWinRate.getMmr() + deltaMmr, cappedMmr, cappedEnabled);
	}

	private void updateScore(PlayerWinRate playerWinRate, BattleResult result) {
		switch (result) {
			case WIN:
				playerWinRate.setWin(playerWinRate.getWin() + 1);
				break;
			case LOSE:
				playerWinRate.setLose(playerWinRate.getLose() + 1);
				break;
			case TIE:
				playerWinRate.setTie(playerWinRate.getTie() + 1);
				break;
		}
	}
}
