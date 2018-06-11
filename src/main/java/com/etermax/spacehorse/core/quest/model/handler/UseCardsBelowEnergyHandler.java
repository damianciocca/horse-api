package com.etermax.spacehorse.core.quest.model.handler;

import java.util.List;

import com.etermax.spacehorse.core.catalog.model.CardDefinition;
import com.etermax.spacehorse.core.catalog.model.QuestDefinition;

public class UseCardsBelowEnergyHandler extends UseCardsHandler {

	public UseCardsBelowEnergyHandler(List<CardDefinition> cardsDefinitionList, List<QuestDefinition> questDefinitionList) {
		super(cardsDefinitionList, questDefinitionList);
	}

	@Override
	protected boolean filterCard(CardDefinition cardDefinition, int questParameter) {
		return cardDefinition.getEnergyCost() <= questParameter;
	}
}
