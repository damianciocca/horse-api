package com.etermax.spacehorse.core.player.resource.response.player.inventory;

import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.cardparts.CardPartsResponse;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestsResponse;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.currency.CurrencyResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryResponse {

	@JsonProperty("gold")
	private CurrencyResponse gold;

	@JsonProperty("gems")
	private CurrencyResponse gems;

	@JsonProperty("cardParts")
	private CardPartsResponse cardParts;

	@JsonProperty("chests")
	private ChestsResponse chests;

	public CurrencyResponse getGold() {
		return gold;
	}

	public CardPartsResponse getCardParts() {
		return cardParts;
	}

	public ChestsResponse getChests() {
		return chests;
	}

	public InventoryResponse(Inventory inventory) {
		this.gold = new CurrencyResponse(inventory.getGold());
		this.gems = new CurrencyResponse(inventory.getGems());
		this.cardParts = new CardPartsResponse(inventory.getCardParts());
		this.chests = new ChestsResponse(inventory.getChests());
	}

	public InventoryResponse(CurrencyResponse gold, CardPartsResponse cardParts, ChestsResponse chests) {
		this.gold = gold;
		this.cardParts = cardParts;
		this.chests = chests;
	}

	public InventoryResponse() {
	}

}
