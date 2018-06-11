package com.etermax.spacehorse.core.catalog.model.versions;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.versions.validation.CardParameterLevelsValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.CardsConsistencyValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.CardsDropRateConsistencyValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.CatalogEntriesCollectionValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.CatalogEntriesWithChestValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.CatalogEntriesWithMapsValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.GameConstantsValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.LeagueValidator;
import com.etermax.spacehorse.core.catalog.model.versions.validation.MothershipsAndCompanionshipsMoveSpeedConsistencyValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.PlayerLevelsConsistencyValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.PowerUpLevelConsistencyValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.QuestChancesValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.ShopInAppValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.SpecialOfferInAppValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.UnitLevelConsistencyValidation;
import com.etermax.spacehorse.core.catalog.model.versions.validation.VideoRewardMapsValidator;
import com.etermax.spacehorse.core.catalog.model.versions.validation.VideoRewardPlacementsValidator;

public class CatalogVersionsValidator {

	private CatalogValidator v1;

	public CatalogVersionsValidator(Catalog catalog) {
		this.v1 = buildV1(catalog);
	}

	public boolean validate(CatalogVersion catalogVersion, Catalog catalog) {
		switch (catalogVersion) {
			case V1:
				return this.v1.validate(catalog);
		}
		throw new CatalogException("Catalog version not found");
	}

	private CatalogValidator buildV1(Catalog catalog) {
		CatalogValidator catalogVersion = new CatalogValidator();

		// Maps
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getMapsCollection()));

		// Units
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getUnitDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getUnitLevelDefinitionsCollection()));
		catalogVersion.add(new UnitLevelConsistencyValidation());
		catalogVersion.add(new MothershipsAndCompanionshipsMoveSpeedConsistencyValidation());

		// Cards
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getCardDefinitionsCollection()));
		catalogVersion.add(new CardsConsistencyValidation());
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getCardLevelDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getCardParameterLevelsCollection()));
		catalogVersion.add(new CardParameterLevelsValidation());

		// Chests
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getChestDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getChestsListsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getGemsCycleCollection()));

		// Shop
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getShopItemsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getShopCardsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getShopInAppCollection()));
		catalogVersion.add(new ShopInAppValidation());

		// Player Levels
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getPlayerLevelsCollection()));
		catalogVersion.add(new PlayerLevelsConsistencyValidation());

		// Power Ups
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getPowerUpDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getPowerUpLevelDefinitionsCollection()));
		catalogVersion.add(new PowerUpLevelConsistencyValidation());

		// Chest Chances
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getChestChancesDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesWithMapsValidation(catalog.getChestChancesDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesWithChestValidation(catalog.getChestChancesDefinitionsCollection()));

		// Drop Rate
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getCardsDropRateCollection()));
		catalogVersion.add(new CatalogEntriesWithMapsValidation(catalog.getCardsDropRateCollection()));
		catalogVersion.add(new CardsDropRateConsistencyValidation());

		// Bots
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getBotsDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getBotsNamesDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getCardCombinationStrategiesCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getCardDefenseStrategiesCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getBotsLevelParametersCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getBotsChancesDefinitionsCollection()));

		// Tutorial
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getTutorialProgressCollection()));

		// Quests
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getQuestCollection()));
		catalogVersion.add(new CatalogEntriesWithChestValidation(catalog.getQuestCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getQuestCycleListCollection()));
		catalogVersion.add(new QuestChancesValidation());

		// Game Constants
		catalogVersion.add(new GameConstantsValidation());

		// Special Offers
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getSpecialOfferItemsDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getSpecialOfferDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getSpecialOfferInAppDefinitionsCollection()));
		catalogVersion.add(new SpecialOfferInAppValidation());

		// Captains
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getCaptainDefinitionsCollection()));
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getCaptainSkinDefinitionsCollection()));

		// Available Features By Player Levels
		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getFeatureByPlayerLvlDefinitionCollection()));

		catalogVersion.add(new CatalogEntriesCollectionValidation(catalog.getVideoRewardDefinitionsCollection()));

		// Video Reward
		catalogVersion.add(new VideoRewardPlacementsValidator());
		catalogVersion.add(new VideoRewardMapsValidator());

		//League
		catalogVersion.add(new LeagueValidator());

		return catalogVersion;
	}

}