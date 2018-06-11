package com.etermax.spacehorse.core.catalog.model.specialoffer;

import java.util.List;

import org.joda.time.DateTime;

public class SpecialOfferDefinitionFactory {

	public SpecialOfferDefinition createScheduledSpecialOffer(String id, int availableAmount, int durationInSeconds, String groupId, int goldPrice,
			int gemsPrice, boolean isInAppPurchase, String shopInAppItemId, List<SpecialOfferItemDefinition> itemDefinitions, DateTime activationTime,
			int frequencyInDays, boolean isScheduledFilterEnabled, int mapNumber, boolean isMapFilterEnabled, int discount, int order) {

		return new SpecialOfferDefinition(id, availableAmount, durationInSeconds, groupId, goldPrice, gemsPrice, isInAppPurchase, shopInAppItemId,
				itemDefinitions, activationTime, frequencyInDays, isScheduledFilterEnabled, mapNumber, isMapFilterEnabled, discount, order);
	}

	public SpecialOfferDefinition createOneTimeSpecialOffer(String id, int availableAmount, int durationInSeconds, String groupId, int goldPrice,
			int gemsPrice, boolean isInAppPurchase, String shopInAppItemId, List<SpecialOfferItemDefinition> itemDefinitions, int mapNumber,
			boolean isMapFilterEnabled, int discount, int order) {

		return new SpecialOfferDefinition(id, availableAmount, durationInSeconds, groupId, goldPrice, gemsPrice, isInAppPurchase, shopInAppItemId,
				itemDefinitions, null, 0, false, mapNumber, isMapFilterEnabled, discount, order);
	}
}
