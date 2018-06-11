package com.etermax.spacehorse.core.capitain.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.etermax.spacehorse.core.capitain.model.Captain;
import com.etermax.spacehorse.core.capitain.model.CaptainsCollection;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.common.repository.dynamo.AbstractDynamoDAO;

@DynamoDBTable(tableName = "captainsCollection")
public class DynamoCaptainsCollection implements AbstractDynamoDAO {

	@DynamoDBHashKey(attributeName = "userId")
	private String userId;

	@DynamoDBAttribute(attributeName = "captains")
	private List<DynamoCaptain> captains;

	@DynamoDBAttribute(attributeName = "selectedCaptainId")
	private String selectedCaptainId;

	public DynamoCaptainsCollection() {
		// just for dynamo mapper
	}

	public DynamoCaptainsCollection(String userId, List<DynamoCaptain> captains, String selectedCaptainId) {
		this.userId = userId;
		this.captains = captains;
		this.selectedCaptainId = selectedCaptainId;
	}

	public static DynamoCaptainsCollection mapToDynamoCaptainsCollection(String userId, CaptainsCollection captainsCollection) {
		List<DynamoCaptain> captains = captainsCollection.getCaptains().stream().map(DynamoCaptain::mapToDynamoCaptain).collect(Collectors.toList());
		return new DynamoCaptainsCollection(userId, captains, captainsCollection.getSelectedCaptainId());
	}

	public static CaptainsCollection mapToCaptainsCollection(DynamoCaptainsCollection dynamoCaptainsCollection, Catalog catalog, String userId) {
		List<Captain> ownedCaptains = dynamoCaptainsCollection.getCaptains().stream()
				.map(dynamoCaptain -> DynamoCaptain.mapToCaptain(dynamoCaptain, catalog)).collect(Collectors.toList());
		return CaptainsCollection.restore(userId, ownedCaptains, dynamoCaptainsCollection.getSelectedCaptainId());
	}

	@Override
	public String getId() {
		return getUserId();
	}

	@Override
	public void setId(String id) {
		setUserId(id);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<DynamoCaptain> getCaptains() {
		return captains;
	}

	public void setCaptains(List<DynamoCaptain> captains) {
		this.captains = captains;
	}

	public String getSelectedCaptainId() {
		return selectedCaptainId;
	}

	public void setSelectedCaptainId(String selectedCaptainId) {
		this.selectedCaptainId = selectedCaptainId;
	}
}
