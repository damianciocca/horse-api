package com.etermax.spacehorse.core.specialoffer.cheat;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;
import com.etermax.spacehorse.core.specialoffer.resource.response.SpecialOfferResponse;

public class AddSpecialOfferCheat extends Cheat {

	private final SpecialOfferBoardRepository specialOfferBoardRepository;

	public AddSpecialOfferCheat(SpecialOfferBoardRepository specialOfferBoardRepository) {
		this.specialOfferBoardRepository = specialOfferBoardRepository;
	}

	@Override
	public String getCheatId() {
		return "addSpecialOffer";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		String specialOfferId = getParameterString(parameters, 0);

		SpecialOfferDefinition specialOfferDefinition = catalog.getSpecialOfferDefinitionsCollection().findByIdOrFail(specialOfferId);

		SpecialOfferBoard specialOfferBoard = specialOfferBoardRepository.findOrDefaultBy(player);

		SpecialOffer specialOffer = new SpecialOffer(specialOfferDefinition, new ServerTime());
		specialOfferBoard.put(specialOffer);

		specialOfferBoardRepository.addOrUpdate(player, specialOfferBoard);
		return new CheatResponse(new SpecialOfferResponse(specialOffer.getId(), specialOffer.getExpirationTimeInSeconds(),
				specialOffer.getAvailableAmountUntilExpiration()));
	}
}