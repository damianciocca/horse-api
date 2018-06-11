package com.etermax.spacehorse.core.ads.videorewards.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.etermax.spacehorse.core.ads.videorewards.model.SpeedupVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.repository.quota.DynamoQuotaVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.repository.quota.DynamoQuotaVideoRewardRepository;
import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.etermax.spacehorse.mock.QuotaVideoRewardScenarioBuilder;
import com.etermax.spacehorse.mock.VideoRewardScenarioBuilder;

public class DynamoQuotaVideoRewardRepositoryTest {

	private static final String USER_ID = "10";
	private static final String OTHER_USER_ID = "30";
	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();
	private DynamoQuotaVideoRewardRepository repository;
	private FixedServerTimeProvider timeProvider;
	private Player player;
	private Player otherPlayer;

	@Before
	public void setUp() {
		RULE.createSimpleTable(DynamoQuotaVideoReward.class);
		DynamoDao dao = new DynamoDao(RULE.getAmazonDynamoDB());
		timeProvider = new FixedServerTimeProvider();
		player = new PlayerScenarioBuilder(USER_ID).build();
		otherPlayer = new PlayerScenarioBuilder(OTHER_USER_ID).build();
		repository = new DynamoQuotaVideoRewardRepository(dao, timeProvider);
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	@Test
	public void givenAnEmtyRepositoryWhenFindAQuotaThenANewQuotaShouldBeCreatedAsDefault() {
		// when
		String placeName = "placeName";
		QuotaVideoReward quotaVideoReward = repository.findOrDefaultBy(player, placeName);
		// then
		assertThatQuotaVideoRewardWasFound(quotaVideoReward, placeName, 0, player);
	}

	@Test
	public void givenARepositoryWithOneQuotaByPlayerWhenFindAQuotaThenAQuotaShouldBeFound() {
		// given
		String placeName = "placeNameTest";
		repository.addOrUpdate(aQuotaVideoReward(placeName, player));
		// when
		QuotaVideoReward quotaVideoReward = repository.findOrDefaultBy(player, placeName);
		// then
		assertThatQuotaVideoRewardWasFound(quotaVideoReward, placeName, 0, player);
	}

	@Test
	public void givenAQuotaInRepositoryWithACounterInThreeWhenFindAQuotaThenAQuotaShouldBeFound() {
		// given
		String placeName = "placeNameTest";
		givenAQuotaInRepositoryWithACounterInThree(placeName, player.getUserId());
		// when
		QuotaVideoReward quotaVideoReward = repository.findOrDefaultBy(player, placeName);
		// then
		assertThatQuotaVideoRewardWasFound(quotaVideoReward, placeName, 3, player);
	}

	@Test
	public void givenManyQuotasForTheSamePlayerInRepositoryWhenFindOneQuotaThenAQuotaShouldBeFoundCase1() {
		// given
		String placeName = "placeNameTest";
		repository.addOrUpdate(aQuotaVideoReward("placeNameTest1", player));
		givenAQuotaInRepositoryWithACounterInThree(placeName, player.getUserId());
		repository.addOrUpdate(aQuotaVideoReward("placeNameTest3", player));
		// when
		QuotaVideoReward quotaVideoReward = repository.findOrDefaultBy(player, placeName);
		// then
		assertThatQuotaVideoRewardWasFound(quotaVideoReward, placeName, 3, player);
	}

	@Test
	public void givenManyQuotasForDifferentPlayersInRepositoryWhenFindOneQuotaThenAQuotaShouldBeFoundCase2() {
		// given
		String placeName = "placeNameTest";
		givenAQuotaInRepositoryWithACounterInThree(placeName, player.getUserId());
		repository.addOrUpdate(aQuotaVideoReward("placeNameTest1", player));
		repository.addOrUpdate(aQuotaVideoReward("placeNameTest3", player));
		repository.addOrUpdate(aQuotaVideoReward(placeName, player));
		// when
		QuotaVideoReward quotaVideoReward = repository.findOrDefaultBy(otherPlayer, placeName);
		// then
		assertThatQuotaVideoRewardWasFound(quotaVideoReward, placeName, 0, otherPlayer);
	}

	@Test
	public void givenManyQuotasForDifferentPlayersInRepositoryWhenFindOneQuotaThenAQuotaShouldBeFoundCase3() {
		// given
		String placeName = "placeNameTest";
		givenAQuotaInRepositoryWithACounterInThree(placeName, player.getUserId());
		repository.addOrUpdate(aQuotaVideoReward("placeNameTest1", player));
		repository.addOrUpdate(aQuotaVideoReward("placeNameTest3", player));
		repository.addOrUpdate(aQuotaVideoReward(placeName, otherPlayer));
		// when
		QuotaVideoReward quotaVideoReward = repository.findOrDefaultBy(player, placeName);
		// then
		assertThatQuotaVideoRewardWasFound(quotaVideoReward, placeName, 3, player);
	}

	private QuotaVideoReward aQuotaVideoReward(String placeName, Player player) {
		return new QuotaVideoRewardScenarioBuilder(timeProvider, placeName, player.getUserId()).build();
	}

	private void givenAQuotaInRepositoryWithACounterInThree(String placeName, String userId) {
		SpeedupVideoReward videoReward = new VideoRewardScenarioBuilder(placeName).build();
		QuotaVideoReward quotaVideoReward = new QuotaVideoRewardScenarioBuilder(timeProvider, placeName, userId)//
				.withConsume(videoReward)//
				.withConsume(videoReward)//
				.withConsume(videoReward)//
				.build();
		repository.addOrUpdate(quotaVideoReward);
	}

	private void assertThatQuotaVideoRewardWasFound(QuotaVideoReward quotaVideoReward, String expectedPlaceName, int expectedCounter, Player player) {
		assertThat(quotaVideoReward).isNotNull();
		assertThat(quotaVideoReward.getPlaceName()).isEqualTo(expectedPlaceName);
		assertThat(quotaVideoReward.getUserId()).isEqualTo(player.getUserId());
		assertThat(quotaVideoReward.getCounter()).isEqualTo(expectedCounter);
		assertThat(quotaVideoReward.getCreationTimeInSeconds()).isEqualTo(ServerTime.fromDate(timeProvider.getDateTime()));
	}

}
