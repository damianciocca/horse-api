package com.etermax.spacehorse.core.catalog.model.quest;

import java.util.List;
import java.util.stream.Collectors;

import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.catalog.resource.response.EntryContainerResponse;
import com.etermax.spacehorse.core.catalog.resource.response.QuestDefinitionResponse;

public class QuestCatalogMapper {

	public List<QuestDefinition> mapFrom(CatalogResponse catalogResponse) {
		return catalogResponse.getDailyQuestDefinitionCollection().getEntries().stream()
				.map(QuestDefinition::new).collect(Collectors.toList());
	}

	public EntryContainerResponse<QuestDefinitionResponse> mapFrom(Catalog catalog) {
		List<QuestDefinitionResponse> responses = catalog.getDailyQuestCollection().getEntries().stream()
				.map(QuestDefinitionResponse::new).collect(Collectors.toList());
		return new EntryContainerResponse<>(responses);
	}
}
