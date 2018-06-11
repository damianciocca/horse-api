package com.etermax.spacehorse.core.liga.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.liga.domain.PlayerSeason;
import com.etermax.spacehorse.core.liga.domain.PlayerLeague;
import com.etermax.spacehorse.core.liga.repository.DynamoPlayerLeague;
import com.etermax.spacehorse.core.liga.repository.DynamoPlayerLeagueRepository;
import com.etermax.spacehorse.core.liga.domain.PlayerLeagueRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class DynamoSeasonsRepositoryTest {

	private static final String USER_ID = "10";
	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();
	private PlayerLeagueRepository playerLeagueRepository;
	private Optional<PlayerLeague> playerSeasonsFound;
	private PlayerSeason current;
	private PlayerSeason previous;

	@Before
	public void setUp() {
		RULE.createSimpleTable(DynamoPlayerLeague.class);
		DynamoDao dao = new DynamoDao(RULE.getAmazonDynamoDB());
		playerLeagueRepository = new DynamoPlayerLeagueRepository(dao);
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	@Test
	public void whenFindAPlayerSeasonsFirstTimeIsCreatedWithEmptyCurrentAndPreviousSeasons(){
		whenFindAPlayerSeasons();

		assertThat(playerSeasonsFound).isEmpty();
	}

	@Test
	public void whenFindAExistentPlayerSeasonsWithCurrentSeasonThisIsReturned(){
		givenAStoredPlayerSeasonsWithCurrentSeason();

		whenFindAPlayerSeasons();

		assertThat(playerSeasonsFound).isNotEmpty();
		assertThat(playerSeasonsFound.get().getCurrent().get()).isEqualToComparingFieldByField(current);
		assertThat(playerSeasonsFound.get().getPrevious()).isEmpty();
	}

	@Test
	public void whenFindAExistentPlayerSeasonsWithCurrentAndPreviousSeasonThisIsReturned(){
		givenAStoredPlayerSeasonsWithCurrentAndPreviousSeasons();

		whenFindAPlayerSeasons();

		assertThat(playerSeasonsFound).isNotEmpty();
		assertThat(playerSeasonsFound.get().getCurrent().get()).isEqualToComparingFieldByField(current);
		assertThat(playerSeasonsFound.get().getPrevious().get()).isEqualToComparingFieldByField(previous);
	}

	private void givenAStoredPlayerSeasonsWithCurrentSeason() {
		DateTime dateTime = ServerTime.roundToStartOfDay("2018-04-13");
		current = new PlayerSeason( dateTime , 3000);
		PlayerLeague playerLeague = new PlayerLeague(current);
		playerLeagueRepository.put(USER_ID, playerLeague);
	}

	private void whenFindAPlayerSeasons() {
		playerSeasonsFound = playerLeagueRepository.findBy(USER_ID);
	}

	private void givenAStoredPlayerSeasonsWithCurrentAndPreviousSeasons() {
		DateTime dateTimeCurrentSeason = ServerTime.roundToStartOfDay("2018-04-13");
		current = new PlayerSeason( dateTimeCurrentSeason , 3000);
		DateTime dateTimePreviousSeason = ServerTime.roundToStartOfDay("2018-04-13");
		previous = new PlayerSeason(dateTimePreviousSeason , 3000);
		PlayerLeague playerLeague = new PlayerLeague(current, previous);
		playerLeagueRepository.put(USER_ID, playerLeague);
	}
}
