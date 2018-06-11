package com.etermax.spacehorse.core.player.resource.response.player.inventory.chest;

import com.etermax.spacehorse.core.player.model.inventory.chest.Chest;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChestResponse {
	@JsonProperty("id")
	private Long id;
	@JsonProperty("chestType")
	private String chestType;
	@JsonProperty("mapNumber")
	private Integer mapNumber;
	@JsonProperty("chestOpeningStartServerTime")
	private Long chestOpeningStartServerTime;
	@JsonProperty("chestOpeningEndServerTime")
	private Long chestOpeningEndServerTime;
	@JsonProperty("slotNumber")
	private Integer slotNumber;

	public Long getId() {
		return id;
	}

	public String getChestType() {
		return chestType;
	}

	public Integer getMapNumber() {
		return mapNumber;
	}

	public Long getChestOpeningStartServerTime() {
		return chestOpeningStartServerTime;
	}

	public Long getChestOpeningEndServerTime() {
		return chestOpeningEndServerTime;
	}

	public Integer getSlotNumber() {
		return slotNumber;
	}

	public ChestResponse(Chest chest) {
		this.id = chest.getId();
		this.chestType = chest.getChestType();
		this.mapNumber = chest.getMapNumber();
		this.chestOpeningStartServerTime = ServerTime.fromDate(chest.getChestOpeningStartDate());
		this.chestOpeningEndServerTime = ServerTime.fromDate(chest.getChestOpeningEndDate());
		this.slotNumber = chest.getSlotNumber();
	}
}
