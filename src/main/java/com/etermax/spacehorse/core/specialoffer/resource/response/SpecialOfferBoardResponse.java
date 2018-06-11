package com.etermax.spacehorse.core.specialoffer.resource.response;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.specialoffer.model.SpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialOfferBoardResponse {

	@JsonProperty("specialOffers")
	private List<SpecialOfferResponse> specialOffers;

	@JsonProperty("refreshServerTimeInSeconds")
	private long refreshServerTimeInSeconds;

	public SpecialOfferBoardResponse() {
		// just for jackson
	}

	public SpecialOfferBoardResponse(SpecialOfferBoard specialOfferBoard) {
		List<SpecialOffer> specialOffers = specialOfferBoard.getOrderedSpecialOffers();
		this.specialOffers = specialOffers.stream().map(toSpecialOfferResponse()).collect(Collectors.toList());
		this.refreshServerTimeInSeconds = specialOfferBoard.getNexRefreshTimeInSeconds();
	}

	private Function<SpecialOffer, SpecialOfferResponse> toSpecialOfferResponse() {
		return specialOffer -> new SpecialOfferResponse(specialOffer.getId(), specialOffer.getExpirationTimeInSeconds(),
				specialOffer.getAvailableAmountUntilExpiration());
	}

}
