package com.etermax.spacehorse.core.cheat.model;

import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.player.model.Player;

public abstract class Cheat {
	public abstract String getCheatId();
	public abstract CheatResponse apply(Player player, String[] parameters, Catalog catalog);

	static protected int getParameterInt(String[] parameters, int index) {
		return Integer.parseInt(parameters[index]);
	}

	static protected long getParameterLong(String[] parameters, int index) {
		return Long.parseLong(parameters[index]);
	}

	static protected String getParameterString(String[] parameters, int index) {
		return parameters[index];
	}
}
