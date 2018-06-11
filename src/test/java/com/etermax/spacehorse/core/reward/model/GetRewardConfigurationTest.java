package com.etermax.spacehorse.core.reward.model;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.catalog.model.GameConstants;
import com.etermax.spacehorse.core.reward.model.strategies.cards.random.utils.CardDropRateCalculatorConfiguration;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.google.common.collect.Lists;

public class GetRewardConfigurationTest {

	private static final String NOT_ACTIVE_CARD_ID = "NotActiveCardId";
	private static final String ACTIVE_CARD_ID = "activeCardId";
	private static final String FREE_CHEST_ID = "freeChest";
	private ServerTimeProvider serverTimeProvider;
	private GetRewardConfiguration getRewardConfiguration;
	private Catalog catalog;

	@Test
	public void getRewardConfigurationIsCreatedThenDisabledCardsAreFiltered() {
		serverTimeProvider = new FixedServerTimeProvider();
		catalog = getCatalog();

		whenCreateGetRewardConfiguration();

		thenDisabledCardAreFiltered();
	}

	private void whenCreateGetRewardConfiguration() {
		getRewardConfiguration = GetRewardConfiguration.createBy(catalog, serverTimeProvider);
	}

	private void thenDisabledCardAreFiltered() {
		List<CardDefinition> cardDefinition = getRewardConfiguration.getCardDefinition();
		assertThat(cardDefinition).hasSize(1);
		assertThat(cardDefinition.get(0).getId()).isEqualTo(ACTIVE_CARD_ID);
	}

	private Catalog getCatalog() {
		Catalog catalog = mock(Catalog.class);

		when(catalog.getGameConstants()).thenReturn(mock(GameConstants.class));

		when(catalog.getCardsDropRateCollection()).thenReturn(new CatalogEntriesCollection<>());

		when(catalog.getTutorialProgressCollection()).thenReturn(new CatalogEntriesCollection<>());

		when(catalog.getChestChancesDefinitionsCollection()).thenReturn(new CatalogEntriesCollection<>());

		CatalogEntriesCollection catalogEntriesCollection = getACardDefinitionsCollectionWithTwoCards();
		when(catalog.getCardDefinitionsCollection()).thenReturn(catalogEntriesCollection);

		when(catalog.getChestDefinitionsCollection())
				.thenReturn(new CatalogEntriesCollection<>(Arrays.asList(new ChestDefinition(FREE_CHEST_ID, "", 0, false, false, false))));

		when(catalog.getGameConstants().getCardDropRateCalculatorConfiguration()).thenReturn(new CardDropRateCalculatorConfiguration());

		return catalog;
	}

	private CatalogEntriesCollection getACardDefinitionsCollectionWithTwoCards() {
		CardDefinition cardDefinition1 = mock(CardDefinition.class);
		when(cardDefinition1.isActiveFor(any())).thenReturn(true);
		when(cardDefinition1.getId()).thenReturn(ACTIVE_CARD_ID);

		CardDefinition cardDefinition2 = mock(CardDefinition.class);
		when(cardDefinition2.isActiveFor(any())).thenReturn(false);
		when(cardDefinition2.getId()).thenReturn(NOT_ACTIVE_CARD_ID);

		CatalogEntriesCollection catalogEntriesCollection = mock(CatalogEntriesCollection.class);
		when(catalogEntriesCollection.getEntries()).thenReturn(Lists.newArrayList(cardDefinition1, cardDefinition2));
		return catalogEntriesCollection;
	}
}
