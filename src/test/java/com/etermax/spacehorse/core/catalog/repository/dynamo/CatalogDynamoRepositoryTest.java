package com.etermax.spacehorse.core.catalog.repository.dynamo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.GemsCycle;
import com.etermax.spacehorse.core.catalog.model.versions.CatalogVersion;
import com.etermax.spacehorse.core.catalog.repository.ServerSettingsRepository;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogIsActive;
import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.etermax.spacehorse.mock.MockUtils;

public class CatalogDynamoRepositoryTest {

	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();
	private CatalogDynamoRepository repository;

	public CatalogDynamoRepositoryTest() {
		DynamoDao dao = new DynamoDao(RULE.getAmazonDynamoDB());
		ServerSettingsRepository serverSettingsRepository = new ServerSettingsRepository(dao);
		ServerTimeProvider serverTimeProvider = new FixedServerTimeProvider();
		this.repository = new CatalogDynamoRepository(dao, serverSettingsRepository, serverTimeProvider);
	}

	@Before
	public void setUp() {
		RULE.createTableWithGlobalSecondaryIndexes(DynamoCatalog.class);
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	@Test
	public void testFindCatalog() {
		Catalog catalog = givenACatalogWithId("1234");

		this.repository.add(catalog);
		Catalog wantedCatalog = this.repository.find("1234");

		assertEquals(catalog, wantedCatalog);
	}

	@Test
	public void testAddCatalogWithoutIdSetsIdInCatalog() {
		Catalog catalog = MockUtils.mockCatalog();

		this.repository.add(catalog);

		assertThat(catalog.getCatalogId()).isNotEmpty();
	}

	@Test
	public void testUpdateCatalog() {
		Catalog catalog = givenACatalogWithId("1234");
		this.repository.add(catalog);

		Catalog catalog1 = repository.find("1234");
		assertThat(catalog1).isEqualTo(catalog);

		catalog.getGemsCycleCollection().addEntry(new GemsCycle("NewGemsCycleId", Collections.emptyList()));
		catalog.setCatalogId("4422");
		this.repository.update(catalog);
		Catalog catalog1Updated = this.repository.find("4422");

		assertThat(catalog1Updated).isEqualTo(catalog);
		assertThat(catalog1Updated.getGemsCycleCollection().findById("NewGemsCycleId").isPresent());
	}

	@Test
	public void givenManyCatalogsWhenGetTheLastTwoCatalogsTheLastTwoCatalogsShouldBeFound() {
		int limit = 2;
		givenPersistedManyCatalogs();

		List<CatalogIsActive> catalogs = this.repository.listLatestCatalogs(limit);

		thenTheLatestTwoCatalogsWereRetrieved(catalogs);
	}

	private Catalog givenACatalogWithId(String id) {
		Catalog catalog = MockUtils.mockCatalog();
		catalog.setCatalogId(id);
		catalog.setCatalogVersion(CatalogVersion.V1);
		return catalog;
	}

	private void givenPersistedManyCatalogs() {
		Catalog catalog1 = givenACatalogWithId("1");
		Catalog catalog2 = givenACatalogWithId("2");
		Catalog catalog3 = givenACatalogWithId("3");
		this.repository.add(catalog1);
		this.repository.add(catalog2);
		this.repository.add(catalog3);
	}

	private void thenTheLatestTwoCatalogsWereRetrieved(List<CatalogIsActive> catalogs) {
		assertThat(catalogs).hasSize(2);
		assertThat(catalogs).extracting(CatalogIsActive::getCatalogId).containsExactly("3", "2");
	}

}
