package com.etermax.spacehorse.core.specialoffer.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;

public class RemoveSpecialOfferCheat extends Cheat {

	private final SpecialOfferBoardRepository specialOfferBoardRepository;

	public RemoveSpecialOfferCheat(SpecialOfferBoardRepository specialOfferBoardRepository) {
		this.specialOfferBoardRepository = specialOfferBoardRepository;
	}

	@Override
	public String getCheatId() {
		return "removeSpecialOffer";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		String specialOfferId = getParameterString(parameters, 0);

		SpecialOfferBoard specialOfferBoard = specialOfferBoardRepository.findOrDefaultBy(player);
		specialOfferBoard.cheatRemoveSpecialOffer(specialOfferId);
		specialOfferBoardRepository.addOrUpdate(player, specialOfferBoard);
		return new CheatResponse(specialOfferId);
	}
}