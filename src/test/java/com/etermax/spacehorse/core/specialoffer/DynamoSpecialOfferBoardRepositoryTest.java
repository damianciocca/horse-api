package com.etermax.spacehorse.core.specialoffer;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.catalog.repository.CatalogRepository;
import com.etermax.spacehorse.core.common.repository.LocalDynamoDBCreationRule;
import com.etermax.spacehorse.core.player.model.Player;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.repository.DynamoSpecialOfferBoard;
import com.etermax.spacehorse.core.specialoffer.repository.DynamoSpecialOfferBoardRepository;
import com.etermax.spacehorse.core.user.repository.dynamo.DynamoDao;
import com.etermax.spacehorse.mock.PlayerScenarioBuilder;
import com.etermax.spacehorse.mock.SpecialOfferDefinitionScenarioBuilder;

public class DynamoSpecialOfferBoardRepositoryTest {

	public static final int DURATION_IN_SECONDS = 100;
	private static final String SPECIAL_OFFER_ID = "offerId";
	private static final String USER_ID = "10";
	@ClassRule
	public static LocalDynamoDBCreationRule RULE = new LocalDynamoDBCreationRule();
	private DynamoSpecialOfferBoardRepository repository;
	private FixedServerTimeProvider timeProvider;
	private Player player;
	private SpecialOfferDefinition specialOfferDefinition;
	private DynamoDao dao;

	@Before
	public void setUp() {
		RULE.createSimpleTable(DynamoSpecialOfferBoard.class);
		dao = new DynamoDao(RULE.getAmazonDynamoDB());
		timeProvider = new FixedServerTimeProvider();
		player = new PlayerScenarioBuilder(USER_ID).build();
		specialOfferDefinition = new SpecialOfferDefinitionScenarioBuilder(SPECIAL_OFFER_ID, DURATION_IN_SECONDS).build();
		CatalogRepository catalogRepository = aCatalogRepository(specialOfferDefinition);
		repository = new DynamoSpecialOfferBoardRepository(dao, timeProvider, catalogRepository);
	}

	@After
	public void tearDown() {
		RULE.deleteAllTables();
	}

	@Test
	public void givenAPlayerWithoutSpecialOfferBoardWhenFindThenTheSpecialOfferBoardShouldFound() throws Exception {
		// When
		SpecialOfferBoard specialOfferBoard = repository.findOrDefaultBy(player);
		// Then
		assertThat(specialOfferBoard).isNotNull();
		assertThat(specialOfferBoard.getOrderedSpecialOffers()).isEmpty();
		DateTime expectedDateTime = ServerTime.roundToStartOfNextDay(timeProvider.getDateTime());
		assertThat(specialOfferBoard.getNexRefreshTimeInSeconds()).isEqualTo(ServerTime.fromDate(expectedDateTime));
	}

	@Test
	public void givenASpecialOfferBoardWithOneSpecialOfferWhenSaveThenTheSpecialOfferBoardShouldBePersistedSuccessfully() {
		// Given
		SpecialOfferBoard specialOfferBoard = new SpecialOfferBoard(timeProvider);
		specialOfferBoard.put(new SpecialOffer(specialOfferDefinition, timeProvider));
		// When
		repository.addOrUpdate(player, specialOfferBoard);
		// Then
		assertThat(repository.findOrDefaultBy(player).getOrderedSpecialOffers()).extracting(SpecialOffer::getId).containsExactly("offerId");
	}

	@Test
	public void givenASpecialOfferBoardWithOneSpecialOfferWhenRefreshCatalogWithEmptyDefinitionsThenTheSpecialOfferBoardShouldBeEmpty() {
		// Given
		SpecialOfferBoard specialOfferBoard = new SpecialOfferBoard(timeProvider);
		specialOfferBoard.put(new SpecialOffer(specialOfferDefinition, timeProvider));
		repository.addOrUpdate(player, specialOfferBoard);
		// When
		repository = new DynamoSpecialOfferBoardRepository(dao, timeProvider, aCatalogRepositoryWitnEmptySpecialOfferDefinitions());
		// Then
		assertThat(repository.findOrDefaultBy(player).getOrderedSpecialOffers()).isEmpty();
	}

	private CatalogRepository aCatalogRepository(SpecialOfferDefinition specialOfferDefinition) {
		CatalogRepository catalogRepository = mock(CatalogRepository.class);
		Catalog catalog = mock(Catalog.class);
		when(catalog.getSpecialOfferDefinitionsCollection()).thenReturn(new CatalogEntriesCollection<>(newArrayList(specialOfferDefinition)));
		when(catalogRepository.getActiveCatalogWithTag(any())).thenReturn(catalog);
		return catalogRepository;
	}

	private CatalogRepository aCatalogRepositoryWitnEmptySpecialOfferDefinitions() {
		CatalogRepository catalogRepository = mock(CatalogRepository.class);
		Catalog catalog = mock(Catalog.class);
		when(catalog.getSpecialOfferDefinitionsCollection()).thenReturn(new CatalogEntriesCollection<>(newArrayList()));
		when(catalogRepository.getActiveCatalogWithTag(any())).thenReturn(catalog);
		return catalogRepository;
	}
}
