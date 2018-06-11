package com.etermax.spacehorse.core.achievements.repository;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Map;

import org.mockito.Mockito;

import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionFactory;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.player.model.Player;
import com.google.common.collect.Maps;

public class InMemoryAchievementCollectionRepository implements AchievementCollectionRepository {

	private Map<String, AchievementCollection> achievementsByPlayerIds = Maps.newHashMap();

	@Override
	public AchievementCollection findOrDefaultBy(Player player) {
		if (achievementsByPlayerIds.containsKey(player.getUserId())) {
			return findAchievementCollection(player);
		}
		AchievementCollection achievementCollection = createDummyAchievementCollection(player);
		addOrUpdate(player.getUserId(), achievementCollection);
		return findAchievementCollection(player);
	}

	@Override
	public void addOrUpdate(String userId, AchievementCollection achievementCollection) {
		achievementsByPlayerIds.put(userId, achievementCollection);
	}

	public AchievementCollection createDummyAchievementCollection(Player player) {
		return new AchievementCollectionFactory().create(player.getUserId(), aMockCatalog());
	}

	private Catalog aMockCatalog() {
		Catalog catalog = Mockito.mock(Catalog.class);
		Mockito.when(catalog.getAchievementsDefinitionsCollection()).thenReturn(new CatalogEntriesCollection<>(newArrayList()));
		return catalog;
	}

	private AchievementCollection findAchievementCollection(Player player) {
		return achievementsByPlayerIds.get(player.getUserId());
	}
}
