package com.etermax.spacehorse.core.specialoffer.action;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.specialoffer.model.RefreshSpecialOfferBoardDomainService;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;

public class RefreshSpecialOfferBoardAction {

	private final RefreshSpecialOfferBoardDomainService refreshSpecialOfferBoardDomainService;

	public RefreshSpecialOfferBoardAction(RefreshSpecialOfferBoardDomainService refreshSpecialOfferBoardDomainService) {
		this.refreshSpecialOfferBoardDomainService = refreshSpecialOfferBoardDomainService;
	}

	public SpecialOfferBoard refresh(Player player, Catalog catalog) {
		List<SpecialOfferDefinition> specialOfferDefinitions = getDefinitionsFrom(catalog);
		return refreshSpecialOfferBoardDomainService.refresh(player, specialOfferDefinitions);
	}

	private List<SpecialOfferDefinition> getDefinitionsFrom(Catalog catalog) {
		return catalog.getSpecialOfferDefinitionsCollection().getEntries();
	}

}
