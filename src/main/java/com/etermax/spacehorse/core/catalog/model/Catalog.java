package com.etermax.spacehorse.core.catalog.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.battle.model.DeltaMmrPercentage;
import com.etermax.spacehorse.core.battle.model.DeltaMmrPercentageSelector;
import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.ads.videorewards.VideoRewardDefinition;
import com.etermax.spacehorse.core.catalog.model.bot.BotsChancesByMmrCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.bot.BotsChancesByMmrDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainDefinition;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;
import com.etermax.spacehorse.core.catalog.model.league.LeagueRewardsDefinition;
import com.etermax.spacehorse.core.catalog.model.quest.QuestCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.specialoffer.CardCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferInAppCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferInAppDefinition;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferItemDefinition;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferItemsCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.tutorialprogress.TutorialProgressEntry;
import com.etermax.spacehorse.core.catalog.model.unlock.FeaturesByPlayerLvlCatalogMapper;
import com.etermax.spacehorse.core.catalog.model.unlock.FeaturesByPlayerLvlDefinition;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogVersion;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogVersionsValidator;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.ChestListEntryResponse;
import com.etermax.spacehorse.core.catalog.resource.response.GemsCycleEntryResponse;

public class Catalog {

	private final CatalogEntriesCollection<MapDefinition> mapsCollection;
	private final CatalogEntriesCollection<UnitDefinition> unitDefinitionsCollection;
	private final CatalogEntriesCollection<UnitLevelDefinition> unitLevelDefinitionsCollection;
	private final CatalogEntriesCollection<CardDefinition> cardDefinitionsCollection;
	private final CatalogEntriesCollection<CardLevelDefinition> cardLevelDefinitionsCollection;
	private final CatalogEntriesCollection<ChestDefinition> chestDefinitionsCollection;
	private final CatalogEntriesCollection<ChestList> chestsListsCollection;
	private final CatalogEntriesCollection<ShopItemDefinition> shopItemsCollection;
	private final CatalogEntriesCollection<ShopCardDefinition> shopCardsCollection;
	private final CatalogEntriesCollection<GemsCycle> gemsCycleCollection;
	private final ShopInAppCollection shopInAppCollection;
	private final PlayerLevelsCollection playerLevelsCollection;
	private final CardParameterLevelsCollection cardParameterLevelsCollection;
	private final GameConstants gameConstants;
	private final CatalogEntriesCollection<PowerUpDefinition> powerUpDefinitionsCollection;
	private final CatalogEntriesCollection<PowerUpLevelDefinition> powerUpLevelDefinitionsCollection;
	private final CatalogEntriesCollection<ChestChancesDefinition> chestChancesDefinitionsCollection;
	private final CatalogEntriesCollection<CardsDropRate> cardsDropRateCollection;
	private final CatalogEntriesCollection<BotDefinition> botsDefinitionsCollection;
	private final CatalogEntriesCollection<BotNameDefinition> botsNamesDefinitionsCollection;
	private final CatalogEntriesCollection<TutorialProgressEntry> tutorialProgressCollection;
	private final CatalogEntriesCollection<QuestDefinition> questCollection;
	private final CatalogEntriesCollection<QuestCycleList> questCycleListCollection;
	private final CatalogEntriesCollection<ABTesterEntry> abTesterCollection;
	private final CatalogEntriesCollection<ShopItemsMapConfigEntry> shopItemsMapConfigCollection;
	private final CatalogEntriesCollection<QuestChancesList> questChancesListCollection;
	private final CatalogEntriesCollection<SpecialOfferDefinition> specialOfferDefinitionsCollection;
	private final CatalogEntriesCollection<SpecialOfferItemDefinition> specialOfferItemsDefinitionsCollection;
	private final CatalogEntriesCollection<CardCombinationStrategyDefinition> cardCombinationStrategiesCollection;
	private final CatalogEntriesCollection<CardDefenseStrategyDefinition> cardDefenseStrategiesCollection;
	private final CatalogEntriesCollection<BotLevelParameterDefinition> botsLevelParametersCollection;
	private final CatalogEntriesCollection<SpecialOfferInAppDefinition> specialOfferInAppDefinitionsCollection;
	private final CatalogEntriesCollection<CaptainDefinition> captainDefinitionsCollection;
	private final CatalogEntriesCollection<FeaturesByPlayerLvlDefinition> featureByPlayerLvlDefinitionCollection;
	private final CatalogEntriesCollection<CaptainSkinDefinition> captainSkinDefinitionsCollection;
	private final CatalogEntriesCollection<DeltaMmrPercentageDefinition> deltaMmrPercentageDefinitionsCollection;
	private final DeltaMmrPercentageSelector deltaMmrPercentageSelector;
	private final CatalogEntriesCollection<QuestDefinition> dailyQuestDefinitionsCollection;
	private final CatalogEntriesCollection<VideoRewardDefinition> videoRewardDefinitionsCollection;
	private final CatalogEntriesCollection<BotsChancesByMmrDefinition> botsChancesDefinitionsCollection;
	private final CatalogEntriesCollection<AchievementDefinition> achievementsDefinitionsCollection;
	private String catalogId;
	private boolean isActive;
	private CatalogVersion catalogVersion;
	private CatalogEntriesCollection<LeagueRewardsDefinition> leagueRewardsDefinitionCollection;

	public Catalog(CatalogResponse catalogResponse) {
		if (catalogResponse.getId() != null && !catalogResponse.getId().isEmpty()) {
			this.setCatalogId(catalogResponse.getId());
		}
		catalogVersion = CatalogVersion.latest();
		mapsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getMaps().getEntries().stream().map(MapDefinition::new).collect(Collectors.toList()));
		unitDefinitionsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getUnits().getEntries().stream().map(UnitDefinition::new).collect(Collectors.toList()));
		unitLevelDefinitionsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getUnitsLevels().getEntries().stream().map(UnitLevelDefinition::new).collect(Collectors.toList()));
		cardDefinitionsCollection = new CatalogEntriesCollection<>(new CardCatalogMapper().mapFrom(catalogResponse));
		cardLevelDefinitionsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getCardsLevels().getEntries().stream().map(CardLevelDefinition::new).collect(Collectors.toList()));
		cardParameterLevelsCollection = new CardParameterLevelsCollection(
				catalogResponse.getCardsParameterLevels().getEntries().stream().map(CardParameterLevel::new).collect(Collectors.toList()));
		chestDefinitionsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getChests().getEntries().stream().map(ChestDefinition::new).collect(Collectors.toList()));
		playerLevelsCollection = new PlayerLevelsCollection(
				catalogResponse.getPlayerLevels().getEntries().stream().map(PlayerLevel::new).collect(Collectors.toList()));
		gameConstants = getGameConstantsFromResponse(catalogResponse);
		chestsListsCollection = getChestListsCollectionFromResponse(catalogResponse);
		gemsCycleCollection = getGemsCycleCollectionFromResponse(catalogResponse);
		shopItemsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getShopItems().getEntries().stream().map(ShopItemDefinition::new).collect(Collectors.toList()));
		shopCardsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getShopCards().getEntries().stream().map(ShopCardDefinition::new).collect(Collectors.toList()));
		shopInAppCollection = new ShopInAppCollection(
				catalogResponse.getShopInApp().getEntries().stream().map(ShopInAppDefinition::new).collect(Collectors.toList()));
		powerUpDefinitionsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getPowerUps().getEntries().stream().map(PowerUpDefinition::new).collect(Collectors.toList()));
		powerUpLevelDefinitionsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getPowerUpsLevels().getEntries().stream().map(PowerUpLevelDefinition::new).collect(Collectors.toList()));
		chestChancesDefinitionsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getChestChances().getEntries().stream().map(ChestChancesDefinition::new).collect(Collectors.toList()));
		cardsDropRateCollection = new CatalogEntriesCollection<>(
				catalogResponse.getCardsDropRate().getEntries().stream().map(CardsDropRate::new).collect(Collectors.toList()));
		botsDefinitionsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getBots().getEntries().stream().map(BotDefinition::new).collect(Collectors.toList()));
		botsNamesDefinitionsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getBotsNames().getEntries().stream().map(BotNameDefinition::new).collect(Collectors.toList()));
		tutorialProgressCollection = new CatalogEntriesCollection<>(
				catalogResponse.getTutorialProgress().getEntries().stream().map(TutorialProgressEntry::new).collect(Collectors.toList()));
		questCollection = new CatalogEntriesCollection<>(
				catalogResponse.getQuestCollection().getEntries().stream().map(QuestDefinition::new).collect(Collectors.toList()));
		questCycleListCollection = getQuestCycleCollectionFromResponse(catalogResponse);
		abTesterCollection = new CatalogEntriesCollection<>(
				catalogResponse.getaBTester().getEntries().stream().map(ABTesterEntry::new).collect(Collectors.toList()));
		shopItemsMapConfigCollection = new CatalogEntriesCollection<>(
				catalogResponse.getShopItemsMapConfiguration().getEntries().stream().map(ShopItemsMapConfigEntry::new).collect(Collectors.toList()));
		questChancesListCollection = getQuestChancesListCollectionFromResponse(catalogResponse);

		specialOfferDefinitionsCollection = new CatalogEntriesCollection<>(new SpecialOfferCatalogMapper().mapFrom(catalogResponse));
		specialOfferItemsDefinitionsCollection = new CatalogEntriesCollection<>(new SpecialOfferItemsCatalogMapper().mapFrom(catalogResponse));

		cardCombinationStrategiesCollection = new CatalogEntriesCollection<>(
				catalogResponse.getCardCombinationStrategiesCollection().getEntries().stream().map(CardCombinationStrategyDefinition::new)
						.collect(Collectors.toList()));

		cardDefenseStrategiesCollection = new CatalogEntriesCollection<>(
				catalogResponse.getCardDefenseStrategiesCollection().getEntries().stream().map(CardDefenseStrategyDefinition::new)
						.collect(Collectors.toList()));

		botsLevelParametersCollection = new CatalogEntriesCollection<>(
				catalogResponse.getBotsLevelParametersCollection().getEntries().stream().map(BotLevelParameterDefinition::new)
						.collect(Collectors.toList()));

		specialOfferInAppDefinitionsCollection = new CatalogEntriesCollection<>(new SpecialOfferInAppCatalogMapper().mapFrom(catalogResponse));

		captainDefinitionsCollection = new CatalogEntriesCollection<>(new CaptainCatalogMapper().mapFrom(catalogResponse));

		featureByPlayerLvlDefinitionCollection = new CatalogEntriesCollection<>(new FeaturesByPlayerLvlCatalogMapper().mapFrom(catalogResponse));

		captainSkinDefinitionsCollection = new CatalogEntriesCollection<>(new CaptainSkinCatalogMapper().mapFrom(catalogResponse));

		deltaMmrPercentageDefinitionsCollection = new CatalogEntriesCollection<>(
				catalogResponse.getMmrDeltaPercentageCollection().getEntries().stream().map(DeltaMmrPercentageDefinition::new)
						.collect(Collectors.toList()));

		List<DeltaMmrPercentage> deltaMmrPercentages = deltaMmrPercentageDefinitionsCollection.getEntries().stream().map(DeltaMmrPercentage::new)
				.collect(Collectors.toList());
		deltaMmrPercentageSelector = new DeltaMmrPercentageSelector(deltaMmrPercentages);

		dailyQuestDefinitionsCollection = new CatalogEntriesCollection<>(new QuestCatalogMapper().mapFrom(catalogResponse));

		videoRewardDefinitionsCollection = new CatalogEntriesCollection<>(new VideoRewardCatalogMapper().mapFrom(catalogResponse));

		botsChancesDefinitionsCollection = new CatalogEntriesCollection<>(new BotsChancesByMmrCatalogMapper().mapFrom(catalogResponse));

		achievementsDefinitionsCollection = new CatalogEntriesCollection<>(new AchievementCatalogMapper().mapFrom(catalogResponse));

		leagueRewardsDefinitionCollection = new CatalogEntriesCollection<>(catalogResponse.getLeagueRewardsCollection().getEntries().stream()
				.map(leagueRewardDefinitionResponse -> new LeagueRewardsDefinition(leagueRewardDefinitionResponse.getId(),
						leagueRewardDefinitionResponse.getMmr(), leagueRewardDefinitionResponse
						.getReward())).collect(Collectors
						.toList()));

		validate();
	}

	private GameConstants getGameConstantsFromResponse(CatalogResponse catalogResponse) {
		List<Tuple> constants = catalogResponse.getConstants().getEntries().stream().map(t -> new Tuple(t.getId(), t.getValue()))
				.collect(Collectors.toList());
		return new GameConstants(constants);
	}

	private CatalogEntriesCollection<ChestList> getChestListsCollectionFromResponse(CatalogResponse catalogResponse) {
		Map<String, List<ChestListEntryResponse>> tmpChestListEntriesByChestId = new HashMap<>();
		catalogResponse.getChestsLists().getEntries().forEach(chestListEntryResponse -> {
			if (!tmpChestListEntriesByChestId.containsKey(chestListEntryResponse.getListId())) {
				tmpChestListEntriesByChestId.put(chestListEntryResponse.getListId(), new ArrayList<>());
			}
			tmpChestListEntriesByChestId.get(chestListEntryResponse.getListId()).add(chestListEntryResponse);
		});
		List<ChestList> chestsLists = new ArrayList<>();
		tmpChestListEntriesByChestId.entrySet().forEach(keyValue -> chestsLists
				.add(new ChestList(keyValue.getKey(), keyValue.getValue().stream().map(ChestListEntry::new).collect(Collectors.toList()))));
		return new CatalogEntriesCollection<>(chestsLists);
	}

	private CatalogEntriesCollection<QuestChancesList> getQuestChancesListCollectionFromResponse(CatalogResponse catalogResponse) {
		Map<Integer, Map<String, List<QuestDefinitionWithChance>>> tmpQuestChanceEntriesByMapNumberAndDifficulty = new HashMap<>();

		catalogResponse.getQuestChanceCollection().getEntries().forEach(questChanceResponse -> {
			int mapNumber = questChanceResponse.getMapNumber();
			String difficulty = questChanceResponse.getDifficulty();
			QuestDefinition questDefinition = this.questCollection.findById(questChanceResponse.getQuestDefinitionId()).orElseThrow(
					() -> new CatalogException("Invalid questDefinitionId " + questChanceResponse.getQuestDefinitionId() + " in QuestChances"));

			if (!tmpQuestChanceEntriesByMapNumberAndDifficulty.containsKey(mapNumber)) {
				tmpQuestChanceEntriesByMapNumberAndDifficulty.put(mapNumber, new HashMap<>());
			}

			if (!tmpQuestChanceEntriesByMapNumberAndDifficulty.get(mapNumber).containsKey(difficulty)) {
				tmpQuestChanceEntriesByMapNumberAndDifficulty.get(mapNumber).put(difficulty, new ArrayList<>());
			}

			tmpQuestChanceEntriesByMapNumberAndDifficulty.get(mapNumber).get(difficulty)
					.add(new QuestDefinitionWithChance(questChanceResponse.getId(), questDefinition, questChanceResponse.getChance()));
		});

		List<QuestChancesList> questChancesList = new ArrayList<>();

		tmpQuestChanceEntriesByMapNumberAndDifficulty.entrySet().forEach(mapNumberToEntries -> {
			int mapNumber = mapNumberToEntries.getKey();
			Map<String, List<QuestDefinitionWithChance>> questChancesByDifficulty = mapNumberToEntries.getValue();

			questChancesByDifficulty.entrySet().forEach(difficultyToEntries -> {
				String difficulty = difficultyToEntries.getKey();
				List<QuestDefinitionWithChance> entries = difficultyToEntries.getValue();

				questChancesList.add(new QuestChancesList(difficulty, mapNumber, entries));
			});
		});

		return new CatalogEntriesCollection<>(questChancesList);
	}

	private CatalogEntriesCollection<GemsCycle> getGemsCycleCollectionFromResponse(CatalogResponse catalogResponse) {
		Map<String, List<GemsCycleEntryResponse>> tmpGemsCycleEntriesByChestId = new HashMap<>();
		catalogResponse.getGemsCycle().getEntries().forEach(gemsCycleEntryResponse -> {
			if (!tmpGemsCycleEntriesByChestId.containsKey(gemsCycleEntryResponse.getListId())) {
				tmpGemsCycleEntriesByChestId.put(gemsCycleEntryResponse.getListId(), new ArrayList<>());
			}
			tmpGemsCycleEntriesByChestId.get(gemsCycleEntryResponse.getListId()).add(gemsCycleEntryResponse);
		});
		List<GemsCycle> gemsCycles = new ArrayList<>();
		tmpGemsCycleEntriesByChestId.entrySet().forEach(keyValue -> gemsCycles
				.add(new GemsCycle(keyValue.getKey(), keyValue.getValue().stream().map(GemsCycleEntry::new).collect(Collectors.toList()))));
		return new CatalogEntriesCollection<>(gemsCycles);
	}

	private CatalogEntriesCollection<QuestCycleList> getQuestCycleCollectionFromResponse(CatalogResponse catalogResponse) {

		Map<String, Map<String, List<QuestCycleEntry>>> tmpQuestCycleEntriesByListIdAndDifficulty = new HashMap<>();

		catalogResponse.getQuestCycleCollection().getEntries().forEach(questCycleResponse -> {
			String listId = questCycleResponse.getListId();
			String difficulty = questCycleResponse.getDifficulty();
			QuestDefinition questDefinition = this.questCollection.findById(questCycleResponse.getQuestDefinitionId()).orElseThrow(
					() -> new CatalogException("Invalid questDefinitionId " + questCycleResponse.getQuestDefinitionId() + " in QuestCycle"));

			if (!tmpQuestCycleEntriesByListIdAndDifficulty.containsKey(listId)) {
				tmpQuestCycleEntriesByListIdAndDifficulty.put(listId, new HashMap<>());
			}

			if (!tmpQuestCycleEntriesByListIdAndDifficulty.get(listId).containsKey(difficulty)) {
				tmpQuestCycleEntriesByListIdAndDifficulty.get(listId).put(difficulty, new ArrayList<>());
			}

			tmpQuestCycleEntriesByListIdAndDifficulty.get(listId).get(difficulty)
					.add(new QuestCycleEntry(questCycleResponse.getId(), questCycleResponse.getSequence(), questDefinition));
		});

		List<QuestCycleList> questCycleLists = new ArrayList<>();

		tmpQuestCycleEntriesByListIdAndDifficulty.entrySet().forEach(listIdToEntries -> {
			String listId = listIdToEntries.getKey();
			Map<String, List<QuestCycleEntry>> questCycleEntriesByDifficulty = listIdToEntries.getValue();

			questCycleEntriesByDifficulty.entrySet().forEach(difficultyToEntries -> {
				String difficulty = difficultyToEntries.getKey();
				List<QuestCycleEntry> entries = difficultyToEntries.getValue();

				questCycleLists.add(new QuestCycleList(listId, difficulty, entries));
			});
		});

		return new CatalogEntriesCollection<>(questCycleLists);
	}

	public void validate() {
		CatalogVersionsValidator catalogVersionsValidator = new CatalogVersionsValidator(this);
		catalogVersionsValidator.validate(catalogVersion, this);
	}

	public String getCatalogId() {
		return this.catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public CatalogVersion getCatalogVersion() {
		return catalogVersion;
	}

	public void setCatalogVersion(CatalogVersion catalogVersion) {
		this.catalogVersion = catalogVersion;
	}

	public GameConstants getGameConstants() {
		return gameConstants;
	}

	public CatalogEntriesCollection<MapDefinition> getMapsCollection() {
		return mapsCollection;
	}

	public CatalogEntriesCollection<UnitDefinition> getUnitDefinitionsCollection() {
		return unitDefinitionsCollection;
	}

	public CatalogEntriesCollection<UnitLevelDefinition> getUnitLevelDefinitionsCollection() {
		return unitLevelDefinitionsCollection;
	}

	public CatalogEntriesCollection<CardDefinition> getCardDefinitionsCollection() {
		return cardDefinitionsCollection;
	}

	public CatalogEntriesCollection<CardLevelDefinition> getCardLevelDefinitionsCollection() {
		return cardLevelDefinitionsCollection;
	}

	public CardParameterLevelsCollection getCardParameterLevelsCollection() {
		return cardParameterLevelsCollection;
	}

	public CatalogEntriesCollection<ChestDefinition> getChestDefinitionsCollection() {
		return chestDefinitionsCollection;
	}

	public CatalogEntriesCollection<ChestList> getChestsListsCollection() {
		return chestsListsCollection;
	}

	public CatalogEntriesCollection<GemsCycle> getGemsCycleCollection() {
		return gemsCycleCollection;
	}

	public PlayerLevelsCollection getPlayerLevelsCollection() {
		return playerLevelsCollection;
	}

	public CatalogEntriesCollection<ShopItemDefinition> getShopItemsCollection() {
		return shopItemsCollection;
	}

	public CatalogEntriesCollection<ShopCardDefinition> getShopCardsCollection() {
		return shopCardsCollection;
	}

	public ShopInAppCollection getShopInAppCollection() {
		return shopInAppCollection;
	}

	public CatalogEntriesCollection<PowerUpDefinition> getPowerUpDefinitionsCollection() {
		return powerUpDefinitionsCollection;
	}

	public CatalogEntriesCollection<PowerUpLevelDefinition> getPowerUpLevelDefinitionsCollection() {
		return powerUpLevelDefinitionsCollection;
	}

	public CatalogEntriesCollection<ChestChancesDefinition> getChestChancesDefinitionsCollection() {
		return chestChancesDefinitionsCollection;
	}

	public CatalogEntriesCollection<CardsDropRate> getCardsDropRateCollection() {
		return cardsDropRateCollection;
	}

	public CatalogEntriesCollection<BotDefinition> getBotsDefinitionsCollection() {
		return botsDefinitionsCollection;
	}

	public CatalogEntriesCollection<TutorialProgressEntry> getTutorialProgressCollection() {
		return tutorialProgressCollection;
	}

	public CatalogEntriesCollection<BotNameDefinition> getBotsNamesDefinitionsCollection() {
		return botsNamesDefinitionsCollection;
	}

	public CatalogEntriesCollection<QuestDefinition> getQuestCollection() {
		return questCollection;
	}

	public CatalogEntriesCollection<QuestCycleList> getQuestCycleListCollection() {
		return questCycleListCollection;
	}

	public CatalogEntriesCollection<ABTesterEntry> getAbTesterCollection() {
		return abTesterCollection;
	}

	public CatalogEntriesCollection<ShopItemsMapConfigEntry> getShopItemsMapConfigCollection() {
		return shopItemsMapConfigCollection;
	}

	public CatalogEntriesCollection<QuestChancesList> getQuestChancesListCollection() {
		return questChancesListCollection;
	}

	public CatalogEntriesCollection<SpecialOfferDefinition> getSpecialOfferDefinitionsCollection() {
		return specialOfferDefinitionsCollection;
	}

	public CatalogEntriesCollection<SpecialOfferItemDefinition> getSpecialOfferItemsDefinitionsCollection() {
		return specialOfferItemsDefinitionsCollection;
	}

	public CatalogEntriesCollection<CardCombinationStrategyDefinition> getCardCombinationStrategiesCollection() {
		return cardCombinationStrategiesCollection;
	}

	public CatalogEntriesCollection<CardDefenseStrategyDefinition> getCardDefenseStrategiesCollection() {
		return cardDefenseStrategiesCollection;
	}

	public CatalogEntriesCollection<BotLevelParameterDefinition> getBotsLevelParametersCollection() {
		return botsLevelParametersCollection;
	}

	public CatalogEntriesCollection<SpecialOfferInAppDefinition> getSpecialOfferInAppDefinitionsCollection() {
		return specialOfferInAppDefinitionsCollection;
	}

	public CatalogEntriesCollection<CaptainDefinition> getCaptainDefinitionsCollection() {
		return captainDefinitionsCollection;
	}

	public CatalogEntriesCollection<FeaturesByPlayerLvlDefinition> getFeatureByPlayerLvlDefinitionCollection() {
		return featureByPlayerLvlDefinitionCollection;
	}

	public CatalogEntriesCollection<CaptainSkinDefinition> getCaptainSkinDefinitionsCollection() {
		return captainSkinDefinitionsCollection;
	}

	public CatalogEntriesCollection<VideoRewardDefinition> getVideoRewardDefinitionsCollection() {
		return videoRewardDefinitionsCollection;
	}

	public CatalogEntriesCollection<BotsChancesByMmrDefinition> getBotsChancesDefinitionsCollection() {
		return botsChancesDefinitionsCollection;
	}

	public CatalogEntriesCollection<AchievementDefinition> getAchievementsDefinitionsCollection() {
		return achievementsDefinitionsCollection;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Catalog catalog = (Catalog) o;
		return catalogId.equals(catalog.catalogId);
	}

	@Override
	public int hashCode() {
		return catalogId.hashCode();
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean active) {
		this.isActive = active;
	}

	public CatalogEntriesCollection<DeltaMmrPercentageDefinition> getDeltaMmrPercentageDefinitionsCollection() {
		return deltaMmrPercentageDefinitionsCollection;
	}

	public DeltaMmrPercentageSelector getDeltaMmrPercentageSelector() {
		return deltaMmrPercentageSelector;
	}

	public CatalogEntriesCollection<QuestDefinition> getDailyQuestCollection() {
		return dailyQuestDefinitionsCollection;
	}

	public CatalogEntriesCollection<LeagueRewardsDefinition> getLeagueRewardsDefinitionCollection() {
		return leagueRewardsDefinitionCollection;
	}
}

