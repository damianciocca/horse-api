package com.etermax.spacehorse.core.capitain.infrastructure.skins;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.etermax.spacehorse.core.capitain.model.skins.CaptainSkin;
import com.etermax.spacehorse.core.catalog.model.captain.CaptainSkinDefinition;

@DynamoDBDocument
public class DynamoCaptainSkin {

	@DynamoDBAttribute(attributeName = "id")
	private String id;

	public DynamoCaptainSkin() {
		// just for dynamo mapper
	}

	public DynamoCaptainSkin(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@DynamoDBIgnore
	public CaptainSkin toCaptainSkin(CaptainSkinDefinition definition) {
		return new CaptainSkin(id, definition);
	}
}
