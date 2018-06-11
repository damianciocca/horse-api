package com.etermax.spacehorse.core.capitain.infrastructure.slots;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.etermax.spacehorse.core.capitain.infrastructure.skins.DynamoCaptainSkin;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.capitain.model.slots.CaptainSlot;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;

@DynamoDBDocument
public class DynamoCaptainSlot {

	@DynamoDBAttribute(attributeName = "slotNumber")
	private int slotNumber;

	@DynamoDBAttribute(attributeName = "captainSkin")
	private DynamoCaptainSkin captainSkin;

	public DynamoCaptainSlot() {
		// just for dynamo mapper
	}

	public DynamoCaptainSlot(int slotNumber, DynamoCaptainSkin captainSkin) {
		this.slotNumber = slotNumber;
		this.captainSkin = captainSkin;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(int slotNumber) {
		this.slotNumber = slotNumber;
	}

	public DynamoCaptainSkin getCaptainSkin() {
		return captainSkin;
	}

	public void setCaptainSkin(DynamoCaptainSkin captainSkin) {
		this.captainSkin = captainSkin;
	}

	@DynamoDBIgnore
	public String getCaptainSkinId() {
		return getCaptainSkin().getId();
	}

	@DynamoDBIgnore
	public CaptainSlot toCaptainSlot(DynamoCaptainSlot dynamoCaptainSlot, CaptainSkinDefinition definition) {
		return new CaptainSlot(dynamoCaptainSlot.getSlotNumber(), new CaptainSkin(dynamoCaptainSlot.getCaptainSkinId(), definition));
	}
}



