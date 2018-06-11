package com.etermax.spacehorse.core.player.model.inventory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.Before;
import org.junit.Test;

import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.shop.exception.NotEnoughGemsException;
import com.etermax.spacehorse.core.shop.exception.NotEnoughGoldException;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class PaymentMethodUtilTest {

	private FixedServerTimeProvider serverTimeProvider;

	@Before
	public void setUp() throws Exception {
		serverTimeProvider = new FixedServerTimeProvider();
	}

	@Test
	public void whenPayGemsAndGoldThenTheGemsAndGoldShouldBeDecreased() throws Exception {
		// given
		Player player = new PlayerScenarioBuilder("100", serverTimeProvider).withGold(50).withGems(70).build();
		int gemsToPay = 30;
		int goldToPay = 50;
		// when
		PaymentMethodUtil.payWith(player, gemsToPay, goldToPay);
		// then
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(0);
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(40);
	}

	@Test
	public void whenPayOnlyGemsThenTheGemsShouldBeDecreasedCase1() throws Exception {
		// given
		Player player = new PlayerScenarioBuilder("100", serverTimeProvider).withGold(50).withGems(70).build();
		int gemsToPay = 30;
		int goldToPay = 0;
		// when
		PaymentMethodUtil.payWith(player, gemsToPay, goldToPay);
		// then
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(50);
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(40);
	}

	@Test
	public void whenPayOnlyGemsThenTheGemsShouldBeDecreasedCase2() throws Exception {
		// given
		Player player = new PlayerScenarioBuilder("100", serverTimeProvider).withGold(50).withGems(70).build();
		int gemsToPay = 30;
		// when
		PaymentMethodUtil.payWithGems(player, gemsToPay);
		// then
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(50);
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(40);
	}

	@Test
	public void whenPayOnlyGoldThenTheGoldShouldBeDecreasedCase1() throws Exception {
		// given
		Player player = new PlayerScenarioBuilder("100", serverTimeProvider).withGold(50).withGems(70).build();
		int goldToPay = 30;
		int gemsToPay = 0;
		// when
		PaymentMethodUtil.payWith(player, gemsToPay, goldToPay);
		// then
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(20);
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(70);
	}

	@Test
	public void whenPayOnlyGoldThenTheGoldShouldBeDecreasedCase2() throws Exception {
		// given
		Player player = new PlayerScenarioBuilder("100", serverTimeProvider).withGold(50).withGems(70).build();
		int goldToPay = 30;
		// when
		PaymentMethodUtil.payWithGold(player, goldToPay);
		// then
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(20);
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(70);
	}

	@Test
	public void whenPayZeroGoldAndGemsThenTheGoldAndGemsShouldNotBeDecreased() throws Exception {
		// given
		Player player = new PlayerScenarioBuilder("100", serverTimeProvider).withGold(50).withGems(70).build();
		int goldToPay = 0;
		int gemsToPay = 0;
		// when
		PaymentMethodUtil.payWith(player, gemsToPay, goldToPay);
		// then
		assertThat(player.getInventory().getGold().getAmount()).isEqualTo(50);
		assertThat(player.getInventory().getGems().getAmount()).isEqualTo(70);
	}

	@Test
	public void whenTryToPayMoreGoldThatThePlayerHasThenAnExceptionShouldBeThrownCase1() throws Exception {
		// given
		Player player = new PlayerScenarioBuilder("100", serverTimeProvider).withGold(50).withGems(70).build();
		int goldToPay = 51;
		int gemsToPay = 0;
		// when - then
		assertThatThrownBy(() -> PaymentMethodUtil.payWith(player, gemsToPay, goldToPay)).isInstanceOf(NotEnoughGoldException.class);
	}

	@Test
	public void whenTryToPayMoreGoldThatThePlayerHasThenAnExceptionShouldBeThrownCase2() throws Exception {
		// given
		Player player = new PlayerScenarioBuilder("100", serverTimeProvider).withGold(50).withGems(70).build();
		int goldToPay = 51;
		// when - then
		assertThatThrownBy(() -> PaymentMethodUtil.payWithGold(player, goldToPay)).isInstanceOf(NotEnoughGoldException.class);
	}

	@Test
	public void whenTryToPayMoreGemsThatThePlayerHasThenAnExceptionShouldBeThrownCase1() throws Exception {
		// given
		Player player = new PlayerScenarioBuilder("100", serverTimeProvider).withGold(50).withGems(70).build();
		int goldToPay = 50;
		int gemsToPay = 71;
		// when - then
		assertThatThrownBy(() -> PaymentMethodUtil.payWith(player, gemsToPay, goldToPay)).isInstanceOf(NotEnoughGemsException.class);
	}

	@Test
	public void whenTryToPayMoreGemsThatThePlayerHasThenAnExceptionShouldBeThrownCase2() throws Exception {
		// given
		Player player = new PlayerScenarioBuilder("100", serverTimeProvider).withGold(50).withGems(70).build();
		int gemsToPay = 71;
		// when - then
		assertThatThrownBy(() -> PaymentMethodUtil.payWithGems(player, gemsToPay)).isInstanceOf(NotEnoughGemsException.class);
	}
}
