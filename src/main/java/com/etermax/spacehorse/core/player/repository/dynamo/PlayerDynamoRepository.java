package com.etermax.spacehorse.core.player.repository.dynamo;

import java.util.ArrayList;
import java.util.List;

import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.google.common.collect.Lists;

public class PlayerDynamoRepository implements PlayerRepository {

	public static final String PLAYER_TABLE_NAME = "player";
	private final DynamoDao dynamoDao;
	private final CatalogRepository catalogRepository;
	private final ServerTimeProvider serverTimeProvider;

	public PlayerDynamoRepository(DynamoDao dao, CatalogRepository catalogRepository, ServerTimeProvider serverTimeProvider) {
		this.dynamoDao = dao;
		this.catalogRepository = catalogRepository;
		this.serverTimeProvider = serverTimeProvider;
	}

	public void update(Player player) {
		DynamoPlayer dynamoPlayer = DynamoPlayer.createFromPlayer(player);
		dynamoDao.update(dynamoPlayer);
	}

	public void add(Player player) {
		AbstractDynamoDAO dynamoPlayer = DynamoPlayer.createFromPlayer(player);
		dynamoDao.add(dynamoPlayer);
	}

	public Player find(String id) {
		DynamoPlayer dynamoPlayerWithId = newPlayerWithId(id);
		DynamoPlayer dynamoPlayer = (DynamoPlayer) dynamoDao.find(dynamoPlayerWithId);
		if (dynamoPlayer != null) {
			Player player = DynamoPlayer.mapFromDynamoPlayerToPlayer(dynamoPlayer);
			player.checkAndFixIntegrity(catalogRepository.getActiveCatalogWithTag(player.getAbTag()), serverTimeProvider.getTimeNowAsSeconds());
			return player;
		}

		return null;
	}

	public List<Player> find(List<String> playerIds) {
		//Se limitan las busqueda en batch a 100 registros para evitar perdida de datos.
		List<List<String>> listsPartition = Lists.partition(playerIds, 100);
		List<Player> players = new ArrayList<>();

		listsPartition.forEach(playerIdList -> {
			List<DynamoPlayer> dynamoPlayers = dynamoDao.findInBatch(playerIds, PLAYER_TABLE_NAME, DynamoPlayer.class);
			dynamoPlayers.forEach(dynamoPlayer -> players.add(DynamoPlayer.mapFromDynamoPlayerToPlayer(dynamoPlayer)));
		});
		return players;
	}

	public void updateLastBattleId(String userId, String lastBattleId) {
		DynamoPlayer dynamoPlayerWithId = newPlayerWithId(userId);
		DynamoPlayer dynamoPlayer = (DynamoPlayer) dynamoDao.find(dynamoPlayerWithId);

		if (dynamoPlayer != null) {
			dynamoPlayer.setLastBattleId(lastBattleId);
			dynamoDao.update(dynamoPlayer);
		}
	}

	protected DynamoPlayer newPlayerWithId(String id) {
		DynamoPlayer dynamoPlayer = new DynamoPlayer(serverTimeProvider.getTimeNowAsSeconds());
		dynamoPlayer.setUserId(id);
		return dynamoPlayer;
	}

}