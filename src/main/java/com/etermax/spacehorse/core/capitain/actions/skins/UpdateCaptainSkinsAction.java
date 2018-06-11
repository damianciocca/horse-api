package com.etermax.spacehorse.core.capitain.actions.skins;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainSlotsValidator;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkinFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class UpdateCaptainSkinsAction {

	private final CaptainCollectionRepository captainCollectionRepository;
	private final CaptainSkinFactory captainSkinFactory;
	private final PlayerRepository playerRepository;
	private final CaptainSlotsValidator captainSlotsValidator;

	public UpdateCaptainSkinsAction(CaptainCollectionRepository captainCollectionRepository, PlayerRepository playerRepository) {
		this.captainCollectionRepository = captainCollectionRepository;
		this.playerRepository = playerRepository;
		this.captainSkinFactory = new CaptainSkinFactory();
		this.captainSlotsValidator = new CaptainSlotsValidator();
	}

	public void updateCaptainSkins(Player player, String captainId, Map<Integer, String> skinIdsBySlots, Catalog catalog) {
		CaptainsCollection captainsCollection = captainCollectionRepository.findOrDefaultBy(player);

		List<CaptainSkin> captainSkinsOfEachSlot = skinIdsBySlots.values().stream()
				.map(captainSkinId -> captainSkinFactory.create(captainSkinId, catalog)).collect(Collectors.toList());
		captainSlotsValidator.validateSlotOfSkins(captainId, skinIdsBySlots, captainSkinsOfEachSlot);
		captainsCollection.updateCaptainSkins(captainId, captainSkinsOfEachSlot);

		captainCollectionRepository.addOrUpdate(captainsCollection);
		playerRepository.update(player);
	}

}
