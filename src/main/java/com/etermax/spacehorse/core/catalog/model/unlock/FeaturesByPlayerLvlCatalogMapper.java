package com.etermax.spacehorse.core.catalog.model.unlock;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;
import com.etermax.spacehorse.core.catalog.resource.response.FeaturesByPlayerLvlDefinitionResponse;
import com.google.common.collect.Maps;

public class FeaturesByPlayerLvlCatalogMapper {

	private final Map<String, FeatureByPlayerLvlType> typesByKeys;

	public FeaturesByPlayerLvlCatalogMapper() {
		typesByKeys = Maps.newHashMap();
		typesByKeys.put(FeatureByPlayerLvlType.COURIER_SLOT.getId(), FeatureByPlayerLvlType.COURIER_SLOT);
		typesByKeys.put(FeatureByPlayerLvlType.QUEST_SLOT.getId(), FeatureByPlayerLvlType.QUEST_SLOT);
		typesByKeys.put(FeatureByPlayerLvlType.DECK_SLOT.getId(), FeatureByPlayerLvlType.DECK_SLOT);
		typesByKeys.put(FeatureByPlayerLvlType.CLUB.getId(), FeatureByPlayerLvlType.CLUB);
	}

	public EntryContainerResponse<FeaturesByPlayerLvlDefinitionResponse> mapFrom(Catalog catalog) {
		List<FeaturesByPlayerLvlDefinitionResponse> responses = catalog.getFeatureByPlayerLvlDefinitionCollection().getEntries().stream()
				.map(toFeaturesByPlayerLvlDefinitionResponse()).collect(Collectors.toList());
		return new EntryContainerResponse<>(responses);
	}

	public List<FeaturesByPlayerLvlDefinition> mapFrom(CatalogResponse catalogResponse) {
		return catalogResponse.getFeaturesByPlayerLvlCollection().getEntries().stream().map(toFeatureByPlayerLvlDefinition())
				.collect(Collectors.toList());
	}

	private Function<FeaturesByPlayerLvlDefinitionResponse, FeaturesByPlayerLvlDefinition> toFeatureByPlayerLvlDefinition() {
		return definitionResponse -> new FeaturesByPlayerLvlDefinition( //
				definitionResponse.getId(), //
				definitionResponse.getAvailableOnLevel(),//
				typeOf(definitionResponse.getFeatureType()),//
				definitionResponse.getCourierMaxSlots(),//
				definitionResponse.getQuestMaxDifficulty(),//
				definitionResponse.getDeckMaxSlots());
	}

	private Function<FeaturesByPlayerLvlDefinition, FeaturesByPlayerLvlDefinitionResponse> toFeaturesByPlayerLvlDefinitionResponse() {
		return definition -> new FeaturesByPlayerLvlDefinitionResponse( //
				definition.getId(),//
				definition.getAvailableOnLevel(),//
				definition.getFeatureType(),//
				definition.getCourierMaxSlots(), //
				definition.getQuestMaxDifficulty(),//
				definition.getDeckMaxSlots());
	}

	private FeatureByPlayerLvlType typeOf(String type) {
		return typesByKeys.get(type);
	}
}
