package com.etermax.spacehorse.mock;

import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferItemDefinition;
import com.etermax.spacehorse.core.reward.model.RewardType;
import com.etermax.spacehorse.core.servertime.model.FixedServerTimeProvider;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOffer;
import com.etermax.spacehorse.core.specialoffer.model.SpecialOfferItem;

public class SpecialOfferScenarioBuilder {

	private static final String DEFAULT_CHEST_ID = "courier_Epic"; // tiene que matchear con el id de algun chestDefinition

	private String specialOfferId;
	private SpecialOffer specialOffer;

	public SpecialOfferScenarioBuilder(String specialOfferId) {
		this.specialOfferId = specialOfferId;
		SpecialOfferDefinition specialOfferDefinition = new SpecialOfferDefinitionScenarioBuilder(specialOfferId, 100).build();
		this.specialOffer = new SpecialOffer(specialOfferDefinition, new FixedServerTimeProvider());
	}

	public SpecialOfferScenarioBuilder(SpecialOfferDefinition definition, FixedServerTimeProvider timeProvider) {
		specialOffer = new SpecialOffer(definition, timeProvider);
	}

	public SpecialOfferScenarioBuilder withChestItem(String itemId) {
		return withAnyItem(itemId, DEFAULT_CHEST_ID, RewardType.CHEST);
	}

	public SpecialOfferScenarioBuilder withAnyItem(String id, String itemTypeId, RewardType rewardType) {
		SpecialOfferItemDefinition itemDefinition = createItemDefinitionWith(id, itemTypeId, rewardType);
		SpecialOfferItem specialOfferItem = new SpecialOfferItem(itemDefinition);
		specialOffer.addItem(specialOfferItem);
		return this;
	}

	private SpecialOfferItemDefinition createItemDefinitionWith(String id, String itemTypeId, RewardType rewardType) {
		return new SpecialOfferItemDefinition(id, itemTypeId, rewardType, 1, specialOfferId);
	}

	public SpecialOffer build() {
		return specialOffer;
	}
}
