package com.etermax.spacehorse.core.ads.videorewards.action;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.ads.videorewards.actions.ClaimBoostVideoRewardAction;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoReward;
import com.etermax.spacehorse.core.ads.videorewards.model.quota.QuotaVideoRewardRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.mock.MockCatalog;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class ClaimBoostVideoRewardActionTest {

	public static final String PLAYER_ID = "PLAYER_ID";
	public static final String VIDEO_REWARD_ID_FOR_MAP_NUMBER_1 = "battle_finish_place_1";
	public static final String VIDEO_REWARD_ID_FOR_MAP_NUMBER_2 = "battle_finish_place_2";
	public static final String VIDEO_REWARD_ID_FOR_MAP_NUMBER_3 = "battle_finish_place_3";
	public static final String BATTLE_FINISH_PLACE = "battle_finish_place";
	private PlayerRepository playerRepository;
	private Catalog catalog;
	private Player player1;
	private ClaimBoostVideoRewardAction claimBoostVideoRewardAction;
	private QuotaVideoRewardRepository quotaVideoRewardRepository;

	@Before
	public void before() {
		catalog = MockCatalog.buildCatalog();

		player1 = new PlayerScenarioBuilder(PLAYER_ID).withActiveTutorial().build();
		playerRepository = mock(PlayerRepository.class);
		when(playerRepository.find(PLAYER_ID)).thenReturn(player1);

		QuotaVideoReward quotaVideoReward = mock(QuotaVideoReward.class);
		quotaVideoRewardRepository = mock(QuotaVideoRewardRepository.class);
		when(quotaVideoRewardRepository.findOrDefaultBy(player1, BATTLE_FINISH_PLACE)).thenReturn(quotaVideoReward);
	}

	@Test
	public void whenClaimVideoRewardWithPlayerNumberMapOneThenCoinsExpectedIs118() {
		givenAClaimVideoRewardAction();

		whenClaimVideoReward(VIDEO_REWARD_ID_FOR_MAP_NUMBER_1);

		thenExpectedPlayerCoinsIs(118);
	}

	@Test
	public void whenClaimVideoRewardWithPlayerNumberMapTwoThenCoinsExpectedIs122() {
		givenAClaimVideoRewardAction();

		whenClaimVideoReward(VIDEO_REWARD_ID_FOR_MAP_NUMBER_2);

		thenExpectedPlayerCoinsIs(122);
	}

	@Test
	public void whenClaimVideoRewardWithPlayerNumberMapThreeThenCoinsExpectedIs128() {
		givenAClaimVideoRewardAction();

		whenClaimVideoReward(VIDEO_REWARD_ID_FOR_MAP_NUMBER_3);

		thenExpectedPlayerCoinsIs(128);
	}

	private void thenExpectedPlayerCoinsIs(int expectedCoins) {
		assertThat(player1.getInventory().getGold().getAmount()).isEqualTo(expectedCoins);
	}

	private void whenClaimVideoReward(String videoRewardId) {
		claimBoostVideoRewardAction.claim(player1, videoRewardId, catalog);
	}

	private void givenAClaimVideoRewardAction() {
		claimBoostVideoRewardAction = new ClaimBoostVideoRewardAction(quotaVideoRewardRepository, playerRepository);
	}

}
