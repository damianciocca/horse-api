package com.etermax.spacehorse.core.capitain.actions;

import java.util.List;

import com.etermax.spacehorse.core.achievements.model.observers.types.AchievementsObserver;
import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainFactory;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.PaymentMethodUtil;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class BuyCaptainAction {

	private final CaptainCollectionRepository captainCollectionRepository;
	private final CaptainFactory captainFactory;
	private final PlayerRepository playerRepository;
	private final AchievementsObserver achievementsObserver;

	public BuyCaptainAction(CaptainCollectionRepository captainCollectionRepository, PlayerRepository playerRepository,
			AchievementsObserver achievementsObserver) {
		this.captainCollectionRepository = captainCollectionRepository;
		this.playerRepository = playerRepository;
		this.captainFactory = new CaptainFactory();
		this.achievementsObserver = achievementsObserver;
	}

	public void buyCaptain(Player player, String captainId, Catalog catalog) {
		CaptainsCollection captainsCollection = captainCollectionRepository.findOrDefaultBy(player);
		Captain captain = createCaptainWith(captainId, catalog);
		PaymentMethodUtil.payWith(player, captain.getGemPrice(), captain.getGoldPrice());
		captainsCollection.addCaptain(player, captain);
		captainsCollection.selectCaptain(captain.getCaptainId());
		captainCollectionRepository.addOrUpdate(captainsCollection);
		playerRepository.update(player);
		achievementsObserver.update(player, catalog.getAchievementsDefinitionsCollection().getEntries());
	}

	private Captain createCaptainWith(String captainId, Catalog catalog) {
		CaptainDefinition captainDefinition = catalog.getCaptainDefinitionsCollection().findByIdOrFail(captainId);
		List<CaptainSkinDefinition> captainSkinDefinitions = catalog.getCaptainSkinDefinitionsCollection().getEntries();
		return captainFactory.createWithDefaultSkin(captainId, captainDefinition, captainSkinDefinitions);
	}
}
