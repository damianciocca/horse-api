package com.etermax.spacehorse.core.catalog.repository.dynamo;

import java.util.List;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.abtest.model.ABTestParser;
import com.etermax.spacehorse.core.catalog.model.ABTesterEntry;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.catalog.repository.DynamoServerSettings;
import com.etermax.spacehorse.core.catalog.repository.ServerSettingsRepository;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogIsActive;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.google.common.collect.Lists;

public class CatalogDynamoRepository implements CatalogRepository {

	private final ABTestParser abTesterParser;
	private final DynamoDao dynamoDao;
	private final ServerSettingsRepository serverSettingsRepository;
	private final ServerTimeProvider serverTimeProvider;

	private CatalogsCache catalogsCache = new CatalogsCache();

	public CatalogDynamoRepository(DynamoDao dao, ServerSettingsRepository serverSettingsRepository, ServerTimeProvider serverTimeProvider) {
		this.dynamoDao = dao;
		this.serverSettingsRepository = serverSettingsRepository;
		this.serverTimeProvider = serverTimeProvider;
		this.abTesterParser = new ABTestParser();
	}

	private Catalog getFromCache(String catalogId) {
		return getWithTagFromCache(ABTag.emptyABTag(), catalogId);
	}

	private DynamoJsonCatalog findRawDynamoCatalogByCatalogId(String catalogId) {
		DynamoJsonCatalog dynamoJsonCatalog = new DynamoJsonCatalog();
		dynamoJsonCatalog.setLastUpdated(Long.valueOf(catalogId));
		return (DynamoJsonCatalog) dynamoDao.find(dynamoJsonCatalog);
	}

	private Catalog applyDeltasToCatalog(String catalogId, String deltas) {
		List<CatalogDelta> catalogDeltas = abTesterParser.getDeltas(deltas);
		DynamoJsonCatalog dynamoCatalog = findRawDynamoCatalogByCatalogId(catalogId);
		DynamoCatalog modifiedDynamoCatalog = abTesterParser.modifyCatalogWithDeltas(catalogId, catalogDeltas, dynamoCatalog);
		modifiedDynamoCatalog.setId(dynamoCatalog.getId());
		modifiedDynamoCatalog.setLastUpdated(Long.valueOf(catalogId));
		return DynamoCatalog.mapToCatalog(modifiedDynamoCatalog);
	}

	@Override
	public List<CatalogIsActive> listLatestCatalogs(int limit) {
		List<CatalogIsActive> catalogs = Lists.newLinkedList();
		QuerySpec querySpec = new QuerySpec() //
				.withConsistentRead(false) //
				.withProjectionExpression(getCatalogProjection()) //
				.withMaxResultSize(limit) //
				.withScanIndexForward(false) //
				.withHashKey(DynamoCatalog.HASH_KEY_NAME, DynamoCatalog.MAIN_CATALOG_ID);
		ItemCollection<QueryOutcome> catalog = dynamoDao.getTable(DynamoCatalog.TABLE_NAME).query(querySpec);
		catalog.forEach(item -> catalogs.add(mapToCatalog(item)));
		return catalogs;
	}

	@Override
	public Catalog getActiveCatalogWithTag(ABTag abTag) {
		DynamoServerSettings serverSettings = serverSettingsRepository.getServerSettings();
		Catalog catalog = find(serverSettings.getCatalogId());
		if (abTag.isEmptyABTag()) {
			return catalog;
		}

		Catalog cachedCatalog = getWithTagFromCache(abTag, catalog.getCatalogId());

		if (cachedCatalog == null) {
			ABTesterEntry abTesterEntry;
			try {
				abTesterEntry = abTesterParser.getAbTesterEntry(catalog, abTag);
				cachedCatalog = applyDeltasToCatalog(serverSettings.getCatalogId(), abTesterEntry.getDeltas());
				addWithTagToCache(abTag, cachedCatalog);
			} catch (TagDoesntExistException e) {
				cachedCatalog = catalog;
			}
		}
		return cachedCatalog;
	}

	@Override
	public Catalog findByIdWithTag(String catalogId, ABTag abTag) {
		Catalog catalog = find(catalogId);
		if (abTag.isEmptyABTag()) {
			return catalog;
		}

		Catalog cachedCatalog = getWithTagFromCache(abTag, catalog.getCatalogId());

		if (cachedCatalog == null) {
			ABTesterEntry abTesterEntry;
			try {
				abTesterEntry = abTesterParser.getAbTesterEntry(catalog, abTag);
				cachedCatalog = applyDeltasToCatalog(catalogId, abTesterEntry.getDeltas());
				addWithTagToCache(abTag, cachedCatalog);
			} catch (TagDoesntExistException e) {
				cachedCatalog = catalog;
			}
		}
		return cachedCatalog;
	}

	private void addWithTagToCache(ABTag abTag, Catalog catalog) {
		catalogsCache.put(abTag, catalog);
	}

	private Catalog getWithTagFromCache(ABTag abTag, String catalogId) {
		return catalogsCache.getActiveCatalogWithTag(catalogId, abTag);
	}

	@Override
	public void add(Catalog catalog) {
		DynamoCatalog dynamoCatalog = new DynamoCatalog(catalog);
		if (catalog.getCatalogId() == null || catalog.getCatalogId().length() == 0) {
			dynamoCatalog.setLastUpdated(System.currentTimeMillis());
		} else {
			dynamoCatalog.setLastUpdated(Long.parseLong(catalog.getCatalogId()));
		}
		dynamoDao.add(dynamoCatalog);
		catalog.setCatalogId(Long.toString(dynamoCatalog.getLastUpdated()));
	}

	@Override
	public Catalog find(String id) {

		Catalog catalog = getFromCache(id);

		if (catalog == null) {
			DynamoCatalog dynamoCatalog = (DynamoCatalog) dynamoDao.find(newDynamoCatalogWithId(id));
			catalog = dynamoCatalog != null ? DynamoCatalog.mapToCatalog(dynamoCatalog) : null;
			if (catalog != null)
				addWithTagToCache(ABTag.emptyABTag(), catalog);
		}

		return catalog;
	}

	@Override
	public void update(Catalog catalog) {
		DynamoCatalog dynamoCatalog = new DynamoCatalog(catalog);
		if (catalog.getCatalogId() == null || catalog.getCatalogId().length() == 0) {
			dynamoCatalog.setLastUpdated(System.currentTimeMillis());
		} else {
			dynamoCatalog.setLastUpdated(Long.parseLong(catalog.getCatalogId()));
		}
		dynamoDao.add(dynamoCatalog);
	}

	protected DynamoCatalog newDynamoCatalogWithId(String id) {
		DynamoCatalog dynamoCatalog = new DynamoCatalog();
		dynamoCatalog.setLastUpdated(Long.parseLong(id));
		return dynamoCatalog;
	}

	private String getCatalogProjection() {
		return DynamoCatalog.HASH_KEY_NAME.concat(",").concat(DynamoCatalog.RANGE_KEY_NAME);
	}

	private CatalogIsActive mapToCatalog(Item item) {
		long lastUpdated = item.getLong(DynamoCatalog.RANGE_KEY_NAME);
		return new CatalogIsActive(Long.toString(lastUpdated), false);
	}

}
