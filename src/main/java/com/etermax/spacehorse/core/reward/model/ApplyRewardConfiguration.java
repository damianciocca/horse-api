package com.etermax.spacehorse.core.reward.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.ChestList;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.catalog.model.achievements.AchievementDefinition;
import com.etermax.spacehorse.core.catalog.model.unlock.FeaturesByPlayerLvlDefinition;
import com.etermax.spacehorse.core.reward.model.unlock.ChestSlotsConfiguration;

public class ApplyRewardConfiguration {

	private final CatalogEntriesCollection<CardDefinition> cardDefinition;
	private final CatalogEntriesCollection<ChestDefinition> chestDifinition;
	private final GameConstants gameConstants;
	private final CatalogEntriesCollection<ChestList> chestList;
	private final ChestSlotsConfiguration chestSlotsConfiguration;
	private final List<AchievementDefinition> achievementDefinitions;

	private ApplyRewardConfiguration(CatalogEntriesCollection<CardDefinition> cardDefinition,
			CatalogEntriesCollection<ChestDefinition> chestDifinition, GameConstants gameConstants, CatalogEntriesCollection<ChestList> chestList,
			ChestSlotsConfiguration chestSlotsConfiguration, List<AchievementDefinition> achievementDefinitions) {
		this.cardDefinition = cardDefinition;
		this.chestDifinition = chestDifinition;
		this.gameConstants = gameConstants;
		this.chestList = chestList;
		this.chestSlotsConfiguration = chestSlotsConfiguration;
		this.achievementDefinitions = achievementDefinitions;
	}

	public static ApplyRewardConfiguration createBy(Catalog catalog) {
		CatalogEntriesCollection<CardDefinition> cardDefinitions = catalog.getCardDefinitionsCollection();
		CatalogEntriesCollection<ChestDefinition> chestDefinition = catalog.getChestDefinitionsCollection();
		GameConstants gameConstants = catalog.getGameConstants();
		CatalogEntriesCollection<ChestList> chestsList = catalog.getChestsListsCollection();
		ChestSlotsConfiguration chestSlotsConfiguration = ChestSlotsConfiguration
				.create(getFeaturesByLvlDefinitions(catalog), getMaxChestSlots(catalog));
		List<AchievementDefinition> achievementDefinitions = catalog.getAchievementsDefinitionsCollection().getEntries();
		return new ApplyRewardConfiguration(cardDefinitions, chestDefinition, gameConstants, chestsList, chestSlotsConfiguration,
				achievementDefinitions);
	}

	private static int getMaxChestSlots(Catalog catalog) {
		return catalog.getGameConstants().getMaxChests();
	}

	private static List<FeaturesByPlayerLvlDefinition> getFeaturesByLvlDefinitions(Catalog catalog) {
		return catalog.getFeatureByPlayerLvlDefinitionCollection().getEntries();
	}

	public CatalogEntriesCollection<CardDefinition> getCardDefinition() {
		return cardDefinition;
	}

	public CatalogEntriesCollection<ChestDefinition> getChestDifinition() {
		return chestDifinition;
	}

	public GameConstants getGameConstants() {
		return gameConstants;
	}

	public CatalogEntriesCollection<ChestList> getChestList() {
		return chestList;
	}

	public ChestSlotsConfiguration getChestSlotsConfiguration() {
		return chestSlotsConfiguration;
	}

	public List<AchievementDefinition> getAchievementDefinitions() {
		return achievementDefinitions;
	}

	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

}