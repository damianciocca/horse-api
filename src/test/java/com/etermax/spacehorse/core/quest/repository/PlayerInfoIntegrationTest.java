package com.etermax.spacehorse.core.quest.repository;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.PlayerInfo;
import com.etermax.spacehorse.core.player.repository.dynamo.DynamoPlayer;
import com.etermax.spacehorse.core.player.repository.dynamo.PlayerDynamoRepository;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class PlayerInfoIntegrationTest {

	private List<String> playerIds;
	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();
	private PlayerDynamoRepository repository;
	private PlayerAction playerAction;

	@Before
	public void setUp() {
		RULE.createSimpleTable(DynamoPlayer.class);
		DynamoDao dao = new DynamoDao(RULE.getAmazonDynamoDB());
		FixedServerTimeProvider timeProvider = new FixedServerTimeProvider();
		CatalogRepository catalogRepository = mock(CatalogRepository.class);
		repository = new PlayerDynamoRepository(dao, catalogRepository, timeProvider);
		playerAction = new PlayerAction(repository, catalogRepository);
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	@Test
	public void findUsersInfoInBatch(){
		givenAListOfTenPlayers();

		List<PlayerInfo> playersInfo = playerAction.findPlayersInfo(playerIds);

		assertThat(playersInfo).hasSize(10);
	}

	private void givenAListOfTenPlayers() {
		playerIds = new ArrayList<>();
		for (int i = 0; i < 10 ; i++) {
			String playerId = String.valueOf(i);
			Player player = new PlayerScenarioBuilder(playerId).build();
			repository.add(player);
			playerIds.add(playerId);
		}
	}
}
