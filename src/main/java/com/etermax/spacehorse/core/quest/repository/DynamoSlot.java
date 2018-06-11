package com.etermax.spacehorse.core.quest.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.etermax.spacehorse.core.quest.model.QuestSlot;

@DynamoDBDocument
public class DynamoSlot {

	@DynamoDBAttribute(attributeName = "slotId")
	private String slotId;

	@DynamoDBAttribute(attributeName = "questSlot")
	private QuestSlot questSlot;

	public DynamoSlot() {
		// just for dynamo mapper
	}

	public DynamoSlot(String slotId, QuestSlot questSlot) {
		this.slotId = slotId;
		this.questSlot = questSlot;
	}

	public String getSlotId() {
		return slotId;
	}

	public void setSlotId(String slotId) {
		this.slotId = slotId;
	}

	public QuestSlot getQuestSlot() {
		return questSlot;
	}

	public void setQuestSlot(QuestSlot questSlot) {
		this.questSlot = questSlot;
	}
}
