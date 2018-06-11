package com.etermax.spacehorse.core.quest.model.handler;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.catalog.resource.response.CardArchetypeResponse;

public class UseCardsWithArchetypeHandler extends UseCardsHandler {

	public UseCardsWithArchetypeHandler(List<CardDefinition> cardsDefinitionList, List<QuestDefinition> questDefinitionList) {
		super(cardsDefinitionList, questDefinitionList);
	}

	@Override
	protected boolean filterCard(CardDefinition cardDefinition, int questParameter) {
		return CardArchetypeResponse.fromCardArchetypeEnum(cardDefinition.getCardArchetype()) == questParameter;
	}
}
