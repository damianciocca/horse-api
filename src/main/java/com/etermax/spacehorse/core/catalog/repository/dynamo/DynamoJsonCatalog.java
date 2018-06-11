package com.etermax.spacehorse.core.catalog.repository.dynamo;

import java.nio.ByteBuffer;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;

@DynamoDBTable(tableName = "catalog")
public class DynamoJsonCatalog implements AbstractDynamoDAO, Comparable<DynamoJsonCatalog> {

	public static final String RANGE_KEY_NAME = "lastUpdated";

	public static final String MAIN_CATALOG_ID = "MAIN_CATALOG_ID";

	@DynamoDBHashKey(attributeName = "catalogId")
	private String catalogId = MAIN_CATALOG_ID;

	@DynamoDBRangeKey(attributeName = RANGE_KEY_NAME)
	private long lastUpdated;

	@DynamoDBAttribute(attributeName = "isActive")
	private Boolean isActive;

	@DynamoDBAttribute(attributeName = "catalogCompressed")
	private ByteBuffer catalogCompressed;

	public DynamoJsonCatalog() {

	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public void setId(String id) {
		this.catalogId = id;
	}

	@Override
	public String getId() {
		return catalogId;
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

	public ByteBuffer getCatalogCompressed() {
		return catalogCompressed;
	}

	public void setCatalogCompressed(ByteBuffer catalogCompressed) {
		this.catalogCompressed = catalogCompressed;
	}

	@DynamoDBIgnore
	public String getCatalogAsJson() {
		return CatalogCompressionUtils.decompressCatalogToJson(getCatalogCompressed());
	}

	@DynamoDBIgnore
	public void setCatalogAsJson(String json) {
		setCatalogCompressed(CatalogCompressionUtils.compressCatalogJson(json));
	}

	@Override
	public int compareTo(DynamoJsonCatalog dynamoJsonCatalog) {
		return (int) (this.lastUpdated - dynamoJsonCatalog.lastUpdated);
	}
}

