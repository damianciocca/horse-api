package com.etermax.spacehorse.core.player.action.deck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.achievements.model.observers.PlayerLevelReachedAchievementObserver;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.matchmaking.action.ChallengeMatchmakingAction;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.player.model.deck.Card;
import com.etermax.spacehorse.core.player.model.inventory.cardparts.CardParts;
import com.etermax.spacehorse.core.player.model.inventory.currency.Currency;
import com.etermax.spacehorse.core.player.repository.PlayerRepository;
import com.etermax.spacehorse.mock.MockCatalog;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;

public class UpgradeCardActionTest {

	private static final Logger logger = LoggerFactory.getLogger(ChallengeMatchmakingAction.class);

	private static final int CARD_PARTS_AMOUNT = 1000;
	private static final int GOLD_AMOUNT = 1000;
	private static final String PLAYER_ID = "100";
	private static final Long CARD_ID = 4L;

	private Player player;
	private UpgradeCardAction action;

	@Before
	public void setup() {
		Catalog catalog = MockCatalog.buildCatalog();
		player = new PlayerScenarioBuilder(PLAYER_ID).build();
		PlayerRepository playerRepository = aPlayerRepository();
		CatalogRepository catalogRepository = aCatalogRepository(catalog, player.getAbTag());
		action = new UpgradeCardAction(playerRepository, catalogRepository, mock(PlayerLevelReachedAchievementObserver.class));
	}

	@Test
	public void whenTryToUpgradeACardWithoutEnoughGoldThenAnExceptionShouldBeThrown() throws Exception {

		givenAPlayerInventoryWithNotEnoughGold();

		assertThatThrownBy(() -> action.upgradeCard(PLAYER_ID, CARD_ID)).isInstanceOf(ApiException.class).hasMessageContaining("Not enough gold");
	}

	@Test
	public void whenTryToUpgradeACardWithoutEnoughCardPartsThenAnExceptionShouldBeThrown() throws Exception {

		assertThatThrownBy(() -> action.upgradeCard(PLAYER_ID, CARD_ID)).isInstanceOf(ApiException.class)
				.hasMessageContaining("Not enough card parts");
	}

	@Test
	public void whenUpgradeACardThenTheLevelShouldBeIncreaseAndCardPartsAndGoldsShouldBeConsumed() throws Exception {
		// Given
		givenAPlayerInventoryWithEnoughGoldAndCardParts();
		givenACardInLevelTwo();
		int cardPartsBeforeUpgrade = aCurrentCardParts();
		int goldBeforeUpgrade = aCurrentGold().getAmount();
		// When
		Card card = whenUpgradeCardInLevelTwo();
		// Then
		thenTheCardUpgradeToLevelThree(cardPartsBeforeUpgrade, card, goldBeforeUpgrade);
	}

	private void givenACardInLevelTwo() {
		logInventory();
		action.upgradeCard(PLAYER_ID, CARD_ID);
		logInventory();
		action.upgradeCard(PLAYER_ID, CARD_ID);
		logInventory();
	}

	private void givenAPlayerInventoryWithNotEnoughGold() {
		givenAPlayerInventoryWithEnoughCardParts();
		Currency gold = aCurrentGold();
		gold.setAmount(0);
	}

	private void givenAPlayerInventoryWithEnoughGoldAndCardParts() {
		givenAPlayerInventoryWithEnoughCardParts();
		Currency gold = aCurrentGold();
		gold.setAmount(GOLD_AMOUNT);
	}

	private void givenAPlayerInventoryWithEnoughCardParts() {
		Card card = aCurrentCardFromDeckWithId(CARD_ID);
		CardParts cardParts = aCurrentCardPartsFromInventory();
		cardParts.cheatSetAmount(card.getCardType(), CARD_PARTS_AMOUNT);
	}

	private Card whenUpgradeCardInLevelTwo() {
		Card card = action.upgradeCard(PLAYER_ID, CARD_ID);
		logger.info("after last upgrade");
		logInventory();
		return card;
	}

	private void thenTheCardUpgradeToLevelThree(int cardPartsBeforeUpgrade, Card upgradedCard, int goldBeforeUpgrade) {
		CardParts currentCardParts = aCurrentCardPartsFromInventory();
		Card currentCard = aCurrentCardFromDeckWithId(upgradedCard.getId());
		Currency currentGold = aCurrentGold();

		assertThat(upgradedCard.getLevel()).isEqualTo(3);
		assertThat(currentCardParts.getAmount(currentCard.getCardType())).isLessThan(cardPartsBeforeUpgrade);
		assertThat(currentGold.getAmount()).isLessThan(goldBeforeUpgrade);
		assertThat(aCurrentProgressLevel()).isEqualTo(3);
		assertThat(aCurrentProgressXp()).isEqualTo(10);
	}

	private int aCurrentProgressXp() {
		return player.getProgress().getXp();
	}

	private Currency aCurrentGold() {
		return player.getInventory().getGold();
	}

	private CardParts aCurrentCardPartsFromInventory() {
		return player.getInventory().getCardParts();
	}

	private Card aCurrentCardFromDeckWithId(Long cardId) {
		return player.getDeck().findCardById(cardId).get();
	}

	private int aCurrentCardParts() {
		CardParts cardParts = aCurrentCardPartsFromInventory();
		Card card = aCurrentCardFromDeckWithId(CARD_ID);
		return cardParts.getAmount(card.getCardType());
	}

	private CatalogRepository aCatalogRepository(Catalog catalog, ABTag abTag) {
		CatalogRepository catalogRepository = mock(CatalogRepository.class);
		when(catalogRepository.getActiveCatalogWithTag(abTag)).thenReturn(catalog);
		return catalogRepository;
	}

	private PlayerRepository aPlayerRepository() {
		PlayerRepository playerRepository = mock(PlayerRepository.class);
		when(playerRepository.find(PLAYER_ID)).thenReturn(player);
		return playerRepository;
	}

	private int aCurrentProgressLevel() {
		return player.getProgress().getLevel();
	}

	private void logInventory() {
		logger.info("------------------------------");
		logger.info("gold {}", aCurrentGold().getAmount());
		logger.info("card parts {}", aCurrentCardParts());
		logger.info("player progress level {}", aCurrentProgressLevel());
		logger.info("player progress XP {}", aCurrentProgressXp());
		logger.info("------------------------------");
	}
}
