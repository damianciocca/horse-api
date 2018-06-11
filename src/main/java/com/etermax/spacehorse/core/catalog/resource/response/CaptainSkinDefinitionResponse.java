package com.etermax.spacehorse.core.catalog.resource.response;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptainSkinDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("CaptainId")
	private String captainId;

	@JsonProperty("SkinId")
	private String skinId;

	@JsonProperty("Slot")
	private int slotNumber;

	@JsonProperty("GoldPrice")
	private int goldPrice;

	@JsonProperty("GemsPrice")
	private int gemsPrice;

	@JsonProperty("IsDefault")
	private boolean isDefault;

	public CaptainSkinDefinitionResponse() {
		// just for jackson library
	}

	public CaptainSkinDefinitionResponse(String id, String captainId, String skinId, int slotNumber, int goldPrice, int gemsPrice, boolean isDefault) {
		this.id = id;
		this.captainId = captainId;
		this.skinId = skinId;
		this.slotNumber = slotNumber;
		this.goldPrice = goldPrice;
		this.gemsPrice = gemsPrice;
		this.isDefault = isDefault;
	}

	public String getId() {
		return id;
	}

	public String getCaptainId() {
		return captainId;
	}

	public String getSkinId() {
		return skinId;
	}

	public int getSlotNumber() {
		return slotNumber;
	}

	public int getGoldPrice() {
		return goldPrice;
	}

	public int getGemsPrice() {
		return gemsPrice;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
