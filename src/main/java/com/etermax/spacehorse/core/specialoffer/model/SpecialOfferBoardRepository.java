package com.etermax.spacehorse.core.specialoffer.model;

import com.etermax.spacehorse.core.player.model.Player;

public interface SpecialOfferBoardRepository {

	SpecialOfferBoard findBy(Player player);

	SpecialOfferBoard findOrDefaultBy(Player player);

	void addOrUpdate(Player player, SpecialOfferBoard specialOfferBoard);
}
