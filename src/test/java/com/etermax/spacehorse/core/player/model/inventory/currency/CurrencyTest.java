package com.etermax.spacehorse.core.player.model.inventory.currency;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Test;

import com.etermax.spacehorse.core.player.model.inventory.currency.Currency;

public class CurrencyTest {

	Currency currency;

	@After
	public void after() {
		currency = null;
	}

	@Test
	public void aNewCurrencyIsEmpty() {
		givenANewCurrency();

		thenTheCurrencyIsZero();
	}

	@Test
	public void amountShouldIncreaseWhenAdded() {
		givenANewCurrency();

		whenAdding(5);

		thenTheCurrencyIs(5);
	}

	@Test
	public void amountShouldDecreaseWhenRemoved() {
		givenANewCurrency();

		whenAdding(5);
		whenRemoving(3);

		thenTheCurrencyIs(2);
	}

	private void whenRemoving(int value) {
		currency.remove(value);
	}

	@Test
	public void amountShouldNeverBecomeNegative() {
		givenANewCurrency();

		whenAdding(5);
		whenRemoving(15);

		thenTheCurrencyIsZero();
	}

	@Test
	public void amountShouldNeverOverflow() {
		givenANewCurrency();

		whenAdding(5);
		whenAdding(Integer.MAX_VALUE);

		thenTheCurrencyIsMax();
	}

	@Test
	public void amountShouldNeverUnderflow() {
		givenANewCurrency();

		whenAdding(5);
		whenRemoving(Integer.MAX_VALUE);

		thenTheCurrencyIsZero();
	}

	private void whenAdding(int toAdd) {
		currency.add(toAdd);
	}

	private void thenTheCurrencyIs(int value) {
		assertThat(currency.getAmount(), equalTo(value));
	}

	private void thenTheCurrencyIsMax() {
		thenTheCurrencyIs(currency.getMax());
	}

	private void thenTheCurrencyIsZero() {
		thenTheCurrencyIs(0);
	}

	private void givenANewCurrency() {
		currency = new Currency();
	}

}
