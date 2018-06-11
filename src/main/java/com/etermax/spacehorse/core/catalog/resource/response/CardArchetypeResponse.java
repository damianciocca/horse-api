package com.etermax.spacehorse.core.catalog.resource.response;

import com.etermax.spacehorse.core.catalog.model.CardArchetype;

public class CardArchetypeResponse {

	static public final int AREA_DAMAGE = 0;
	static public final int BUILDING = 1;
	static public final int DIRECT_DAMAGE = 2;
	static public final int TANK = 3;
	static public final int FIGHTER = 4;
	static public final int HORDE = 5;
	static public final int MARKSMAN = 6;

	static public CardArchetype toCardArchetypeEnum(int archetype) {
		switch (archetype) {
			case AREA_DAMAGE:
				return CardArchetype.AREA_DAMAGE;
			case BUILDING:
				return CardArchetype.BUILDING;
			case DIRECT_DAMAGE:
				return CardArchetype.DIRECT_DAMAGE;
			case TANK:
				return CardArchetype.TANK;
			case FIGHTER:
				return CardArchetype.FIGHTER;
			case HORDE:
				return CardArchetype.HORDE;
			case MARKSMAN:
				return CardArchetype.MARKSMAN;
		}

		return CardArchetype.FIGHTER;
	}

	public static int fromCardArchetypeEnum(CardArchetype archetype) {
		switch (archetype) {
			case AREA_DAMAGE:
				return AREA_DAMAGE;
			case BUILDING:
				return BUILDING;
			case DIRECT_DAMAGE:
				return DIRECT_DAMAGE;
			case TANK:
				return TANK;
			case FIGHTER:
				return FIGHTER;
			case HORDE:
				return HORDE;
			case MARKSMAN:
				return MARKSMAN;
		}
		return FIGHTER;
	}
}
