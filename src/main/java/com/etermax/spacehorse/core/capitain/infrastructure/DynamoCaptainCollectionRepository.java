package com.etermax.spacehorse.core.capitain.infrastructure;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.capitain.model.CaptainCollectionRepository;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollectionFactory;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;

public class DynamoCaptainCollectionRepository implements CaptainCollectionRepository {

	private final DynamoDao<DynamoCaptainsCollection> dynamoDao;
	private final CatalogRepository catalogRepository;
	private final CaptainsCollectionFactory captainsCollectionFactory;

	public DynamoCaptainCollectionRepository(DynamoDao dynamoDao, CatalogRepository catalogRepository) {
		this.dynamoDao = dynamoDao;
		this.catalogRepository = catalogRepository;
		this.captainsCollectionFactory = new CaptainsCollectionFactory();
	}

	@Override
	public CaptainsCollection findOrDefaultBy(Player player) {
		DynamoCaptainsCollection dynamoCaptainsCollection = findCaptainsCollectionsBy(player);
		Catalog catalog = getActiveCatalog(player.getAbTag());
		if (notExists(dynamoCaptainsCollection)) {
			CaptainsCollection captainsCollection = captainsCollectionFactory.create(player.getUserId(), catalog);
			addOrUpdate(captainsCollection);
			return captainsCollection;
		}
		return DynamoCaptainsCollection.mapToCaptainsCollection(dynamoCaptainsCollection, catalog, player.getUserId());
	}

	@Override
	public void addOrUpdate(CaptainsCollection captainsCollection) {
		DynamoCaptainsCollection dynamoCaptainsCollection = DynamoCaptainsCollection
				.mapToDynamoCaptainsCollection(captainsCollection.getUserId(), captainsCollection);
		dynamoDao.add(dynamoCaptainsCollection);
	}

	private DynamoCaptainsCollection findCaptainsCollectionsBy(Player player) {
		return dynamoDao.find(DynamoCaptainsCollection.class, player.getUserId());
	}

	private Catalog getActiveCatalog(ABTag abTag) {
		return catalogRepository.getActiveCatalogWithTag(abTag);
	}

	private boolean notExists(DynamoCaptainsCollection dynamoCaptainsCollection) {
		return dynamoCaptainsCollection == null;
	}
}
