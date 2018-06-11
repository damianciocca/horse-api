package com.etermax.spacehorse.core.quest.model.unlock.strategies;

import com.etermax.spacehorse.core.quest.model.unlock.QuestSlotDifficultiesConfiguration;

public class QuestSlotsInspectorFactory {

	public QuestSlotsInspector create(boolean enableFeaturesByPlayerLvl, QuestSlotDifficultiesConfiguration configuration) {
		if (enableFeaturesByPlayerLvl) {
			return new AvailableByPlayerLevelQuestSlotsInspector(configuration);
		}
		return new DefaultQuestSlotsInspector();
	}
}
