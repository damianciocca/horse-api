package com.etermax.spacehorse.core.player.resource.response.player.inventory.currency;

import com.etermax.spacehorse.core.player.model.inventory.currency.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyResponse {

	@JsonProperty("amount")
	private Integer amount;

	public Integer getAmount() {
		return amount;
	}

	public CurrencyResponse(Currency currency) {
		this.amount = currency.getAmount();
	}

	public CurrencyResponse(Integer amount) {
		this.amount = amount;
	}

	public CurrencyResponse() {
	}

}
