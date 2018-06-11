package com.etermax.spacehorse.core.achievements.repository;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.achievements.model.AchievementCollection;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionFactory;
import com.etermax.spacehorse.core.achievements.model.AchievementCollectionRepository;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class DynamoAchievementCollectionRepository implements AchievementCollectionRepository {

	private final DynamoDao<DynamoAchievementCollection> dynamoDao;
	private final CatalogRepository catalogRepository;
	private final AchievementCollectionFactory achievementCollectionFactory;

	public DynamoAchievementCollectionRepository(DynamoDao dynamoDao, CatalogRepository catalogRepository) {
		this.dynamoDao = dynamoDao;
		this.catalogRepository = catalogRepository;
		this.achievementCollectionFactory = new AchievementCollectionFactory();
	}

	@Override
	public AchievementCollection findOrDefaultBy(Player player) {
		DynamoAchievementCollection dynamoAchievementCollection = findAchievementCollectionsBy(player);
		Catalog catalog = getActiveCatalog(player.getAbTag());
		if (notExists(dynamoAchievementCollection)) {
			AchievementCollection achievementCollection = achievementCollectionFactory.create(player.getUserId(), catalog);
			addOrUpdate(player.getUserId(), achievementCollection);
			return achievementCollection;
		}
		return DynamoAchievementCollection.mapToAchievementCollection(dynamoAchievementCollection, catalog, player.getUserId());
	}

	@Override
	public void addOrUpdate(String userId, AchievementCollection achievementCollection) {
		DynamoAchievementCollection dynamoCaptainsCollection = DynamoAchievementCollection
				.mapToDynamoAchievementCollection(userId, achievementCollection);
		dynamoDao.add(dynamoCaptainsCollection);
	}

	private DynamoAchievementCollection findAchievementCollectionsBy(Player player) {
		return dynamoDao.find(DynamoAchievementCollection.class, player.getUserId());
	}

	private Catalog getActiveCatalog(ABTag abTag) {
		return catalogRepository.getActiveCatalogWithTag(abTag);
	}

	private boolean notExists(DynamoAchievementCollection dynamoAchievementCollection) {
		return dynamoAchievementCollection == null;
	}

}
