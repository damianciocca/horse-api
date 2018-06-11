package com.etermax.spacehorse.core.specialoffer.model;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.servertime.model.ServerTime;
import com.etermax.spacehorse.core.servertime.model.ServerTimeProvider;
import com.google.common.collect.ImmutableList;

public class SpecialOffer {

	private final String id;
	private final SpecialOfferDefinition specialOfferDefinition;
	private DateTime expirationTime;
	private final List<SpecialOfferItem> specialOfferItems;
	private int availableAmountUntilExpiration;

	public SpecialOffer(SpecialOfferDefinition specialOfferDefinition, ServerTimeProvider timeProvider) {
		this(specialOfferDefinition, //
				timeProvider.getDateTime().plusSeconds(specialOfferDefinition.getDurationInSeconds()),//
				specialOfferDefinition.getAvailableAmount());
	}

	private SpecialOffer(SpecialOfferDefinition specialOfferDefinition, DateTime expirationTime, int availableAmountUntilExpiration) {
		this.id = specialOfferDefinition.getId();
		this.specialOfferDefinition = specialOfferDefinition;
		this.expirationTime = expirationTime;
		this.availableAmountUntilExpiration = availableAmountUntilExpiration;
		this.specialOfferItems = specialOfferDefinition.getItemDefinitions().stream().map(SpecialOfferItem::new).collect(Collectors.toList());
	}

	public static SpecialOffer restore(SpecialOfferDefinition specialOfferDefinition, DateTime expirationDateTime,
			int availableAmountUntilExpiration) {
		return new SpecialOffer(specialOfferDefinition, expirationDateTime, availableAmountUntilExpiration);
	}

	public boolean hasAvailableAmount() {
		return availableAmountUntilExpiration > 0;
	}

	public boolean hasFixedMapNumber() {
		return specialOfferDefinition.isMapFilterEnabled();
	}

	public int getFixedMapNumber() {
		return specialOfferDefinition.getMapNumber();
	}

	public void consume() {
		availableAmountUntilExpiration--;
	}

	public String getId() {
		return id;
	}

	public int getGoldPrice() {
		return specialOfferDefinition.getGoldPrice();
	}

	public int getGemsPrice() {
		return specialOfferDefinition.getGemsPrice();
	}

	public List<SpecialOfferItem> getItems() {
		return ImmutableList.copyOf(specialOfferItems);
	}

	public String getShopItemIdInApp() {
		return specialOfferDefinition.getShopInAppItemId();
	}

	public void addItem(SpecialOfferItem specialOfferItem) {
		this.specialOfferItems.add(specialOfferItem);
	}

	public long getExpirationTimeInSeconds() {
		return ServerTime.fromDate(expirationTime);
	}

	public int getAvailableAmountUntilExpiration() {
		return availableAmountUntilExpiration;
	}

	public boolean isExpired(DateTime now) {
		return now.isAfter(expirationTime);
	}

	public String getGroupId() {
		return specialOfferDefinition.getGroupId();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	public void cheatSetExpirationTime(long expirationTime) {
		this.expirationTime = ServerTime.toDateTime(expirationTime);
	}
}
