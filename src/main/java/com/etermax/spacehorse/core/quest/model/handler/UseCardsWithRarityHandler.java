package com.etermax.spacehorse.core.quest.model.handler;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;
import com.etermax.spacehorse.core.catalog.resource.response.CardRarityResponse;

public class UseCardsWithRarityHandler extends UseCardsHandler {

	public UseCardsWithRarityHandler(List<CardDefinition> cardsDefinitionList, List<QuestDefinition> questDefinitionList) {
		super(cardsDefinitionList, questDefinitionList);
	}

	@Override
	protected boolean filterCard(CardDefinition cardDefinition, int questParameter) {
		return CardRarityResponse.fromCardRarityEnum(cardDefinition.getCardRarity()) >= questParameter;
	}
}
