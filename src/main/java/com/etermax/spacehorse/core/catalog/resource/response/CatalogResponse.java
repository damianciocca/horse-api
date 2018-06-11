package com.etermax.spacehorse.core.catalog.resource.response;

import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.bot.BotsChancesByMmrCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.quest.QuestCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.specialoffer.CardCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferInAppCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferItemsCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.unlock.FeaturesByPlayerLvlCatalogMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CatalogResponse {

	@JsonProperty("Id")
	private String id = null;

	@JsonProperty("Maps")
	private EntryContainerResponse<MapDefinitionResponse> maps = new EntryContainerResponse<>();

	@JsonProperty("Units")
	private EntryContainerResponse<UnitDefinitionResponse> units = new EntryContainerResponse<>();

	@JsonProperty("UnitsLevels")
	private EntryContainerResponse<UnitLevelDefinitionResponse> unitsLevels = new EntryContainerResponse<>();

	@JsonProperty("Cards")
	private EntryContainerResponse<CardDefinitionResponse> cards = new EntryContainerResponse<>();

	@JsonProperty("CardsLevels")
	private EntryContainerResponse<CardLevelDefinitionResponse> cardsLevels = new EntryContainerResponse<>();

	@JsonProperty("CardsParametersLevels")
	private EntryContainerResponse<CardParameterLevelResponse> cardsParameterLevels = new EntryContainerResponse<>();

	@JsonProperty("GameConstants")
	private EntryContainerResponse<TupleResponse> constants = new EntryContainerResponse<>();

	@JsonProperty("Chests")
	private EntryContainerResponse<ChestDefinitionResponse> chests = new EntryContainerResponse<>();

	@JsonProperty("PlayerLevels")
	private EntryContainerResponse<PlayerLevelResponse> playerLevels = new EntryContainerResponse<>();

	@JsonProperty("ChestsLists")
	private EntryContainerResponse<ChestListEntryResponse> chestsLists = new EntryContainerResponse<>();

	@JsonProperty("ShopItems")
	private EntryContainerResponse<ShopItemEntryResponse> shopItems = new EntryContainerResponse<>();

	@JsonProperty("ShopCards")
	private EntryContainerResponse<ShopCardDefinitionResponse> shopCards = new EntryContainerResponse<>();

	@JsonProperty("GemsCycle")
	private EntryContainerResponse<GemsCycleEntryResponse> gemsCycle = new EntryContainerResponse<>();

	@JsonProperty("PowerUps")
	private EntryContainerResponse<PowerUpDefinitionResponse> powerUps = new EntryContainerResponse<>();

	@JsonProperty("PowerUpsLevels")
	private EntryContainerResponse<PowerUpLevelDefinitionResponse> powerUpsLevels = new EntryContainerResponse<>();

	@JsonProperty("ShopInApp")
	private EntryContainerResponse<ShopInAppDefinitionResponse> shopInApp = new EntryContainerResponse<>();

	@JsonProperty("ChestChances")
	private EntryContainerResponse<ChestChancesDefinitionResponse> chestChances = new EntryContainerResponse<>();

	@JsonProperty("CardsDropRate")
	private EntryContainerResponse<CardsDropRateResponse> cardsDropRate = new EntryContainerResponse<>();

	@JsonProperty("Bots")
	private EntryContainerResponse<BotDefinitionResponse> bots = new EntryContainerResponse<>();

	@JsonProperty("BotsNames")
	private EntryContainerResponse<BotNameDefinitionResponse> botsNames = new EntryContainerResponse<>();

	@JsonProperty("TutorialProgress")
	private EntryContainerResponse<TutorialProgressCatalogResponse> tutorialProgress = new EntryContainerResponse<>();

	@JsonProperty("QuestDefinition")
	private EntryContainerResponse<QuestDefinitionResponse> questCollection = new EntryContainerResponse<>();

	@JsonProperty("QuestCycle")
	private EntryContainerResponse<QuestCycleResponse> questCycleCollection = new EntryContainerResponse<>();

	@JsonProperty("ABTester")
	private EntryContainerResponse<ABTesterResponse> aBTester = new EntryContainerResponse<>();

	@JsonProperty("ShopItemsMapConfiguration")
	private EntryContainerResponse<ShopItemsMapConfigEntryResponse> shopItemsMapConfiguration = new EntryContainerResponse<>();

	@JsonProperty("QuestChances")
	private EntryContainerResponse<QuestChanceResponse> questChanceCollection = new EntryContainerResponse<>();

	@JsonProperty("SpecialOffersItems")
	private EntryContainerResponse<SpecialOffersItemResponse> specialOffersItemsCollection = new EntryContainerResponse<>();

	@JsonProperty("SpecialOffers")
	private EntryContainerResponse<SpecialOfferDefinitionResponse> specialOffersCollection = new EntryContainerResponse<>();

	@JsonProperty("BotsCardsCombinations")
	private EntryContainerResponse<CardCombinationStrategyDefinitionResponse> cardCombinationStrategiesCollection = new EntryContainerResponse<>();

	@JsonProperty("BotsCardsDefense")
	private EntryContainerResponse<CardDefenseStrategyDefinitionResponse> cardDefenseStrategiesCollection = new EntryContainerResponse<>();

	@JsonProperty("BotsLevelParameters")
	private EntryContainerResponse<BotLevelParameterDefinitionResponse> botsLevelParametersCollection = new EntryContainerResponse<>();

	@JsonProperty("SpecialOfferInApp")
	private EntryContainerResponse<SpecialOfferInAppDefinitionResponse> specialOfferInAppCollection = new EntryContainerResponse<>();

	@JsonProperty("Captains")
	private EntryContainerResponse<CaptainDefinitionResponse> captainsCollection = new EntryContainerResponse<>();

	@JsonProperty("CaptainsSkins")
	private EntryContainerResponse<CaptainSkinDefinitionResponse> captainsSkinsCollection = new EntryContainerResponse<>();

	@JsonProperty("FeaturesByPlayerLvl")
	private EntryContainerResponse<FeaturesByPlayerLvlDefinitionResponse> featuresByPlayerLvlCollection = new EntryContainerResponse<>();

	@JsonProperty("DeltaMmrPercentage")
	private EntryContainerResponse<DeltaMmrPercentageDefinitionRepresentation> mmrDeltaPercentageCollection = new EntryContainerResponse<>();

	@JsonProperty("DailyQuestDefinitions")
	private EntryContainerResponse<QuestDefinitionResponse> dailyQuestDefinitionCollection = new EntryContainerResponse<>();

	@JsonProperty("VideoRewards")
	private EntryContainerResponse<VideoRewardDefinitionResponse> videoRewardsCollection = new EntryContainerResponse<>();

	@JsonProperty("BotsChancesByMmr")
	private EntryContainerResponse<BotsChancesByMmrDefinitionResponse> botsChancesByMmrCollection = new EntryContainerResponse<>();

	@JsonProperty("Achievements")
	private EntryContainerResponse<AchievementDefinitionResponse> achievementsCollection = new EntryContainerResponse<>();

	@JsonProperty("LeagueRewards")
	private EntryContainerResponse<LeagueRewardsDefinitionResponse> leagueRewardsCollection = new EntryContainerResponse<>();

	public CatalogResponse() {
	}

	public CatalogResponse(Catalog catalog) {
		this.id = catalog.getCatalogId();

		this.maps = new EntryContainerResponse<>(
				catalog.getMapsCollection().getEntries().stream().map(MapDefinitionResponse::new).collect(Collectors.toList()));

		this.units = new EntryContainerResponse<>(
				catalog.getUnitDefinitionsCollection().getEntries().stream().map(UnitDefinitionResponse::new).collect(Collectors.toList()));

		this.unitsLevels = new EntryContainerResponse<>(
				catalog.getUnitLevelDefinitionsCollection().getEntries().stream().map(UnitLevelDefinitionResponse::new).collect(Collectors.toList()));

		this.cards = new CardCatalogMapper().mapFrom(catalog);

		this.cardsLevels = new EntryContainerResponse<>(
				catalog.getCardLevelDefinitionsCollection().getEntries().stream().map(CardLevelDefinitionResponse::new).collect(Collectors.toList()));

		this.cardsParameterLevels = new EntryContainerResponse<>(
				catalog.getCardParameterLevelsCollection().getEntries().stream().map(CardParameterLevelResponse::new).collect(Collectors.toList()));

		this.constants = new EntryContainerResponse<>(
				catalog.getGameConstants().getTuples().stream().map(TupleResponse::new).collect(Collectors.toList()));

		this.chests = new EntryContainerResponse<>(
				catalog.getChestDefinitionsCollection().getEntries().stream().map(ChestDefinitionResponse::new).collect(Collectors.toList()));

		this.playerLevels = new EntryContainerResponse<>(
				catalog.getPlayerLevelsCollection().getEntries().stream().map(PlayerLevelResponse::new).collect(Collectors.toList()));

		this.chestsLists = new EntryContainerResponse<>();
		catalog.getChestsListsCollection().getEntries().forEach(chestList -> chestList.getEntries()
				.forEach(chestListEntry -> this.chestsLists.addEntry(new ChestListEntryResponse(chestList, chestListEntry))));

		this.shopItems = new EntryContainerResponse<>(
				catalog.getShopItemsCollection().getEntries().stream().map(ShopItemEntryResponse::new).collect(Collectors.toList()));

		this.shopCards = new EntryContainerResponse<>(
				catalog.getShopCardsCollection().getEntries().stream().map(ShopCardDefinitionResponse::new).collect(Collectors.toList()));

		this.gemsCycle = new EntryContainerResponse<>();
		catalog.getGemsCycleCollection().getEntries().forEach(gemsCycleConsumer -> gemsCycleConsumer.getEntries()
				.forEach(gemsCycleEntryConsumer -> this.gemsCycle.addEntry(new GemsCycleEntryResponse(gemsCycleConsumer, gemsCycleEntryConsumer))));

		this.powerUps = new EntryContainerResponse<>(
				catalog.getPowerUpDefinitionsCollection().getEntries().stream().map(PowerUpDefinitionResponse::new).collect(Collectors.toList()));

		this.powerUpsLevels = new EntryContainerResponse<>(
				catalog.getPowerUpLevelDefinitionsCollection().getEntries().stream().map(PowerUpLevelDefinitionResponse::new)
						.collect(Collectors.toList()));

		this.shopInApp = new EntryContainerResponse<>(
				catalog.getShopInAppCollection().getEntries().stream().map(ShopInAppDefinitionResponse::new).collect(Collectors.toList()));

		this.chestChances = new EntryContainerResponse<>(
				catalog.getChestChancesDefinitionsCollection().getEntries().stream().map(ChestChancesDefinitionResponse::new)
						.collect(Collectors.toList()));

		this.cardsDropRate = new EntryContainerResponse<>(
				catalog.getCardsDropRateCollection().getEntries().stream().map(CardsDropRateResponse::new).collect(Collectors.toList()));

		this.bots = new EntryContainerResponse<>(
				catalog.getBotsDefinitionsCollection().getEntries().stream().map(BotDefinitionResponse::new).collect(Collectors.toList()));

		this.botsNames = new EntryContainerResponse<>(
				catalog.getBotsNamesDefinitionsCollection().getEntries().stream().map(BotNameDefinitionResponse::new).collect(Collectors.toList()));

		this.tutorialProgress = new EntryContainerResponse<>(
				catalog.getTutorialProgressCollection().getEntries().stream().map(TutorialProgressCatalogResponse::new).collect(Collectors.toList()));

		this.aBTester = new EntryContainerResponse<>(
				catalog.getAbTesterCollection().getEntries().stream().map(ABTesterResponse::new).collect(Collectors.toList()));

		this.questCollection = new EntryContainerResponse<>(
				catalog.getQuestCollection().getEntries().stream().map(QuestDefinitionResponse::new).collect(Collectors.toList()));

		this.questCycleCollection = new EntryContainerResponse<>();
		catalog.getQuestCycleListCollection().getEntries().forEach(questCycle -> questCycle.getEntries()
				.forEach(questCycleEntry -> this.questCycleCollection.addEntry(new QuestCycleResponse(questCycle, questCycleEntry))));

		this.shopItemsMapConfiguration = new EntryContainerResponse<>(
				catalog.getShopItemsMapConfigCollection().getEntries().stream().map(ShopItemsMapConfigEntryResponse::new)
						.collect(Collectors.toList()));

		this.questChanceCollection = new EntryContainerResponse<>();
		catalog.getQuestChancesListCollection().getEntries().forEach(chancesList -> chancesList.getQuestDefinitionWithChances()
				.forEach(definitionWithChance -> this.questChanceCollection.addEntry(new QuestChanceResponse(chancesList, definitionWithChance))));

		this.specialOffersItemsCollection = new SpecialOfferItemsCatalogMapper().mapFrom(catalog);
		this.specialOffersCollection = new SpecialOfferCatalogMapper().mapFrom(catalog);

		this.specialOfferInAppCollection = new SpecialOfferInAppCatalogMapper().mapFrom(catalog);

		this.cardCombinationStrategiesCollection = new EntryContainerResponse<>(
				catalog.getCardCombinationStrategiesCollection().getEntries().stream().map(CardCombinationStrategyDefinitionResponse::new)
						.collect(Collectors.toList()));

		this.cardDefenseStrategiesCollection = new EntryContainerResponse<>(
				catalog.getCardDefenseStrategiesCollection().getEntries().stream().map(CardDefenseStrategyDefinitionResponse::new)
						.collect(Collectors.toList()));

		this.botsLevelParametersCollection = new EntryContainerResponse<>(
				catalog.getBotsLevelParametersCollection().getEntries().stream().map(BotLevelParameterDefinitionResponse::new)
						.collect(Collectors.toList()));

		this.captainsCollection = new CaptainCatalogMapper().mapFrom(catalog);

		this.captainsSkinsCollection = new CaptainSkinCatalogMapper().mapFrom(catalog);

		this.featuresByPlayerLvlCollection = new FeaturesByPlayerLvlCatalogMapper().mapFrom(catalog);

		mmrDeltaPercentageCollection = new EntryContainerResponse<>(
				catalog.getDeltaMmrPercentageDefinitionsCollection().getEntries().stream().map(DeltaMmrPercentageDefinitionRepresentation::new)
						.collect(Collectors.toList()));

		dailyQuestDefinitionCollection = new QuestCatalogMapper().mapFrom(catalog);

		videoRewardsCollection = new VideoRewardCatalogMapper().mapFrom(catalog);

		botsChancesByMmrCollection = new BotsChancesByMmrCatalogMapper().mapFrom(catalog);

		achievementsCollection = new AchievementCatalogMapper().mapFrom(catalog);

		leagueRewardsCollection = new EntryContainerResponse<>(catalog.getLeagueRewardsDefinitionCollection().getEntries().stream()
				.map(leagueRewardsDefinition -> new LeagueRewardsDefinitionResponse(leagueRewardsDefinition.getId(),
						leagueRewardsDefinition.getMmr(), leagueRewardsDefinition.getReward()))
				.collect(Collectors.toList()));

	}

	public String getId() {
		return id;
	}

	public EntryContainerResponse<MapDefinitionResponse> getMaps() {
		return maps;
	}

	public EntryContainerResponse<UnitDefinitionResponse> getUnits() {
		return units;
	}

	public EntryContainerResponse<UnitLevelDefinitionResponse> getUnitsLevels() {
		return unitsLevels;
	}

	public EntryContainerResponse<CardDefinitionResponse> getCards() {
		return cards;
	}

	public EntryContainerResponse<CardLevelDefinitionResponse> getCardsLevels() {
		return cardsLevels;
	}

	public EntryContainerResponse<CardParameterLevelResponse> getCardsParameterLevels() {
		return cardsParameterLevels;
	}

	public EntryContainerResponse<TupleResponse> getConstants() {
		return constants;
	}

	public EntryContainerResponse<ChestDefinitionResponse> getChests() {
		return chests;
	}

	public EntryContainerResponse<ChestListEntryResponse> getChestsLists() {
		return chestsLists;
	}

	public EntryContainerResponse<PlayerLevelResponse> getPlayerLevels() {
		return playerLevels;
	}

	public EntryContainerResponse<ShopItemEntryResponse> getShopItems() {
		return shopItems;
	}

	public EntryContainerResponse<ShopCardDefinitionResponse> getShopCards() {
		return shopCards;
	}

	public EntryContainerResponse<GemsCycleEntryResponse> getGemsCycle() {
		return gemsCycle;
	}

	public EntryContainerResponse<PowerUpDefinitionResponse> getPowerUps() {
		return powerUps;
	}

	public EntryContainerResponse<PowerUpLevelDefinitionResponse> getPowerUpsLevels() {
		return powerUpsLevels;
	}

	public EntryContainerResponse<ShopInAppDefinitionResponse> getShopInApp() {
		return shopInApp;
	}

	public EntryContainerResponse<ChestChancesDefinitionResponse> getChestChances() {
		return chestChances;
	}

	public EntryContainerResponse<CardsDropRateResponse> getCardsDropRate() {
		return cardsDropRate;
	}

	public EntryContainerResponse<BotDefinitionResponse> getBots() {
		return bots;
	}

	public EntryContainerResponse<BotNameDefinitionResponse> getBotsNames() {
		return botsNames;
	}

	public EntryContainerResponse<TutorialProgressCatalogResponse> getTutorialProgress() {
		return tutorialProgress;
	}

	public EntryContainerResponse<QuestDefinitionResponse> getQuestCollection() {
		return questCollection;
	}

	public EntryContainerResponse<QuestCycleResponse> getQuestCycleCollection() {
		return questCycleCollection;
	}

	public EntryContainerResponse<ABTesterResponse> getaBTester() {
		return aBTester;
	}

	public EntryContainerResponse<ShopItemsMapConfigEntryResponse> getShopItemsMapConfiguration() {
		return shopItemsMapConfiguration;
	}

	public EntryContainerResponse<QuestChanceResponse> getQuestChanceCollection() {
		return questChanceCollection;
	}

	public EntryContainerResponse<SpecialOfferDefinitionResponse> getSpecialOffersCollection() {
		return specialOffersCollection;
	}

	public EntryContainerResponse<SpecialOffersItemResponse> getSpecialOffersItemsCollection() {
		return specialOffersItemsCollection;
	}

	public EntryContainerResponse<CardCombinationStrategyDefinitionResponse> getCardCombinationStrategiesCollection() {
		return cardCombinationStrategiesCollection;
	}

	public EntryContainerResponse<CardDefenseStrategyDefinitionResponse> getCardDefenseStrategiesCollection() {
		return cardDefenseStrategiesCollection;
	}

	public EntryContainerResponse<BotLevelParameterDefinitionResponse> getBotsLevelParametersCollection() {
		return botsLevelParametersCollection;
	}

	public EntryContainerResponse<SpecialOfferInAppDefinitionResponse> getSpecialOfferInAppCollection() {
		return specialOfferInAppCollection;
	}

	public EntryContainerResponse<CaptainDefinitionResponse> getCaptainsCollection() {
		return captainsCollection;
	}

	public EntryContainerResponse<FeaturesByPlayerLvlDefinitionResponse> getFeaturesByPlayerLvlCollection() {
		return featuresByPlayerLvlCollection;
	}

	public EntryContainerResponse<CaptainSkinDefinitionResponse> getCaptainsSkinsCollection() {
		return captainsSkinsCollection;
	}

	public EntryContainerResponse<DeltaMmrPercentageDefinitionRepresentation> getMmrDeltaPercentageCollection() {
		return mmrDeltaPercentageCollection;
	}

	public EntryContainerResponse<QuestDefinitionResponse> getDailyQuestDefinitionCollection() {
		return dailyQuestDefinitionCollection;
	}

	public EntryContainerResponse<VideoRewardDefinitionResponse> getVideoRewardsCollection() {
		return videoRewardsCollection;
	}

	public EntryContainerResponse<BotsChancesByMmrDefinitionResponse> getBotsChancesByMmrCollection() {
		return botsChancesByMmrCollection;
	}

	public EntryContainerResponse<AchievementDefinitionResponse> getAchievementsCollection() {
		return achievementsCollection;
	}

	public EntryContainerResponse<LeagueRewardsDefinitionResponse> getLeagueRewardsCollection() {
		return leagueRewardsCollection;
	}

}
