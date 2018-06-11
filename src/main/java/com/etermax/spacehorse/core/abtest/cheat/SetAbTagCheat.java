package com.etermax.spacehorse.core.abtest.cheat;

import java.util.List;

import com.etermax.spacehorse.core.abtest.model.ABTag;
import com.etermax.spacehorse.core.catalog.model.ABTesterEntry;
import com.etermax.spacehorse.core.catalog.model.Catalog;
import com.etermax.spacehorse.core.cheat.model.Cheat;
import com.etermax.spacehorse.core.cheat.resource.response.CheatResponse;
import com.etermax.spacehorse.core.player.model.Player;

public class SetAbTagCheat extends Cheat {

	@Override
	public String getCheatId() {
		return "setAbTag";
	}

	@Override
	public CheatResponse apply(Player player, String[] parameters, Catalog catalog) {
		String abTagId = getParameterString(parameters, 0);

		ABTag abTag = new ABTag(abTagId);

		if (validAbTag(abTag, catalog.getAbTesterCollection().getEntries())) {
			player.cheatSetAbTag(abTag);
			return new CheatResponse(abTag.toString());
		}

		return new CheatResponse("");
	}

	private boolean validAbTag(ABTag abTag, List<ABTesterEntry> entries) {
		return abTag.isEmptyABTag() || entries.stream()
				.anyMatch(x -> getSegment(x).equals(abTag.getSegmentId()) && getCampaignId(x).equals(abTag.getCampaignId()));
	}

	private String getSegment(ABTesterEntry entry) {
		return entry.getSegment();
	}

	static private String getCampaignId(ABTesterEntry entry) {
		return entry.getId().split("-")[0];
	}
}
