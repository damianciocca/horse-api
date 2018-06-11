package com.etermax.spacehorse.core.specialoffer.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.player.model.Player;

public class RefreshSpecialOfferBoardDomainService {

	private static final Logger logger = LoggerFactory.getLogger(RefreshSpecialOfferBoardDomainService.class);

	private final SpecialOfferBoardRepository specialOfferBoardRepository;

	public RefreshSpecialOfferBoardDomainService(SpecialOfferBoardRepository specialOfferBoardRepository) {
		this.specialOfferBoardRepository = specialOfferBoardRepository;
	}

	public SpecialOfferBoard refresh(Player player, List<SpecialOfferDefinition> specialOfferDefinitions) {
		SpecialOfferBoard specialOfferBoard = specialOfferBoardRepository.findOrDefaultBy(player);
		specialOfferBoard.refresh(player, specialOfferDefinitions);
		specialOfferBoardRepository.addOrUpdate(player, specialOfferBoard);
		return specialOfferBoard;
	}

}
