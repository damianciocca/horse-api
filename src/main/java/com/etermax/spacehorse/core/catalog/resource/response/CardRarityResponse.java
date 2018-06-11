package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.CardRarity;

public class CardRarityResponse {

	static public final int COMMON = 0;
	static public final int RARE = 1;
	static public final int EPIC = 2;
	static public final int LEGENDARY = 3;

	static public CardRarity toCardRarityEnum(int rarity) {
		switch (rarity) {
			case COMMON:
				return CardRarity.COMMON;
			case RARE:
				return CardRarity.RARE;
			case EPIC:
				return CardRarity.EPIC;
			case LEGENDARY:
				return CardRarity.LEGENDARY;
		}

		return CardRarity.COMMON;
	}

	public static int fromCardRarityEnum(CardRarity rarity) {
		switch (rarity) {
			case COMMON:
				return COMMON;
			case RARE:
				return RARE;
			case EPIC:
				return EPIC;
			case LEGENDARY:
				return LEGENDARY;
		}
		return COMMON;
	}

}
