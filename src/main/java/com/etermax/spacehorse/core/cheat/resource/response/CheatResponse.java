package com.etermax.spacehorse.core.cheat.resource.response;

import com.etermax.spacehorse.core.player.resource.response.player.deck.CardResponse;
import com.etermax.spacehorse.core.player.resource.response.player.inventory.chest.ChestResponse;
import com.etermax.spacehorse.core.player.resource.response.player.progress.RewardsReceivedTodayResponse;
import com.etermax.spacehorse.core.specialoffer.resource.response.SpecialOfferResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CheatResponse {

	@JsonProperty("chest")
	private ChestResponse chest;

	@JsonProperty("card")
	private CardResponse card;

	@JsonProperty("number")
	private int number;

	@JsonProperty("str")
	private String str;

	@JsonProperty("rewardsReceivedToday")
	private RewardsReceivedTodayResponse rewardsReceivedToday;

	@JsonProperty("specialOffer")
	private SpecialOfferResponse specialOffer;

	public CheatResponse(ChestResponse chest) {
		this.chest = chest;
	}

	public CheatResponse(CardResponse card) {
		this.card = card;
	}

	public CheatResponse(RewardsReceivedTodayResponse rewardsReceivedToday) {
		this.rewardsReceivedToday = rewardsReceivedToday;
	}

	public CheatResponse(SpecialOfferResponse specialOffer) {
		this.specialOffer = specialOffer;
	}

	public CheatResponse(int number) {
		this.number = number;
	}

	public CheatResponse(String str) { this.str = str; }

	public CheatResponse() {
	}

	public ChestResponse getChest() {
		return chest;
	}

	public int getNumber() {
		return number;
	}

	public CardResponse getCard() {
		return card;
	}

	public String getStr() {
		return str;
	}

	public RewardsReceivedTodayResponse getRewardsReceivedToday() {
		return rewardsReceivedToday;
	}

	public SpecialOfferResponse getSpecialOffer() {
		return specialOffer;
	}
}
