package com.etermax.spacehorse.core.catalog.model.specialoffer;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.etermax.spacehorse.core.catalog.exception.CatalogException;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.catalog.model.CatalogEntry;
import com.google.common.collect.ImmutableList;

public class SpecialOfferDefinition extends CatalogEntry {

	private int availableAmount;
	private int durationInSeconds;
	private String groupId;
	private List<SpecialOfferItemDefinition> itemDefinitions;

	private int goldPrice;
	private int gemsPrice;
	private boolean isInAppPurchase; // soft link con SpecialOfferInApp
	private String shopInAppItemId;

	private DateTime activationTime;
	private int frequencyInDays;
	private boolean isScheduledFilterEnabled;

	private int mapNumber;
	private boolean isMapFilterEnabled;

	private int discount;
	private int order;

	public SpecialOfferDefinition(String id, int availableAmount, int durationInSeconds, String groupId, int goldPrice, int gemsPrice,
			boolean isInAppPurchase, String shopInAppItemId, List<SpecialOfferItemDefinition> itemDefinitions, DateTime activationTime,
			int frequencyInDays, boolean isScheduledFilterEnabled, int mapNumber, boolean isMapFilterEnabled, int discount, int order) {
		super(id);
		this.availableAmount = availableAmount;
		this.durationInSeconds = durationInSeconds;
		this.groupId = groupId;

		this.goldPrice = goldPrice;
		this.gemsPrice = gemsPrice;
		this.isInAppPurchase = isInAppPurchase;
		this.shopInAppItemId = shopInAppItemId;

		this.itemDefinitions = itemDefinitions;
		this.activationTime = activationTime;
		this.frequencyInDays = frequencyInDays;
		this.isScheduledFilterEnabled = isScheduledFilterEnabled;

		this.mapNumber = mapNumber;
		this.isMapFilterEnabled = isMapFilterEnabled;
		this.discount = discount;
		this.order = order;
	}

	@Override
	public void validate(Catalog catalog) {
		List<String> specialOfferIds = catalog.getSpecialOfferItemsDefinitionsCollection().getEntries().stream()
				.map(SpecialOfferItemDefinition::getSpecialOfferId).collect(Collectors.toList());
		if (!specialOfferIds.contains(getId())) {
			throw new CatalogException("The special offer [ " + getId() + " ] has not any special offer item");
		}
		if (!isInAppPurchase()) {
			validateGemsAndGold();
		}
		if (isInAppPurchase()) {
			validateInAppItemId();
		}
		if (isScheduledFilterEnabled()) {
			validateActivationAndFrequency();
			validateActivationAndFrequencyByGroupId(catalog);
		}
		if (isMapFilterEnabled()) {
			validateValidateMaps(catalog);
		}
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

	public boolean isInAppPurchase() {
		return isInAppPurchase;
	}

	public String getShopInAppItemId() {
		return shopInAppItemId;
	}

	public List<SpecialOfferItemDefinition> getItemDefinitions() {
		return ImmutableList.copyOf(itemDefinitions);
	}

	public DateTime getActivationTime() {
		return activationTime;
	}

	public int getFrequencyInDays() {
		return frequencyInDays;
	}

	public boolean isScheduledFilterEnabled() {
		return isScheduledFilterEnabled;
	}

	public boolean isOneTimeEnabled() {
		return !isScheduledFilterEnabled;
	}

	public boolean isMapFilterEnabled() {
		return isMapFilterEnabled;
	}

	public int getMapNumber() {
		return mapNumber;
	}

	// just for test
	public void addItem(SpecialOfferItemDefinition itemDefinition) {
		this.itemDefinitions.add(itemDefinition);
	}

	public int getDiscount() {
		return discount;
	}

	public int getOrder() {
		return order;
	}

	private void validateValidateMaps(Catalog catalog) {
		if (!isValidMapNumber(catalog)) {
			throw new CatalogException("the scheduled special offer [ " + getId() + " ] has an invalid map number");
		}
	}

	private void validateActivationAndFrequency() {
		if (getActivationTime() == null) {
			throw new CatalogException("the scheduled special offer [ " + getId() + " ] should have an activation time");
		}
		if (getFrequencyInDays() <= 0) {
			throw new CatalogException("the scheduled special offer [ " + getId() + " ] should have a minimum frequency in days");
		}
		int durationInDays = getDurationInSeconds() / 60 / 60 / 24;
		if (durationInDays > frequencyInDays) {
			throw new CatalogException("the scheduled special offer [ " + getId() + " ] should not have a duration higher than frequency in days");
		}
	}

	private void validateInAppItemId() {
		if (isBlank(getShopInAppItemId())) {
			throw new CatalogException("the special offer [ " + getId() + " ] should have a shop in app item id");
		}
	}

	private void validateGemsAndGold() {
		if (getGemsPrice() <= 0 && getGoldPrice() <= 0) {
			throw new CatalogException("the special offer [ " + getId() + " ] should have any price in gems or gold");
		}
	}

	private boolean isValidMapNumber(Catalog catalog) {
		return catalog.getMapsCollection().getEntries().stream().anyMatch(x -> x.getMapNumber() == getMapNumber());
	}

	private void validateActivationAndFrequencyByGroupId(Catalog catalog) {
		Map<String, List<SpecialOfferDefinition>> scheduledOfferDefinitionsGroupedByGroupIds = catalog.getSpecialOfferDefinitionsCollection()
				.getEntries().stream() //
				.filter(specialOfferDefinition -> specialOfferDefinition.isScheduledFilterEnabled) //
				.filter(specialOfferDefinition -> StringUtils.isNotBlank(specialOfferDefinition.getGroupId())) //
				.collect(Collectors.groupingBy(SpecialOfferDefinition::getGroupId));

		for (Map.Entry<String, List<SpecialOfferDefinition>> offersByGroupIds : scheduledOfferDefinitionsGroupedByGroupIds.entrySet()) {
			List<SpecialOfferDefinition> specialOffersGroupedByGroupId = offersByGroupIds.getValue();
			if (specialOffersGroupedByGroupId.stream().collect(Collectors.groupingBy(SpecialOfferDefinition::getActivationTime)).size() > 1) {
				throw new CatalogException(
						"The special offer grouped by group id [ " + offersByGroupIds.getKey() + " ] has different activation times. Should be the "
								+ "same!");
			}
			if (specialOffersGroupedByGroupId.stream().collect(Collectors.groupingBy(SpecialOfferDefinition::getFrequencyInDays)).size() > 1) {
				throw new CatalogException(
						"The special offer grouped by group id [ " + offersByGroupIds.getKey() + " ] has different frequency times. Should be the "
								+ "same!");
			}
		}
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
