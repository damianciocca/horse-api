package com.etermax.spacehorse.core.specialoffer.exception;

import static java.lang.String.format;

import com.etermax.spacehorse.core.common.exception.ApiException;

public class SpecialOfferBoardNotFoundExpception extends ApiException {

	public SpecialOfferBoardNotFoundExpception(String userId) {
		super(format("Special Offer board for player ID [ %s ] not found", userId));
	}

}
