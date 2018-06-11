package com.etermax.spacehorse.mock;

import static com.etermax.spacehorse.core.quest.model.QuestDifficultyType.EASY;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.etermax.spacehorse.core.authenticator.model.Role;
import com.etermax.spacehorse.core.catalog.action.CatalogAction;
import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.CardRarity;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.MarketType;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.inapps.resource.response.InAppsItemResponse;
import com.etermax.spacehorse.core.inapps.resource.response.ProductPurchaseStatus;
import com.etermax.spacehorse.core.player.action.PlayerAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.model.inventory.Inventory;
import com.etermax.spacehorse.core.player.model.inventory.currency.Currency;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.core.quest.model.Quest;
import com.etermax.spacehorse.core.quest.model.QuestSlot;
import com.etermax.spacehorse.core.quest.model.QuestType;
import com.etermax.spacehorse.core.reward.model.ApplyRewardDomainService;
import com.etermax.spacehorse.core.reward.model.Reward;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.reward.resource.response.RewardResponse;
import com.etermax.spacehorse.core.shop.action.ShopBuyCardAction;
import com.etermax.spacehorse.core.shop.action.ShopBuyInAppItemAction;
import com.etermax.spacehorse.core.shop.action.ShopBuyItemAction;
import com.etermax.spacehorse.core.shop.action.ShopRefreshCardsAction;
import com.etermax.spacehorse.core.shop.model.ShopCard;
import com.etermax.spacehorse.core.shop.model.ShopCards;
import com.etermax.spacehorse.core.user.action.UserAction;
import com.etermax.spacehorse.core.user.model.Platform;
import com.etermax.spacehorse.core.user.model.User;

public class MockUtils {

	public static Catalog mockCatalog() {
		return MockCatalog.buildCatalog();
	}

	public static ShopBuyItemAction mockShopBuyItemAction(Catalog catalog, String shopBuyItem) {
		ShopBuyItemAction shopBuyItemAction = mock(ShopBuyItemAction.class);
		List<RewardResponse> rewards = new ArrayList<>();
		rewards.add(new RewardResponse(RewardType.GOLD, "Reward Gold 100", 100));
		when(shopBuyItemAction.buy(any(), eq(catalog), eq(shopBuyItem))).thenReturn(rewards);
		when(shopBuyItemAction.buy(any(), eq(catalog), eq(shopBuyItem), eq(false))).thenReturn(rewards);
		return shopBuyItemAction;
	}

	public static ShopBuyInAppItemAction mockShopBuyInAppItemAction(Catalog catalog, String shopBuyItem, String productId, MarketType marketType) {
		ShopBuyInAppItemAction shopBuyInAppItemAction = mock(ShopBuyInAppItemAction.class);
		List<RewardResponse> rewards = new ArrayList<>();
		rewards.add(new RewardResponse(RewardType.GOLD, "Reward Gold 100", 100));
		when(shopBuyInAppItemAction.buyInApp(any(), any(), eq(catalog), eq(shopBuyItem), any())).thenReturn(rewards);
		List<InAppsItemResponse> inAppsItemResponse = new ArrayList<>();
		inAppsItemResponse.add(new InAppsItemResponse(new ProductPurchaseStatus(productId, true), rewards));
		when(shopBuyInAppItemAction.applyPendingReceipts(any(), any(), eq(catalog), any())).thenReturn(inAppsItemResponse);
		return shopBuyInAppItemAction;
	}

	public static ShopBuyCardAction mockShopBuyCardAction() {
		ShopBuyCardAction shopBuyCardAction = mock(ShopBuyCardAction.class);
		Card unlockedCard = new Card(1L, "type", 1);
		when(shopBuyCardAction.buy(any(), any(), anyList())).thenReturn(unlockedCard);
		return shopBuyCardAction;
	}

	public static ShopRefreshCardsAction mockShopRefreshCardsAction() {
		ShopRefreshCardsAction shopRefreshCardsAction = mock(ShopRefreshCardsAction.class);
		List<ShopCard> shopCardList = new ArrayList<>();
		shopCardList.add(new ShopCard());
		ShopCards shopCards = new ShopCards(shopCardList, 1L);
		when(shopRefreshCardsAction.refresh(any(), any(), any(), any())).thenReturn(shopCards);
		return shopRefreshCardsAction;
	}

	public static QuestSlot mockPlayerActiveQuest() {
		QuestSlot questSlot = new QuestSlot();
		String questId = "questId";
		String chestId = "mockChestId";
		int goalAmount = 10;
		QuestDefinition questDefinition = new QuestDefinition(questId, QuestType.QUEST_SIMPLE_VICTORIES, chestId, goalAmount, EASY.toString());
		questSlot.setActiveQuest(new Quest(questDefinition));
		return questSlot;
	}

	public static UserAction mockUserAction(String userId, Platform platform) {
		UserAction userAction = mock(UserAction.class);
		User user = new User(userId, null, null, platform);
		when(userAction.findByUserId(userId)).thenReturn(user);
		return userAction;
	}

	public static PlayerAction mockPlayerAction(String playerId, String userId) {
		HttpServletRequest request = MockUtils.mockHttpServletRequest(playerId);
		PlayerAction playerAction = mock(PlayerAction.class);
		Player player = mock(Player.class);
		when(player.getUserId()).thenReturn(userId);
		when(playerAction.findByLoginId(request.getHeader("Login-Id"))).thenReturn(Optional.of(player));
		return playerAction;
	}

	public static CatalogAction mockCatalogAction(Catalog catalog) {
		CatalogAction catalogAction = mock(CatalogAction.class);
		when(catalogAction.getCatalogForUser(any())).thenReturn(catalog);
		when(catalogAction.findById(any())).thenReturn(Optional.of(catalog));
		return catalogAction;
	}

	public static HttpServletRequest mockHttpServletRequest(String userId) {
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Login-Id")).thenReturn(userId);
		return request;
	}

	public static Player mockPlayerWithGems(String playerId, Integer gemsAmount, PlayerRepository playerRepository) {
		Player player2 = mock(Player.class);
		player2.setUserId(playerId);
		when(playerRepository.find(playerId)).thenReturn(player2);
		Inventory inventoryPlayer2 = mock(Inventory.class);
		Currency gemsPlayer2 = new Currency();
		gemsPlayer2.add(gemsAmount);
		when(inventoryPlayer2.getGems()).thenReturn(gemsPlayer2);
		when(player2.getInventory()).thenReturn(inventoryPlayer2);
		when(player2.getMapNumber()).thenReturn(0);
		return player2;
	}

	public static void mockGemsRewardsForPlayer(Player player, Integer gemsQuantity, PlayerRepository playerRepository,
			ApplyRewardDomainService applyRewardDomainService) {
		List<Reward> gemsRewards = new ArrayList<>();
		gemsRewards.add(new Reward(RewardType.GEMS, gemsQuantity));
		List<RewardResponse> gemsRewardsResonse = new ArrayList<>();
		gemsRewardsResonse.add(new RewardResponse(RewardType.GEMS, gemsQuantity));
		when(applyRewardDomainService.applyRewards(any(), anyList(), any(), any(), anyList())).thenReturn(gemsRewardsResonse);
		doNothing().when(playerRepository).update(player);
	}

	public static User mockUser(String userId, Platform platform) {
		User user = mock(User.class);
		when(user.getUserId()).thenReturn(userId);
		when(user.getPlatform()).thenReturn(platform);
		return user;
	}

	public static User mockUser() {
		String userId = "userId";
		String password = "password";
		Role role = Role.PLAYER;
		Platform platform = Platform.EDITOR;
		User user = new User(userId, password, role, platform);
		return user;
	}

	public static List<CardDefinition> mockCardsDefinitionsList(CardRarity cardRarity) {
		List<CardDefinition> cardsDefinitionsList = new ArrayList();
		cardsDefinitionsList.add(new CardDefinition("card_fighter", cardRarity, 0));
		cardsDefinitionsList.add(new CardDefinition("card_bomber", cardRarity, 0));
		cardsDefinitionsList.add(new CardDefinition("card_corvette", cardRarity, 0));
		cardsDefinitionsList.add(new CardDefinition("card_corvette_bomber", cardRarity, 0));
		cardsDefinitionsList.add(new CardDefinition("card_frigate", cardRarity, 0));
		cardsDefinitionsList.add(new CardDefinition("card_cruiser", cardRarity, 0));
		cardsDefinitionsList.add(new CardDefinition("card_laser", cardRarity, 0));
		cardsDefinitionsList.add(new CardDefinition("card_missile", cardRarity, 0));
		cardsDefinitionsList.add(new CardDefinition("card_decoy", cardRarity, 0));
		return cardsDefinitionsList;
	}

	public static List<CardDefinition> mockCardsDefinitionsListWithTwoRarities(CardRarity cardRarity1, CardRarity cardRarity2) {
		List<CardDefinition> cardsDefinitionsList = new ArrayList();
		cardsDefinitionsList.add(new CardDefinition("card_fighter", cardRarity1, 0));
		cardsDefinitionsList.add(new CardDefinition("card_bomber", cardRarity1, 0));
		cardsDefinitionsList.add(new CardDefinition("card_corvette", cardRarity1, 0));
		cardsDefinitionsList.add(new CardDefinition("card_corvette_bomber", cardRarity1, 0));
		cardsDefinitionsList.add(new CardDefinition("card_frigate", cardRarity1, 0));
		cardsDefinitionsList.add(new CardDefinition("card_frigate2", cardRarity1, 0));
		cardsDefinitionsList.add(new CardDefinition("card_frigate3", cardRarity1, 0));

		cardsDefinitionsList.add(new CardDefinition("card_cruiser", cardRarity2, 0));
		cardsDefinitionsList.add(new CardDefinition("card_laser", cardRarity2, 0));
		cardsDefinitionsList.add(new CardDefinition("card_missile", cardRarity2, 0));
		cardsDefinitionsList.add(new CardDefinition("card_decoy", cardRarity2, 0));
		cardsDefinitionsList.add(new CardDefinition("card_decoy2", cardRarity2, 0));
		cardsDefinitionsList.add(new CardDefinition("card_decoy3", cardRarity2, 0));
		cardsDefinitionsList.add(new CardDefinition("card_decoy4", cardRarity2, 0));
		return cardsDefinitionsList;
	}
}
