package com.etermax.spacehorse.core.catalog.model.specialoffer;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.joda.time.DateTime;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CardArchetypeResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CardDefinitionResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CardRarityResponse;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;
import com.etermax.spacehorse.core.catalog.resource.response.FintResponse;
import com.etermax.spacehorse.core.servertime.model.ServerTime;

public class CardCatalogMapper {

	public EntryContainerResponse<CardDefinitionResponse> mapFrom(Catalog catalog) {
		List<CardDefinitionResponse> cardDefinitionResponses = catalog.getCardDefinitionsCollection().getEntries().stream()
				.map(this::toCardDefinitionResponse).collect(Collectors.toList());
		return new EntryContainerResponse<>(cardDefinitionResponses);
	}

	public List<CardDefinition> mapFrom(CatalogResponse catalogResponse) {
		return catalogResponse.getCards().getEntries().stream().map(this::toCardDefinition).collect(Collectors.toList());
	}

	private CardDefinition toCardDefinition(CardDefinitionResponse cardDefinitionResponse){
		return new CardDefinition( //
				cardDefinitionResponse.getId(), //
				cardDefinitionResponse.getEnergyCost(),//
				cardDefinitionResponse.getCardAction(), //
				CardRarityResponse.toCardRarityEnum(cardDefinitionResponse.getCardRarity()), //
				CardArchetypeResponse.toCardArchetypeEnum(cardDefinitionResponse.getCardArchetype()), //
				cardDefinitionResponse.getCardTarget(), //
				cardDefinitionResponse.getCardTargetRadius().getRaw(), //
				cardDefinitionResponse.getTargetTeam(), //
				cardDefinitionResponse.getCastTime().getRaw(), //
				cardDefinitionResponse.getUnitId(), //
				cardDefinitionResponse.getPowerUpId(), //
				cardDefinitionResponse.getEnabled(), //
				cardDefinitionResponse.getAvailableFromMapId(), //
				getActivationTime(cardDefinitionResponse.getActivationTime()));
	}

	private CardDefinitionResponse toCardDefinitionResponse(CardDefinition definition) {
		return new CardDefinitionResponse(
				definition.getId(),//
				definition.getEnergyCost(),//
				definition.getCardAction(),//
				CardRarityResponse.fromCardRarityEnum(definition.getCardRarity()),//
				CardArchetypeResponse.fromCardArchetypeEnum(definition.getCardArchetype()),//
				definition.getCardTarget(),//
				new FintResponse(definition.getCardTargetRadius()),//
				definition.getTargetTeam(),//
				new FintResponse(definition.getCastTime()),//
				definition.getUnitId(),//
				definition.getPowerUpId(),//
				definition.getEnabled(),//
				definition.getAvailableFromMapId(),//
				getActivationTime(definition.getActivationTime()));
	}

	private String getActivationTime(Optional<DateTime> activationTimeOptional) {
		return activationTimeOptional.isPresent() ? ServerTime.toDateTimeAsText(activationTimeOptional.get()) : null;
	}

	private DateTime getActivationTime(String activationTime) {
		return isNotBlank(activationTime) ? ServerTime.roundToStartOfDay(activationTime) : null;
	}

}