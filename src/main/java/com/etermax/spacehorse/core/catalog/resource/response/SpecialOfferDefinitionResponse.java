package com.etermax.spacehorse.core.catalog.resource.response;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpecialOfferDefinitionResponse {

	@JsonProperty("Id")
	private String id;

	@JsonProperty("AvailableAmount")
	private int availableAmount;

	@JsonProperty("DurationInSeconds")
	private int durationInSeconds;

	@JsonProperty("GroupId")
	private String groupId;

	// ------ Costo en InApp o soft currency ------
	@JsonProperty("GoldPrice")
	private int goldPrice;
	@JsonProperty("GemsPrice")
	private int gemsPrice;
	@JsonProperty("ShopInAppPurchase")
	private boolean shopInAppPurchase;
	@JsonProperty("ShopInAppItemId")
	private String shopInAppItemId;

	// ------ Activacion de oferta programable ------
	@JsonProperty("ActivationTime")
	private String activationTime;
	@JsonProperty("FrequencyInDays")
	private int frequencyInDays;
	@JsonProperty("ScheduledFilterEnabled")
	private boolean scheduledFilterEnabled;

	// ------ Activacion de filtro por mapa ------
	@JsonProperty("MapNumber")
	private int mapNumber;
	@JsonProperty("MapFilterEnabled")
	private boolean mapFilterEnabled;

	@JsonProperty("Discount")
	private int discount;

	@JsonProperty("Order")
	private int order;

	public SpecialOfferDefinitionResponse() {
		// just for jackson library
	}

	public SpecialOfferDefinitionResponse(String id, int availableAmount, int durationInSeconds, String groupId, int goldPrice, int gemsPrice,
			boolean shopInAppPurchase, String shopInAppItemId, String activationTime, int frequencyInDays, boolean scheduledFilterEnabled,
			int mapNumber, boolean mapFilterEnabled, int discount, int order) {
		this.id = id;
		this.availableAmount = availableAmount;
		this.durationInSeconds = durationInSeconds;
		this.groupId = groupId;
		this.goldPrice = goldPrice;
		this.gemsPrice = gemsPrice;
		this.shopInAppPurchase = shopInAppPurchase;
		this.shopInAppItemId = shopInAppItemId;
		this.activationTime = activationTime;
		this.frequencyInDays = frequencyInDays;
		this.scheduledFilterEnabled = scheduledFilterEnabled;
		this.mapNumber = mapNumber;
		this.mapFilterEnabled = mapFilterEnabled;
		this.discount = discount;
		this.order = order;
	}

	public String getId() {
		return id;
	}

	public int getAvailableAmount() {
		return availableAmount;
	}

	public int getDurationInSeconds() {
		return durationInSeconds;
	}

	public String getGroupId() {
		return groupId;
	}

	public int getGoldPrice() {
		return goldPrice;
	}

	public int getGemsPrice() {
		return gemsPrice;
	}

	public boolean getShopInAppPurchase() {
		return shopInAppPurchase;
	}

	public String getShopInAppItemId() {
		return shopInAppItemId;
	}

	public String getActivationTime() {
		return activationTime;
	}

	public int getFrequencyInDays() {
		return frequencyInDays;
	}

	public boolean getScheduledFilterEnabled() {
		return scheduledFilterEnabled;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	public boolean getMapFilterEnabled() {
		return mapFilterEnabled;
	}

	public int getDiscount() {
		return discount;
	}

	public int getOrder() {
		return order;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
