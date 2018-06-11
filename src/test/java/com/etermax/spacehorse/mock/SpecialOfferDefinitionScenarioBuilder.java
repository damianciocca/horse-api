package com.etermax.spacehorse.mock;

import static com.google.common.collect.Lists.newArrayList;

import org.joda.time.DateTime;

import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinition;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferDefinitionFactory;
import com.etermax.spacehorse.core.catalog.model.specialoffer.SpecialOfferItemDefinition;
import com.etermax.spacehorse.core.reward.model.RewardType;

public class SpecialOfferDefinitionScenarioBuilder {

	private String specialOfferId;
	private SpecialOfferDefinition specialOfferDefinition;
	private SpecialOfferDefinitionFactory factory = new SpecialOfferDefinitionFactory();

	// definiciones de ofertas que aparezca solo una vez
	public SpecialOfferDefinitionScenarioBuilder(String id, int durationInSeconds) {
		this.specialOfferId = id;
		this.specialOfferDefinition = factory.createOneTimeSpecialOffer(id, 1, durationInSeconds, "", 200, 0, false, "", newArrayList(), 0,
				false, 0, 1);
	}

	// definiciones de ofertas programadas
	public SpecialOfferDefinitionScenarioBuilder(String id, DateTime activationTime, int frequencyInDays, int durationInSeconds) {
		this.specialOfferDefinition = factory
				.createScheduledSpecialOffer(id, 1, durationInSeconds, "", 200, 0, false, "", newArrayList(), activationTime, frequencyInDays, true,
						0, false, 0, 1);
	}

	// definiciones de ofertas programadas y con filtro por mapa
	public SpecialOfferDefinitionScenarioBuilder(String id, DateTime activationTime, int frequencyInDays, int durationInSeconds, int mapNumber) {
		this.specialOfferDefinition = factory
				.createScheduledSpecialOffer(id, 1, durationInSeconds, "", 200, 0, false, "", newArrayList(), activationTime, frequencyInDays, true,
						mapNumber, true, 0, 1);
	}

	public SpecialOfferDefinitionScenarioBuilder withAnyItem(String id, String itemTypeId, RewardType rewardType) {
		SpecialOfferItemDefinition itemDefinition = new SpecialOfferItemDefinition(id, itemTypeId, rewardType, 1, specialOfferId);
		specialOfferDefinition.addItem(itemDefinition);
		return this;
	}

	public SpecialOfferDefinition build() {
		return specialOfferDefinition;
	}
}
