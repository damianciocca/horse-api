package com.etermax.spacehorse.core.cheat.action;

import java.util.HashMap;

import com.etermax.spacehorse.core.abtest.cheat.SetAbTagCheat;
import com.etermax.spacehorse.core.achievements.model.observers.CardsUnlockedReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.PlayerLevelReachedAchievementObserver;
import com.etermax.spacehorse.core.achievements.model.observers.PlayerMapReachedAchievementObserver;
import com.etermax.spacehorse.core.battle.repository.PlayerWinRateRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.request.CheatRequest;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.freechest.cheat.SetFreeChestOpeningTimeCheat;
import com.etermax.spacehorse.core.login.action.LinkGameCenterAction;
import com.etermax.spacehorse.core.login.action.LinkGooglePlayAction;
import com.etermax.spacehorse.core.login.cheat.CreateNewUserPasswordCheat;
import com.etermax.spacehorse.core.login.cheat.DeleteAuthLink;
import com.etermax.spacehorse.core.player.cheat.AddCardCheat;
import com.etermax.spacehorse.core.player.cheat.AddChestCheat;
import com.etermax.spacehorse.core.player.cheat.AddFinishedTutorialIdCheat;
import com.etermax.spacehorse.core.player.cheat.RemoveCardCheat;
import com.etermax.spacehorse.core.player.cheat.RemoveChestCheat;
import com.etermax.spacehorse.core.player.cheat.RemoveFinishedTutorialIdCheat;
import com.etermax.spacehorse.core.player.cheat.SetActiveTutorialIdCheat;
import com.etermax.spacehorse.core.player.cheat.SetCardLevelCheat;
import com.etermax.spacehorse.core.player.cheat.SetCardParts;
import com.etermax.spacehorse.core.player.cheat.SetChestOpeningTime;
import com.etermax.spacehorse.core.player.cheat.SetGoldRewardsReceivedTodayCheat;
import com.etermax.spacehorse.core.player.cheat.SetPlayerBotMmrCheat;
import com.etermax.spacehorse.core.player.cheat.SetPlayerGemsCheat;
import com.etermax.spacehorse.core.player.cheat.SetPlayerGoldCheat;
import com.etermax.spacehorse.core.player.cheat.SetPlayerLevelCheat;
import com.etermax.spacehorse.core.player.cheat.SetPlayerMmrCheat;
import com.etermax.spacehorse.core.player.cheat.SetPlayerXpCheat;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.cheat.SetDailyQuestRemainingRefreshTime;
import com.etermax.spacehorse.core.quest.cheat.SetRemainingAmountForActiveQuest;
import com.etermax.spacehorse.core.quest.cheat.SetRemainingAmountForDailyQuest;
import com.etermax.spacehorse.core.quest.cheat.SetRemainingRefreshTime;
import com.etermax.spacehorse.core.quest.cheat.SetRemainingSkipTime;
import com.etermax.spacehorse.core.quest.model.QuestBoardRepository;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.shop.cheat.SetShopCardsExpirationTime;
import com.etermax.spacehorse.core.specialoffer.cheat.AddSpecialOfferCheat;
import com.etermax.spacehorse.core.specialoffer.cheat.RemoveSpecialOfferCheat;
import com.etermax.spacehorse.core.specialoffer.cheat.SetSpecialOfferExpirationTimeCheat;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoardRepository;
import com.etermax.spacehorse.core.user.repository.UserRepository;

public class CheatAction {

	private final PlayerRepository playerRepository;

	private final CatalogRepository catalogRepository;

	private final PlayerWinRateRepository playerWinRateRepository;

	private final UserRepository userRepository;
	private ServerTimeProvider timeProvider;

	private HashMap<String, Cheat> cheats;

	private final QuestBoardRepository questBoardRepository;
	private final SpecialOfferBoardRepository specialOfferBoardRepository;

	private final LinkGooglePlayAction linkGooglePlayAction;
	private final LinkGameCenterAction linkGameCenterAction;

	private final CardsUnlockedReachedAchievementObserver cardsUnlockedReachedAchievementObserver;
	private final PlayerLevelReachedAchievementObserver playerLevelReachedAchievementObserver;
	private final PlayerMapReachedAchievementObserver playerMapReachedAchievementObserver;

	public CheatAction(PlayerRepository playerRepository, CatalogRepository catalogRepository, PlayerWinRateRepository playerWinRateRepository,
			UserRepository userRepository, ServerTimeProvider timeProvider, QuestBoardRepository questBoardRepository,
			SpecialOfferBoardRepository specialOfferBoardRepository, LinkGooglePlayAction linkGooglePlayAction,
			LinkGameCenterAction linkGameCenterAction, CardsUnlockedReachedAchievementObserver cardsUnlockedReachedAchievementObserver,
			PlayerLevelReachedAchievementObserver playerLevelReachedAchievementObserver,
			PlayerMapReachedAchievementObserver playerMapReachedAchievementObserver) {
		this.playerRepository = playerRepository;
		this.catalogRepository = catalogRepository;
		this.playerWinRateRepository = playerWinRateRepository;
		this.userRepository = userRepository;
		this.timeProvider = timeProvider;
		this.questBoardRepository = questBoardRepository;
		this.specialOfferBoardRepository = specialOfferBoardRepository;
		this.linkGooglePlayAction = linkGooglePlayAction;
		this.linkGameCenterAction = linkGameCenterAction;
		this.cardsUnlockedReachedAchievementObserver = cardsUnlockedReachedAchievementObserver;
		this.playerLevelReachedAchievementObserver = playerLevelReachedAchievementObserver;
		this.playerMapReachedAchievementObserver = playerMapReachedAchievementObserver;
		registerCheats();
	}

	private void registerCheats() {
		cheats = new HashMap<>();
		registerCheat(new AddChestCheat());
		registerCheat(new RemoveChestCheat());
		registerCheat(new SetPlayerGoldCheat());
		registerCheat(new SetPlayerGemsCheat());
		registerCheat(new SetChestOpeningTime(timeProvider));
		registerCheat(new SetCardParts());
		registerCheat(new SetPlayerXpCheat());
		registerCheat(new SetPlayerLevelCheat(playerLevelReachedAchievementObserver));
		registerCheat(new AddCardCheat(cardsUnlockedReachedAchievementObserver));
		registerCheat(new RemoveCardCheat());
		registerCheat(new SetCardLevelCheat());
		registerCheat(new SetShopCardsExpirationTime());
		registerCheat(new SetPlayerMmrCheat(playerWinRateRepository, playerMapReachedAchievementObserver));
		registerCheat(new SetPlayerBotMmrCheat());
		registerCheat(new SetFreeChestOpeningTimeCheat());
		registerCheat(new SetActiveTutorialIdCheat());
		registerCheat(new AddFinishedTutorialIdCheat());
		registerCheat(new RemoveFinishedTutorialIdCheat());
		registerCheat(new RemoveFinishedTutorialIdCheat());
		registerCheat(new CreateNewUserPasswordCheat(userRepository));
		registerCheat(new SetGoldRewardsReceivedTodayCheat());
		registerCheat(new SetRemainingRefreshTime(questBoardRepository));
		registerCheat(new SetRemainingAmountForActiveQuest(questBoardRepository));
		registerCheat(new SetRemainingSkipTime(questBoardRepository));
		registerCheat(new SetSpecialOfferExpirationTimeCheat(specialOfferBoardRepository));
		registerCheat(new RemoveSpecialOfferCheat(specialOfferBoardRepository));
		registerCheat(new AddSpecialOfferCheat(specialOfferBoardRepository));
		registerCheat(new SetAbTagCheat());
		registerCheat(new SetDailyQuestRemainingRefreshTime(questBoardRepository));
		registerCheat(new SetRemainingAmountForDailyQuest(questBoardRepository));
		registerCheat(new DeleteAuthLink(linkGooglePlayAction, linkGameCenterAction));
	}

	private void registerCheat(Cheat cheat) {
		cheats.put(cheat.getCheatId(), cheat);
	}

	public CheatResponse applyCheat(String loginId, CheatRequest cheatRequest) {
		Player player = playerRepository.find(loginId);
		if (player != null) {
			return applyCheat(player, cheatRequest);
		}
		return null;
	}

	private CheatResponse applyCheat(Player player, CheatRequest cheatRequest) {
		CheatResponse response = null;
		String cheatId = cheatRequest.getCheatId();
		String[] parameters = cheatRequest.getParameters();
		Cheat cheat = cheats.get(cheatId);
		Catalog catalog = catalogRepository.getActiveCatalogWithTag(player.getAbTag());
		if (cheat != null) {
			response = cheat.apply(player, parameters, catalog);
			if (response != null) {
				playerRepository.update(player);
			}
		}
		return response;
	}

}
