package com.etermax.spacehorse.core.capitain.actions.skins;

import com.etermax.spacehorse.core.achievements.action.CompleteAchievementAction;
import com.etermax.spacehorse.core.achievements.action.FindAchievementAction;
import com.etermax.spacehorse.core.achievements.model.observers.CaptainSkinsReachedAchievementObserver;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkinFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.inventory.PaymentMethodUtil;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;

public class BuyCaptainSkinAction {

	private final CaptainCollectionRepository captainCollectionRepository;
	private final CaptainSkinFactory captainSkinFactory;
	private final PlayerRepository playerRepository;
	private final FindAchievementAction findAchievementAction;
	private final CompleteAchievementAction completeAchievementAction;

	public BuyCaptainSkinAction(CaptainCollectionRepository captainCollectionRepository, PlayerRepository playerRepository,
			FindAchievementAction findAchievementAction, CompleteAchievementAction completeAchievementAction) {
		this.captainCollectionRepository = captainCollectionRepository;
		this.playerRepository = playerRepository;
		this.findAchievementAction = findAchievementAction;
		this.completeAchievementAction = completeAchievementAction;
		this.captainSkinFactory = new CaptainSkinFactory();
	}

	public void buyCaptainSkin(Player player, String captainId, String captainSkinId, Catalog catalog) {
		CaptainsCollection captainsCollection = captainCollectionRepository.findOrDefaultBy(player);
		CaptainSkin captainSkin = captainSkinFactory.create(captainSkinId, catalog);

		PaymentMethodUtil.payWith(player, captainSkin.getGemPrice(), captainSkin.getGoldPrice());
		captainsCollection.addCaptainSkin(player, captainId, captainSkin);

		captainCollectionRepository.addOrUpdate(captainsCollection);
		playerRepository.update(player);

		CaptainSkinsReachedAchievementObserver achievementObserver = createCaptainSkinsReachedAchievementObserver(captainsCollection);
		achievementObserver.update(player, catalog.getAchievementsDefinitionsCollection().getEntries());
	}

	private CaptainSkinsReachedAchievementObserver createCaptainSkinsReachedAchievementObserver(CaptainsCollection captainsCollection) {
		return new CaptainSkinsReachedAchievementObserver(findAchievementAction, completeAchievementAction, captainsCollection);
	}
}
