package com.etermax.spacehorse.core.catalog.repository.dynamo;

import java.nio.ByteBuffer;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.resource.response.CatalogResponse;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;

@DynamoDBTable(tableName = "catalog")
public class DynamoCatalog implements AbstractDynamoDAO, Comparable<DynamoCatalog> {

	public static final String TABLE_NAME = "catalog";
	public static final String RANGE_KEY_NAME = "lastUpdated";
	public static final String MAIN_CATALOG_ID = "MAIN_CATALOG_ID";
	public static final String HASH_KEY_NAME = "catalogId";

	@DynamoDBHashKey(attributeName = HASH_KEY_NAME)
	private String catalogId = MAIN_CATALOG_ID;

	@DynamoDBRangeKey(attributeName = RANGE_KEY_NAME)
	private long lastUpdated;

	@DynamoDBAttribute(attributeName = "isActive")
	private Boolean isActive;

	@DynamoDBAttribute(attributeName = "catalogCompressed")
	private ByteBuffer catalogCompressed;

	public DynamoCatalog() {
		catalogCompressed = ByteBuffer.allocate(0);
	}

	public DynamoCatalog(Catalog catalog) {
		this.isActive = catalog.getIsActive();
		this.catalogCompressed = CatalogCompressionUtils.compressCatalog(new CatalogResponse(catalog));
	}

	public DynamoCatalog(String catalogId, boolean isActive, CatalogResponse catalogContent) {
		this.catalogId = catalogId;
		this.isActive = isActive;
		this.catalogCompressed = CatalogCompressionUtils.compressCatalog(catalogContent);
	}

	public static Catalog mapToCatalog(DynamoCatalog dynamoCatalog) {
		Catalog catalog = new Catalog(CatalogCompressionUtils.decompressCatalog(dynamoCatalog.getCatalogCompressed()));
		catalog.setCatalogId(Long.toString(dynamoCatalog.getLastUpdated()));
		catalog.setIsActive(dynamoCatalog.getActive());
		return catalog;
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

	public ByteBuffer getCatalogCompressed() {
		return catalogCompressed;
	}

	public void setCatalogCompressed(ByteBuffer catalogCompressed) {
		this.catalogCompressed = catalogCompressed;
	}

	@Override
	public String getId() {
		return catalogId;
	}

	@Override
	public void setId(String id) {
		this.catalogId = id;
	}

	@Override
	public int compareTo(DynamoCatalog dynamoCatalog) {
		return (int) (this.lastUpdated - dynamoCatalog.lastUpdated);
	}

	public Boolean getActive() {
		return isActive;
	}

	public void setActive(Boolean active) {
		isActive = active;
	}

}
