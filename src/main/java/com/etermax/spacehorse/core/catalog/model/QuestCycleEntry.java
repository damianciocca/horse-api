package com.etermax.spacehorse.core.catalog.model;

public class QuestCycleEntry extends CatalogEntry {

	private final int sequenceOrder;

	private final QuestDefinition questDefinition;

	public QuestCycleEntry(String id, int sequenceOrder, QuestDefinition questDefinition) {
		super(id);
		this.sequenceOrder = sequenceOrder;
		this.questDefinition = questDefinition;
	}

	public int getSequenceOrder() {
		return sequenceOrder;
	}

	public QuestDefinition getQuestDefinition() {
		return questDefinition;
	}

	@Override
	public void validate(Catalog catalog) {
	}
}
