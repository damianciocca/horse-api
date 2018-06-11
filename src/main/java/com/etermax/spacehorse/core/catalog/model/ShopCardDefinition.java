package com.etermax.spacehorse.core.catalog.model;

import com.etermax.spacehorse.core.catalog.resource.response.ShopCardDefinitionResponse;
import com.etermax.spacehorse.core.shop.model.PriceType;

public class ShopCardDefinition extends CatalogEntry {

	private final CardRarity cardRarity;

	private final int amount;

	private final int basePrice;

	private final int priceIncreasePerPurchase;

	private final PriceType priceType;

	public ShopCardDefinition(String id, CardRarity cardRarity, int amount, int basePrice, int priceIncreasePerPurchase, PriceType priceType) {
		super(id);
		this.cardRarity = cardRarity;
		this.amount = amount;
		this.basePrice = basePrice;
		this.priceIncreasePerPurchase = priceIncreasePerPurchase;
		this.priceType = priceType;
	}

	public ShopCardDefinition(ShopCardDefinitionResponse shopCardDefinitionResponse) {
		super(shopCardDefinitionResponse.getId());
		this.cardRarity = shopCardDefinitionResponse.getCardRarity();
		this.amount = shopCardDefinitionResponse.getAmount();
		this.basePrice = shopCardDefinitionResponse.getBasePrice();
		this.priceIncreasePerPurchase = shopCardDefinitionResponse.getPriceIncreasePerPurchase();
		this.priceType = shopCardDefinitionResponse.getPriceType();
	}

	public CardRarity getCardRarity() {
		return cardRarity;
	}

	public int getAmount() {
		return amount;
	}

	public int getBasePrice() {
		return basePrice;
	}

	public int getPriceIncreasePerPurchase() {
		return priceIncreasePerPurchase;
	}

	public PriceType getPriceType() {
		return priceType;
	}

	@Override
	public void validate(Catalog catalog) {
		validateParameter(amount > 0, "amount <= 0");
		validateParameter(basePrice > 0, "basePrice <= 0");
		validateParameter(priceIncreasePerPurchase >= 0, "priceIncreasePerPurchase < 0");
		validateParameter(
				catalog.getCardDefinitionsCollection().getEntries().stream().anyMatch(x -> x.getEnabled() && x.getCardRarity().equals(cardRarity)),
				"No enabled card with rarity %s found", cardRarity);
	}

}
