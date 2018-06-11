package com.etermax.spacehorse.core.catalog.model.specialoffer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;
import com.etermax.spacehorse.core.catalog.resource.response.SpecialOffersItemResponse;
import com.etermax.spacehorse.core.catalog.resource.response.SpecialOfferDefinitionResponse;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.servertime.model.ServerTime;

public class SpecialOfferCatalogMapper {

	public EntryContainerResponse<SpecialOfferDefinitionResponse> mapFrom(Catalog catalog) {

		List<SpecialOfferDefinitionResponse> specialOfferDefinitionRespons = catalog.getSpecialOfferDefinitionsCollection().getEntries().stream()
				.map(this::toSpecialOffersResponse).collect(Collectors.toList());
		return new EntryContainerResponse<>(specialOfferDefinitionRespons);
	}

	public List<SpecialOfferDefinition> mapFrom(CatalogResponse catalogResponse) {

		return catalogResponse.getSpecialOffersCollection().getEntries().stream().map(specialOfferDefinitionResponse -> new SpecialOfferDefinition( //
				specialOfferDefinitionResponse.getId(), //
				specialOfferDefinitionResponse.getAvailableAmount(),//
				specialOfferDefinitionResponse.getDurationInSeconds(), //
				specialOfferDefinitionResponse.getGroupId(), //
				specialOfferDefinitionResponse.getGoldPrice(), //
				specialOfferDefinitionResponse.getGemsPrice(), //
				specialOfferDefinitionResponse.getShopInAppPurchase(), // FILTRO 1
				specialOfferDefinitionResponse.getShopInAppItemId(), //
				collectItemsFromSpecialOffer(catalogResponse.getSpecialOffersItemsCollection(), specialOfferDefinitionResponse.getId()), //
				getActivationTime(specialOfferDefinitionResponse), specialOfferDefinitionResponse.getFrequencyInDays(), //
				specialOfferDefinitionResponse.getScheduledFilterEnabled(), // FILTRO 2
				specialOfferDefinitionResponse.getMapNumber(), //
				specialOfferDefinitionResponse.getMapFilterEnabled(), // // FILTRO 3
				specialOfferDefinitionResponse.getDiscount(), //
				specialOfferDefinitionResponse.getOrder()) //
		).collect(Collectors.toList());
	}

	// TODO: 11/29/17 este metodo claramente necesita un REFACTOR.. pero viene de la mano de separa de las definiciones los filtros y los eventos (triggers)
	private SpecialOfferDefinitionResponse toSpecialOffersResponse(SpecialOfferDefinition definition) {
		String id = definition.getId();
		int availableAmount = definition.getAvailableAmount();
		int durationInSeconds = definition.getDurationInSeconds();
		String groupId = definition.getGroupId();

		int goldPrice = definition.getGoldPrice();
		int gemsPrice = definition.getGemsPrice();

		boolean inAppPurchase = false;
		String shopInAppItemId = null;
		if (definition.isInAppPurchase()) {
			inAppPurchase = definition.isInAppPurchase();
			shopInAppItemId = definition.getShopInAppItemId();
		}

		String activationTime = null;
		int frequencyInDays = 0;
		boolean scheduledFilterEnabled = false;
		if (definition.isScheduledFilterEnabled()) {
			activationTime = ServerTime.toDateTimeAsText(definition.getActivationTime());
			frequencyInDays = definition.getFrequencyInDays();
			scheduledFilterEnabled = definition.isScheduledFilterEnabled();
		}

		int mapNumber = 0;
		boolean mapFilterEnabled = false;
		if (definition.isMapFilterEnabled()) {
			mapNumber = definition.getMapNumber();
			mapFilterEnabled = definition.isMapFilterEnabled();
		}

		int discount = definition.getDiscount();

		int order = definition.getOrder();

		return new SpecialOfferDefinitionResponse(id, availableAmount, durationInSeconds, groupId, goldPrice, gemsPrice, inAppPurchase,
				shopInAppItemId, activationTime, frequencyInDays, scheduledFilterEnabled, mapNumber, mapFilterEnabled, discount, order);

	}

	private List<SpecialOfferItemDefinition> collectItemsFromSpecialOffer(EntryContainerResponse<SpecialOffersItemResponse> itemsResponses,
			String specialOfferId) {

		List<SpecialOffersItemResponse> specialOffersItemsResponses = itemsResponses.getEntries().stream()
				.filter(item -> item.getSpecialOfferId().trim().equalsIgnoreCase(specialOfferId)).collect(Collectors.toList());

		if (specialOffersItemsResponses.isEmpty()) {
			throw new CatalogException("Should not be empty special offer items for special offer id " + specialOfferId);
		}

		return specialOffersItemsResponses.stream().map(toSpecialOfferItemDefinition(specialOfferId)).collect(Collectors.toList());
	}

	private DateTime getActivationTime(SpecialOfferDefinitionResponse specialOfferDefinitionResponse) {
		String activationTime = specialOfferDefinitionResponse.getActivationTime();
		return isNotBlank(activationTime) ? ServerTime.roundToStartOfDay(activationTime) : null;
		// TODO: 11/25/17 REFACTORIZAR PARA NO TENER Q DEVOLVER NULL
	}

	private Function<SpecialOffersItemResponse, SpecialOfferItemDefinition> toSpecialOfferItemDefinition(String specialOfferId) {
		return itemResponse -> new SpecialOfferItemDefinition(itemResponse.getId(), itemResponse.getItemRewardTypeId(),
				RewardType.valueOf(itemResponse.getRewardType()), itemResponse.getQuantity(), specialOfferId);
	}

}
