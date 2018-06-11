package com.etermax.spacehorse.core.player.model.inventory.chest;

import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntriesCollection;
import com.etermax.spacehorse.core.catalog.model.ChestDefinition;
import com.etermax.spacehorse.core.common.exception.ApiException;
import com.etermax.spacehorse.core.servertime.model.ServerTime;

@DynamoDBDocument
public class Chest {

	private static final int FREE_COST = 0;

	@DynamoDBAttribute(attributeName = "id")
	private Long id;

	@DynamoDBAttribute(attributeName = "chestType")
	private String chestType;

	@DynamoDBAttribute(attributeName = "mapNumber")
	private Integer mapNumber = 0;

	@DynamoDBAttribute(attributeName = "chestOpeningStartDate")
	private Date chestOpeningStartDate;

	@DynamoDBAttribute(attributeName = "chestOpeningEndDate")
	private Date chestOpeningEndDate;

	@DynamoDBAttribute(attributeName = "slotNumber")
	private Integer slotNumber;

	public Chest(Long id, String chestType, Integer mapNumber, Integer slotNumber) {
		this.id = id;
		this.chestType = chestType;
		this.mapNumber = mapNumber;
		this.slotNumber = slotNumber;
	}

	public Chest() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getChestType() {
		return chestType;
	}

	public void setChestType(String chestType) {
		this.chestType = chestType;
	}

	public Integer getMapNumber() {
		return mapNumber;
	}

	public void setMapNumber(Integer mapNumber) {
		this.mapNumber = mapNumber;
	}

	public Date getChestOpeningStartDate() {
		return chestOpeningStartDate;
	}

	public void setChestOpeningStartDate(Date chestOpeningStartDate) {
		this.chestOpeningStartDate = chestOpeningStartDate;
	}

	public Date getChestOpeningEndDate() {
		return chestOpeningEndDate;
	}

	public void setChestOpeningEndDate(Date chestOpeningEndDate) {
		this.chestOpeningEndDate = chestOpeningEndDate;
	}

	public Integer getSlotNumber() {
		return slotNumber;
	}

	public void setSlotNumber(Integer slotNumber) {
		this.slotNumber = slotNumber;
	}

	@DynamoDBIgnore
	public boolean isClosed() {
		return chestOpeningStartDate == null;
	}

	@DynamoDBIgnore
	public ChestState getChestState(Date serverDate) {
		if (isClosed()) {
			return ChestState.CLOSED;
		} else if (serverDate.before(chestOpeningEndDate)) {
			return ChestState.OPENING;
		}
		return ChestState.OPENED;
	}

	public void startOpening(Date serverDate, CatalogEntriesCollection<ChestDefinition> chestDefinitionsCollection) {
		if (!isClosed()) {
			throw new ApiException("Chest can't be started to open");
		}
		chestOpeningStartDate = serverDate;
		chestOpeningEndDate = new Date(serverDate.getTime() + chestDefinitionsCollection.findByIdOrFail(chestType).getDuration() * 1000L);
	}

	public boolean canFinishOpening(Date serverDate) {
		return getChestState(serverDate).equals(ChestState.OPENED);
	}

	public void finishOpening(Date serverDate) {
		if (!canFinishOpening(serverDate)) {
			throw new ApiException("Chest can't be finished to open");
		}
		this.id = -1L;
		this.chestOpeningStartDate = null;
		this.chestOpeningEndDate = null;
	}

	@DynamoDBIgnore
	public boolean canOpen(Date serverDate) {
		return getChestState(serverDate).equals(ChestState.OPENING) || getChestState(serverDate).equals(ChestState.CLOSED);
	}

	@DynamoDBIgnore
	public void forceOpening(Date serverDate) {
		if (!canOpen(serverDate)) {
			throw new ApiException("Chest opening can't be forced");
		}
		this.id = -1L;
		this.chestOpeningStartDate = null;
		this.chestOpeningEndDate = null;
	}

	@DynamoDBIgnore
	public void speedupOpening(Date now, int speedupTimeInSeconds) {
		if (!canOpen(now)) {
			throw new ApiException("Chest opening can't be speedup");
		}
		if (this.chestOpeningEndDate != null) {
			long openingEndTimeInSeconds = ServerTime.fromDate(this.chestOpeningEndDate);
			long newOpeningEndTimeInSeconds = openingEndTimeInSeconds - speedupTimeInSeconds;
			this.chestOpeningEndDate = ServerTime.toDate(newOpeningEndTimeInSeconds);
		}
	}

	public int getForceOpeningCostInGems(Date serverDate, ChestConstants chestConstants,
			CatalogEntriesCollection<ChestDefinition> chestDefinitionsCollection) {
		ChestDefinition chestDefinition = chestDefinitionsCollection.findByIdOrFail(chestType);
		if(chestDefinition.isSpeedUpFree()){
			return FREE_COST;
		}

		long remainingTime;
		if (isClosed()) {
			remainingTime = chestDefinition.getDuration();
		} else {
			remainingTime = fetchRemainingTime(serverDate);
		}
		if (remainingTime != 0) {
			return ((int) Math.ceil((double) remainingTime / (double) chestConstants.getOpeningChestSpeedupCostSeconds())) * chestConstants
					.getOpeningChestSpeedupCostGems();
		}
		return 0;
	}

	private long fetchRemainingTime(Date serverDate) {
		if (isClosed()) {
			return Integer.MAX_VALUE;
		}
		if (serverDate.before(chestOpeningEndDate)) {
			return (chestOpeningEndDate.getTime() - serverDate.getTime()) / 1000L;
		}
		return 0;
	}

	public void checkIntegrity(Catalog catalog, Chests chests) {
		if (slotNumber == null) {
			slotNumber = chests.getNextSlotNumber(catalog.getGameConstants());
		}
	}
}
