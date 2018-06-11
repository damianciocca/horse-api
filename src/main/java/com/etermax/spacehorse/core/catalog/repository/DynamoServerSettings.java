package com.etermax.spacehorse.core.catalog.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;

@DynamoDBTable(tableName = "serverSettings")
public class DynamoServerSettings implements AbstractDynamoDAO {

	private static String FIXED_ID = "hardcodeado";

	@DynamoDBHashKey(attributeName = "id")
	private String id;

	@DynamoDBAttribute(attributeName = "catalogId")
	private String catalogId;

	@DynamoDBAttribute(attributeName = "isUnderMaintenance")
	private boolean isUnderMaintenance;

	public DynamoServerSettings(String catalogId, String lastUpdated, boolean isUnderMaintenance) {
		this.catalogId = catalogId;
		this.id = FIXED_ID;
		this.isUnderMaintenance = isUnderMaintenance;
	}

	public DynamoServerSettings() {
		this.id = FIXED_ID;
		this.catalogId = "";
		this.isUnderMaintenance = true;
	}

	@Override
	public void setId(String id) {
		this.id = FIXED_ID;
	}

	public static String getFixedId() {
		return FIXED_ID;
	}

	public static void setFixedId(String fixedId) {
		DynamoServerSettings.FIXED_ID = fixedId;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	@Override
	public String getId() {
		return id;
	}

	public boolean isUnderMaintenance() {
		return isUnderMaintenance;
	}

	public void setUnderMaintenance(boolean underMaintenance) {
		isUnderMaintenance = underMaintenance;
	}
}
